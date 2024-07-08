package com.example.myapplication.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Comment {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "video_id")
    private int videoId;

    public Comment(String content, int userId, String date, int videoId) {
        this.content = content;
        this.userId = userId;
        this.date = date;
        this.videoId = videoId;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public int getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }
}
