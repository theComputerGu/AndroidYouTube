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
    public LiveData<List<Video>> getTopVideos() {
        return mRepository.getTopVideos();
    }
    public LiveData<List<Video>> getTcpVideos(String userId) {
        return mRepository.getTcpVideos(userId);
    }

    public LiveData<List<Video>> getVideoByPrefix(String prefix){
        return mRepository.getVideoByPrefix(prefix);
    }
    public LiveData<Video> guestWatchVideo(String videoId) {
        return mRepository.guestWatchVideo(videoId);
    }
    public LiveData<Video> userWatchVideo(String videoId, String userId) {
        return mRepository.userWatchVideo(videoId, userId);
    }
    public LiveData<List<Video>> getTopVideosExcept(String videoId) {
        return mRepository.getTopVideosExcept(videoId);
    }
    public LiveData<List<Video>> getTcpVideosExcept(String videoId, String userId) {
        return mRepository.getTcpVideosExcept(videoId, userId);
    }

    public LiveData<Video> updateVideo(Video video) {
        return mRepository.updateVideo(video);
    }


}