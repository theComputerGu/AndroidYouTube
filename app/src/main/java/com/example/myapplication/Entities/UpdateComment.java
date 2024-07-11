package com.example.myapplication.Entities;

import androidx.room.ColumnInfo;

public class UpdateComment {

    @ColumnInfo(name = "text")
    private String text;

    public UpdateComment(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
