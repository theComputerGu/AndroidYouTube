package com.example.myapplication.API;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.Entities.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM User WHERE id = :id")
    LiveData<User> getUserById(int id);

    @Query("SELECT * FROM User")
    LiveData<List<User>> getAllUsers();
}
