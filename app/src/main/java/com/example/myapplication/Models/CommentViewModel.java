package com.example.myapplication.Models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Entities.Comment;
import com.example.myapplication.Entities.Video;
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

    public void createComment(Comment comment) {
         mRepository.createComment(comment);
    }
    public MutableLiveData<List<Comment>> getComments(Comment comment) {
        return mRepository.getComments(comment);
    }

    public void deleteComment(Comment comment) {
        mRepository.deleteComment(comment);
    }

}