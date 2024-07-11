package com.example.myapplication.API;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myapplication.Entities.Video;

import java.util.List;

@Dao
public interface VideoDao {
    @Query("SELECT * FROM videos")
    List<Video> getAllVideos();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Video... videos);

    @Query("DELETE FROM videos")
    void deleteAllVideos();

    @Delete
    void delete(Video video);
}
