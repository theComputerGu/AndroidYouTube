package com.example.myapplication.Repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Entities.Comment;

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
}
