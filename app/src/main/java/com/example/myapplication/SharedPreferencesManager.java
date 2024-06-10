package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SharedPreferencesManager {

    private static final String SHARED_PREFS_NAME = "UserPrefs";
    private static final String KEY_USERS = "users";

    public static void saveUsers(Context context, List<User> userList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Set<String> userSet = new HashSet<>();
        for (User user : userList) {
            // Serialize the user object into a string representation
            String userString = user.getNickname() + "," + user.getUsername() + "," + user.getPassword() + "," + user.getPhotoUri();
            userSet.add(userString);
        }
        editor.putStringSet(KEY_USERS, userSet);
        editor.apply();
    }

    public static List<User> loadUsers(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> userSet = sharedPreferences.getStringSet(KEY_USERS, new HashSet<>());

        List<User> userList = new ArrayList<>();
        for (String userString : userSet) {
            String[] userDetails = userString.split(",");
            if (userDetails.length == 4) {
                userList.add(new User(userDetails[0], userDetails[1], userDetails[2], userDetails[3]));
            }
        }
        return userList;
    }
}
