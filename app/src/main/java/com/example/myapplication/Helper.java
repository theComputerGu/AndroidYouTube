package com.example.myapplication;

import android.app.Application;
import android.content.Context;

import com.example.myapplication.Entities.User;

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
}
