package com.example.myapplication.API;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.Entities.Comment;

import java.util.List;

@Dao
public interface CommentDao {
    @Insert
    void insertComment(Comment comment);

    @Update
    void updateComment(Comment comment);

    @Delete
    void deleteComment(Comment comment);

    @Query("SELECT * FROM Comment WHERE video_id = :videoId")
    LiveData<List<Comment>> getCommentsByVideoId(int videoId);
}
