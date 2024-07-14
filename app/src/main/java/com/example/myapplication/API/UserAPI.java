package com.example.myapplication.API;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Helper;
import com.example.myapplication.Entities.Result;
import com.example.myapplication.Entities.UpdateUser;
import com.example.myapplication.Entities.User;
import com.example.myapplication.Entities.UserCredentials;
import com.example.myapplication.Entities.UserLoginResponse;
import com.example.myapplication.Entities.Video;
import com.example.myapplication.R;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;

    public UserAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Helper.context.getString(R.string.baseServerURL)+"/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public LiveData<Result> login(String username, String password) {
        MutableLiveData<Result> resultLiveData = new MutableLiveData<>();

        webServiceAPI.login(new UserCredentials(username, password))
                .enqueue(new Callback<UserLoginResponse>() {
                    @Override
                    public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Save the token using UserLoginResponse
                            response.body().saveToken(Helper.context);
                            resultLiveData.setValue(new Result(true, null));
                        } else {
                            String errorMessage = "Login failed: " + response.message();
                            resultLiveData.setValue(new Result(false, errorMessage));
                        }
                    }

                    @Override
                    public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                        String errorMessage = "Network error: " + t.getMessage();
                        resultLiveData.setValue(new Result(false, errorMessage));
                    }
                });

        return resultLiveData;
    }
    public String getToken() {
        return UserLoginResponse.getToken(Helper.context);
    }

    public WebServiceAPI getWebServiceAPI() {
        return webServiceAPI;
    }

    public void createUser(User user, MutableLiveData<Result> result) {
        Call<ResponseBody> call = webServiceAPI.createUser(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(new Result(true, null));
                } else {
                    // Customize this message based on the specific response
                    String errorMessage = "Failed to create user: " + response.message();
                    result.setValue(new Result(false, errorMessage));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String errorMessage = "Network error: " + t.getMessage();
                result.setValue(new Result(false, errorMessage));
            }
        });
    }
    public MutableLiveData<Result> createUserVideo(String userId, String title, String author, File videoFile, String photo) {
        MutableLiveData<Result> resultLiveData = new MutableLiveData<>();

        // Create request parts
        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody authorBody = RequestBody.create(MediaType.parse("text/plain"), author);
        RequestBody photoBody = RequestBody.create(MediaType.parse("text/plain"), photo);
        RequestBody userIdBody = RequestBody.create(MediaType.parse("text/plain"), userId);

        // Create MultipartBody.Part for video file
        MultipartBody.Part videoPart = MultipartBody.Part.createFormData(
                "videoFile",
                videoFile.getName(),
                RequestBody.create(MediaType.parse("video/*"), videoFile)
        );

        webServiceAPI.createUserVideo(userId, titleBody, authorBody, videoPart, photoBody)
                .enqueue(new Callback<Video>() {
                    @Override
                    public void onResponse(Call<Video> call, Response<Video> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            resultLiveData.setValue(new Result(true, null));
                        } else {
                            String errorMessage = "Failed to create video: " + response.message();
                            resultLiveData.setValue(new Result(false, errorMessage));
                        }
                    }

                    @Override
                    public void onFailure(Call<Video> call, Throwable t) {
                        String errorMessage = "Network error: " + t.getMessage();
                        resultLiveData.setValue(new Result(false, errorMessage));
                    }
                });

        return resultLiveData;
    }


    public void getUser(String token, String username, Callback<User> callback) {
        Call<User> call = webServiceAPI.getUser(token, username);
        call.enqueue(callback);
    }

    public void getUserByUsername(String username, MutableLiveData<User> user) {
        Call<User> call = webServiceAPI.getUserByUsername(username);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    user.setValue(response.body());
                } else {
                    // Handle the case where the video is not found or some other error occurred
                    user.setValue(null);
                }
            }
            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                t.printStackTrace();
                user.setValue(null);
            }
        });
    }

    public void getUserById(String id, MutableLiveData<User> user) {
        Call<User> call = webServiceAPI.getUserById(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    user.setValue(response.body());
                } else {
                    // Handle the case where the video is not found or some other error occurred
                    user.setValue(null);
                }
            }
            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                t.printStackTrace();
                user.setValue(null);
            }
        });
    }

    public void getUserByIdWithPassword(String id, Callback<User> callback) {
        Call<User> call = webServiceAPI.getUserByIdWithPassword(id);
        call.enqueue(callback);
    }

    public void updateUser(String token, String id, UpdateUser update, Callback<User> callback) {
        Call<User> call = webServiceAPI.updateUser(token, id, update);
        call.enqueue(callback);
    }

    public void deleteUser(String token, String id, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = webServiceAPI.deleteUser(token, id);
        call.enqueue(callback);
    }

    public void updateUserVideo(String token, String videoId, String userId, String title, Callback<Video> callback) {
        Call<Video> call = webServiceAPI.updateUserVideo(token, videoId, userId, title);
        call.enqueue(callback);
    }

    public LiveData<Result> deleteUserVideo(String userId, String videoId, String token) {
        MutableLiveData<Result> resultLiveData = new MutableLiveData<>();

        webServiceAPI.deleteUserVideo(userId, videoId, token)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            resultLiveData.setValue(new Result(true, null));
                        } else {
                            String errorMessage = "Failed to delete video: " + response.message();
                            resultLiveData.setValue(new Result(false, errorMessage));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        String errorMessage = "Network error: " + t.getMessage();
                        resultLiveData.setValue(new Result(false, errorMessage));
                    }
                });

        return resultLiveData;
    }
    public MutableLiveData<List<Video>> getUserVideos(String userId) {
        MutableLiveData<List<Video>> userVideos = new MutableLiveData<>();
        Call<List<Video>> call = webServiceAPI.getUserVideos(userId);

        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                if (response.isSuccessful()) {
                    userVideos.setValue(response.body());
                } else {
                    userVideos.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                t.printStackTrace();
                userVideos.setValue(null);
            }
        });

        return userVideos;
    }
}
