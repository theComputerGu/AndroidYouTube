package com.example.myapplication.Activities;

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
import com.example.myapplication.R;

public class BaseActivity extends AppCompatActivity {

    public static String ServerIP = "10.0.2.2";
    private static boolean isDarkMode = false;
    private AppDB appDB;
    private UserDao userDao;
    private VideoDao videoDao;
    private CommentDao commentDao;
    private VideoAPI videoApi;
    private UserAPI userApi;
    private CommentAPI commentApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply the theme based on the isDarkMode variable before setting the content view
        if (isDarkMode) {
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);

        appDB = Room.databaseBuilder(this, AppDB.class, "app_database")
                .fallbackToDestructiveMigration()
                .build();
        userDao = appDB.userDao();
        videoDao = appDB.videoDao();
        commentDao = appDB.commentDao();


        videoApi = new VideoAPI(ServerIP);
        userApi = new UserAPI(ServerIP);
        commentApi = new CommentAPI(ServerIP);

    }

    // Method to toggle dark mode
    protected void toggleDarkMode() {
        isDarkMode = !isDarkMode;
        recreate(); // Restart the activity to apply the new theme
    }
}
