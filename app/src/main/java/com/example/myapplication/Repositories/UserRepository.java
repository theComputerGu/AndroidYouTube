package com.example.myapplication.Repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.API.UserAPI;
import com.example.myapplication.Entities.Result;
import com.example.myapplication.Entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private UserRepository.UserListData userListData;
    private UserAPI userAPI;

    public UserRepository(){
        userListData=new UserRepository.UserListData();
        userAPI = new UserAPI();

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

    public MutableLiveData<Result> login(String username, String password) {
        MutableLiveData<Result> loginResult = new MutableLiveData<>();
        userAPI.login(username, password, loginResult);
        return loginResult;
    }

    public MutableLiveData<Result> createUser(User user) {
        MutableLiveData<Result> Result = new MutableLiveData<>();
        userAPI.createUser(user, Result);
        return Result;
    }
}
