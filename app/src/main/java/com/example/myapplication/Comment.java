package com.example.myapplication;

public class Comment {
    private String content;
    private String user;
    private String date;
    public Comment(String content, String user, String date) {
        this.content = content;
        this.user = user;
        this.date = date;
    }
    public String getContent() {
        return content;
    }
    public String getUser() {
        return user;
        }
    public String getDate() {
        return date;
    }
}
