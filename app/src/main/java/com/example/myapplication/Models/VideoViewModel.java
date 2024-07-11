package com.example.myapplication.Models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Entities.Video;
import com.example.myapplication.Repositories.VideoRepository;

import java.util.List;

public class VideoViewModel extends ViewModel {
    private VideoRepository mRepository;
    private LiveData<List<Video>> videos;
    public VideoViewModel(){
        mRepository = new VideoRepository();
        videos = mRepository.getAll();
    }
    public LiveData<List<Video>> get() {return videos; }
    public void setVideos(List<Video> v) {
        mRepository.setVideos(v);
    }
}