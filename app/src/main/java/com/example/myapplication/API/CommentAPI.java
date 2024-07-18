package com.example.myapplication.API;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Entities.AuthInterceptor;
import com.example.myapplication.Entities.Comment;
import com.example.myapplication.Entities.Result;
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

    public static void getComments(String videoId, MutableLiveData<List<Comment>> commentLiveData) {
        Call<List<Comment>> call = webServiceAPI.getComments(videoId);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "getComments onResponse: Successful");
                    commentLiveData.postValue(response.body());
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e(TAG, "getComments onResponse: Error body: " + errorBody);
                        // Handle error body for detailed error message
                        // Update UI or notify user based on error message
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Log.e(TAG, "getComments onFailure: " + t.getMessage());
                t.printStackTrace();
                // Handle failure
            }
        });
    }


    public static void createComment(String videoId, String username, String text, MutableLiveData<Comment> commentLiveData) {
        // Assuming you have the user details available synchronously or asynchronously
        String displayName = "User Display Name"; // Replace with actual display name
        String profilePicture = "base64_encoded_profile_picture"; // Replace with actual profile picture

        // Create a Comment object with the available details
        Comment comment = new Comment(username, displayName, profilePicture, videoId, text);

        // Make the Retrofit API call to create the comment
        Call<Comment> call = webServiceAPI.createComment(videoId, comment);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()) {
                    commentLiveData.postValue(response.body());
                } else {
                    // Handle unsuccessful response
                    // Parse errorBody to get detailed error message
                    try {
                        String errorBody = response.errorBody().string();
                        // Update UI or notify user based on error message
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                // Handle failure
                t.printStackTrace();
            }
        });
    }



    public static void updateComment(Comment comment, String videoId, MutableLiveData<Comment> commentLiveData) {
        // Log the comment details before making the call
        Log.d(TAG, "Updating comment with ID: " + comment.getCommentId() + " for video ID: " + comment.getVideoId());

        try {
            Call<Comment> call = webServiceAPI.updateComment(comment.getCommentId(), videoId, comment);
            call.enqueue(new Callback<Comment>() {
                @Override
                public void onResponse(Call<Comment> call, Response<Comment> response) {
                    if (response.isSuccessful()) {
                        commentLiveData.postValue(response.body());
                        Log.d(TAG, "Update successful: " + response.body());
                    } else {
                        Log.d(TAG, "Update failed: " + response.errorBody());
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorBody);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Comment> call, Throwable t) {
                    Log.e(TAG, "Network call failed", t);
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Exception occurred before making network call", e);
        }
    }


    public static void deleteComment(String videoId, String commentId, MutableLiveData<Boolean> commentLiveData) {
        Call<ResponseBody> call = webServiceAPI.deleteComment(videoId, commentId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    commentLiveData.postValue(true);
                    Log.d(TAG, "hhhhhh: ggggggg");
                } else {
                    commentLiveData.postValue(false);
                    Log.d(TAG, "hhhhhh: hhhhhhhh");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                commentLiveData.postValue(false);
            }
        });
    }

    public void deleteComments(String token, String videoId, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = webServiceAPI.deleteComments(token, videoId);
        call.enqueue(callback);
    }
}
