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

    private List<User> usersLike;

    private List<User> usersDislike;

    private List<User> usersShares;
    private List<User> usersComments;


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
        this.usersDislike = new ArrayList<>();
        this.usersComments = new ArrayList<>();
        this.usersLike = new ArrayList<>();
        this.usersShares = new ArrayList<>();
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
    public Uri getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(Uri videoUri) {
        this.videoUri = videoUri;
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
