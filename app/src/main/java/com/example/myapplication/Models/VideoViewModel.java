package com.example.myapplication.Models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplication.Entities.Video;
import com.example.myapplication.Entities.VideoRepository;

import java.util.List;

public class VideoViewModel extends AndroidViewModel {
    private VideoRepository repository;
    private LiveData<List<Video>> allVideos;

    public VideoViewModel(Application application) {
        super(application);
        repository = new VideoRepository(application);
        allVideos = repository.getAllVideos();
    }

    public LiveData<List<Video>> getAllVideos() {
        return allVideos;
    }

    public void insert(Video video) {
        repository.insert(video);
    }

    public void update(Video video) {
        repository.update(video);
    }

    public void delete(Video video) {
        repository.delete(video);
    }

    public LiveData<Video> getVideoByTitle(String title) {
        return repository.getVideoByTitle(title);
    }

    public LiveData<List<Video>> getVideosByUsername(String username) {
        return repository.getVideosByUsername(username);
    }
}
