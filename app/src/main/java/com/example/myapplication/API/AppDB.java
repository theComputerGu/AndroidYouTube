package com.example.myapplication.API;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.myapplication.Entities.Comment;
import com.example.myapplication.Entities.User;
import com.example.myapplication.Entities.Video;

@Database(entities = {Video.class, User.class, Comment.class}, version = 1, exportSchema = false)
@TypeConverters({Convertors.class})
public abstract class AppDB extends RoomDatabase {

    public abstract VideoDao videoDao();
    public abstract UserDao userDao();
    public abstract CommentDao commentDao();
    public void clearAllTables() {
    }
}
