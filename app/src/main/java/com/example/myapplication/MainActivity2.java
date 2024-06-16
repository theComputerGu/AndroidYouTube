package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class MainActivity2 extends BaseActivity implements VideoAdapter.OnVideoClickListener {

    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_MODE = "dark_mode";
    private EditText searchEditText;
    private ImageButton searchButton;
    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    private List<Video> videoList;
    VideoListManager videoManager;
    UserManager userManager;
    ImageButton imageViewProfilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btnDarkMode = findViewById(R.id.btnDarkMode);
        btnDarkMode.setOnClickListener(v -> {
            SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            boolean darkMode = preferences.getBoolean(PREF_DARK_MODE, false);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(PREF_DARK_MODE, !darkMode);
            editor.apply();

            // Restart the activity to apply the new theme
            recreate();
        });

        // Initialize VideoListManager with context
        videoManager = VideoListManager.getInstance(this);
        userManager = UserManager.getInstance();

        // Get the video list from the singleton
        videoList = videoManager.getVideos();

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.lstPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VideoAdapter(videoList, VideoAdapter.VIEW_TYPE_MAIN, this);
        recyclerView.setAdapter(adapter);


        // Initialize the profile photo
        imageViewProfilePhoto = findViewById(R.id.imageViewProfilePhoto);
        // Retrieve signed-in user from UserManager
        User signedInUser = userManager.getSignedInUser();
        // Display profile photo and nickname if user is signed in
        if (signedInUser != null) {
            Bitmap photo = signedInUser.getPhoto();
            if (photo != null) {
                imageViewProfilePhoto.setImageBitmap(photo);
            } else {
                // Use default photo if photo is null
                imageViewProfilePhoto.setImageResource(R.drawable.ic_default_avatar);
            }
        }

        ImageButton buttonToHomePage = findViewById(R.id.buttonToHomePage);
        buttonToHomePage.setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity2.class);
            startActivity(i);
        });

        ImageButton buttonToAddVideoPage = findViewById(R.id.buttonToAddVideoPage);
        buttonToAddVideoPage.setOnClickListener(v -> {
            startAddVideoActivity();
        });

        // Initialize search EditText and Button
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.buttonSearch);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEditText.getText().toString().trim();
                if (!query.isEmpty()) {
                    VideoListManager videoManager = VideoListManager.getInstance(MainActivity2.this);
                    List<Video> searchResults = videoManager.searchVideos(query);
                    adapter.updateVideos(searchResults);
                }
            }
        });
    }
    private void startAddVideoActivity() {

        // Check if user is signed in
        if (userManager.getSignedInUser() == null) {
            Toast.makeText(this, "Please sign in.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, AddVideoActivity2.class);
        startActivity(intent);
    }
        @Override
    public void onVideoClick(Video video) {
        Intent intent = new Intent(this, WatchVideoActivity2.class);
        intent.putExtra("selectedVideoUsername", video.getUsername());
        intent.putExtra("selectedVideoTitle", video.getTitle());
        startActivity(intent);
    }

    public void onProfilePhotoClicked(View view) {
        Intent i = new Intent(this, LogInActivity.class);
        startActivity(i);
    }
    public void onSignOutClicked(View view) {
        if (userManager.getSignedInUser() == null) {
            Toast.makeText(this, "you are already signed out", Toast.LENGTH_SHORT).show();
        } else {
            userManager.signOut();
            imageViewProfilePhoto.setImageResource(R.drawable.ic_default_avatar);
            Toast.makeText(this, "singed out successfully", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, LogInActivity.class);
            startActivity(i);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        adapter.updateVideos(videoManager.getVideos());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Perform cleanup operations here
        clearFilesInInternalStorage();
    }

    private void clearFilesInInternalStorage() {
        // Your method to clear files from internal storage
        File[] files = getFilesDir().listFiles();
        if (files != null) {
            for (File file : files) {
                boolean deleted = file.delete();
                if (!deleted) {
                    Log.e("FileDeletion", "Failed to delete file: " + file.getName());
                }
            }
        }
    }
}
