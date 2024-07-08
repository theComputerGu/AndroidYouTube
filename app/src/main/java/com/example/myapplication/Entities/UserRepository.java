package com.example.myapplication.Entities;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myapplication.API.AppDB;
import com.example.myapplication.API.UserDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private UserDao userDao;
    private LiveData<List<User>> allUsers;
    private ExecutorService executorService;

    public UserRepository(Application application) {
        AppDB db = AppDB.getDatabase(application);
        userDao = db.userDao();
        allUsers = userDao.getAllUsers();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public void insert(User user) {
        executorService.execute(() -> userDao.insertUser(user));
    }

    public void update(User user) {
        executorService.execute(() -> userDao.updateUser(user));
    }

    public void delete(User user) {
        executorService.execute(() -> userDao.deleteUser(user));
    }

    public LiveData<User> getUserById(int userId) {
        return userDao.getUserById(userId);
    }
}
