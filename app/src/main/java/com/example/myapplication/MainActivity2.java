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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity implements VideoAdapter.OnVideoClickListener {
    private EditText searchEditText;
    private ImageButton searchButton;
    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    private List<Video> videoList;
    private List<Serializable> serializablevideos = new ArrayList<>();
    private Video previousSelectedvideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize VideoListManager with context
        VideoListManager videoManager = VideoListManager.getInstance(this);

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
        User signedInUser = UserManager.getSignedInUser();
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
        for (Video video : videoList) {
            serializablevideos.add(video);
        }
    }
    private void startAddVideoActivity() {
        // Retrieve signed-in user from UserManager
        User signedInUser = UserManager.getSignedInUser();

        // Check if user is signed in
        if (signedInUser == null) {
            Toast.makeText(this, "Please sign in to add a video.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the video list manager instance
        VideoListManager videoManager = VideoListManager.getInstance(this);

        // Filter videos by signed-in user's username
        List<Video> userVideos = videoManager.getVideosByUser(signedInUser);

        // Start AddVideoActivity2 and pass filtered videos
        Intent intent = new Intent(this, AddVideoActivity2.class);
        intent.putExtra("userVideos", (ArrayList<Video>) userVideos);
        startActivity(intent);
    }
        @Override
    public void onVideoClick(Video video) {
        List<Video> videos = new ArrayList<>();
        for (Serializable serializable : serializablevideos) {
            if (serializable instanceof Video) {
                videos.add((Video) serializable);
            }
        }

        // Add the previously selected video back to the list if it exists
        if (previousSelectedvideo != null && !videos.contains(previousSelectedvideo)) {
            videos.add(previousSelectedvideo);
        }

        // Remove the newly selected video from the list
        videos.remove(video);
        serializablevideos.clear();
        serializablevideos.addAll(videos);

        Intent intent = new Intent(this, WatchVideoActivity2.class);
        intent.putExtra("selectedVideo", video);
            intent.putExtra("otherVideos", new ArrayList<>(serializablevideos));
        previousSelectedvideo = video; // Update the previously selected video
        startActivity(intent);
    }

    public void onProfilePhotoClicked(View view) {
        Intent i = new Intent(this, LogInActivity.class);
        startActivity(i);
    }
}
