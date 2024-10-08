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

    public LiveData<List<Video>> getTopVideos() {
        LiveData<List<Video>> videosFromAPI = videoAPI.getTopVideos();
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
    public LiveData<List<Video>> getTopVideosExcept(String videoId) {
        LiveData<List<Video>> videosFromAPI = videoAPI.getTopVideos();
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
    public LiveData<List<Video>> getTcpVideos(String userId) {
        LiveData<List<Video>> videosFromAPI = videoAPI.getTcpVideos(userId);
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
    public LiveData<List<Video>> getTcpVideosExcept(String videoId, String userId) {
        LiveData<List<Video>> videosFromAPI = videoAPI.getTcpVideos(userId);
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
    public LiveData<Video> guestWatchVideo(String videoId) {
        return videoAPI.guestWatchVideo(videoId);
    }
    public LiveData<Video> userWatchVideo(String videoId, String userId) {
        return videoAPI.userWatchVideo(videoId, userId);
    }

    public LiveData<Video> updateVideo(Video video) {
        return videoAPI.updateVideo(video.getVideoId(),video.getViews(), video.getLikedBy(),video.getDislikedBy());
    }
}
