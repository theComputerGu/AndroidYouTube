package com.example.myapplication;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String nickname;
    private String username;
    private String password;
    private Bitmap photo;  // Changed to Bitmap
    private List<Post> posts;

    public User(String nickname, String username, String password, Bitmap photo) {
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.photo = photo;
        this.posts = new ArrayList<>();
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

    public List<Post> getPosts() {
        return posts;
    }

    public void addPost(Post post) {
        this.posts.add(post);
    }
}
