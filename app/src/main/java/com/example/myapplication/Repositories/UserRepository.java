package com.example.myapplication.Repositories;

import androidx.lifecycle.LiveData;

import com.example.myapplication.API.UserAPI;
import com.example.myapplication.Entities.Result;
import com.example.myapplication.Entities.User;
import com.example.myapplication.Entities.Video;

import java.io.File;
import java.util.List;

public class UserRepository {
    private UserAPI userAPI;

    public UserRepository() {
        this.userAPI = new UserAPI();
    }
    public LiveData<Result> login(String username, String password) {
        return userAPI.login(username, password);
    }

    public LiveData<Result> createUser(User user) {
        return userAPI.createUser(user);
    }

    public LiveData<User> getUserByUsername(String username) {
        return userAPI.getUserByUsername(username);
    }
    public LiveData<User> getUserById(String userId) {
        return userAPI.getUserById(userId);
    }
    public LiveData<Result> createUserVideo(String userId, String title, String author, String photo, File videoFile) {
        return userAPI.createUserVideo(userId, title, author, photo, videoFile);
    }
    public LiveData<Result> deleteUserVideo(String userId, String videoId) {
        return userAPI.deleteUserVideo(userId, videoId);
    }
    public LiveData<Result> updateUserVideo(String userId, String videoId, String title) {
        return userAPI.updateUserVideo(userId, videoId,title);
    }

    public LiveData<List<Video>> getUserVideos(String userId) {
        return userAPI.getUserVideos(userId);
    }
    public LiveData<Result> updateDisplayName(String userId, String newDisplayName) {
        return userAPI.updateDisplayName(userId, newDisplayName);
    }
    public LiveData<Result> deleteUser(String userId, String username) {
        return userAPI.deleteUser(userId, username);
    }
}
