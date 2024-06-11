package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
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
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private List<User> userList;
    private EditText nicknameEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button signUpButton;
    private ImageView imageViewPhoto;
    private Button buttonUploadPhoto;
    private Bitmap selectedPhotoBitmap;  // Bitmap to store selected photo

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

        userList = UserManager.getUsers();

        buttonUploadPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });

        signUpButton.setOnClickListener(v -> {
            String nickname = nicknameEditText.getText().toString();
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();

            String validationMessage = checkInfo(username, password, confirmPassword);
            if (validationMessage != null) {
                Toast.makeText(this, validationMessage, Toast.LENGTH_SHORT).show();
            } else {
                // Create User with selected photo Bitmap
                User newUser = new User(nickname, username, password, selectedPhotoBitmap);
                UserManager.addUser(newUser);
                UserManager.saveSignedInUser(newUser);

                Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show();

                // Proceed to MainActivity after successful sign-up
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedPhotoUri = data.getData();
            try {
                selectedPhotoBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedPhotoUri);
                imageViewPhoto.setImageBitmap(selectedPhotoBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String checkInfo(String username, String password, String confirmPassword) {
        if (isUsernameTaken(username)) {
            return "This username is already taken";
        } else if (!password.equals(confirmPassword)) {
            return "Passwords don't match";
        } else if (password.length() < 8) {
            return "Password needs to be at least 8 characters";
        } else if (!password.matches(".*[a-zA-Z].*") || !password.matches(".*\\d.*")) {
            return "Password needs to have a mix of numbers and letters";
        }
        return null;
    }

    private boolean isUsernameTaken(String username) {
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}
