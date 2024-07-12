package com.example.myapplication.Activities;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myapplication.API.AppDB;
import com.example.myapplication.API.CommentAPI;
import com.example.myapplication.API.CommentDao;
import com.example.myapplication.API.UserAPI;
import com.example.myapplication.API.UserDao;
import com.example.myapplication.API.VideoAPI;
import com.example.myapplication.API.VideoDao;
import com.example.myapplication.Entities.User;
import com.example.myapplication.Models.CommentViewModel;
import com.example.myapplication.Models.UserViewModel;
import com.example.myapplication.Models.VideoViewModel;
import com.example.myapplication.R;

public class BaseActivity extends AppCompatActivity {

    private static boolean isDarkMode = false;
    protected AppDB appDB;
    protected UserDao userDao;
    protected VideoDao videoDao;
    protected CommentDao commentDao;
    protected VideoAPI videoApi;
    protected UserAPI userApi;
    protected CommentAPI commentApi;
    protected User signedInUser;
    protected VideoViewModel videoViewModel;
    protected UserViewModel userViewModel;
    protected CommentViewModel commentViewModel;
    public static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply the theme based on the isDarkMode variable before setting the content view
        if (isDarkMode) {
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        appDB = Room.databaseBuilder(this, AppDB.class, "app_database")
                .fallbackToDestructiveMigration()
                .build();
        userDao = appDB.userDao();
        videoDao = appDB.videoDao();
        commentDao = appDB.commentDao();

        videoViewModel = new VideoViewModel();
        userViewModel = new UserViewModel();
        commentViewModel = new CommentViewModel();
    }

    // Method to toggle dark mode
    protected void toggleDarkMode() {
        isDarkMode = !isDarkMode;
        recreate(); // Restart the activity to apply the new theme
    }
}
