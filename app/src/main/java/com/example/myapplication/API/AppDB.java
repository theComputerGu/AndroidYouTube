package com.example.myapplication.API;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.myapplication.Entities.Comment;
import com.example.myapplication.Entities.User;
import com.example.myapplication.Entities.Video;

@Database(entities = {Video.class, User.class, Comment.class}, version = 1, exportSchema = false)
@TypeConverters({Convertors.class})
public abstract class AppDB extends RoomDatabase {

    private static volatile AppDB INSTANCE;

    public abstract VideoDao videoDao();
    public abstract UserDao userDao();
    public abstract CommentDao commentDao();

    public static AppDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDB.class, "app_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
