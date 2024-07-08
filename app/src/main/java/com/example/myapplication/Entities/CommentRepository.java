package com.example.myapplication.Entities;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.myapplication.API.AppDB;
import com.example.myapplication.API.CommentDao;
import com.example.myapplication.API.UserDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommentRepository {
    private CommentDao commentDao;
    private UserDao userDao;
    private ExecutorService executorService;

    public CommentRepository(Application application) {
        AppDB db = AppDB.getDatabase(application);
        commentDao = db.commentDao();
        userDao = db.userDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Comment>> getCommentsByVideoId(int videoId) {
        return commentDao.getCommentsByVideoId(videoId);
    }

    public LiveData<User> getUserById(int userId) {
        return userDao.getUserById(userId);
    }

    public void insert(Comment comment) {
        executorService.execute(() -> commentDao.insertComment(comment));
    }

    public void update(Comment comment) {
        executorService.execute(() -> commentDao.updateComment(comment));
    }

    public void delete(Comment comment) {
        executorService.execute(() -> commentDao.deleteComment(comment));
    }
}
