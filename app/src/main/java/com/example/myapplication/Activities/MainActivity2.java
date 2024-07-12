package com.example.myapplication.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.VideoAdapter;
import com.example.myapplication.Entities.Video;
import com.example.myapplication.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity2 extends BaseActivity implements VideoAdapter.OnVideoClickListener {

    private EditText searchEditText;
    private ImageButton searchButton;
    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    ImageButton imageViewProfilePhoto;
    private List<Video> videoList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initialize the profile photo
        imageViewProfilePhoto = findViewById(R.id.imageViewProfilePhoto);
        // Retrieve signed-in user from UserManager
        // Retrieve signed-in user from UserManager
        if (signedInUser != null) {
            // Display profile photo if available
            Bitmap photo = signedInUser.getProfilePicture();
            if (photo != null) {
                imageViewProfilePhoto.setImageBitmap(photo);
            } else {
                // Use default photo if photo is null
                imageViewProfilePhoto.setImageResource(R.drawable.ic_default_avatar);
            }
        } else {
            // Handle case when user is not signed in
            imageViewProfilePhoto.setImageResource(R.drawable.ic_default_avatar);
        }

        recyclerView = findViewById(R.id.lstPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity2.this));
        adapter = new VideoAdapter(null, VideoAdapter.VIEW_TYPE_MAIN, MainActivity2.this); // Pass null initially
        recyclerView.setAdapter(adapter);
        videoViewModel.get().observe(this, videos -> {
            adapter.updateVideos(videos);
        });

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
                    videoViewModel.getVideoByPrefix(query).observe(this, videos -> {
                        adapter.updateVideos(videos);
                    });
                }
            }
        });
    }
    private void startAddVideoActivity() {

        // Check if user is signed in
        if (signedInUser == null) {
            Toast.makeText(this, "Please sign in.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, AddVideoActivity2.class);
        startActivity(intent);
    }
        @Override
    public void onVideoClick(Video video) {
        Intent intent = new Intent(this, WatchVideoActivity2.class);
        intent.putExtra("selectedVideoId", video.getId());
        startActivity(intent);
    }

    public void onProfilePhotoClicked(View view) {
        Intent i = new Intent(this, LogInActivity.class);
        startActivity(i);
    }
    public void onSignOutClicked(View view) {
        if (signedInUser == null) {
            Toast.makeText(this, "you are already signed out", Toast.LENGTH_SHORT).show();
        } else {
            signedInUser = null;
            imageViewProfilePhoto.setImageResource(R.drawable.ic_default_avatar);
            Toast.makeText(this, "singed out successfully", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, LogInActivity.class);
            startActivity(i);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        videoApi.getVideos(new Callback<List<Video>>() {
            @Override
            public void onResponse(@NonNull Call<List<Video>> call, @NonNull Response<List<Video>> response) {
                videoList = response.body();
                videoDao = appDB.videoDao();
                for(Video p:videoList){
                    new Thread(() -> videoDao.insert(p)).start();
                }
                // Initialize RecyclerView and Adapter

                videoViewModel.setVideos(videoList);
                videoViewModel.get().observe(MainActivity2.this, posts -> adapter.updateVideos(videoList));
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<List<Video>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
    public void onDarkModeClicked(View view) {
        toggleDarkMode();
    }
}
