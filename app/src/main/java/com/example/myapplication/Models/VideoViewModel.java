package com.example.myapplication.Models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Entities.Video;
import com.example.myapplication.Repositories.VideoRepository;

import java.util.List;

public class VideoViewModel extends ViewModel {
    private VideoRepository mRepository;
    public VideoViewModel(){
        mRepository = new VideoRepository();
    }
    public LiveData<List<Video>> getAll() {
        return mRepository.getAll();
    }
    public void setVideos(List<Video> v) {
        mRepository.setVideos(v);
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

}