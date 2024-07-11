package com.example.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.Entities.User;
import com.example.myapplication.Models.UserViewModel;
import com.example.myapplication.R;

public class LogInActivity extends BaseActivity {

    private UserViewModel userViewModel;
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

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        buttonLogIn.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();

            validateLogin(username, password);
        });

        TextView ToSignUp = findViewById(R.id.ToSignUp);
        ToSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void validateLogin(String username, String password) {
        userViewModel.getAllUsers().observe(this, users -> {
            if (users != null) {
                boolean validUser = false;
                for (User user : users) {
                    if (user.getUsername().equals(username)) {
                        if (user.getPassword().equals(password)) {
                            userViewModel.signIn(user);
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(this, MainActivity2.class);
                            startActivity(i);
                            validUser = true;
                            break;
                        } else {
                            Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                if (!validUser) {
                    Toast.makeText(this, "Invalid username", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
