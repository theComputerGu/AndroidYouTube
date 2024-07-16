package com.example.myapplication.API;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myapplication.Entities.Video;

import java.util.List;

@Dao
public interface VideoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Video> videos);

    @Query("DELETE FROM videos")
    void deleteAll();

    @Query("SELECT * FROM videos")
    LiveData<List<Video>> getAll();

    @Query("SELECT * FROM videos WHERE video_id != :videoId")
    LiveData<List<Video>> getAllExcept(String videoId);
}
