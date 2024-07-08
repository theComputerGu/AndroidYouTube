package com.example.myapplication.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.UserManager;
import com.example.myapplication.VideoListManager;

public class BaseActivity extends AppCompatActivity {

    private static boolean isDarkMode = false;
    protected VideoListManager videoManager;
    protected UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply the theme based on the isDarkMode variable before setting the content view
        if (isDarkMode) {
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);

        // Initialize VideoListManager with context
        videoManager = VideoListManager.getInstance(this);
        userManager = UserManager.getInstance();
    }

    // Method to toggle dark mode
    protected void toggleDarkMode() {
        isDarkMode = !isDarkMode;
        recreate(); // Restart the activity to apply the new theme
    }
}