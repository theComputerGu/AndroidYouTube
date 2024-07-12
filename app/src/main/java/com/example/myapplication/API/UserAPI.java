package com.example.myapplication.API;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Activities.BaseActivity;
import com.example.myapplication.Entities.UpdateUser;
import com.example.myapplication.Entities.User;
import com.example.myapplication.Entities.UserCredentials;
import com.example.myapplication.Entities.Video;
import com.example.myapplication.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

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
                .baseUrl(BaseActivity.context.getString(R.string.baseServerURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public WebServiceAPI getWebServiceAPI() {
        return webServiceAPI;
    }

    public void createUser(User user, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = webServiceAPI.createUser(user);
        call.enqueue(callback);
    }

    public void login(UserCredentials credentials, Callback<TokenResponse> callback) {
        Call<ResponseBody> call = webServiceAPI.login(credentials);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Gson gson = new Gson();
                        TokenResponse tokenResponse = gson.fromJson(responseBody, TokenResponse.class);
                        callback.onResponse(call, Response.success(tokenResponse));
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.onFailure(call, e);
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        callback.onFailure(call, new Throwable(errorBody));
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.onFailure(call, new Throwable("Login failed"));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                callback.onFailure(call, t);
            }
        });
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

    public void deleteUserVideo(String token, String videoId, String userId, Callback<List<Video>> callback) {
        Call<List<Video>> call = webServiceAPI.deleteUserVideo(token, videoId, userId);
        call.enqueue(callback);
    }
}
