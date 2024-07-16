package com.example.myapplication.API;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Entities.AuthInterceptor;
import com.example.myapplication.Helper;
import com.example.myapplication.Entities.Video;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
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

    public LiveData<List<Video>> getVideos() {
        MutableLiveData<List<Video>> videos = new MutableLiveData<>();

        webServiceAPI.getVideos().enqueue(new Callback<List<Video>>() {
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

    public void getVideoById(String videoId, MutableLiveData<Video> video) {
        Call<Video> call = webServiceAPI.getVideoById(videoId);
        call.enqueue(new Callback<Video>() {
            @Override
            public void onResponse(@NonNull Call<Video> call, @NonNull Response<Video> response) {
                if (response.isSuccessful()) {
                    video.setValue(response.body());
                } else {
                    // Handle the case where the video is not found or some other error occurred
                    video.setValue(null);
                }
            }
            @Override
            public void onFailure(@NonNull Call<Video> call, @NonNull Throwable t) {
                t.printStackTrace();
                video.setValue(null);
            }
        });
    }


    public void updateVideo(String videoId, Video video, Callback<Video> callback) {
        Call<Video> call = webServiceAPI.updateVideo(videoId, video);
        call.enqueue(callback);
    }

    public void deleteVideo(String videoId, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = webServiceAPI.deleteVideo(videoId);
        call.enqueue(callback);
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
    public LiveData<List<Video>> getVideosExcept(String videoId) {
        MutableLiveData<List<Video>> videos = new MutableLiveData<>();

        webServiceAPI.getVideos().enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(@NonNull Call<List<Video>> call, @NonNull Response<List<Video>> response) {
                if (response.isSuccessful()) {
                    List<Video> allVideos = response.body();
                    List<Video> filteredList = new ArrayList<>();

                    // Filter out the video with the specified videoId
                    for (Video video : allVideos) {
                        if (!video.getVideoId().equals(videoId)) {
                            filteredList.add(video);
                        }
                    }
                    videos.setValue(filteredList);
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Video>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
        return videos;
    }
}
