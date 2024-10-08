package com.example.myapplication.Entities;

import android.util.Log;

import com.example.myapplication.Helper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Retrieve the token from the Helper class
        String authToken = Helper.getToken();
        Log.d("AuthInterceptor", "Token: " + authToken);

        // Add the token to the request headers if it's not empty
        if (authToken != null && !authToken.isEmpty()) {
            Request newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + authToken)
                    .build();
            return chain.proceed(newRequest);
        }

        // Proceed without the token if it's empty
        return chain.proceed(originalRequest);
    }
}
