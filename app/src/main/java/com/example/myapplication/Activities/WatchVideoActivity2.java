package com.example.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Entities.Comment;
import com.example.myapplication.Adapters.CommentAdapter;
import com.example.myapplication.Models.CustomMediaController;
import com.example.myapplication.R;
import com.example.myapplication.Entities.User;
import com.example.myapplication.Entities.Video;
import com.example.myapplication.Adapters.VideoAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WatchVideoActivity2 extends BaseActivity implements VideoAdapter.OnVideoClickListener, CommentAdapter.onCommentDelete {
    private Video currentVideo;
    private List<Video> otherVideos;
    private int counterForShrares = 0;
    private int counterForLikes = 0;
    private int counterForDislikes =0;
    private TextView tvLikes;
    private TextView tvDislikes;
    private TextView tvShares;
    private CommentAdapter adapter1;
    private ActivityResultLauncher<String[]> requestPermissionsLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_video);

        String selectedVideoUsername = getIntent().getStringExtra("selectedVideoUsername");
        String selectedVideoTitle = getIntent().getStringExtra("selectedVideoTitle");

        currentVideo = videoManager.getVideoByTag(selectedVideoUsername, selectedVideoTitle);
        otherVideos = videoManager.getVideosExcluding(currentVideo);

        // Initialize the TextViews
        tvLikes = findViewById(R.id.tvLikes);
        tvDislikes = findViewById(R.id.tvDislikes);
        tvShares = findViewById(R.id.tvShares);

        // Initialize the like, dislike, and share counts
        tvLikes.setText(String.valueOf(currentVideo.getLikes()));
        tvDislikes.setText(String.valueOf(currentVideo.getDislikes()));
        tvShares.setText(String.valueOf(currentVideo.getShares()));


        // Display the selected post content, author, etc.
        TextView tvAuthor = findViewById(R.id.tvAuthor);
        TextView tvContent = findViewById(R.id.tvContent);
        TextView tvDate = findViewById(R.id.tvDate);

        tvAuthor.setText(currentVideo.getUsername());
        tvContent.setText(currentVideo.getTitle());
        tvDate.setText(currentVideo.getDate());

        VideoView videoView = findViewById(R.id.videoView);
        String videoPath = currentVideo.getVideoPath();

        CustomMediaController mediaController = new CustomMediaController(this,videoView);
        videoView.setMediaController(mediaController);

        // Set the video path
        videoView.setVideoPath(videoPath);
        videoView.start();

        // Setup the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.otherPostsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        VideoAdapter adapter = new VideoAdapter(otherVideos, VideoAdapter.VIEW_TYPE_WATCH, this);
        recyclerView.setAdapter(adapter);

        //Setup the comments RecycleView
        RecyclerView commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter1 = new CommentAdapter(currentVideo.getComments(),this);
        commentsRecyclerView.setAdapter(adapter1);


        // Setup comment button click listener
        Button commentButton = findViewById(R.id.commentButton);
        commentButton.setOnClickListener(v -> {
                    if(userManager.getSignedInUser()!=null)
                    {
                        EditText commentEditText = findViewById(R.id.commentEditText);
                        String commentContent = commentEditText.getText().toString().trim();

                        if (!commentContent.isEmpty()) {
                            // Create a new comment object
                            Comment comment = new Comment(commentContent,userManager.getSignedInUser().getId(), getCurrentDate(), currentVideo.getId());

                            // Add the comment to the current video
                            currentVideo.addComment(comment);
                            adapter1.updateData(currentVideo.getComments());

                            // Clear the comment text field
                            commentEditText.setText("");
                        } else {
                            Toast.makeText(this, "Please enter a comment.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Please sign in", Toast.LENGTH_SHORT).show();
                    }
        });


        ImageButton btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(v -> {
            if(userManager.getSignedInUser()!=null)
            {
                for (User user : currentVideo.getUsersShares()) {
                    if(user.getUsername().equals(userManager.getSignedInUser().getUsername()))
                    {
                        Toast.makeText(this, "The User shared the video once already", Toast.LENGTH_SHORT).show();
                        counterForShrares=1;
                    }
                }
                if(counterForShrares==0)
                {
                    currentVideo.setShares();
                    tvShares.setText(String.valueOf(currentVideo.getShares()));
                    currentVideo.getUsersShares().add(userManager.getSignedInUser());
                }
                else {
                    counterForShrares= 0;
                }
            }
            else {
                Toast.makeText(this, "Please sign in", Toast.LENGTH_SHORT).show();
            }

        });

        // add dislike to current video
        ImageButton btnDislike = findViewById(R.id.btnDislike);
        btnDislike.setOnClickListener(v -> {
            if (userManager.getSignedInUser()!=null)
            {
                for (User user : currentVideo.getUsersDislike()) {
                    if(user.getUsername().equals(userManager.getSignedInUser().getUsername()))
                    {
                        Toast.makeText(this, "The User disliked the video once already", Toast.LENGTH_SHORT).show();
                        counterForDislikes=1;
                    }
                }
                if(counterForDislikes==0)
                {
                    currentVideo.setDislikes();
                    tvDislikes.setText(String.valueOf(currentVideo.getDislikes()));
                    currentVideo.getUsersDislike().add(userManager.getSignedInUser());
                }
                else {
                    counterForDislikes= 0;
                }
            }
            else {
                Toast.makeText(this, "Please sign in", Toast.LENGTH_SHORT).show();
            }

        });

        // add likes to current video
        ImageButton btnLike = findViewById(R.id.btnLike);
        btnLike.setOnClickListener(v -> {
            if(userManager.getSignedInUser()!=null)
            {
                for (User user : currentVideo.getUsersLike()) {
                    if(user.getUsername().equals(userManager.getSignedInUser().getUsername()) )
                    {
                        Toast.makeText(this, "The User liked the video once already", Toast.LENGTH_SHORT).show();
                        counterForLikes=1;
                    }
                }
                if(counterForLikes==0)
                {
                    currentVideo.setLikes();
                    tvLikes.setText(String.valueOf(currentVideo.getLikes()));
                    currentVideo.getUsersLike().add(userManager.getSignedInUser());
                }
                else {
                    counterForLikes= 0;
                }
            }else {
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
        intent.putExtra("selectedVideoUsername", video.getUsername());
        intent.putExtra("selectedVideoTitle", video.getTitle());
        startActivity(intent);
        finish();
    }

    //Method to handle the like button click
   public void onLikeButtonClick(View view) {
       currentVideo.setLikes();
       //int likes = currentVideo.getLikes();
       //tvLikes.setText(String.valueOf(likes));
   }

    public void onDisLikeButtonClick(View view) {
        currentVideo.setDislikes();
        //int Dislike = currentVideo.getDislikes();
       // tvDislikes.setText(String.valueOf(Dislike));
    }

    public void onShareButtonClick(View view) {
        currentVideo.setShares();
        //int Shares = currentVideo.getShares();
        //tvShares.setText(String.valueOf(Shares));
    }
    @Override
    public void onCommentDelete(Comment comment) {
        if (userManager.getSignedInUser() != null && userManager.getSignedInUser().getUsername().equals(getUserById(comment.getUserId()))) {
            currentVideo.removeComment(comment);
            adapter1.notifyDataSetChanged();
        }
        else {
            Toast.makeText(this, "You are not authorized to delete this comment", Toast.LENGTH_SHORT).show();
        }

    }
}
