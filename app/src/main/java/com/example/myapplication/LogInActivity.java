package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.SignUpActivity;
import com.example.myapplication.User;
import com.example.myapplication.SharedPreferencesManager;

import java.util.List;

public class LogInActivity extends AppCompatActivity {

    private List<User> userList;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in2);

        editTextUsername = findViewById(R.id.editTextText);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        buttonLogIn = findViewById(R.id.buttonLogIn);

        userList = SharedPreferencesManager.loadUsers(this);

        buttonLogIn.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();

            String validationMessage = validateLogin(username, password);
            if (validationMessage == null) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, validationMessage, Toast.LENGTH_SHORT).show();
            }
        });

        TextView ToSignUp = findViewById(R.id.ToSignUp);
        ToSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private String validateLogin(String username, String password) {
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                if (user.getPassword().equals(password)) {
                    return null; // Login successful
                } else {
                    return "Wrong password"; // Wrong password
                }
            }
        }
        return "Invalid username"; // Invalid username
    }
}
