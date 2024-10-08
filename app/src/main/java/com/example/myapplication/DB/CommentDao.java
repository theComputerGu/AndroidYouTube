package com.example.myapplication.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myapplication.Entities.Comment;

import java.util.List;

@Dao
public interface CommentDao {
    @Query("SELECT * FROM comments")
    List<Comment> getAllComments();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Comment... comment);

    @Query("DELETE FROM comments")
    void deleteAllComments();

    @Delete
    void delete(Comment comment);
}
