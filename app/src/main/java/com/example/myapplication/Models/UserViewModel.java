package com.example.myapplication.Models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Entities.User;
import com.example.myapplication.Entities.Video;
import com.example.myapplication.Repositories.UserRepository;

import java.util.List;

public class UserViewModel extends ViewModel {
    private UserRepository mRepository;
    private LiveData<List<User>> users;
    public UserViewModel(){
        mRepository = new UserRepository();
        users = mRepository.getAll();
    }
    public LiveData<List<User>> get() {return users; }
    public void setUsers(List<User> u) {
        mRepository.setUsers(u);
    }

    public LiveData<User> getUserById(String idOfUser){
        mRepository.getUserById(idOfUser);
    }

    public LiveData<User> getUserByUsername(String username){
        mRepository.getUserByUsername(username);
    }
}
