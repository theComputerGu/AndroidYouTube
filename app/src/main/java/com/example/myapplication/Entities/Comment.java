package com.example.myapplication.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "comments")
public class Comment {
    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    private String commentId;
    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "displayName")
    private String displayName;

    @ColumnInfo(name = "photo")
    private String photo;

    @ColumnInfo(name = "videoId")
    private String videoId;

    @ColumnInfo(name = "text")
    private String text;

    public Comment(String username, String displayName, String photo, String videoId, String text) {
        this.username = username;
        this.displayName = displayName;
        this.photo = photo;
        this.videoId = videoId;
        this.text = text;
    }

    // Getters and setters for all fields

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
