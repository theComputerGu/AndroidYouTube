package com.example.myapplication;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;

public class Post implements Serializable {
    private String content;
    private String author;
    private Bitmap pic;
    private Uri videoUri;

    private int likes;
    private int dislikes;
    private int shares;
    private String date;

    public Post(String content, String author, String date, Bitmap pic,Uri videoUri) {
        this.content = content;
        this.author = author;
        this.pic = pic;
        this.videoUri = videoUri ;
        this.date = date;
        this.likes = 0;
        this.dislikes = 0;
        this.shares = 0;
    }

    public Uri getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(Uri videoUri) {
        this.videoUri = videoUri;
    }


    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
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

    public void setLikes(int likes) {
        this.likes = likes;
    }


    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }
    public int getShares() {
        return shares;
    }
    public void setShares(int shares) {
        this.shares = shares;
    }
}
