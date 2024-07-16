package com.example.myapplication.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.UserVideosAdapter;
import com.example.myapplication.Entities.Video;
import com.example.myapplication.Helper;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ProfileActivity extends BaseActivity implements UserVideosAdapter.OnVideoClickListener {
    private ImageView imageViewProfilePhoto;
    private TextView tvUsername;
    private TextView tvDisplayName;
    private Button btnSignOut;
    private RecyclerView recyclerView;
    private UserVideosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        imageViewProfilePhoto = findViewById(R.id.imageViewProfilePhoto);
        tvUsername = findViewById(R.id.tvUsername);
        tvDisplayName = findViewById(R.id.tvDisplayName);
        btnSignOut = findViewById(R.id.btnSignOut);

        String photoPath = Helper.getSignedInUser().getProfilePicture();
        Helper.loadPhotoIntoImageView(this, imageViewProfilePhoto, photoPath);
        tvUsername.setText(Helper.getSignedInUser().getUsername());
        tvDisplayName.setText(Helper.getSignedInUser().getDisplayName());


        // Initialize RecyclerView and set adapter
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserVideosAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        userViewModel.getUserVideos(Helper.getSignedInUser().getUserId()).observe(this, videos -> {
            adapter.updateVideos(videos);
        });
    }

    // Implement other methods and logic as needed

    public void onSignOutClicked(View view) {
        Helper.setSignedInUser(null);
        Toast.makeText(this, "singed out successfully", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, MainActivity2.class);
        startActivity(i);
    }
    public void onUpdateDisplayNameClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Display Name");

        final EditText input = new EditText(this);
        input.setText(tvDisplayName.getText().toString());
        builder.setView(input);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newDisplayName = input.getText().toString();
            String userId = Helper.getSignedInUser().getUserId();
            Log.d("ProfileActivity", "User ID: " + userId);

            userViewModel.updateDisplayName(userId, newDisplayName).observe(this, result -> {
                if (result.isSuccess()) {
                    tvDisplayName.setText(newDisplayName);
                    Toast.makeText(this, "Display name updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to update display name: " + result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
    public void onDeleteUserClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete your account? This action cannot be undone.");

        builder.setPositiveButton("Delete", (dialog, which) -> {
            userViewModel.deleteUser(Helper.getSignedInUser().getUserId()).observe(this, result -> {
                if (result.isSuccess()) {
                    // Handle successful deletion, navigate to login screen or perform cleanup
                    Toast.makeText(ProfileActivity.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                    // Example: Navigate back to login screen
                    startActivity(new Intent(ProfileActivity.this, MainActivity2.class));
                    finish();
                } else {
                    // Handle deletion failure
                    Toast.makeText(ProfileActivity.this, "Failed to delete user: " + result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }


    @Override
    public void onVideoUpdate(Video video) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Video Title");

        final EditText input = new EditText(this);
        input.setText(video.getTitle());
        builder.setView(input);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newTitle = input.getText().toString();
            userViewModel.updateUserVideo(Helper.getSignedInUser().getUserId(), video.getVideoId(), newTitle)
                    .observe(this, result -> {
                        if (result.isSuccess()) {
                            userViewModel.getUserVideos(Helper.getSignedInUser().getUserId()).observe(this, videos -> {
                                adapter.updateVideos(videos);
                            });
                            Toast.makeText(this, "Video updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Failed to update video: " + result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
    @Override
    public void onVideoDelete(Video video) {
        userViewModel.deleteUserVideo(Helper.getSignedInUser().getUserId(), video.getVideoId()).observe(this, result -> {
            if (result.isSuccess()) {
                // Get signed-in user's videos
                userViewModel.getUserVideos(Helper.getSignedInUser().getUserId()).observe(this, videos -> {
                    adapter.updateVideos(videos);
                });
                Toast.makeText(this, "Video deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ProfileActivity.this, result.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
