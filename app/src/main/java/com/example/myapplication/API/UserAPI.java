package com.example.myapplication.API;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Entities.AuthInterceptor;
import com.example.myapplication.Entities.Result;
import com.example.myapplication.Entities.User;
import com.example.myapplication.Entities.UserCredentials;
import com.example.myapplication.Entities.Video;
import com.example.myapplication.Helper;
import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
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
    private final Retrofit retrofit;
    private final WebServiceAPI webServiceAPI;

    public UserAPI() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new AuthInterceptor()).build();
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

    public LiveData<Result> createUser(User user) {
        MutableLiveData<Result> resultLiveData = new MutableLiveData<>();
        webServiceAPI.createUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result(true, null));
                } else {
                    String errorMessage = "Failed to create user: " + response.message();
                    resultLiveData.postValue(new Result(false, errorMessage));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                String errorMessage = "Network error: " + t.getMessage();
                resultLiveData.postValue(new Result(false, errorMessage));
            }
        });

        return resultLiveData;
    }


    public LiveData<Result> login(String username, String password) {
        MutableLiveData<Result> resultLiveData = new MutableLiveData<>();

        webServiceAPI.login(new UserCredentials(username, password)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseBody);
                        String token = jsonObject.getString("token");
                        Helper.setToken(token);

                        resultLiveData.postValue(new Result(true, null));
                    } catch (Exception e) {
                        e.printStackTrace();
                        resultLiveData.postValue(new Result(false, "Error parsing response"));
                    }
                } else {
                    String errorMessage = "Login failed: " + response.message();
                    resultLiveData.postValue(new Result(false, errorMessage));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String errorMessage = "Network error: " + t.getMessage();
                resultLiveData.postValue(new Result(false,  errorMessage));
            }
        });

        return resultLiveData;
    }




    public LiveData<Result> createUserVideo(String userId, String title, String author, String photo, File videoFile) {
        MutableLiveData<Result> resultLiveData = new MutableLiveData<>();

        // Create request parts
        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody authorBody = RequestBody.create(MediaType.parse("text/plain"), author);
        RequestBody photoBody = RequestBody.create(MediaType.parse("text/plain"), photo);

        String mimeType;
        String fileExtension = videoFile.getName().substring(videoFile.getName().lastIndexOf(".") + 1).toLowerCase();
        switch (fileExtension) {
            case "mp4":
                mimeType = "video/mp4";
                break;
            case "avi":
                mimeType = "video/x-msvideo";
                break;
            case "mkv":
                mimeType = "video/x-matroska";
                break;
            default:
                mimeType = "video/*"; // Fallback to a generic video MIME type
        }

        // Create MultipartBody.Part for video file with the correct MIME type
        MultipartBody.Part videoPart = MultipartBody.Part.createFormData(
                "video", // Ensure the form data name matches what the server expects
                videoFile.getName(),
                RequestBody.create(MediaType.parse(mimeType), videoFile)
        );
        webServiceAPI.createUserVideo(userId, titleBody, authorBody, videoPart, photoBody).enqueue(new Callback<Video>() {
            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.postValue(new Result(true, "Video created successfully"));
                } else {
                    resultLiveData.postValue(new Result(false, "Failed to create User: Username is already taken"));
                }
            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {
                resultLiveData.postValue(new Result(false, "Network error: " + t.getMessage()));
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

    public LiveData<Result> updateDisplayName(String userId, String newDisplayName) {
        MutableLiveData<Result> result = new MutableLiveData<>();
        webServiceAPI.updateUser(userId, newDisplayName).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(new Result(true, null));
                } else {
                    result.postValue(new Result(false, "Update failed"));
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                result.postValue(new Result(false, t.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Result> deleteUser(String userId, String username) {
        MutableLiveData<Result> result = new MutableLiveData<>();
        Log.d("UserAPI", "Attempting to delete user with ID: " + userId);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
        } catch (JSONException e) {
            Log.e("UserAPI", "Error creating JSON body", e);
            result.postValue(new Result(false, "Error creating request body"));
            return result;
        }

        String bodyString = jsonBody.toString();
        Log.d("UserAPI", "Request body: " + bodyString);

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), bodyString);

        webServiceAPI.deleteUser(userId, body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("UserAPI", "Delete user response code: " + response.code());
                if (response.isSuccessful()) {
                    result.postValue(new Result(true, null));
                } else {
                    String errorBody = "";
                    try {
                        errorBody = response.errorBody().string();
                    } catch (IOException e) {
                        Log.e("UserAPI", "Error reading error body", e);
                    }
                    String errorMessage = "Failed to delete user: " + response.code() + " - " + errorBody;
                    Log.e("UserAPI", errorMessage);
                    result.postValue(new Result(false, errorMessage));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("UserAPI", "Delete user request failed", t);
                result.postValue(new Result(false, t.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Result> updateUserVideo(String userId, String videoId, String title) {
        MutableLiveData<Result> result = new MutableLiveData<>();
        webServiceAPI.updateUserVideo(userId, videoId, title).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(new Result(true, null));
                } else {
                    result.postValue(new Result(false, "Update failed"));
                }
            }
            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                result.postValue(new Result(false, t.getMessage()));
            }
        });
        return result;
    }


    public LiveData<Result> deleteUserVideo(String userId, String videoId) {
        MutableLiveData<Result> resultLiveData = new MutableLiveData<>();

        webServiceAPI.deleteUserVideo(userId, videoId).enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Assuming Result class can handle List<Video> in some way
                    resultLiveData.postValue(new Result(true, "Video deleted successfully"));
                } else {
                    resultLiveData.postValue(new Result(false, "Failed to delete video: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                resultLiveData.postValue(new Result(false, "Network error: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }


    public LiveData<List<Video>> getUserVideos(String userId) {
        MutableLiveData<List<Video>> userVideos = new MutableLiveData<>();
        webServiceAPI.getUserVideos(userId).enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(@NonNull Call<List<Video>> call, @NonNull Response<List<Video>> response) {
                userVideos.postValue(response.body());
            }
            @Override
            public void onFailure(@NonNull retrofit2.Call<List<Video>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
        return userVideos;
    }
}
