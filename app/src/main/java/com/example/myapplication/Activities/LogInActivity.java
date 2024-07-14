package com.example.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Helper;
import com.example.myapplication.R;

public class LogInActivity extends BaseActivity {
    private EditText editTextUsername;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in2);

        editTextUsername = findViewById(R.id.editTextText);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        Button buttonLogIn = findViewById(R.id.buttonLogIn);

        buttonLogIn.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();

            if (!username.isEmpty() && !password.isEmpty()) {
                userViewModel.login(username, password).observe(this, result -> {
                    if (result.isSuccess()) {
                        Helper.setToken(result.getData());
                        Helper.setSignedInUser(userViewModel.getUserByUsername(username).getValue());
                        Toast.makeText(LogInActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        Log.d("Login successful!", Helper.getSignedInUser().getUsername());
                        Intent intent = new Intent(this, MainActivity2.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LogInActivity.this, result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(LogInActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            }
        });

        TextView ToSignUp = findViewById(R.id.ToSignUp);
        ToSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}
