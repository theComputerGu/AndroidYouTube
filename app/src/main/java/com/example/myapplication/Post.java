package com.example.myapplication;

import java.io.Serializable;

public class Post implements Serializable {
    private String content;
    private String author;
    private int pic;
    private int likes;
    private int dislikes;
    private String date;
    private int type; // New field to store the type of the post (image or video)

    // Constants to represent the types of posts
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;

    public Post(String content, String author, int pic, String date) {
        this.content = content;
        this.author = author;
        this.pic = pic;
        this.date = date;
        this.type = TYPE_IMAGE; // By default, assume it's an image post
    }


    // Getter and setter methods for likes, dislikes, and other fields (omitted for brevity)

    // Add getter and setter methods for type and fileName
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public int getPic() {
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

}
