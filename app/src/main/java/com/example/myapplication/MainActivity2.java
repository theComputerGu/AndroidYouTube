package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity2 extends AppCompatActivity implements VideoAdapter.OnVideoClickListener {
    private EditText searchEditText;
    private ImageButton searchButton;
    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    private List<Video> videoList;
    VideoListManager videoManager;
    UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        ImageButton imageViewProfilePhoto = findViewById(R.id.imageViewProfilePhoto);
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
            Toast.makeText(this, "Please sign in to add a video.", Toast.LENGTH_SHORT).show();
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
}
