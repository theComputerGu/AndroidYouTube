package com.example.myapplication.Entities;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.myapplication.API.Convertors;

@Entity(tableName = "comments")
public class Comment {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "displayName")
    private String displayName;

    @ColumnInfo(name = "photo")
    @TypeConverters(Convertors.class)
    private Bitmap photo;

    @ColumnInfo(name = "videoId")
    private String videoId;

    @ColumnInfo(name = "text")
    private String text;

    public Comment(String username, String displayName, Bitmap photo, String videoId, String text) {
        this.username = username;
        this.displayName = displayName;
        this.photo = photo;
        this.videoId = videoId;
        this.text = text;
    }

    // Getters and setters for all fields

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
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
