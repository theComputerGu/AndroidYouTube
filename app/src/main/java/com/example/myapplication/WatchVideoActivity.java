package com.example.myapplication;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class WatchVideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_video);

        VideoView videoView = findViewById(R.id.videoView);
        TextView tvAuthor = findViewById(R.id.tvAuthor);
        TextView tvContent = findViewById(R.id.tvContent);
        ImageView ivPic = findViewById(R.id.ivPic);

        String videoUri = getIntent().getStringExtra("videoUri");
        String postAuthor = getIntent().getStringExtra("postAuthor");
        String postContent = getIntent().getStringExtra("postContent");
        String postDate = getIntent().getStringExtra("postDate");
        int postPic = getIntent().getIntExtra("postPic", -1);

        if (videoUri != null) {
            videoView.setVideoURI(Uri.parse(videoUri));
            videoView.start();
        }

        tvAuthor.setText(postAuthor);
        tvContent.setText(postContent);
        if (postPic != -1) {
            ivPic.setImageResource(postPic);
        }
    }
}
