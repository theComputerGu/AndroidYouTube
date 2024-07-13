package com.example.myapplication.Entities;

public class Result {
    private boolean success;
    private String errorMessage;

    public Result(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
}

