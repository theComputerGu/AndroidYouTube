package com.example.myapplication.Models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Entities.Video;
import com.example.myapplication.Repositories.VideoRepository;

import java.util.List;

public class VideoViewModel extends ViewModel {
    private VideoRepository mRepository;
    private static VideoViewModel instance;

    private VideoViewModel(){
        mRepository = new VideoRepository();
    }
    // Public static method to access the singleton instance
    public static synchronized VideoViewModel getInstance() {
        if (instance == null) {
            instance = new VideoViewModel();
        }
        return instance;
    }
    public LiveData<List<Video>> getAll() {
        return mRepository.getAll();
    }

    public LiveData<List<Video>> getVideoByPrefix(String prefix){
        return mRepository.getVideoByPrefix(prefix);
    }
    public LiveData<Video> getVideoById(String videoId) {
        return mRepository.getVideoById(videoId);
    }
    public LiveData<List<Video>> getVideosExcept(String videoId) {
        return mRepository.getVideosExcept(videoId);
    }

    public LiveData<Video> updateVideo(Video video) {
        return mRepository.updateVideo(video);
    }


}