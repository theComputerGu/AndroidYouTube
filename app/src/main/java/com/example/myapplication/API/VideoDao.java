package com.example.myapplication.API;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.Entities.Video;

import java.util.List;

@Dao
public interface VideoDao {

    // Insert a single video
    @Insert
    void insertVideo(Video video);

    // Insert multiple videos
    @Insert
    void insertVideos(List<Video> videos);

    // Update a video
    @Update
    void updateVideo(Video video);

    // Delete a video
    @Delete
    void deleteVideo(Video video);

    // Query to get all videos
    @Query("SELECT * FROM Video")
    LiveData<List<Video>> getAllVideos();

    @Query("SELECT * FROM Video WHERE title = :title")
    LiveData<Video> getVideoByTitle(String title);

    @Query("SELECT * FROM Video WHERE idUserName = :username")
    LiveData<List<Video>> getVideosByUsername(String username);

    // Delete all videos
    @Query("DELETE FROM Video")
    void deleteAllVideos();
}
