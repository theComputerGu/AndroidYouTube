package com.example.myapplication.API;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Entities.MyApplication;
import com.example.myapplication.Entities.Video;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

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
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.baseServerURL))
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

    public void getVideos(MutableLiveData<List<Video>> videos) {
        Call<List<Video>> call = webServiceAPI.getVideos();
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(@NonNull Call<List<Video>> call, @NonNull Response<List<Video>> response) {
                videos.setValue(response.body());
            }
            @Override
            public void onFailure(@NonNull retrofit2.Call<List<Video>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
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

    public void getVideosByPrefix(String prefix, MutableLiveData<List<Video>> videos) {
        Call<List<Video>> call = webServiceAPI.getVideosByPrefix(prefix);
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(@NonNull Call<List<Video>> call, @NonNull Response<List<Video>> response) {
                videos.setValue(response.body());
            }
            @Override
            public void onFailure(@NonNull retrofit2.Call<List<Video>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
    public void getVideosExcept(MutableLiveData<List<Video>> videos,String videoId) {
        Call<List<Video>> call = webServiceAPI.getVideos();
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(@NonNull Call<List<Video>> call, @NonNull Response<List<Video>> response) {
                if (response.isSuccessful()) {
                    List<Video> allVideos = response.body();
                    List<Video> filteredList = new ArrayList<>();

                    // Filter out the video with the specified videoId
                    for (Video video : allVideos) {
                        if (!video.getId().equals(videoId)) {
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
    }
}
