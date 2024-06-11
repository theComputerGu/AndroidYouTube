package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static List<User> userList = new ArrayList<>();
    private static  User UserSignedIn = null;

    public static void addUser(User user) {
        userList.add(user);
    }


    public static void saveSignedInUser(User user) {
        UserSignedIn = user;
    }

    public static User getSignedInUser() {
        return UserSignedIn;
    }

    public static List<User> getUsers() {
        return userList;
    }
}

