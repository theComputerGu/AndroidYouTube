package com.example.myapplication.API;

import com.example.myapplication.Activities.BaseActivity;
import com.example.myapplication.Entities.UpdateUser;
import com.example.myapplication.Entities.User;
import com.example.myapplication.Entities.UserCredentials;
import com.example.myapplication.Entities.Video;
import com.example.myapplication.R;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
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

    public void login(UserCredentials credentials, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = webServiceAPI.login(credentials);
        call.enqueue(callback);
    }

    public void getUser(String token, String username, Callback<User> callback) {
        Call<User> call = webServiceAPI.getUser(token, username);
        call.enqueue(callback);
    }

    public void getUserByUsername(String username, Callback<User> callback) {
        Call<User> call = webServiceAPI.getUserByUsername(username);
        call.enqueue(callback);
    }

    public void getUserById(String id, Callback<User> callback) {
        Call<User> call = webServiceAPI.getUserById(id);
        call.enqueue(callback);
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
