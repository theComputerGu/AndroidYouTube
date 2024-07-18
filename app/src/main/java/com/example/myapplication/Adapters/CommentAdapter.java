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

import com.example.myapplication.DB.Converters;
import com.example.myapplication.Entities.Comment;
import com.example.myapplication.Helper;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> comments;

    private boolean buttonsEnabled;

    public interface onCommentDelete{
        void onCommentDelete(Comment comment);
    }

    public interface CommentUpdate{
        void CommentUpdate(Comment comment);
    }


    private onCommentDelete ontDelete;

    private CommentUpdate ontDelete2;

    public CommentAdapter(List<Comment> comments, onCommentDelete onCommentDelete,boolean buttonsEnabled,CommentUpdate commentUpdateListener) {
        this.comments = comments != null ? comments : new ArrayList<>();
        this.ontDelete = onCommentDelete;
        this.buttonsEnabled = buttonsEnabled;
        this.ontDelete2 = commentUpdateListener;
    }

    // Method to update the dataset and refresh the RecyclerView
    public void updateData(List<Comment> comments) {
        //this.comments.clear();
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
        holder.bind(comment,ontDelete,ontDelete2,buttonsEnabled);
    }

    @Override
    public int getItemCount() {
        return comments != null ? comments.size() : 0;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivUserPic;
        private final TextView tvUserName;
        private final TextView tvCommentText;

        private final TextView btnEdit;
        private Button tvDelete;


        CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserPic = itemView.findViewById(R.id.ivUserPic);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvCommentText = itemView.findViewById(R.id.tvCommentText);
            tvDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }

        void bind(Comment comment, onCommentDelete onCommentDelete,CommentUpdate CommentUpdate,boolean buttonsEnabled) {
            tvUserName.setText(comment.getDisplayName());
//            ivUserPic.setImageBitmap(Converters.base64ToBitmap(comment.getPhoto()));
            tvCommentText.setText(comment.getText());
            Helper.loadPhotoIntoImageView(itemView.getContext(), ivUserPic,comment.getPhoto());

            btnEdit.setVisibility(buttonsEnabled ? View.VISIBLE : View.GONE);
            btnEdit.setEnabled(buttonsEnabled);

            tvDelete.setOnClickListener(v -> {
                if (onCommentDelete != null) {
                    onCommentDelete.onCommentDelete(comment);
                }
            });

            btnEdit.setOnClickListener(v -> {
                if (CommentUpdate != null) {
                    CommentUpdate.CommentUpdate(comment);
                }
            });
        }
    }
}
