package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WatchVideoActivity2 extends AppCompatActivity implements VideoAdapter.OnVideoClickListener {
    private Video currentVideo;
    private List<Video> otherVideos;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_video);

        userManager = UserManager.getInstance();

        String selectedUsername = getIntent().getStringExtra("selectedUsername");
        String selectedVideoTitle = getIntent().getStringExtra("selectedVideoTitle");

        // Get the video list from the singleton
        VideoListManager videoManager = VideoListManager.getInstance(this);
        currentVideo = videoManager.getVideosByTag(selectedUsername, selectedVideoTitle);
        otherVideos = videoManager.getVideosExcluding(currentVideo);


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

//        // Setup the RecyclerView for comments
//        RecyclerView recyclerView = findViewById(R.id.commentsRecyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        commentAdapter = new CommentAdapter(this, comments);
//        recyclerView.setAdapter(commentAdapter);
//
//        // For testing, add a sample comment
//        User signedInUser = UserManager.getSignedInUser();
//        if (signedInUser != null) {
//            Comment sampleComment = new Comment(signedInUser.getUsername(), "This is a sample comment", new Date());
//            comments.add(sampleComment);
//            commentAdapter.notifyDataSetChanged();
//        }
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    public void onVideoClick(Video video) {
        Intent intent = new Intent(this, WatchVideoActivity2.class);
        intent.putExtra("selectedVideoUsername", video.getUsername());
        intent.putExtra("selectedVideoTitle", video.getTitle());
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
