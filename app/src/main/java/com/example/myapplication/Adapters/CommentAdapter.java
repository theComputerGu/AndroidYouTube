package com.example.myapplication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    public interface onCommentDelete{
        void onCommentDelete(Comment comment);
    }
    private onCommentDelete ontDelete;

    public CommentAdapter(List<Comment> comments, onCommentDelete onCommentDelete) {
        this.comments = comments != null ? comments : new ArrayList<>();
        this.ontDelete = onCommentDelete;
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
        holder.bind(comment,ontDelete);
    }

    @Override
    public int getItemCount() {
        return comments != null ? comments.size() : 0;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivUserPic;
        private final TextView tvUserName;
        private final TextView tvCommentText;
        private Button tvDelete;


        CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserPic = itemView.findViewById(R.id.ivUserPic);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvCommentText = itemView.findViewById(R.id.tvCommentText);
            tvDelete = itemView.findViewById(R.id.btnDelete);
        }

        void bind(Comment comment, onCommentDelete onCommentDelete) {
            tvUserName.setText(comment.getDisplayName());
            tvCommentText.setText(comment.getText());
            Helper.loadPhotoIntoImageView(itemView.getContext(), ivUserPic,comment.getPhoto());

            tvDelete.setOnClickListener(v -> {
                if (onCommentDelete != null) {
                    onCommentDelete.onCommentDelete(comment);
                }
            });
        }
    }
}
