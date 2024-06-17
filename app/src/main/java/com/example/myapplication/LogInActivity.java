package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class LogInActivity extends BaseActivity {

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

        userList = userManager.getUsers();

        buttonLogIn.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();

            if (isValidLogin(username, password)) {
                Intent i = new Intent(this, MainActivity2.class);
                startActivity(i);
            }
        });

        TextView ToSignUp = findViewById(R.id.ToSignUp);
        ToSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private boolean isValidLogin(String username, String password) {
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                if (user.getPassword().equals(password)) {
                    userManager.saveSignedInUser(user);
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                    return true; // Login successful
                } else {
                    Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show();
                    return false; // Wrong password
                }
            }
        }
        Toast.makeText(this, "Invalid username", Toast.LENGTH_SHORT).show();
        return false; // Invalid username
    }

}
