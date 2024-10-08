package com.example.myapplication.API;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Entities.AuthInterceptor;
import com.example.myapplication.Entities.Video;
import com.example.myapplication.Helper;
import com.example.myapplication.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoAPI {
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;


    public VideoAPI() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new AuthInterceptor()).build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Helper.context.getString(R.string.baseServerURL)+"/api/")
                .client(client)
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public WebServiceAPI getWebServiceApi() {
        return webServiceAPI;
    }

    public void createVideo(Video video, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = webServiceAPI.createVideo(video);
        call.enqueue(callback);
    }

    public LiveData<List<Video>> getTopVideos() {
        MutableLiveData<List<Video>> videos = new MutableLiveData<>();

        webServiceAPI.getTopVideos().enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(@NonNull Call<List<Video>> call, @NonNull Response<List<Video>> response) {
                videos.postValue(response.body());
            }
            @Override
            public void onFailure(@NonNull retrofit2.Call<List<Video>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
        return videos;
    }
    public LiveData<List<Video>> getTcpVideos(String userId) {
        MutableLiveData<List<Video>> videos = new MutableLiveData<>();
        webServiceAPI.getTcpVideos(userId).enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(@NonNull Call<List<Video>> call, @NonNull Response<List<Video>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    videos.postValue(response.body());
                } else {
                    // Handle the case where the response is not successful
                    Log.e("getTcpVideos", "Response failed: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Video>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });

        return videos;
    }


    public LiveData<Video> guestWatchVideo(String videoId) {
        MutableLiveData<Video> video = new MutableLiveData<>();
        Call<Video> call = webServiceAPI.guestWatchVideo(videoId);
        call.enqueue(new Callback<Video>() {
            @Override
            public void onResponse(@NonNull Call<Video> call, @NonNull Response<Video> response) {
                if (response.isSuccessful()) {
                    video.postValue(response.body());
                } else {
                    video.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Video> call, @NonNull Throwable t) {
                t.printStackTrace();
                video.postValue(null);
            }
        });
        return video;
    }
    public LiveData<Video> userWatchVideo(String videoId, String userId) {
        MutableLiveData<Video> video = new MutableLiveData<>();
        Map<String, String> requestBody = new HashMap<>(); // Create a Map to hold the request body
        requestBody.put("userId", userId); // Add userId to the Map

        Call<Video> call = webServiceAPI.userWatchVideo(videoId, requestBody); // Pass the Map as the request body
        call.enqueue(new Callback<Video>() {
            @Override
            public void onResponse(@NonNull Call<Video> call, @NonNull Response<Video> response) {
                if (response.isSuccessful()) {
                    video.postValue(response.body());
                } else {
                    video.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Video> call, @NonNull Throwable t) {
                t.printStackTrace();
                video.postValue(null);
            }
        });
        return video;
    }





    public LiveData<List<Video>> getVideosByPrefix(String prefix) {
        MutableLiveData<List<Video>> videos = new MutableLiveData<>();

        webServiceAPI.getVideosByPrefix(prefix).enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(@NonNull Call<List<Video>> call, @NonNull Response<List<Video>> response) {
                videos.postValue(response.body());
            }
            @Override
            public void onFailure(@NonNull retrofit2.Call<List<Video>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
        return videos;
    }

    public LiveData<Video> updateVideo(String videoId,int views, List<String> likedBy, List<String> disLikedBy) {
        MutableLiveData<Video> liveData = new MutableLiveData<>();

        // Prepare the update payload
        Map<String, Object> updates = new HashMap<>();
        updates.put("views", views);
        updates.put("likedBy", likedBy);
        updates.put("dislikedBy", disLikedBy);

        Call<Video> call = webServiceAPI.updateVideo(videoId, updates);
        call.enqueue(new Callback<Video>() {
            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(response.body());
                } else {
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {
                liveData.postValue(null);
            }
        });

        return liveData;
    }
}
