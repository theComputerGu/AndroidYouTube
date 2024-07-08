package com.example.myapplication.Entities;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myapplication.API.AppDB;
import com.example.myapplication.API.VideoDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VideoRepository {
    private VideoDao videoDao;
    private LiveData<List<Video>> allVideos;
    private ExecutorService executorService;

    public VideoRepository(Application application) {
        AppDB db = AppDB.getDatabase(application);
        videoDao = db.videoDao();
        allVideos = videoDao.getAllVideos();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Video>> getAllVideos() {
        return allVideos;
    }

    public void insert(Video video) {
        executorService.execute(() -> videoDao.insertVideo(video));
    }

    public void update(Video video) {
        executorService.execute(() -> videoDao.updateVideo(video));
    }

    public void delete(Video video) {
        executorService.execute(() -> videoDao.deleteVideo(video));
    }

    public LiveData<Video> getVideoByTitle(String title) {
        return videoDao.getVideoByTitle(title);
    }

    public LiveData<List<Video>> getVideosByUsername(String username) {
        return videoDao.getVideosByUsername(username);
    }
}
