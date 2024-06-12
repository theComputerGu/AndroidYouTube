package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PostsListAdapter.OnItemClickListener {

    // Declare serializablePosts and previousSelectedPost as class members
    private ArrayList<Serializable> serializablePosts = new ArrayList<>();
    private Post previousSelectedPost = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize views
        ImageButton imageViewProfilePhoto = findViewById(R.id.imageViewProfilePhoto);

        // Retrieve signed-in user from UserManager
        User signedInUser = UserManager.getSignedInUser();
        // Display profile photo and nickname if user is signed in
        if (signedInUser != null) {
            Bitmap photo = signedInUser.getPhoto();
            if (photo != null) {
                imageViewProfilePhoto.setImageBitmap(photo);
            } else {
                // Use default photo if photo is null
                imageViewProfilePhoto.setImageResource(R.drawable.ic_default_avatar);
            }

        }

        ImageButton buttonToHomePage = findViewById(R.id.buttonToHomePage);
        buttonToHomePage.setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        });

        ImageButton buttonToAddVideoPage = findViewById(R.id.buttonToAddVideoPage);
        buttonToAddVideoPage.setOnClickListener(v -> {
            Intent i = new Intent(this, AddVideoActivity.class);
            startActivity(i);
        });

        RecyclerView lstVideos = findViewById(R.id.lstPosts);
        final PostsListAdapter adapter = new PostsListAdapter(this);
        adapter.setOnItemClickListener(this);
        lstVideos.setAdapter(adapter);
        lstVideos.setLayoutManager(new LinearLayoutManager(this));

        List<Post> posts = new ArrayList<>();
        posts.add(new Post("boat", "Author1", R.drawable.boat, "4/09/2004"));
        posts.add(new Post("buildings", "Author2", R.drawable.buildings, "3/04/2022"));
        posts.add(new Post("ferris_wheel", "Author3", R.drawable.ferris_wheel,"3/03/2021"));
        posts.add(new Post("girl_flowers", "Author4", R.drawable.girl_flowers,"5/08/2001"));
        posts.add(new Post("hilton", "Author5", R.drawable.hilton,"6/07/2003"));
        posts.add(new Post("hard_rock_cafe", "Author6", R.drawable.hard_rock_cafe,"5/04/2018"));
        posts.add(new Post("keyboard", "Author7", R.drawable.keyboard,"5/05/2001"));
        posts.add(new Post("kite", "Author8", R.drawable.kite,"6/07/2020"));
        posts.add(new Post("men_sunset", "Author9", R.drawable.men_sunset,"3/01/2020"));
        posts.add(new Post("road", "Author10", R.drawable.road,"1/01/2011"));
        posts.add(new Post("women_reading", "Author11", R.drawable.women_reading,"3/01/2021"));
        adapter.setPosts(posts);

        ImageButton searchButton = findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(v -> {
            // Get search query entered by the user
            EditText searchEditText = findViewById(R.id.searchEditText);
            String searchQuery = searchEditText.getText().toString().toLowerCase();

            // Filter posts based on the search query
            List<Post> filteredPosts = new ArrayList<>();
            for (Post post : posts) {
                if (post.getContent().toLowerCase().contains(searchQuery) ||
                        post.getAuthor().toLowerCase().contains(searchQuery)) {
                    filteredPosts.add(post);
                }
            }

            // Update adapter with filtered posts
            adapter.setPosts(filteredPosts);
        });

        // Iterate through the list of posts and add each one to the serializablePosts list
        for (Post post : posts) {
            serializablePosts.add(post);
        }
    }

    @Override
    public void onItemClick(Post post) {
        List<Post> posts = new ArrayList<>();
        for (Serializable serializable : serializablePosts) {
            if (serializable instanceof Post) {
                posts.add((Post) serializable);
            }
        }

        // Add the previously selected post back to the list if it exists
        if (previousSelectedPost != null && !posts.contains(previousSelectedPost)) {
            posts.add(previousSelectedPost);
        }

        // Remove the newly selected post from the list
        posts.remove(post);
        serializablePosts.clear();
        serializablePosts.addAll(posts);

        Intent intent = new Intent(this, WatchVideoActivity.class);
        intent.putExtra("selectedPost", post);
        intent.putExtra("allPosts", serializablePosts);
        previousSelectedPost = post; // Update the previously selected post
        startActivity(intent);
    }

    public void onProfilePhotoClicked(View view) {
        Intent i = new Intent(this, LogInActivity.class);
        startActivity(i);
    }
}
