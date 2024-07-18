package com.example.myapplication.Repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.API.CommentAPI;
import com.example.myapplication.Entities.Comment;
import com.example.myapplication.Entities.Result;

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

    public LiveData<Comment> createComment(String videoId, String username,String text) {
        MutableLiveData<Comment> data = new MutableLiveData<>();
        CommentAPI.createComment(videoId, username,text, data);
        return data;
    }

    public LiveData<Comment> updateComment(Comment comment, String videoId) {
        MutableLiveData<Comment> data = new MutableLiveData<>();
        CommentAPI.updateComment(comment,videoId, data);
        return data;
    }

    public MutableLiveData<List<Comment>> getComments(String videoId) {
        MutableLiveData<List<Comment>> data = new MutableLiveData<>();
        CommentAPI.getComments(videoId, data);
        return data;
    }

    public LiveData<Boolean> deleteComment(String videoId, String commentId) {
        MutableLiveData<Boolean> deleteResult = new MutableLiveData<>();
        CommentAPI.deleteComment(videoId, commentId, deleteResult);
        return deleteResult;
    }

}
