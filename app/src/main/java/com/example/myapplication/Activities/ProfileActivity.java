package com.example.myapplication.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.UserVideosAdapter;
import com.example.myapplication.Entities.User;
import com.example.myapplication.Entities.Video;
import com.example.myapplication.Helper;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ProfileActivity extends BaseActivity implements UserVideosAdapter.OnVideoClickListener {
    private ImageView imageViewProfilePhoto;
    private TextView tvUsername;
    private TextView tvDisplayName;
    private RecyclerView recyclerView;
    private UserVideosAdapter adapter;
    private User userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String username = getIntent().getStringExtra("username");

        userViewModel.getUserByUsername(username).observe(this, user -> {
            userProfile = user;

            // Check if the signed-in user matches the fetched user
            boolean isCurrentUser = Helper.getSignedInUser() != null && Helper.getSignedInUser().getUsername().equals(userProfile.getUsername());
            findViewById(R.id.btnSignOut).setVisibility(isCurrentUser ? View.VISIBLE : View.GONE);
            findViewById(R.id.btnEditDisplayName).setVisibility(isCurrentUser ? View.VISIBLE : View.GONE);

            // Set clickability based on visibility
            setViewClickability(R.id.btnSignOut, isCurrentUser);
            setViewClickability(R.id.btnEditDisplayName, isCurrentUser);

            // Initialize views
            imageViewProfilePhoto = findViewById(R.id.imageViewProfilePhoto);
            tvUsername = findViewById(R.id.tvUsername);
            tvDisplayName = findViewById(R.id.tvDisplayName);

            String photoPath = userProfile.getProfilePicture();
            Helper.loadPhotoIntoImageView(this, imageViewProfilePhoto, photoPath);
            tvUsername.setText(userProfile.getUsername());
            tvDisplayName.setText(userProfile.getDisplayName());


            // Initialize RecyclerView and set adapter
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new UserVideosAdapter(new ArrayList<>(), this, isCurrentUser);
            recyclerView.setAdapter(adapter);

            userViewModel.getUserVideos(userProfile.getUserId()).observe(this, videos -> {
                adapter.updateVideos(videos);
            });
        });
    }
    // Method to set clickability and focusability of a view
    private void setViewClickability(int viewId, boolean clickable) {
        View view = findViewById(viewId);
        view.setClickable(clickable);
        view.setFocusable(clickable);
        view.setFocusableInTouchMode(clickable);
    }

    // Implement other methods and logic as needed

    public void onSignOutClicked(View view) {
        Helper.setSignedInUser(null);
        Toast.makeText(this, "signed out successfully", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, MainActivity2.class);
        startActivity(i);
        finish();
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
    @Override
    public void onVideoClick(Video video) {
        Intent intent = new Intent(this, WatchVideoActivity2.class);
        intent.putExtra("selectedVideoId", video.getVideoId());
        startActivity(intent);
    }
}
//    public void onDeleteUserClicked(View view) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Confirm Deletion");
//        builder.setMessage("Are you sure you want to delete your account? This action cannot be undone.");
//
//        builder.setPositiveButton("Delete", (dialog, which) -> {
//            userViewModel.deleteUser(Helper.getSignedInUser().getUserId(), Helper.getSignedInUser().getUsername()).observe(this, result -> {
//                if (result.isSuccess()) {
//                    Helper.setSignedInUser(null);
//                    Helper.setToken(null);
//                    Toast.makeText(ProfileActivity.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(ProfileActivity.this, MainActivity2.class));
//                    finish();
//                } else {
//                    // Handle deletion failure
//                    Toast.makeText(ProfileActivity.this, "Failed to delete user: " + result.getErrorMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        });
//
//        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
//
//        builder.show();
//    }