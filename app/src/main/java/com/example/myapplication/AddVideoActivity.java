package com.example.myapplication;

import static com.example.myapplication.UserManager.getSignedInUser;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddVideoActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PHOTO = 1;
    private static final int REQUEST_CODE_VIDEO = 2;
    private EditText etContent;
    private Button btnUploadPhoto, btnUploadVideo, btnAdd;
    private RecyclerView recyclerView;

    private PostsListAdapter postAdapter;
    private ImageView imageViewPhoto;
    private ImageView videoViewPhoto;
    private Uri selectedVideoUri;
    private Bitmap selectedPhotoBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);


        etContent = findViewById(R.id.etContent);
        btnUploadPhoto = findViewById(R.id.btnUploadPhoto);
        btnUploadVideo = findViewById(R.id.btnUploadVideo);
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);

        List<Post> posts = new ArrayList<>();
        postAdapter = new PostsListAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(postAdapter);


        btnUploadPhoto.setOnClickListener(v -> uploadPhoto());
        btnUploadVideo.setOnClickListener(v -> uploadVideo());

        btnUploadPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_PHOTO);
        });

        btnUploadVideo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_VIDEO);
        });


        btnAdd.setOnClickListener(v -> addPost());
        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        videoViewPhoto = findViewById(R.id.selectedVideoUri);

        // Set text change listeners
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateAddButtonState();
            }
        });
        updateAddButtonState();
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date());
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_PHOTO) {
                Uri photoUri = data.getData();
                try {
                    selectedPhotoBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                    imageViewPhoto.setImageBitmap(selectedPhotoBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (requestCode == REQUEST_CODE_VIDEO) {
                selectedVideoUri = data.getData();
                try {
                    Bitmap thumbnail = getVideoThumbnail(selectedVideoUri);
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


    private void updateAddButtonState() {
        boolean isContentFilled = !TextUtils.isEmpty(etContent.getText().toString().trim());
        boolean isPhotoUploaded = selectedPhotoBitmap != null;
        boolean isVideoUploaded = selectedVideoUri != null;

        btnAdd.setEnabled(isContentFilled && isPhotoUploaded && isVideoUploaded);
    }


    private void displayErrorMessage() {
        if (TextUtils.isEmpty(etContent.getText().toString().trim())) {
            etContent.setError("Content is required");
        }
        if (imageViewPhoto.getDrawable() == null) {
            Toast.makeText(this, "Please upload a photo", Toast.LENGTH_SHORT).show();
        }
        if (videoViewPhoto.getDrawable() == null) {
            Toast.makeText(this, "Please upload a video", Toast.LENGTH_SHORT).show();
        }
    }
    private void addPost() {
        String content = etContent.getText().toString().trim();

        if (!TextUtils.isEmpty(content) && selectedPhotoBitmap != null) {
            // Create a new Post object with the uploaded image
            Post newPost = new Post(content, getSignedInUser().getUsername(), getCurrentDate(), selectedPhotoBitmap,selectedVideoUri);

            // Update the RecyclerView
            postAdapter.addPost(newPost);

            // Clear input fields and reset button state
            etContent.setText("");
            imageViewPhoto.setImageBitmap(null);
            selectedPhotoBitmap = null;
            updateAddButtonState();
        } else {
            displayErrorMessage();
        }
    }



}

