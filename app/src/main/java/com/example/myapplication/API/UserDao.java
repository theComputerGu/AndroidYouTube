package com.example.myapplication.API;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.Entities.User;

@Dao
public interface UserDao {
    @Update
    void updateUser(User user);

    @Query("SELECT * FROM users LIMIT 1")
    User getUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... user);

    @Query("DELETE FROM users")
    void deleteAllUsers();
}
