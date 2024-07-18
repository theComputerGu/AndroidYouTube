package com.example.myapplication.Activities;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.CommentAdapter;
import com.example.myapplication.Adapters.VideoAdapter;
import com.example.myapplication.Entities.Comment;
import com.example.myapplication.Entities.Video;
import com.example.myapplication.Helper;
import com.example.myapplication.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class WatchVideoActivity2 extends BaseActivity implements VideoAdapter.OnVideoClickListener, CommentAdapter.onCommentDelete {
    private Video currentVideo;
    private TextView tvLikes;
    private TextView tvDislikes;
    private CommentAdapter commentAdapter;
    private VideoAdapter videoAdapter;
    private ImageButton imageButtonProfilePhoto;

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
        commentAdapter = new CommentAdapter(new ArrayList<>(),this,true,this::CommentUpdate);
        commentsRecyclerView.setAdapter(commentAdapter);

        Button commentsButton = findViewById(R.id.commentsButton);
        RelativeLayout commentsSection = findViewById(R.id.commentsSection);

        commentsButton.setOnClickListener(v -> {
            if (commentsSection.getVisibility() == View.GONE) {
                commentsSection.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                commentsSection.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        // Setup comment button click listener
        Button commentButton = findViewById(R.id.addCommentButton);
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

        // Initialize the profile photo ImageView
        userViewModel.getUserByUsername(currentVideo.getAuthor()).observe(this, user -> {
            if (user != null) {
                imageButtonProfilePhoto = findViewById(R.id.imageViewProfilePhoto);
                Helper.loadPhotoIntoImageView(this, imageButtonProfilePhoto, user.getProfilePicture());
            }
        });

        // Initialize the video details TextViews
        TextView tvViews = findViewById(R.id.tvViews);
        TextView tvAuthor = findViewById(R.id.tvAuthor);
        TextView tvContent = findViewById(R.id.tvContent);
        TextView tvDate = findViewById(R.id.tvDate);

        tvViews.setText(currentVideo.getViewsString());
        tvAuthor.setText(currentVideo.getAuthorDisplayName());
        tvContent.setText(currentVideo.getTitle());
        tvDate.setText(currentVideo.calculateTimeElapsed());

        // Initialize the like, dislike, and share counts
        tvLikes.setText(String.valueOf(currentVideo.getLikes()));
        tvDislikes.setText(String.valueOf(currentVideo.getDislikes()));


        VideoView videoView = findViewById(R.id.videoView);
        Helper.loadVideoIntoVideoView(this, videoView, currentVideo.getPath());
    }

    private void setupCommentSection() {
        commentViewModel.getComments(currentVideo.getVideoId()).observe(this, comments -> {
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
        if (Helper.isSignedIn()) {
            EditText commentEditText = findViewById(R.id.commentEditText);
            String commentContent = commentEditText.getText().toString().trim();

            if (!commentContent.isEmpty()) {
                Comment comment = new Comment(
                        Helper.getSignedInUser().getUsername(),
                        Helper.getSignedInUser().getDisplayName(),
                        Helper.getSignedInUser().getProfilePicture(),
                        currentVideo.getVideoId(),
                        commentContent
                );

                Log.d(TAG, "addComment: Creating comment with content: " + commentContent);
                commentViewModel.createComment(currentVideo.getVideoId(),Helper.getSignedInUser().getUsername(),commentContent).observe(this, createdComment -> {
                    if (createdComment != null) {
                        Toast.makeText(this, "Comment added successfully.", Toast.LENGTH_SHORT).show();
                        commentEditText.setText("");
                        Log.d(TAG, "addComment: Comment added successfully.");
                        setupCommentSection();
                    } else {
                        Toast.makeText(this, "Failed to add comment.", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "addComment: Failed to add comment.");
                    }
                });

            } else {
                Toast.makeText(this, "Please enter a comment.", Toast.LENGTH_SHORT).show();
            }
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

    @Override
    public void onVideoClick(Video video) {
        Intent intent = new Intent(this, WatchVideoActivity2.class);
        intent.putExtra("selectedVideoId", video.getVideoId());
        startActivity(intent);
        finish();
    }

    @Override
    public void onCommentDelete(Comment comment) {

        if (Helper.isSignedIn()) {
            if (Helper.getSignedInUser().getUsername().equals(comment.getUsername())) {
                commentViewModel.deleteComment(comment.getVideoId(), comment.getText());
                Toast.makeText(this, "Comment deleted", Toast.LENGTH_SHORT).show();
                setupCommentSection();
            } else {
                Toast.makeText(this, "You are not authorized to delete this comment", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Please sign in", Toast.LENGTH_SHORT).show();
        }
    }
    public void onProfilePhotoClicked(View view) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("username", currentVideo.getAuthor());
        startActivity(i);
    }

    public void CommentUpdate(Comment comment) {
        if (Helper.isSignedIn()) {
            if (Helper.getSignedInUser().getUsername().equals(comment.getUsername())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Update comment");

                final EditText input = new EditText(this);
                input.setText(comment.getText());
                builder.setView(input);

                builder.setPositiveButton("Update", (dialog, which) -> {
                    String newText = input.getText().toString();
                    Comment updatedComment = new Comment(Helper.getSignedInUser().getUsername(),currentVideo.getTitle(), Helper.getSignedInUser().getProfilePicture(), currentVideo.getVideoId(), newText);
                    updatedComment.setText(newText);
                    commentViewModel.updateComment(comment,currentVideo.getVideoId()).observe(this, result -> {
                        Log.d(TAG, "addComment: Comment added successfully.");
                        if (result != null) {
                            Toast.makeText(this, "Comment updated successfully", Toast.LENGTH_SHORT).show();
                            setupCommentSection();
                        } else {
                             Toast.makeText(this, "Failed to update comment", Toast.LENGTH_SHORT).show();
                        }
                    });
                });

                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

                builder.show();
            } else {
                Toast.makeText(this, "You are not authorized to update this comment", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Please sign in", Toast.LENGTH_SHORT).show();
        }
    }
}
