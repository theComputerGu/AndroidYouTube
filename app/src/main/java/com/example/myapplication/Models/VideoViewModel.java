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
    public LiveData<List<Video>> getVideoListData() {
        return videos;
    }
    public void getAll() {
        mRepository.getAll();
    }
    public void setVideos(List<Video> v) {
        mRepository.setVideos(v);
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