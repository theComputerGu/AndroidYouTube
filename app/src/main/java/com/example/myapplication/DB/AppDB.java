package com.example.myapplication.DB;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.myapplication.Entities.Comment;
import com.example.myapplication.Entities.User;
import com.example.myapplication.Entities.Video;
import com.example.myapplication.Helper;

import java.util.concurrent.Executors;

@Database(entities = {Video.class, User.class, Comment.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDB extends RoomDatabase {
    private static AppDB instance;
    private static final Object LOCK = new Object();

    public abstract VideoDao videoDao();
    public abstract UserDao userDao();
    public abstract CommentDao commentDao();
    public static AppDB getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                instance = Room.databaseBuilder(Helper.context, AppDB.class, "app_database")
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return instance;
    }
    public void clearDatabase() {
        Executors.newSingleThreadExecutor().execute(() -> {
            videoDao().deleteAll();
        });
    }
}
