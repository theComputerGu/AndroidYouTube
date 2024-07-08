package com.example.myapplication.Entities;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.myapplication.API.Convertors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Entity
public class Video implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "idUserName")
    private String username;

    @ColumnInfo(name = "videoPath")
    private String videoPath;

    @ColumnInfo(name = "pic")
    @TypeConverters(Convertors.class)
    private Bitmap pic;

    @ColumnInfo(name = "likes")
    private int likes;

    @ColumnInfo(name = "dislikes")
    private int dislikes;

    @ColumnInfo(name = "shares")
    private int shares;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "comments")
    @TypeConverters(Convertors.class)
    private List<Comment> comments;

    @ColumnInfo(name = "usersLike")
    @TypeConverters(Convertors.class)
    private List<User> usersLike;

    @ColumnInfo(name = "usersDislike")
    @TypeConverters(Convertors.class)
    private List<User> usersDislike;

    @ColumnInfo(name = "usersShares")
    @TypeConverters(Convertors.class)
    private List<User> usersShares;

    @ColumnInfo(name = "usersComments")
    @TypeConverters(Convertors.class)
    private List<User> usersComments;


    public Video(String title, String username,  String date, Bitmap pic, String videoPath) {
        this.title = title;
        this.username = username;
        this.pic = pic;
        this.date = date;
        this.videoPath = videoPath;
        this.comments = new ArrayList<>();
        this.likes = 0;
        this.dislikes = 0;
        this.shares = 0;
        this.usersDislike = new ArrayList<>();
        this.usersComments = new ArrayList<>();
        this.usersLike = new ArrayList<>();
        this.usersShares = new ArrayList<>();
    }
    public int getId() {
        return id;
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

    public void setLikes() {
        this.likes++;
    }


    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes() {
        this.dislikes++;
    }
    public void setPic(Bitmap pic) {
        this.pic = pic;
    }
    public String getVideoPath() {
        return videoPath;
    }


    public int getShares() {
        return shares;
    }

    public void setShares() {
        this.shares++;
    }

    public List<User> getUsersLike() {
        return usersLike;
    }

    public void setUsersLike(User usersLike) {
        this.usersLike.add(usersLike);
    }

    public List<User> getUsersDislike() {
        return usersDislike;
    }

    public void setUsersDislike(User usersDislike) {
        this.usersDislike.add(usersDislike);
    }

    public List<User> getUsersShares() {
        return usersShares;
    }

    public void setUsersShares(User usersShares) {
        this.usersShares.add(usersShares);
    }

    public List<User> getUsersComments() {
        return usersComments;
    }

    public void setUsersComments(User usersComments) {
        this.usersComments.add(usersComments);
    }
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void removeComment (Comment comment)
    {
        this.comments.remove(comment);
    }
}
