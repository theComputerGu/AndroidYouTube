package com.example.myapplication.Repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.API.AppDB;
import com.example.myapplication.API.VideoAPI;
import com.example.myapplication.API.VideoDao;
import com.example.myapplication.Entities.Video;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class VideoRepository {
    private VideoListData videoListData;
    private VideoAPI videoAPI;
    private VideoDao videoDao;
    private Executor executor;

    public VideoRepository(){
        videoAPI = new VideoAPI();
        videoListData= new VideoListData();
        AppDB db = AppDB.getInstance();
        videoDao = db.videoDao();
        executor = Executors.newSingleThreadExecutor();
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
        // Fetch videos from the API
        LiveData<List<Video>> videosFromAPI = videoAPI.getVideos();

        videosFromAPI.observeForever(videos -> {
            if (videos != null) {
                executor.execute(() -> {
                    videoDao.deleteAll();
                    videoDao.insertAll(videos);
                });
            }
        });

        // Return videos from the DAO
        return videoDao.getAll();
    }
    public LiveData<List<Video>> getVideosExcept(String videoId) {
        return videoDao.getAllExcept(videoId);
    }

    public LiveData<List<Video>> getVideoByPrefix(String prefix) {
        return videoAPI.getVideosByPrefix(prefix);
    }
    public LiveData<Video> getVideoById(String videoId) {
        MutableLiveData<Video> videoData = new MutableLiveData<>();
        videoAPI.getVideoById(videoId, videoData);
        return videoData;
    }



}
