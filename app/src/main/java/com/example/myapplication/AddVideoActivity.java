package com.example.myapplication;
import android.app.MediaRouteButton;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AddVideoActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PHOTO = 1;
    private static final int REQUEST_CODE_VIDEO = 2;

    private EditText etContent, etAuthor;
    private TextView tvDate;
    private Button btnUploadPhoto, btnUploadVideo, btnAdd;
    private RecyclerView recyclerView;

    private ArrayList<Post> posts;
    private PostsListAdapter postAdapter;
    private ImageView imageViewPhoto;
    private ImageView btnFgha;
    private Uri photoUri;
    private Uri videoUri;
    private Bitmap selectedPhotoBitmap;  // Bitmap to

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);


        etContent = findViewById(R.id.etContent);
        etAuthor = findViewById(R.id.etAuthor);
        tvDate = findViewById(R.id.tvDate);
        btnUploadPhoto = findViewById(R.id.btnUploadPhoto);
        btnUploadVideo = findViewById(R.id.btnUploadVideo);
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);

        posts = new ArrayList<>();
        postAdapter = new PostsListAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(postAdapter);

        tvDate.setText(getCurrentDate());

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
        btnFgha = findViewById(R.id.btnFgha);

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

        etAuthor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateAddButtonState();
            }
        });

        // Your other initialization code

        // Initially update button state
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
                // Handle video upload
                Uri videoUri = data.getData();
                try {
                    Bitmap thumbnail = getVideoThumbnail(videoUri);
                    btnFgha.setImageBitmap(thumbnail);
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
        boolean isAuthorFilled = !TextUtils.isEmpty(etAuthor.getText().toString().trim());
        boolean isPhotoUploaded = imageViewPhoto.getDrawable() != null;
        boolean isVideoUploaded = btnFgha.getDrawable() != null;

        btnAdd.setEnabled(isContentFilled && isAuthorFilled && isPhotoUploaded && isVideoUploaded);
    }

    private void displayErrorMessage() {
        if (TextUtils.isEmpty(etContent.getText().toString().trim())) {
            etContent.setError("Content is required");
        }
        if (TextUtils.isEmpty(etAuthor.getText().toString().trim())) {
            etAuthor.setError("Author/User is required");
        }
        if (imageViewPhoto.getDrawable() == null) {
            Toast.makeText(this, "Please upload a photo", Toast.LENGTH_SHORT).show();
        }
        if (btnFgha.getDrawable() == null) {
            Toast.makeText(this, "Please upload a video", Toast.LENGTH_SHORT).show();
        }
    }
    private void addPost() {
        String content = etContent.getText().toString().trim();
        String author = etAuthor.getText().toString().trim();

        if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(author) && selectedPhotoBitmap != null) {
            // Create a new Post object with the uploaded image
            Post newPost = new Post(author, content, selectedPhotoBitmap, getCurrentDate());

            // Add the new post to the list
            posts.add(newPost);

            // Update the RecyclerView
            postAdapter.setPosts(posts);

            // Clear input fields and reset button state
            etContent.setText("");
            etAuthor.setText("");
            imageViewPhoto.setImageBitmap(null);
            selectedPhotoBitmap = null;
            updateAddButtonState();
        } else {
            displayErrorMessage();
        }
    }
}

