package com.example.myapplication.Models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Entities.Result;
import com.example.myapplication.Entities.User;
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

    public LiveData<Result> getLoginResult(String username, String password) {
        return mRepository.login(username, password);
    }

    public LiveData<Result> getCreateUserResult(User user) {
        return mRepository.createUser(user);
    }
}
