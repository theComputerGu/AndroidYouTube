package com.example.myapplication.API;

import com.example.myapplication.Entities.Comment;
import com.example.myapplication.Entities.UpdateComment;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentAPI {
    private String serverUrl;
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;

    public CommentAPI(String url) {
        this.serverUrl = url;
        retrofit = new Retrofit.Builder()
                .baseUrl(this.serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public WebServiceAPI getWebServiceAPI() {
        return webServiceAPI;
    }

    public void getComments(String token, String videoId, Callback<List<Comment>> callback) {
        Call<List<Comment>> call = webServiceAPI.getComments(token, videoId);
        call.enqueue(callback);
    }

    public void createComment(String token, Comment comment, String videoId, Callback<Comment> callback) {
        Call<Comment> call = webServiceAPI.createComment(token, comment, videoId);
        call.enqueue(callback);
    }

    public void updateComment(String token, String commentId, UpdateComment comment, Callback<Comment> callback) {
        Call<Comment> call = webServiceAPI.updateComment(token, commentId, comment);
        call.enqueue(callback);
    }

    public void deleteComment(String token, String videoId, String commentId, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = webServiceAPI.deleteComment(token, videoId, commentId);
        call.enqueue(callback);
    }

    public void deleteComments(String token, String videoId, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = webServiceAPI.deleteComments(token, videoId);
        call.enqueue(callback);
    }
}
