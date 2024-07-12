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
        mRepository.getAll();
    }
    public LiveData<List<Video>> get() {
        return mRepository.getVideoListData();
    }
    public void setVideos(List<Video> v) {
        mRepository.setVideos(v);
    }
    public void getAll() {
        mRepository.getAll();
    }

    public void getVideoByPrefix(String prefix){
        mRepository.getVideoByPrefix(prefix);
    }
    public LiveData<Video> getVideoById(String videoId) {
        return mRepository.getVideoById(videoId);
    }
    public void getVideosExcept(String videoId) {
        mRepository.getVideosExcept(videoId);
    }
}