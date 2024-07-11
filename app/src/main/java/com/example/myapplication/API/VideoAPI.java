package com.example.myapplication.API;

import com.example.myapplication.Entities.Video;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoAPI {
    private String ServerurlVideo;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;


    public VideoAPI(String url) {
        this.setServerUrl(url);
        retrofit = new Retrofit.Builder()
                .baseUrl(this.ServerurlVideo)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public WebServiceAPI getWebServiceApi() {
        return webServiceAPI;
    }

    public void setServerUrl(String serverIp) {
        String url = "http://" + serverIp + ":8080/api/";
        this.ServerurlVideo = url;
    }

    public void createVideo(Video video, Callback<Video> callback) {
        Call<Video> call = webServiceAPI.createVideo(video);
        call.enqueue(callback);
    }

    public void getVideos(Callback<List<Video>> callback) {
        Call<List<Video>> call = webServiceAPI.getVideos();
        call.enqueue(callback);
    }

    public void getVideoById(String videoId, Callback<Video> callback) {
        Call<Video> call = webServiceAPI.getVideoById(videoId);
        call.enqueue(callback);
    }

    public void updateVideo(String videoId, Video video, Callback<Video> callback) {
        Call<Video> call = webServiceAPI.updateVideo(videoId, video);
        call.enqueue(callback);
    }

    public void deleteVideo(String videoId, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = webServiceAPI.deleteVideo(videoId);
        call.enqueue(callback);
    }

    public void getVideosByPrefix(String prefix, Callback<List<Video>> callback) {
        Call<List<Video>> call = webServiceAPI.getVideosByPrefix(prefix);
        call.enqueue(callback);
    }
}
