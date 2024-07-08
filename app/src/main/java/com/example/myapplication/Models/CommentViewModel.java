package com.example.myapplication.Models;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplication.Entities.Comment;
import com.example.myapplication.Entities.CommentRepository;
import com.example.myapplication.Entities.User;

import java.util.List;

public class CommentViewModel extends AndroidViewModel {
    private CommentRepository commentRepository;
    private LiveData<List<Comment>> comments;
    private LiveData<User> user;

    public CommentViewModel(Application application) {
        super(application);
        commentRepository = new CommentRepository(application);
    }

    public LiveData<List<Comment>> getCommentsByVideoId(int videoId) {
        comments = commentRepository.getCommentsByVideoId(videoId);
        return comments;
    }

    public LiveData<User> getUserById(int userId) {
        user = commentRepository.getUserById(userId);
        return user;
    }

    public void insert(Comment comment) {
        commentRepository.insert(comment);
    }

    public void update(Comment comment) {
        commentRepository.update(comment);
    }

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }
}
