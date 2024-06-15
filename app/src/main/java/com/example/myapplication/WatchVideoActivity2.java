package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WatchVideoActivity2 extends BaseActivity implements VideoAdapter.OnVideoClickListener {
    private Video currentVideo;
    private List<Video> otherVideos;
    private UserManager userManager;
    private int counterForShrares = 0;
    private int counterForLikes = 0;
    private int counterForDislikes =0;
    private TextView tvLikes;
    private TextView tvDislikes;
    private TextView tvShares;
    private ActivityResultLauncher<String[]> requestPermissionsLauncher;
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
        Uri videoUri = currentVideo.getVideoUri();

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.setVideoURI(videoUri);
        videoView.start();


        // Setup the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.otherPostsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        VideoAdapter adapter = new VideoAdapter(otherVideos, VideoAdapter.VIEW_TYPE_WATCH, this);
        recyclerView.setAdapter(adapter);



//        Button commentTitle = findViewById(R.id.commentTitle);
//        commentTitle.setOnClickListener(v -> {
//            if()
//        });




        ImageButton btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(v -> {
            if(UserManager.getInstance().getSignedInUser()!=null)
            {
                for (User user : currentVideo.getUsersShares()) {
                    if(user.getUsername() == UserManager.getInstance().getSignedInUser().getUsername())
                    {
                        Toast.makeText(this, "The User shared the video once already", Toast.LENGTH_SHORT).show();
                        counterForShrares=1;
                    }
                }
                if(counterForShrares==0)
                {
                    currentVideo.setShares();
                    tvShares.setText(String.valueOf(currentVideo.getShares()));
                    currentVideo.getUsersShares().add(UserManager.getInstance().getSignedInUser());
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
            if (UserManager.getInstance().getSignedInUser()!=null)
            {
                for (User user : currentVideo.getUsersDislike()) {
                    if(user.getUsername() == UserManager.getInstance().getSignedInUser().getUsername())
                    {
                        Toast.makeText(this, "The User disliked the video once already", Toast.LENGTH_SHORT).show();
                        counterForDislikes=1;
                    }
                }
                if(counterForDislikes==0)
                {
                    currentVideo.setDislikes();
                    tvDislikes.setText(String.valueOf(currentVideo.getDislikes()));
                    currentVideo.getUsersDislike().add(UserManager.getInstance().getSignedInUser());
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
            if(UserManager.getInstance().getSignedInUser()!=null)
            {
                for (User user : currentVideo.getUsersLike()) {
                    if(user.getUsername() == UserManager.getInstance().getSignedInUser().getUsername())
                    {
                        Toast.makeText(this, "The User liked the video once already", Toast.LENGTH_SHORT).show();
                        counterForLikes=1;
                    }
                }
                if(counterForLikes==0)
                {
                    currentVideo.setLikes();
                    tvLikes.setText(String.valueOf(currentVideo.getLikes()));
                    currentVideo.getUsersLike().add(UserManager.getInstance().getSignedInUser());
                }
                else {
                    counterForLikes= 0;
                }
            }else {
                Toast.makeText(this, "Please sign in", Toast.LENGTH_SHORT).show();
            }

        });

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
}
