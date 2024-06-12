package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
        addDefaultPosts(posts);
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
    public void addDefaultPosts(List<Post> posts) {
        posts.add(new Post("boat", "Author1", "4/09/2004", getBitmapFromUri(R.drawable.boat), getUriFromRawResource(R.raw.boat)));
        posts.add(new Post("buildings", "Author2", "3/04/2022", getBitmapFromUri(R.drawable.buildings), getUriFromRawResource(R.raw.buildings)));
        posts.add(new Post("ferris_wheel", "Author3", "3/03/2021", getBitmapFromUri(R.drawable.ferris_wheel), getUriFromRawResource(R.raw.ferris_wheel)));
        posts.add(new Post("girl_flowers", "Author4", "5/08/2001", getBitmapFromUri(R.drawable.girl_flowers), getUriFromRawResource(R.raw.girl_flowers)));
        posts.add(new Post("hilton", "Author5", "6/07/2003", getBitmapFromUri(R.drawable.hilton), getUriFromRawResource(R.raw.hilton)));
        posts.add(new Post("hard_rock_cafe", "Author6", "5/04/2018", getBitmapFromUri(R.drawable.hard_rock_cafe), getUriFromRawResource(R.raw.hard_rock_cafe)));
        posts.add(new Post("keyboard", "Author7", "5/05/2001", getBitmapFromUri(R.drawable.keyboard), getUriFromRawResource(R.raw.keyboard)));
        posts.add(new Post("kite", "Author8", "6/07/2020", getBitmapFromUri(R.drawable.kite), getUriFromRawResource(R.raw.kite)));
        posts.add(new Post("men_sunset", "Author9", "3/01/2020", getBitmapFromUri(R.drawable.men_sunset), getUriFromRawResource(R.raw.men_sunset)));
        posts.add(new Post("road", "Author10", "1/01/2011", getBitmapFromUri(R.drawable.road), getUriFromRawResource(R.raw.road)));
        posts.add(new Post("women_reading", "Author11", "3/01/2021", getBitmapFromUri(R.drawable.women_reading), getUriFromRawResource(R.raw.women_reading)));
    }


    private Bitmap getBitmapFromUri(int drawableId) {
        return BitmapFactory.decodeResource(getResources(), drawableId);
    }

    private Uri getUriFromRawResource(int rawResourceId) {
        return Uri.parse("android.resource://" + getPackageName() + "/" + rawResourceId);
    }

}
