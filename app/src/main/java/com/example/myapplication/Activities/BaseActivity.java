package com.example.myapplication.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;
import com.example.myapplication.API.AppDB;
import com.example.myapplication.API.CommentDao;
import com.example.myapplication.API.UserDao;
import com.example.myapplication.API.VideoDao;
import com.example.myapplication.Models.CommentViewModel;
import com.example.myapplication.Models.UserViewModel;
import com.example.myapplication.Models.VideoViewModel;
import com.example.myapplication.R;

public class BaseActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String PREF_DARK_MODE = "darkMode";
    private static boolean isDarkMode = false;

    protected AppDB appDB;
    protected UserDao userDao;
    protected VideoDao videoDao;
    protected CommentDao commentDao;
    protected VideoViewModel videoViewModel;
    protected UserViewModel userViewModel;
    protected CommentViewModel commentViewModel;
    protected SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        isDarkMode = sharedPreferences.getBoolean(PREF_DARK_MODE, false);
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

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Initialize ViewModelProvider with this activity
        videoViewModel = new ViewModelProvider(this).get(VideoViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);
    }

    // Method to toggle dark mode
    protected void toggleDarkMode() {
        isDarkMode = !isDarkMode;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_DARK_MODE, isDarkMode);
        editor.apply();
        recreate();
    }
}
