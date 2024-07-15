package com.example.myapplication.API;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Entities.AuthInterceptor;
import com.example.myapplication.Entities.Result;
import com.example.myapplication.Entities.UpdateUser;
import com.example.myapplication.Entities.User;
import com.example.myapplication.Entities.UserCredentials;
import com.example.myapplication.Entities.Video;
import com.example.myapplication.Helper;
import com.example.myapplication.R;

import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
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
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new AuthInterceptor(Helper.context)).build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Helper.context.getString(R.string.baseServerURL)+"/api/")
                .client(client)
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        webServiceAPI = retrofit.create(WebServiceAPI.class);
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
                    result.setValue(new Result(true,null, null));
                } else {
                    // Customize this message based on the specific response
                    String errorMessage = "Failed to create user: " + response.message();
                    result.setValue(new Result(false,null, errorMessage));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String errorMessage = "Network error: " + t.getMessage();
                result.setValue(new Result(false,null, errorMessage));
            }
        });
    }
    public LiveData<Result<String>> login(String username, String password) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();

        webServiceAPI.login(new UserCredentials(username, password)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseBody);
                        String token = jsonObject.getString("token");
                        Helper.setToken(token);

                        resultLiveData.postValue(new Result<>(true, token, null));
                    } catch (Exception e) {
                        e.printStackTrace();
                        resultLiveData.postValue(new Result<>(false, null, "Error parsing response"));
                    }
                } else {
                    String errorMessage = "Login failed: " + response.message();
                    resultLiveData.postValue(new Result<>(false, null, errorMessage));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String errorMessage = "Network error: " + t.getMessage();
                resultLiveData.postValue(new Result<>(false, null, errorMessage));
            }
        });

        return resultLiveData;
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
                            resultLiveData.setValue(new Result(true,null, null));
                        } else {
                            String errorMessage = "Failed to create video: " + response.message();
                            resultLiveData.setValue(new Result(false,null, errorMessage));
                        }
                    }

                    @Override
                    public void onFailure(Call<Video> call, Throwable t) {
                        String errorMessage = "Network error: " + t.getMessage();
                        resultLiveData.setValue(new Result(false,null, errorMessage));
                    }
                });

        return resultLiveData;
    }


    public void getUser(String token, String username, Callback<User> callback) {
        Call<User> call = webServiceAPI.getUser(token, username);
        call.enqueue(callback);
    }

    public LiveData<User> getUserByUsername(String username) {
        MutableLiveData<User> userLiveData = new MutableLiveData<>();
        webServiceAPI.getUserByUsername(username).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userLiveData.postValue(response.body());
                } else {
                    userLiveData.postValue(null); // Handle error case
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
                userLiveData.postValue(null); // Handle failure case
            }
        });
        return userLiveData;
    }

    public LiveData<User> getUserById(String id) {
        MutableLiveData<User> userLiveData = new MutableLiveData<>();
        webServiceAPI.getUserById(id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userLiveData.setValue(response.body());
                } else {
                    userLiveData.setValue(null); // Handle error case
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
                userLiveData.setValue(null); // Handle failure case
            }
        });
        return userLiveData;
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
                            resultLiveData.setValue(new Result(true,null, null));
                        } else {
                            String errorMessage = "Failed to delete video: " + response.message();
                            resultLiveData.setValue(new Result(false,null, errorMessage));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        String errorMessage = "Network error: " + t.getMessage();
                        resultLiveData.setValue(new Result(false,null, errorMessage));
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
