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
import com.example.myapplication.Helper;
import com.example.myapplication.Models.CommentViewModel;
import com.example.myapplication.Models.VideoViewModel;
import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        videoAdapter = new VideoAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(videoAdapter);


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

        tvAuthor.setText(currentVideo.getAuthorDisplayName());
        tvContent.setText(currentVideo.getTitle());
        tvDate.setText(currentVideo.getTimeAgo().toString());

        // Initialize the like, dislike, and share counts
        tvLikes.setText(String.valueOf(currentVideo.getLikes()));
        tvDislikes.setText(String.valueOf(currentVideo.getDislikes()));


        VideoView videoView = findViewById(R.id.videoView);
        Helper.loadVideoIntoVideoView(this, videoView, currentVideo.getPath());
        // Setup the VideoView
        //VideoView videoView = findViewById(R.id.videoView);
        //CustomMediaController mediaController = new CustomMediaController(this, videoView);
        //videoView.setMediaController(mediaController);
        //Helper.loadVideoIntoVideoView(this, videoView, currentVideo.getPath());
        //videoView.setVideoPath(Helper.context.getString(R.string.baseServerURL)+currentVideo.getPath());
        //videoView.start();
    }

    private void setupCommentSection() {
        commentViewModel.get().observe(this, comments -> {
            commentAdapter.updateData(comments);
        });
    }

    private void observeOtherVideos() {
        // Observe other videos excluding the current video
        videoViewModel.getVideosExcept(currentVideo.getVideoId()).observe(this, videos -> {
            videoAdapter.updateVideos(videos);
        });
    }

    private void addComment() {
        if ( Helper.isSignedIn() ) {
            EditText commentEditText = findViewById(R.id.commentEditText);
            String commentContent = commentEditText.getText().toString().trim();
            videoViewModel.getVideoById(currentVideo.getVideoId()).observe(this, user -> {
                if (!commentContent.isEmpty()) {
                    Comment comment = new Comment(user.getAuthor(), currentVideo.getTitle(),user.getPhoto() ,currentVideo.getVideoId(),commentContent);
                    commentViewModel.createComment(comment);
                    commentEditText.setText("");
                } else {
                    Toast.makeText(this, "Please enter a comment.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Please sign in", Toast.LENGTH_SHORT).show();
        }
    }


    private void dislikeVideo() {
        if ( Helper.isSignedIn()) {
            videoViewModel.getVideoById(currentVideo.getVideoId()).observe(this, user -> {
                if (currentVideo.getDislikedBy().contains(Helper.getSignedInUser().getUsername())) {
                    Toast.makeText(this, "The User disliked the video once already", Toast.LENGTH_SHORT).show();
                } else {
                    currentVideo.incrementDislikes(Helper.getSignedInUser().getUsername());
                    tvDislikes.setText(String.valueOf(currentVideo.getDislikes()));
                }
            });
        }
        else {
            Toast.makeText(this, "Please sign in", Toast.LENGTH_SHORT).show();
        }
    }

    private void likeVideo() {
        if ( Helper.isSignedIn() ) {
            videoViewModel.getVideoById(currentVideo.getVideoId()).observe(this, user -> {
                if (currentVideo.getLikedBy().contains(Helper.getSignedInUser().getUsername())) {
                    Toast.makeText(this, "The User liked the video once already", Toast.LENGTH_SHORT).show();
                } else {
                    currentVideo.incrementLikes(Helper.getSignedInUser().getUsername());
                    tvLikes.setText(String.valueOf(currentVideo.getLikes()));
                }
            });
        }
        else {
            Toast.makeText(this, "Please sign in", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    public void onVideoClick(Video video) {
        Intent intent = new Intent(this, WatchVideoActivity2.class);
        intent.putExtra("selectedVideoId", video.getVideoId());
        startActivity(intent);
        finish();
    }

    @Override
    public void onCommentDelete(Comment comment) {

        if ( Helper.isSignedIn() ) {
            commentViewModel.getComments(comment).observe(this, user -> {
                if (Helper.getSignedInUser().getUsername().equals (comment.getUsername())) {
                    commentViewModel.deleteComment(comment);
                    Toast.makeText(this, "Comment deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "You are not authorized to delete this comment", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(this, "Please sign in", Toast.LENGTH_SHORT).show();
        }
    }
}
