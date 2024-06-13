package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WatchVideoActivity2 extends AppCompatActivity implements VideoAdapter.OnVideoClickListener {
    private Video currentVideo;
    private List<Video> otherVideos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_video);

        // Retrieve the current video from the intent
        currentVideo = (Video) getIntent().getSerializableExtra("selectedVideo");
        otherVideos = (List<Video>) getIntent().getSerializableExtra("otherVideos");

        // Display the selected post content, author, etc.
        TextView tvAuthor = findViewById(R.id.tvAuthor);
        TextView tvContent = findViewById(R.id.tvContent);
        TextView tvDate = findViewById(R.id.tvDate);

        tvAuthor.setText(currentVideo.getUsername());
        tvContent.setText(currentVideo.getTitle());
        tvDate.setText(currentVideo.getDate());

        // Initialize the VideoView and set the video URL
        VideoView videoView = findViewById(R.id.videoView);
        String videoPath = "android.resource://" + getPackageName() + "/raw/" + currentVideo.getTitle();
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        // Add media controls to the VideoView
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        // Start the video
        videoView.start();


        // Setup the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.otherPostsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        VideoAdapter adapter = new VideoAdapter(otherVideos, VideoAdapter.VIEW_TYPE_WATCH, this);
        recyclerView.setAdapter(adapter);

        // save comment and like handling
    }

    @Override
    public void onVideoClick(Video video) {
        Intent intent = new Intent(this, WatchVideoActivity2.class);
        intent.putExtra("video", video);

        startActivity(intent);
        finish();
    }
//    // Method to handle the like button click
//    public void onLikeButtonClick(View view) {
//        likes++;
//        updateLikeDislikeShareCounts();
//    }
//
//    // Method to handle the dislike button click
//    public void onDislikeButtonClick(View view) {
//        dislikes++;
//        updateLikeDislikeShareCounts();
//    }
//
//    // Method to handle the share button click
//    public void onShareButtonClick(View view) {
//        shares++;
//        updateLikeDislikeShareCounts();
//    }
}
