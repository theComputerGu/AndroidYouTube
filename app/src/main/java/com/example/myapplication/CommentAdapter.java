package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> comments;
    private Video currentVideo;
    private User currentUser;

    public CommentAdapter(List<Comment> comments, Video currentVideo, User currentUser) {
        this.comments = comments;
        this.currentUser = currentUser;
        this.currentVideo = currentVideo;
    }

    // Method to update the dataset and refresh the RecyclerView
    public void updateData(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivUserPic;
        private final TextView tvUserName;
        private final TextView tvCommentDate;
        private final TextView tvCommentText;

        CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserPic = itemView.findViewById(R.id.ivUserPic);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvCommentDate = itemView.findViewById(R.id.tvCommentDate);
            tvCommentText = itemView.findViewById(R.id.tvCommentText);
        }

        void bind(Comment comment) {
            tvUserName.setText(comment.getUser());

            for (User users: UserManager.getInstance().getUsers())
            {
                if(users.getUsername()==currentUser.getUsername())
                {
                    ivUserPic.setImageBitmap(currentUser.getPhoto());
                    tvCommentText.setText(comment.getContent());
                }
                else {
                    if(users.getUsername()==comment.getUser())
                    {
                        ivUserPic.setImageBitmap(users.getPhoto());
                        tvCommentText.setText(comment.getContent());
                        return;
                    }
                }
            }

            tvCommentDate.setText(comment.getDate());
        }
    }
}
