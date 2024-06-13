package com.example.myapplication;

import java.util.Date;

public class Comment {
    private String content;
    private User user;
    private Date date;
    public Comment(User content, String user, String date) {
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
    public Date getDate() {
        return date;
    }
}
