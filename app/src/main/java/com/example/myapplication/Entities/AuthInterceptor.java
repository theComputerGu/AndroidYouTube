package com.example.myapplication.Entities;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private Context mContext;

    public AuthInterceptor(Context context) {
        this.mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Retrieve the token from SharedPreferences or any other storage
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("YourPreferencesName", Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("authToken", "");

        // Add the token to the request headers
        Request newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + authToken)
                .build();

        return chain.proceed(newRequest);
    }
}

