package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {
    // SharedPreferences constants
    private static final String SHARED_PREFS_NAME = "my_app_prefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PHOTO_URI = "photo_uri";

    // Method to save username to SharedPreferences
    public static void saveUsername(Context context, String username) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    // Method to retrieve username from SharedPreferences
    public static String getUsername(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USERNAME, "");
    }

    // Method to save password to SharedPreferences
    public static void savePassword(Context context, String password) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    // Method to retrieve password from SharedPreferences
    public static String getPassword(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_PASSWORD, "");
    }

    // Method to save photo URI to SharedPreferences
    public static void savePhotoUri(Context context, String photoUri) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_PHOTO_URI, photoUri);
        editor.apply();
    }

    // Method to retrieve photo URI from SharedPreferences
    public static String getPhotoUri(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_PHOTO_URI, "");
    }
}
