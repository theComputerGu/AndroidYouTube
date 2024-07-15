package com.example.myapplication.Repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.API.UserAPI;
import com.example.myapplication.Entities.Result;
import com.example.myapplication.Entities.User;
import com.example.myapplication.Entities.Video;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private UserRepository.UserListData userListData;
    private UserAPI userAPI;

    public UserRepository() {
        userListData = new UserListData();
        this.userAPI = new UserAPI();
    }
    public void setUsers(List<User> users) {
        userListData.setValue(users);
    }


    public class UserListData extends MutableLiveData<List<User>> {
        public UserListData(){
            super();
            setValue(new ArrayList<>());
        }
        @Override
        protected void onActive(){
            super.onActive();

        }

    }
    public LiveData<List<User>> getAll() {return userListData; }

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
    public LiveData<Result> createUserVideo(String userId, String title, String author, File videoFile, String photo) {
        return userAPI.createUserVideo(userId, title, author, videoFile, photo);
    }
    public LiveData<Result> deleteUserVideo(String userId, String videoId, String token) {
        return userAPI.deleteUserVideo(userId, videoId, token);
    }

    public LiveData<List<Video>> getUserVideos(String userId) {
        return userAPI.getUserVideos(userId);
    }
}
