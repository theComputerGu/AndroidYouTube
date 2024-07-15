package com.example.myapplication.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myapplication.API.Converters;
import com.example.myapplication.Entities.User;
import com.example.myapplication.Helper;
import com.example.myapplication.R;

import java.io.IOException;


public class SignUpActivity extends BaseActivity {

    private static final int PICK_IMAGE = 1;
    private EditText nicknameEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button signUpButton;
    private ImageView imageViewPhoto;
    private Button buttonUploadPhoto;
    private Bitmap selectedPhotoBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        nicknameEditText = findViewById(R.id.editTextNickname);
        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword);
        signUpButton = findViewById(R.id.buttonSignUp);
        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        buttonUploadPhoto = findViewById(R.id.buttonUploadPhoto);


        buttonUploadPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });

        signUpButton.setOnClickListener(v -> {
            String displayName = nicknameEditText.getText().toString();
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();

            String validationMessage = checkPassword(password, confirmPassword);
            if (validationMessage != null) {
                Toast.makeText(this, validationMessage, Toast.LENGTH_SHORT).show();
            } else {
                // Create User with selected photo Bitmap
                User newUser =  new User(username, password, displayName, Converters.bitmapToBase64(selectedPhotoBitmap));
                userViewModel.createUser(newUser).observe(this, result -> {
                    if (result.isSuccess()) {
                        Toast.makeText(SignUpActivity.this, "Created user successfully", Toast.LENGTH_SHORT).show();

                        // Login the user after successful creation to get the token
                        login(newUser);
                    } else {
                        Toast.makeText(SignUpActivity.this, result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedPhotoUri = data.getData();
            try {
                // Convert selected Uri to Bitmap
                selectedPhotoBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedPhotoUri);
                // Crop the bitmap into a circle
                selectedPhotoBitmap = cropBitmapToCircle(selectedPhotoBitmap);
                imageViewPhoto.setImageBitmap(selectedPhotoBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to crop a bitmap into a circular shape
    private Bitmap cropBitmapToCircle(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float centerX = bitmap.getWidth() / 2f;
        float centerY = bitmap.getHeight() / 2f;
        float radius = Math.min(centerX, centerY);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(centerX, centerY, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }


    private String checkPassword(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return "Passwords don't match";
        } else if (password.length() < 8) {
            return "Password needs to be at least 8 characters";
        } else if (!password.matches(".*[a-zA-Z].*") || !password.matches(".*\\d.*")) {
            return "Password needs to have a mix of numbers and letters";
        } else if (selectedPhotoBitmap == null) {
            return "Please select a photo";
        }
        return null;
    }
    public void login(User newUser) {
        userViewModel.login(newUser.getUsername(), newUser.getPassword()).observe(this, result -> {
            if (result.isSuccess()) {
                Helper.setSignedInUser(newUser);
                Intent intent = new Intent(this, MainActivity2.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(SignUpActivity.this, result.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
