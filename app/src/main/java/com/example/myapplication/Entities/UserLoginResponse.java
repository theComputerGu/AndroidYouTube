package com.example.myapplication.Entities;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLoginResponse {
    private String token;
    private static final String PREF_NAME = "MyApp";
    private static final String KEY_AUTH_TOKEN = "auth_token";

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    // Save token to SharedPreferences
    public void saveToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.apply();
    }

    // Retrieve token from SharedPreferences
    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null);
    }

    // Clear token from SharedPreferences
    public static void clearToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_AUTH_TOKEN);
        editor.apply();
    }
}
