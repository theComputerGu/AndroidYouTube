package com.example.myapplication.Models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Entities.Comment;
import com.example.myapplication.Entities.Result;
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

    public LiveData<Comment> createComment(String videoId, String username, String text) {
        return mRepository.createComment(videoId, username, text);
    }
    public MutableLiveData<List<Comment>> getComments(String videoId) {
        return mRepository.getComments(videoId);
    }

    public  LiveData<Boolean> deleteComment(String videoId, String commentId) {
        return mRepository.deleteComment(videoId, commentId);
    }

    public LiveData<Comment> updateComment(Comment comment,String videoId) {
        return mRepository.updateComment(comment,videoId);
    }

}