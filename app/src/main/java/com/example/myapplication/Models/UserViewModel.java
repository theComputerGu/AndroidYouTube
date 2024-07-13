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
    private UserRepository mRepository;
    private LiveData<List<User>> users;
    public UserViewModel() {
        this.mRepository = new UserRepository();
        this.users = mRepository.getAll();
    }


    public String getToken() {
        return mRepository.getToken();
    }
    public LiveData<List<User>> get() {return users; }
    public void setUsers(List<User> u) {
        mRepository.setUsers(u);
    }


    public LiveData<Result> login(String username, String password) {
        return mRepository.login(username, password);
    }

    public LiveData<Result> getCreateUserResult(User user) {
        return mRepository.createUser(user);
    }

    public LiveData<User> getUserById(String idOfUser){
        return mRepository.getUserById(idOfUser);
    }

    public LiveData<User> getUserByUsername(String username){
        return mRepository.getUserByUsername(username);
    }
    public LiveData<List<Video>> getUserVideos(String userId) {
        return mRepository.getUserVideos(userId);
    }
    public LiveData<Result> createUserVideo(String userId, String title, String author, File videoFile, String photo) {
        return mRepository.createUserVideo(userId, title, author, videoFile, photo);
    }
    public LiveData<Result> deleteUserVideo(String userId, String videoId, String token) {
        return mRepository.deleteUserVideo(userId, videoId, token);
    }
}
