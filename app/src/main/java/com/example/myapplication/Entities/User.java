package com.example.myapplication.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    private String userId;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "displayName")
    private String displayName;

    @ColumnInfo(name = "profilePicture")
    private String profilePicture;

    // Constructor
    public User(String username, String password, String displayName, String profilePicture) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.profilePicture = profilePicture;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String id) {
        this.userId = id;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
