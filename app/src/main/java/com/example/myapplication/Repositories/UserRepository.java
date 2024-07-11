package com.example.myapplication.Repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private UserRepository.UserListData userListData;

    public UserRepository(){
        userListData=new UserRepository.UserListData();
        //api = new PostAPI(postListData);

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
}
