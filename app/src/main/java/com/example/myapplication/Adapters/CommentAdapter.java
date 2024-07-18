package com.example.myapplication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Entities.Comment;
import com.example.myapplication.Helper;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> comments;

    public interface OnCommentClickListener{
        void onCommentDelete(Comment comment);
        void onCommentUpdate(Comment comment);
    }
    private OnCommentClickListener onCommentClick;

    public CommentAdapter(List<Comment> comments, OnCommentClickListener onCommentClick) {
        this.comments = comments != null ? comments : new ArrayList<>();
        this.onCommentClick = onCommentClick;
    }
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
        holder.bind(comment,onCommentClick);
    }

    @Override
    public int getItemCount() {
        return comments != null ? comments.size() : 0;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivUserPic;
        private final TextView tvUserName;
        private final TextView tvCommentText;
        private Button btnDelete;
        private ImageButton btnUpdate;


        CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserPic = itemView.findViewById(R.id.ivUserPic);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvCommentText = itemView.findViewById(R.id.tvCommentText);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
        }

        void bind(Comment comment, OnCommentClickListener onCommentClick) {
            tvUserName.setText(comment.getDisplayName());
            tvCommentText.setText(comment.getText());
            Helper.loadPhotoIntoImageView(itemView.getContext(), ivUserPic,comment.getPhoto());

            btnDelete.setOnClickListener(v -> {
                if (onCommentClick != null) {
                    onCommentClick.onCommentDelete(comment);
                }
            });
            btnUpdate.setOnClickListener(v -> {
                if (onCommentClick != null) {
                    onCommentClick.onCommentUpdate(comment);
                }
            });

        }
    }
}
