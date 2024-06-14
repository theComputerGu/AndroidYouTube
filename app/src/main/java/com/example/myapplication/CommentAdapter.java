//// CommentAdapter.java
//package com.example.myapplication;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.text.SimpleDateFormat;
//import java.util.List;
//import java.util.Locale;
//
//public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
//    private Context context;
//    private List<Comment> comments;
//
//    public CommentAdapter(List<Comment> comments) {
//        this.comments = comments;
//    }
//
//    @NonNull
//    @Override
//    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
//        return new CommentViewHolder(view);
//
//    @Override
//    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
//        Comment comment = comments.get(position);
//        holder.bind(comment);
//    }
//
//    @Override
//    public int getItemCount() {
//        return comments.size();
//    }
//
//    class CommentViewHolder extends RecyclerView.ViewHolder {
//        private TextView tvUsername;
//        private TextView tvContent;
//        private TextView tvDate;
//
//        CommentViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvUsername = itemView.findViewById(R.id.tvUsername);
//            tvContent = itemView.findViewById(R.id.tvContent);
//            tvDate = itemView.findViewById(R.id.tvDate);
//        }
//
//        void bind(Comment comment) {
//            tvUsername.setText(comment.getUsername());
//            tvContent.setText(comment.getContent());
//
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//            String dateString = sdf.format(comment.getDate());
//            tvDate.setText(dateString);
//        }
//    }
//}
