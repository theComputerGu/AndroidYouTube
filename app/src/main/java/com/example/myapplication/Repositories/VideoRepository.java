package com.example.myapplication.Repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.API.VideoAPI;
import com.example.myapplication.Entities.Result;
import com.example.myapplication.Entities.Video;

import java.util.ArrayList;
import java.util.List;

public class VideoRepository {
    private VideoListData videoListData;
    private VideoAPI videoAPI;
    public VideoRepository(){
        videoAPI = new VideoAPI();
        videoListData= new VideoListData();
    }

    public void setVideos(List<Video> Videos) {
        videoListData.setValue(Videos);
    }


    public static class VideoListData extends MutableLiveData<List<Video>> {
        public VideoListData(){
            super();
            setValue(new ArrayList<>());
        }
        @Override
        protected void onActive(){
            super.onActive();
        }
    }
    public LiveData<List<Video>> getAll() {
        return videoAPI.getVideos();
    }

    public LiveData<List<Video>> getVideoByPrefix(String prefix) {
        return videoAPI.getVideosByPrefix(prefix);
    }
    public LiveData<Video> getVideoById(String videoId) {
        MutableLiveData<Video> videoData = new MutableLiveData<>();
        videoAPI.getVideoById(videoId, videoData);
        return videoData;
    }
    public LiveData<List<Video>> getVideosExcept(String videoId) {
        return videoAPI.getVideosExcept(videoId);
    }


}
