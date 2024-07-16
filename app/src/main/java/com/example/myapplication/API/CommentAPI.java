package com.example.myapplication.API;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Entities.AuthInterceptor;
import com.example.myapplication.Entities.Comment;
import com.example.myapplication.Helper;
import com.example.myapplication.Entities.UpdateComment;
import com.example.myapplication.R;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentAPI {
    private Retrofit retrofit;
    private static WebServiceAPI webServiceAPI;
//
    public CommentAPI() {
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

    public static void getComments(String videoId, MutableLiveData<List<Comment>> commentLiveData) {
        Call<List<Comment>> call = webServiceAPI.getComments(videoId);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful()) {
                    commentLiveData.setValue(response.body());
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        // Parse errorBody to get detailed error message
                        // Update UI or notify user based on error message
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public  static void createComment(Comment comment, String videoId, MutableLiveData<Comment> commentLiveData) {
        Call<Comment> call = webServiceAPI.createComment(comment, videoId);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(@NonNull Call<Comment> call, @NonNull Response<Comment> response) {
                if (response.isSuccessful()) {
                    commentLiveData.setValue(response.body());
                } else {
                    // Handle the case where the response is not successful
                    commentLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Comment> call, @NonNull Throwable t) {
                t.printStackTrace();
                // Handle the failure
                commentLiveData.setValue(null);
            }
        });
    }



    public void updateComment(String token, String commentId, UpdateComment comment, Callback<Comment> callback) {
        Call<Comment> call = webServiceAPI.updateComment(token, commentId, comment);
        call.enqueue(callback);
    }

    public static void deleteComment(String videoId, String commentId, MutableLiveData<Boolean> commentLiveData) {
        Call<ResponseBody> call = webServiceAPI.deleteComment(videoId, commentId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body()!=null)
                {
                    commentLiveData.setValue(true);
                } else {
                    commentLiveData.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                commentLiveData.setValue(false);
            }
        });
    }

    public void deleteComments(String token, String videoId, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = webServiceAPI.deleteComments(token, videoId);
        call.enqueue(callback);
    }
}
