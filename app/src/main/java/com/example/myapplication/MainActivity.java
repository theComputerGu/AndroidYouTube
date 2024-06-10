package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ImageButton buttonToHomePage = findViewById(R.id.buttonToHomePage);
        buttonToHomePage.setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        });

        ImageButton buttonToLogIn = findViewById(R.id.buttonToLogIn);
        buttonToLogIn.setOnClickListener(v -> {
            Intent i = new Intent(this, LogInActivity.class);
            startActivity(i);
        });

        ImageButton buttonToAddVideoPage = findViewById(R.id.buttonToAddVideoPage);
        buttonToAddVideoPage.setOnClickListener(v -> {
            Intent i = new Intent(this, AddVideoActivity.class);
            startActivity(i);
        });


        RecyclerView lstVideos = findViewById(R.id.lstPosts);
        final PostsListAdapter adapter = new PostsListAdapter(this);
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



    }
}


