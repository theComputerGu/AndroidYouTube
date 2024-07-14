package com.example.myapplication;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.myapplication.Entities.User;
import com.example.myapplication.Models.UserViewModel;

public class Helper extends Application {
    public static Context context;
    public static String token;
    public static User signedInUser;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    public static String getToken() {
        return token;
    }
    public static void setToken(String token) {
        Helper.token = token;
    }
    public static User getSignedInUser() {
        return signedInUser;
    }
    public static void setSignedInUser(User signedInUser) {
        Helper.signedInUser = signedInUser;
    }
    public static boolean isSignedIn() {
        return signedInUser != null;
    }
    public static void setUserById(String id) {
        UserViewModel userViewModel = UserViewModel.getInstance();
        // Call your API or repository to fetch the user by ID
        userViewModel.getUserById(id).observeForever(user -> {
            if (user != null) {
                setSignedInUser(user);
            } else {
                // Handle case where user with ID isn't found or other error
                Log.e("Helper", "User with ID " + id + " not found.");
            }
        });
    }
}
