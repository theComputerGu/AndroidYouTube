package com.example.myapplication;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Post {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String author;

    private String content;

    private String Date;
    private int likes;


    private int pic;


    public Post() {
        this.pic = R.drawable.boat;
    }

    public Post(String author, String content, int pic,String Date){
        this.pic = pic;
        this.author = author;
        this.content = content;
        this.Date = Date;
    }
    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getDate() {
        return Date;
    }

    public void setTimeAgo(String Date) {
        this.Date = Date;
    }
}
