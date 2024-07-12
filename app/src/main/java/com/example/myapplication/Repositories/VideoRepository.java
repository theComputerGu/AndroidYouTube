package com.example.myapplication.Repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.API.VideoAPI;
import com.example.myapplication.Entities.Video;

import java.util.ArrayList;
import java.util.List;

public class VideoRepository {
    private VideoListData videoListData;
    private VideoAPI videoAPI;
    public VideoRepository(){
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
            VideoAPI videoAPI = new VideoAPI();
            videoAPI.getVideos(this);
        }
    }
    public void getAll() {
        videoAPI.getVideos(videoListData);
    }

    public LiveData<List<Video>> getVideoListData() {
        return videoListData;
    }

    public void getVideoByPrefix(String prefix) {
        videoAPI.getVideosByPrefix(prefix, videoListData);
    }
    public LiveData<Video> getVideoById(String videoId) {
        MutableLiveData<Video> videoData = new MutableLiveData<>();
        videoAPI.getVideoById(videoId, videoData);
        return videoData;
    }
    public void getVideosExcept(String videoId) {
        videoAPI.getVideosExcept(videoListData, videoId);
    }

}
