package com.example.myapplication;

public class User {
    private String nickname;
    private String username;
    private String password;
    private String photoUri;

    public User(String nickname, String username, String password, String photoUri) {
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.photoUri = photoUri;
    }

    // Getters and Setters
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }
}
