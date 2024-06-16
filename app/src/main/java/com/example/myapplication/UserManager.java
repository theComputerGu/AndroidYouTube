package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static UserManager instance;
    private List<User> userList;
    private User userSignedIn;

    // Private constructor to prevent instantiation from outside
    private UserManager() {
        userList = new ArrayList<>();
        userSignedIn = null;
    }

    // Method to get the singleton instance of UserManager
    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void addUser(User user) {
        userList.add(user);
    }

    public void saveSignedInUser(User user) {
        userSignedIn = user;
    }

    public User getSignedInUser() {
        return userSignedIn;
    }

    public List<User> getUsers() {
        return userList;
    }
    public void signOut() {
        userSignedIn = null;
    }
}
