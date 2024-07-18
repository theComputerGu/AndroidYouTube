package com.example.myapplication.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DB.Converters;
import com.example.myapplication.Adapters.UserVideosAdapter;
import com.example.myapplication.Entities.Video;
import com.example.myapplication.Helper;
import com.example.myapplication.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddVideoActivity2 extends BaseActivity implements UserVideosAdapter.OnVideoClickListener {
    private static final int REQUEST_CODE_PHOTO = 1;
    private static final int REQUEST_CODE_VIDEO = 2;
    private RecyclerView recyclerView;
    private EditText etTitle;
    private Uri videoUri;
    private Bitmap thumbnailBitmap;
    private ImageView imageViewPhoto;
    private ImageView videoViewPhoto;
    private UserVideosAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        // Initialize views
        etTitle = findViewById(R.id.etTitle);
        Button btnUploadPhoto = findViewById(R.id.btnUploadPhoto);
        Button btnUploadVideo = findViewById(R.id.btnUploadVideo);
        Button btnAdd = findViewById(R.id.btnAdd);
        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        videoViewPhoto = findViewById(R.id.selectedVideoUri);

        // Set click listeners
        btnUploadPhoto.setOnClickListener(v -> uploadPhoto());
        btnUploadVideo.setOnClickListener(v -> uploadVideo());
        btnAdd.setOnClickListener(v -> addVideo());

        // Initialize RecyclerView and set adapter
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserVideosAdapter(new ArrayList<>(), this, true);
        recyclerView.setAdapter(adapter);

        userViewModel.getUserVideos(Helper.getSignedInUser().getUserId()).observe(this, videos -> {
            adapter.updateVideos(videos);
        });
    }

    private static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    private void addVideo() {
        String title = etTitle.getText().toString().trim();

        if (title.isEmpty()|| videoUri == null || thumbnailBitmap == null) {
            Toast.makeText(this, "Please fill all fields and select a video.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Save video file to internal storage
        String videoFileName = "video_" + System.currentTimeMillis() + ".mp4";
        File videoFile = saveVideoToFile(videoUri, videoFileName);

        if (videoFile == null) {
            Toast.makeText(this, "Failed to save video file.", Toast.LENGTH_SHORT).show();
            return;
        }
        String photoPath = Converters.bitmapToBase64(thumbnailBitmap);

        userViewModel.createUserVideo(Helper.getSignedInUser().getUserId(),title, Helper.getSignedInUser().getUsername(), photoPath, videoFile).observe(this, result ->{
            if (result.isSuccess()) {
                userViewModel.getUserVideos(Helper.getSignedInUser().getUserId()).observe(this, videos -> {
                    adapter.updateVideos(videos);
                });
                Toast.makeText(this, "Video added successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AddVideoActivity2.this, result.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Clear input fields or perform any other necessary actions
        etTitle.setText("");
        imageViewPhoto.setImageResource(0); // Clear image
        videoViewPhoto.setImageResource(0); // Clear video thumbnail
        videoUri = null;
        thumbnailBitmap = null;
    }


    // Handle result of video selection (if implemented)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_PHOTO) {
                Uri photoUri = data.getData();
                try {
                    thumbnailBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                    imageViewPhoto.setImageBitmap(thumbnailBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (requestCode == REQUEST_CODE_VIDEO) {
                videoUri = data.getData();
                try {
                    Bitmap thumbnail = getVideoThumbnail(videoUri);
                    videoViewPhoto.setImageBitmap(thumbnail);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private Bitmap getVideoThumbnail(Uri videoUri) throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this, videoUri);
        Bitmap thumbnail = retriever.getFrameAtTime();
        retriever.release();
        return thumbnail;
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
                Toast.makeText(AddVideoActivity2.this, result.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
    private File saveVideoToFile(Uri videoUri, String fileName) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(videoUri);
            File videoFile = new File(getFilesDir(), fileName);
            FileOutputStream outputStream = new FileOutputStream(videoFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
            return videoFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void uploadPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PHOTO);
    }

    private void uploadVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_VIDEO);
    }
    @Override
    public void onVideoClick(Video video) {
        Intent intent = new Intent(this, WatchVideoActivity2.class);
        intent.putExtra("selectedVideoId", video.getVideoId());
        startActivity(intent);
    }
}
