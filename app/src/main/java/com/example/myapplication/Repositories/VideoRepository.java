package com.example.myapplication.Repositories;

import androidx.lifecycle.LiveData;

import com.example.myapplication.API.VideoAPI;
import com.example.myapplication.DB.AppDB;
import com.example.myapplication.DB.VideoDao;
import com.example.myapplication.Entities.Video;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class VideoRepository {
    private final VideoAPI videoAPI;
    private final VideoDao videoDao;
    private final Executor executor;

    public VideoRepository(){
        videoAPI = new VideoAPI();
        AppDB db = AppDB.getInstance();
        videoDao = db.videoDao();
        executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Video>> getAll() {
        LiveData<List<Video>> videosFromAPI = videoAPI.getVideos();
        videosFromAPI.observeForever(videos -> {
            if (videos != null) {
                executor.execute(() -> {
                    videoDao.deleteAll();
                    videoDao.insertAll(videos);
                });
            }
        });
        return videoDao.getAll();
    }
    public LiveData<List<Video>> getVideosExcept(String videoId) {
        LiveData<List<Video>> videosFromAPI = videoAPI.getVideos();
        videosFromAPI.observeForever(videos -> {
            if (videos != null) {
                executor.execute(() -> {
                    videoDao.deleteAll();
                    videoDao.insertAll(videos);
                });
            }
        });
        return videoDao.getAllExcept(videoId);
    }

    public LiveData<List<Video>> getVideoByPrefix(String prefix) {
        return videoAPI.getVideosByPrefix(prefix);
    }
    public LiveData<Video> getVideoById(String videoId) {
        return videoAPI.getVideoById(videoId);
    }

    public LiveData<Video> updateVideo(Video video) {
        return videoAPI.updateVideo(video.getVideoId(),video.getViews(), video.getLikedBy(),video.getDislikedBy());
    }
}
