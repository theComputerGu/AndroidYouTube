package com.example.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.Entities.UserCredentials;
import com.example.myapplication.Models.UserViewModel;
import com.example.myapplication.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

            if (!username.isEmpty() && !password.isEmpty()) {
                UserCredentials credentials = new UserCredentials(username, password);
                userApi.login(credentials, new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        int statusCode = response.code();
                        if (statusCode == 200) {
                            signedInUser = userApi.getUserByUsername(credentials.getUsername());
                            Intent intent = new Intent(LogInActivity.this, MainActivity2.class);
                            startActivity(intent);
                            finish(); // Optionally, close the login activity so it can't be returned to
                        } else {
                            Toast.makeText(LogInActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(LogInActivity.this, "Network error", Toast.LENGTH_SHORT).show();
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
