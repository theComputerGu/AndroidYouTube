package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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
        posts.add(new Post("Alice", "Hello world", R.drawable.boat));
        posts.add(new Post("Alice", "Hello world", R.drawable.buildings));
        posts.add(new Post("Alice", "Hello world", R.drawable.ferris_wheel));
        posts.add(new Post("Alice", "Hello world", R.drawable.girl_flowers));
        posts.add(new Post("Alice", "Hello world", R.drawable.hilton));
        posts.add(new Post("Alice", "Hello world", R.drawable.hard_rock_cafe));
        posts.add(new Post("Alice", "Hello world", R.drawable.keyboard));
        posts.add(new Post("Alice", "Hello world", R.drawable.kite));
        posts.add(new Post("Alice", "Hello world", R.drawable.men_sunset));
        posts.add(new Post("Alice", "Hello world", R.drawable.road));
        posts.add(new Post("Alice", "Hello world", R.drawable.women_reading));
        adapter.setPosts(posts);





    }
}