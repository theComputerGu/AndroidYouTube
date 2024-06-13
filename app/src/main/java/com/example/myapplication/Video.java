package com.example.myapplication;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Video implements Serializable {
    private String title;
    private String username;
    private Uri videoUri;
    private Bitmap pic;
    private int likes;
    private int dislikes;
    private int shares;
    private String date;
    private List<Comment> comments;


    public Video(String title, String username,  String date, Bitmap pic, Uri videoUri) {
        this.title = title;
        this.username = username;
        this.pic = pic;
        this.date = date;
        this.videoUri = videoUri;
        this.comments = new ArrayList<>();
        this.likes = 0;
        this.dislikes = 0;
        this.shares = 0;
    }
    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public String getTitle() {
        return title;
    }

    public String getUsername() {
        return username;
    }

    public Bitmap getPic() {
        return pic;
    }

    public String getDate() {
        return date;
    }

    public int getLikes() {
        return likes;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }


    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }
    public void setPic(Bitmap pic) {
        this.pic = pic;
    }
    public Uri getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(Uri videoUri) {
        this.videoUri = videoUri;
    }


}
