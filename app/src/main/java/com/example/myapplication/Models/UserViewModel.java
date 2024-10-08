package com.example.myapplication.Models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Entities.Result;
import com.example.myapplication.Entities.User;
import com.example.myapplication.Entities.Video;
import com.example.myapplication.Repositories.UserRepository;

import java.io.File;
import java.util.List;

public class UserViewModel extends ViewModel {
    private static UserViewModel instance;
    private UserRepository mRepository;

    // Private constructor to prevent instantiation outside this class
    private UserViewModel() {
        this.mRepository = new UserRepository();
    }

    // Public static method to access the singleton instance
    public static synchronized UserViewModel getInstance() {
        if (instance == null) {
            instance = new UserViewModel();
        }
        return instance;
    }


    public LiveData<Result> login(String username, String password) {
        return mRepository.login(username, password);
    }
    public LiveData<Result> createUser(User user) {
        return mRepository.createUser(user);
    }
    public LiveData<User> getUserByUsername(String username){
        return mRepository.getUserByUsername(username);
    }
    public LiveData<User> getUserById(String userId){
        return mRepository.getUserById(userId);
    }
    public LiveData<List<Video>> getUserVideos(String userId) {
        return mRepository.getUserVideos(userId);
    }
    public LiveData<Result> createUserVideo(String userId, String title, String author, String photo, File videoFile) {
        return mRepository.createUserVideo(userId, title, author, photo, videoFile);
    }
    public LiveData<Result> deleteUserVideo(String userId, String videoId) {
        return mRepository.deleteUserVideo(userId, videoId);
    }
    public LiveData<Result> updateUserVideo(String userId, String videoId, String title) {
        return mRepository.updateUserVideo(userId, videoId,title);
    }
    public LiveData<Result> updateDisplayName(String userId, String newDisplayName) {
        return mRepository.updateDisplayName(userId, newDisplayName);
    }
    public LiveData<Result> deleteUser(String userId, String username) {
        return mRepository.deleteUser(userId, username);
    }
}
