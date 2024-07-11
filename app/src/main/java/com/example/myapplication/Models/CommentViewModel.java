package com.example.myapplication.Models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Entities.Comment;
import com.example.myapplication.Repositories.CommentRepository;

import java.util.List;

public class CommentViewModel extends ViewModel {
    private CommentRepository mRepository;
    private LiveData<List<Comment>> comments;
    public CommentViewModel(){
        mRepository = new CommentRepository();
        comments = mRepository.getAll();
    }
    public LiveData<List<Comment>> get() {return comments; }
    public void setComments(List<Comment> c) {
        mRepository.setComments(c);
    }
}