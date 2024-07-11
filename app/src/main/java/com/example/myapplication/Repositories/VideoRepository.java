package com.example.myapplication.Repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Entities.Video;

import java.util.ArrayList;
import java.util.List;

public class VideoRepository {
    private VideoListData videoListData;
    public VideoRepository(){
        videoListData= new VideoListData();
        //api = new VideoAPI(VideoListData);

    }
    public void setVideos(List<Video> Videos) {
        videoListData.setValue(Videos);
    }


    public static class VideoListData extends MutableLiveData<List<Video>> {
        public VideoListData(){
            super();
            setValue(new ArrayList<>());
        }
        @Override
        protected void onActive(){
            super.onActive();
            new Thread(()-> {
                //VideoListData.VideoValue(dao.get());
            }).start();
        }

    }
    public LiveData<List<Video>> getAll() {return videoListData; }
}
