package com.example.myapplication.Activities;

import static com.example.myapplication.API.Converters.bitmapToBase64;

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

import com.example.myapplication.API.Converters;
import com.example.myapplication.Adapters.VideoAdapter;
import com.example.myapplication.Entities.Video;
import com.example.myapplication.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddVideoActivity2 extends BaseActivity implements VideoAdapter.OnVideoClickListener {
    private static final int REQUEST_CODE_PHOTO = 1;
    private static final int REQUEST_CODE_VIDEO = 2;
    private RecyclerView recyclerView;
    private EditText etTitle;
    private Uri videoUri;
    private Bitmap thumbnailBitmap;
    private ImageView imageViewPhoto;
    private ImageView videoViewPhoto;
    private VideoAdapter videoAdapter;
    private List<Video> userVideos;


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
        videoAdapter = new VideoAdapter(null, VideoAdapter.VIEW_TYPE_ADD, this);
        recyclerView.setAdapter(videoAdapter);

        // Get signed-in user's videos
        userViewModel.getUserVideos(signedInUser.getUserId()).observe(this, videos -> {
            userVideos = videos;
            videoAdapter.updateVideos(videos);
        });
    }
    private void uploadPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PHOTO);
    }

    private void uploadVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_VIDEO);
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
        if (!isTitleAvailable(title)) {
            Toast.makeText(this, "Video title already exists for this user.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Save video file to internal storage
        String videoFileName = "video_" + System.currentTimeMillis() + ".mp4";
        File videoFile = saveVideoToFile(videoUri, videoFileName);

        if (videoFile == null) {
            Toast.makeText(this, "Failed to save video file.", Toast.LENGTH_SHORT).show();
            return;
        }

        userViewModel.createUserVideo(signedInUser.getUserId(),title, signedInUser.getUsername(), videoFile, bitmapToBase64(thumbnailBitmap )).observe(this, result ->{
            if (result.isSuccess()) {
                userVideos.add(new Video(title, signedInUser.getUsername(),signedInUser.getDisplayName(), getCurrentDate(), Converters.bitmapToBase64(thumbnailBitmap), videoFile.getAbsolutePath()));
                videoAdapter.updateVideos(userVideos);
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
    public boolean isTitleAvailable(String title) {
        for (Video video : userVideos) {
            if (video.getTitle().equalsIgnoreCase(title)) {
                return false;
            }
        }
        return true;
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
    public void onVideoClick(Video video) {
        userViewModel.deleteUserVideo(signedInUser.getUserId(), video.getVideoId(), userViewModel.getToken()).observe(this, result -> {
            if (result.isSuccess()) {
                userVideos.remove(video);
                videoAdapter.updateVideos(userVideos);
                Toast.makeText(this, "Video deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AddVideoActivity2.this, result.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
}
