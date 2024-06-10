package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.MediaController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.Serializable;
import java.util.List;

public class WatchVideoActivity extends AppCompatActivity implements PostsListAdapter.OnItemClickListener {

    // List to store comments
    private List<String> commentsList;

    // TextViews to display the number of likes, dislikes, and shares
    private TextView tvLikes;
    private TextView tvDislikes;
    private TextView tvShares;

    // Variables to keep track of the number of likes, dislikes, and shares
    private int likes = 0;
    private int dislikes = 0;
    private int shares = 0;
    // Declare RecyclerView and its adapter as class members
    private RecyclerView recyclerView;
    private PostsListAdapter adapter;
    private Post previousSelectedPost = null; // To keep track of the previously selected post


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_video);

        // Retrieve the selected post and all posts
        Post selectedPost = (Post) getIntent().getSerializableExtra("selectedPost");
        List<Post> allPosts = (List<Post>) getIntent().getSerializableExtra("allPosts");

        // If a previously selected post exists, add it back to the list
        if (previousSelectedPost != null) {
            allPosts.add(previousSelectedPost);
        }

        // Display the selected post content, author, etc.
        TextView tvAuthor = findViewById(R.id.tvAuthor);
        TextView tvContent = findViewById(R.id.tvContent);
        TextView tvDate = findViewById(R.id.tvDate);

        tvAuthor.setText(selectedPost.getAuthor());
        tvContent.setText(selectedPost.getContent());
        tvDate.setText(selectedPost.getDate());

        // Initialize the VideoView and set the video URL
        VideoView videoView = findViewById(R.id.videoView);
        String videoPath = "android.resource://" + getPackageName() + "/raw/" + selectedPost.getContent(); // Get video file name from the post object
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        // Add media controls to the VideoView
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        // Start the video
        videoView.start();

        // Remove the current selected post from the list
        allPosts.remove(selectedPost);

        // Initialize RecyclerView and its adapter
        recyclerView = findViewById(R.id.otherPostsRecyclerView);
        adapter = new PostsListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setPosts(allPosts);

        Button commentButton = findViewById(R.id.commentButton);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etComment = findViewById(R.id.commentEditText);
                String commentText = etComment.getText().toString();

                // Add the comment to the LinearLayout
                TextView commentTextView = new TextView(WatchVideoActivity.this);
                commentTextView.setText(commentText);
                LinearLayout commentLayout = findViewById(R.id.commentLayout);
                commentLayout.addView(commentTextView);

                // Add a line between comments
                View line = new View(WatchVideoActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        2 // Height of the line
                );
                layoutParams.setMargins(0, 8, 0, 8); // Adjust margins as needed
                line.setLayoutParams(layoutParams);
                line.setBackgroundColor(getResources().getColor(android.R.color.black));
                commentLayout.addView(line);

                // Clear the EditText
                etComment.getText().clear();
            }
        });

        // Set item click listener for the RecyclerView
        adapter.setOnItemClickListener(this);
        previousSelectedPost = selectedPost; // Update the previously selected post


        // Set item click listener for the RecyclerView
        adapter.setOnItemClickListener(this);
        previousSelectedPost = selectedPost; // Update the previously selected post

        // Initialize TextViews for likes, dislikes, and shares
        tvLikes = findViewById(R.id.tvLikes);
        tvDislikes = findViewById(R.id.tvDislikes);
        tvShares = findViewById(R.id.tvShares);

        // Update the initial count for likes, dislikes, and shares
        updateLikeDislikeShareCounts();
    }

    // Handle item click from the RecyclerView
    @Override
    public void onItemClick(Post post) {
        List<Post> posts = adapter.getPosts(); // Get the list of all posts from the adapter

        // Add the previously selected post back to the list if it exists
        if (previousSelectedPost != null && !posts.contains(previousSelectedPost)) {
            posts.add(previousSelectedPost);
        }

        // Remove the newly selected post from the list
        posts.remove(post);
        Intent intent = new Intent(this, WatchVideoActivity.class);
        intent.putExtra("selectedPost", post);
        intent.putExtra("allPosts", (Serializable) posts); // Pass the updated list of posts
        startActivity(intent);
        finish(); // Close current activity to prevent stacking
    }

    // Method to update the count for likes, dislikes, and shares
    private void updateLikeDislikeShareCounts() {
        tvLikes.setText(String.valueOf(likes));
        tvDislikes.setText(String.valueOf(dislikes));
        tvShares.setText(String.valueOf(shares));
    }

    // Method to handle the like button click
    public void onLikeButtonClick(View view) {
        likes++;
        updateLikeDislikeShareCounts();
    }

    // Method to handle the dislike button click
    public void onDislikeButtonClick(View view) {
        dislikes++;
        updateLikeDislikeShareCounts();
    }

    // Method to handle the share button click
    public void onShareButtonClick(View view) {
        shares++;
        updateLikeDislikeShareCounts();
    }
}
