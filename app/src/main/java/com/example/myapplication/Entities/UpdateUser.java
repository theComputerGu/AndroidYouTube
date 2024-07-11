package com.example.myapplication.Entities;

import com.google.gson.annotations.SerializedName;

public class UpdateUser {

    @SerializedName("displayName")
    private String displayName;

    public UpdateUser(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}

