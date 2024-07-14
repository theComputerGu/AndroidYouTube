package com.example.myapplication.Entities;

public class Result<T> {
    private boolean success;
    private T data;
    private String errorMessage;

    public Result(boolean success, T data, String errorMessage) {
        this.success = success;
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
