package com.example.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

public class LogInActivity extends BaseActivity {
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

        buttonLogIn.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();

            if (!username.isEmpty() && !password.isEmpty()) {
                userViewModel.getLoginResult(username, password).observe(this, loginResult -> {
                    if (loginResult.isSuccess()) {
                        Toast.makeText(LogInActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LogInActivity.this, loginResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
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
