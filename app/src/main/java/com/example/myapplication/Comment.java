package com.example.myapplication;

public class Comment {
    private String content;
    private User user;
    private String date;
    public Comment(String content, User user, String date) {
        this.content = content;
        this.user = user;
        this.date = date;
    }
    public String getContent() {
        return content;
    }
    public User getUser() {
        return user;
    }
    public String getDate() {
        return date;
    }
}
