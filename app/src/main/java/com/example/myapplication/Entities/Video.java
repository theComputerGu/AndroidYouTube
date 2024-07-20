package com.example.myapplication.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.myapplication.DB.Converters;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Entity(tableName = "videos")
public class Video {
    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    @ColumnInfo(name = "video_id")
    private String videoId;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "author")
    private String author;

    @ColumnInfo(name = "author_display_name")
    private String authorDisplayName;


    @ColumnInfo(name = "time_ago")
    @TypeConverters(Converters.class)
    private Date timeAgo;

    @ColumnInfo(name = "views")
    private int views;

    @ColumnInfo(name = "photo")
    private String photo;

    @ColumnInfo(name = "path")
    private String path;

    @ColumnInfo(name = "likes")
    private int likes;

    @ColumnInfo(name = "liked_by")
    private List<String> likedBy;

    @ColumnInfo(name = "dislikes")
    private int dislikes;

    @ColumnInfo(name = "disliked_by")
    private List<String> dislikedBy;

    @ColumnInfo(name = "comments_num")
    private int commentsNum;

    @ColumnInfo(name = "comments")
    private List<String> comments;

    public Video(String title, String author,String authorDisplayName, Date timeAgo, String photo, String path) {
        this.title = title;
        this.author = author;
        this.authorDisplayName = authorDisplayName;
        this.timeAgo = timeAgo;
        this.photo = photo;
        this.path = path;
        this.views = 0;
        this.likes = 0;
        this.dislikes = 0;
        this.dislikedBy = new ArrayList<>();
        this.likedBy = new ArrayList<>();
        this.commentsNum = 0;
        this.comments = new ArrayList<>();
    }

    public String getVideoId() {
        return videoId;
    }
    public void setVideoId(String id) {
        this.videoId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorDisplayName() {
        return authorDisplayName;
    }

    public void setAuthorDisplayName(String authorDisplayName) {
        this.authorDisplayName = authorDisplayName;
    }

    public Date getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(Date timeAgo) {
        this.timeAgo = timeAgo;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public List<String> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(List<String> likedBy) {
        this.likedBy = likedBy;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public List<String> getDislikedBy() {
        return dislikedBy;
    }

    public void setDislikedBy(List<String> dislikedBy) {
        this.dislikedBy = dislikedBy;
    }

    public int getCommentsNum() {
        return commentsNum;
    }

    public void setCommentsNum(int commentsNum) {
        this.commentsNum = commentsNum;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }
    public void incrementDislikes(String username) {
        dislikedBy.add(username);
    }
    public void incrementLikes(String username) {
        likedBy.add(username);
    }
    public void decrementDislikes(String username) {
        dislikedBy.remove(username);
    }
    public void decrementLikes(String username) {
        likedBy.remove(username);
    }
    public String getViewsString() {
        return (this.views + " views");
    }
    public String calculateTimeElapsed() {
        Date now = new Date();
        long durationMillis = now.getTime() - this.timeAgo.getTime();

        // Convert milliseconds to appropriate units
        long seconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis);
        long hours = TimeUnit.MILLISECONDS.toHours(durationMillis);
        long days = TimeUnit.MILLISECONDS.toDays(durationMillis);
        long weeks = days / 7;
        long years = days / 365;

        if (seconds < 60) {
            return seconds + (seconds == 1 ? " second ago" : " seconds ago");
        } else if (minutes < 60) {
            return minutes + (minutes == 1 ? " minute ago" : " minutes ago");
        } else if (hours < 24) {
            return hours + (hours == 1 ? " hour ago" : " hours ago");
        } else if (days < 7) {
            return days + (days == 1 ? " day ago" : " days ago");
        } else if (weeks < 52) {
            return weeks + (weeks == 1 ? " week ago" : " weeks ago");
        } else {
            return years + (years == 1 ? " year ago" : " years ago");
        }
    }

    public int getLikesCount() {
        if (likedBy == null) {
            return 0;
        }
        return likedBy.size();
    }

    public int getDisLikesCount() {
        if (dislikedBy == null) {
            return 0;
        }
        return dislikedBy.size();
    }
}
