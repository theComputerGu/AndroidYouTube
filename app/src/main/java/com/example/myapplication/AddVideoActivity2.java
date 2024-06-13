package com.example.myapplication;

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
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddVideoActivity2 extends AppCompatActivity {
    private static final int REQUEST_CODE_PHOTO = 1;
    private static final int REQUEST_CODE_VIDEO = 2;
    private EditText etTitle;
    private Uri videoUri;
    private Bitmap thumbnailBitmap;
    private ImageView imageViewPhoto;
    private ImageView videoViewPhoto;

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

        btnUploadPhoto.setOnClickListener(v -> uploadPhoto());
        btnUploadVideo.setOnClickListener(v -> uploadVideo());
        btnAdd.setOnClickListener(v -> addVideo());


    }
    private void uploadPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PHOTO);
    }

    private void uploadVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_VIDEO);
    }
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void addVideo() {
        String title = etTitle.getText().toString().trim();

        if (title.isEmpty()|| videoUri == null || thumbnailBitmap == null) {
            Toast.makeText(this, "Please fill all fields and select a video.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create new Video object
        Video newVideo = new Video(title, UserManager.getSignedInUser().getUsername(), getCurrentDate(), thumbnailBitmap, videoUri);

        // Add the video to the VideoListManager
        VideoListManager videoManager = VideoListManager.getInstance(this);
        videoManager.addVideo(newVideo);

        // Show success message
        Toast.makeText(this, "Video added successfully!", Toast.LENGTH_SHORT).show();

        // Navigate back to MainActivity or any other desired activity
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
        finish();
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
}
