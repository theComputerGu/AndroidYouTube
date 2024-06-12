package com.example.myapplication;

import android.graphics.Bitmap;

public class User {
    private String nickname;
    private String username;
    private String password;
    private Bitmap photo;


    public User(String nickname, String username, String password, Bitmap photo) {
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.photo = photo;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Bitmap getPhoto() {  // Getter for Bitmap photo
        return photo;
    }

}
