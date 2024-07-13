package com.example.myapplication.Repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.API.CommentAPI;
import com.example.myapplication.Entities.Comment;
import com.example.myapplication.Entities.Video;

import java.util.ArrayList;
import java.util.List;

public class CommentRepository {
    private CommentRepository.CommentListData commentListData;
    public CommentRepository(){
        commentListData=new CommentRepository.CommentListData();
        //api = new PostAPI(postListData);

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
        CommentAPI.createComment(comment,  comment.getVideoId(), commentLiveData);
    }

    public MutableLiveData<List<Comment>> getComments(Comment comment) {
        MutableLiveData<List<Comment>> CommentData = new MutableLiveData<List<Comment>>();
        CommentAPI.getComments(comment.getVideoId(), CommentData);
        return CommentData;
    }

    public MutableLiveData<Boolean> deleteComment(Comment comment) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        CommentAPI.deleteComment(comment.getVideoId(),comment.getCommentId(), result);
        return result;
    }

}
