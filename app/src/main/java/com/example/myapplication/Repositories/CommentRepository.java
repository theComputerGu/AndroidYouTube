package com.example.myapplication.Repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.API.CommentAPI;
import com.example.myapplication.Entities.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentRepository {
    private CommentRepository.CommentListData commentListData;
    private CommentAPI commentAPI;
    public CommentRepository(){
        commentListData=new CommentRepository.CommentListData();
        commentAPI=new CommentAPI();

    }
    public void setComments(List<Comment> Comments) {
        commentListData.setValue(Comments);
    }
    public class CommentListData extends MutableLiveData<List<Comment>> {
        public CommentListData(){
            super();
            setValue(new ArrayList<>());
        }
        @Override
        protected void onActive(){
            super.onActive();
        }
    }
    public LiveData<List<Comment>> getAll() {return commentListData; }

    public void createComment(Comment comment) {
        MutableLiveData<Comment> commentLiveData = new MutableLiveData<>(comment);
        commentAPI.createComment(comment,  comment.getVideoId(), commentLiveData);
    }

    public MutableLiveData<List<Comment>> getComments(Comment comment) {
        MutableLiveData<List<Comment>> CommentData = new MutableLiveData<List<Comment>>();
        commentAPI.getComments(comment.getVideoId(), CommentData);
        return CommentData;
    }

    public MutableLiveData<Boolean> deleteComment(Comment comment) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        commentAPI.deleteComment(comment.getVideoId(),comment.getCommentId(), result);
        return result;
    }

}
