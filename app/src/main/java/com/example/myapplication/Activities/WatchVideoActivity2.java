package com.example.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.CommentAdapter;
import com.example.myapplication.Adapters.VideoAdapter;
import com.example.myapplication.Entities.Comment;
import com.example.myapplication.Entities.CustomMediaController;
import com.example.myapplication.Entities.Video;
import com.example.myapplication.Models.CommentViewModel;
import com.example.myapplication.Models.UserViewModel;
import com.example.myapplication.Models.VideoViewModel;
import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WatchVideoActivity2 extends BaseActivity implements VideoAdapter.OnVideoClickListener, CommentAdapter.onCommentDelete {
    private Video currentVideo;
    private TextView tvLikes;
    private TextView tvDislikes;
    private CommentAdapter commentAdapter;
    private VideoAdapter videoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_video);

        // Retrieve selected video ID from Intent
        String selectedVideoId = getIntent().getStringExtra("selectedVideoId");

        // Observe the current video
        videoViewModel.getVideoById(selectedVideoId).observe(this, video -> {
            if (video != null) {
                currentVideo = video;
                setupVideoDetails();
                setupCommentSection();
                observeOtherVideos();
            }
        });


        // Setup the RecyclerView for other videos
        RecyclerView recyclerView = findViewById(R.id.otherPostsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        videoAdapter = new VideoAdapter(null, VideoAdapter.VIEW_TYPE_WATCH, this);
        recyclerView.setAdapter(videoAdapter);

        videoViewModel.getVideosExcept(selectedVideoId);
        videoViewModel.get().observe(this, videos -> {
            videoAdapter.updateVideos(videos);
        });

        // Setup the comments RecyclerView
        RecyclerView commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(null, this);
        commentsRecyclerView.setAdapter(commentAdapter);

        // Setup comment button click listener
        Button commentButton = findViewById(R.id.commentButton);
        commentButton.setOnClickListener(v -> addComment());



        // Setup the dislike button click listener
        ImageButton btnDislike = findViewById(R.id.btnDislike);
        btnDislike.setOnClickListener(v -> dislikeVideo());

        // Setup the like button click listener
        ImageButton btnLike = findViewById(R.id.btnLike);
        btnLike.setOnClickListener(v -> likeVideo());
    }

    private void setupVideoDetails() {
        // Initialize the TextViews
        tvLikes = findViewById(R.id.tvLikes);
        tvDislikes = findViewById(R.id.tvDislikes);

        // Initialize the video details TextViews
        TextView tvAuthor = findViewById(R.id.tvAuthor);
        TextView tvContent = findViewById(R.id.tvContent);
        TextView tvDate = findViewById(R.id.tvDate);

        // Set the video details
        userViewModel.getUserByUsername(currentVideo.getAuthor()).observe(this, user -> {
            if (user != null) {
                String username = user.getUsername();
                tvAuthor.setText(username);
            }
        });
        tvContent.setText(currentVideo.getTitle());
        tvDate.setText(currentVideo.getTimeAgo().toString());

        // Initialize the like, dislike, and share counts
        tvLikes.setText(String.valueOf(currentVideo.getLikes()));
        tvDislikes.setText(String.valueOf(currentVideo.getDislikes()));

        // Setup the VideoView
        VideoView videoView = findViewById(R.id.videoView);
        CustomMediaController mediaController = new CustomMediaController(this, videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoPath(currentVideo.getPath());
        videoView.start();
    }

    private void setupCommentSection() {
        // Observe comments for the current video
        commentViewModel.getCommentsByVideoId(currentVideo.getId()).observe(this, comments -> {
            commentAdapter.updateData(comments);
        });
    }
    //

    private void observeOtherVideos() {
        // Observe other videos excluding the current video
        videoViewModel.getVideosExcept(currentVideo.getId());
        videoViewModel.get().observe(this, videos -> {
            videoAdapter.updateVideos(videos);
        });
    }

    private void addComment() {
        userViewModel.getSignedInUser().observe(this, user -> {
            if (user != null) {
                EditText commentEditText = findViewById(R.id.commentEditText);
                String commentContent = commentEditText.getText().toString().trim();

                if (!commentContent.isEmpty()) {
                    Comment comment = new Comment(commentContent, user.getId(), getCurrentDate(), currentVideo.getId());
                    commentViewModel.insert(comment);
                    commentEditText.setText("");
                } else {
                    Toast.makeText(this, "Please enter a comment.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please sign in", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void dislikeVideo() {
        userViewModel.getSignedInUser().observe(this, user -> {
            if (user != null) {
                if (currentVideo.getUsersDislike().contains(user.getUsername())) {
                    Toast.makeText(this, "The User disliked the video once already", Toast.LENGTH_SHORT).show();
                } else {
                    currentVideo.incrementDislikes(user.getUsername());
                    tvDislikes.setText(String.valueOf(currentVideo.getDislikes()));
                    videoViewModel.update(currentVideo);
                }
            } else {
                Toast.makeText(this, "Please sign in", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void likeVideo() {
        userViewModel.getSignedInUser().observe(this, user -> {
            if (user != null) {
                if (currentVideo.getUsersLike().contains(user.getUsername())) {
                    Toast.makeText(this, "The User liked the video once already", Toast.LENGTH_SHORT).show();
                } else {
                    currentVideo.incrementLikes(user.getUsername());
                    tvLikes.setText(String.valueOf(currentVideo.getLikes()));
                    videoViewModel.update(currentVideo);
                }
            } else {
                Toast.makeText(this, "Please sign in", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    public void onVideoClick(Video video) {
        Intent intent = new Intent(this, WatchVideoActivity2.class);
        intent.putExtra("selectedVideoId", video.getId());
        startActivity(intent);
        finish();
    }

    @Override
    public void onCommentDelete(Comment comment) {
        userViewModel.getSignedInUser().observe(this, user -> {
            if (user != null && user.getId() == (comment.getUserId())) {
                commentViewModel.delete(comment);
                Toast.makeText(this, "Comment deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "You are not authorized to delete this comment", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
