package com.example.myapplication.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Video video);
    @Delete
    void delete(Video video);
    @Query("SELECT * FROM videos WHERE title LIKE :prefix || '%'")
    LiveData<List<Video>> getVideosByPrefix(String prefix);

    @Query("SELECT * FROM videos WHERE video_id = :videoId")
    LiveData<Video> getVideoById(String videoId);

    @Query("UPDATE videos SET likes = likes + 1 WHERE video_id = :videoId")
    void incrementLikes(String videoId);

    @Query("UPDATE videos SET dislikes = dislikes + 1 WHERE video_id = :videoId")
    void incrementDislikes(String videoId);

    @Query("UPDATE videos SET views = views + 1 WHERE video_id = :videoId")
    void incrementViews(String videoId);

    @Update
    void update(Video video);
}
