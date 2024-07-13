package com.example.myapplication.Entities;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.example.myapplication.API.Convertors;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "users")
public class User {

    @SerializedName("_id")
    private String userId;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "displayName")
    private String displayName;

    @ColumnInfo(name = "profilePicture")
    @TypeConverters(Convertors.class)
    private Bitmap profilePicture;

    // Constructor
    public User(String username, String password, String displayName, Bitmap profilePicture) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.profilePicture = profilePicture;
    }

    // Getters and setters
    public String getId() {
        return userId;
    }

    public void setId(String id) {
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

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }
}
