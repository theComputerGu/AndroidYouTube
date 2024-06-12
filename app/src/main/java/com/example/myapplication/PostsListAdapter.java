package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder> {



    public interface OnItemClickListener {
        void onItemClick(Post post);
    }
    private static List<Post> posts = new ArrayList<>();

    private final LayoutInflater mInflater;
    private OnItemClickListener listener;
    public PostsListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.post_layout, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder,int position) {
        final Post current = posts.get(position);
        holder.tvAuthor.setText(current.getAuthor());
        holder.tvContent.setText(current.getContent());
        holder.tvDate.setText(current.getDate());

        holder.ivPic.setVisibility(View.VISIBLE);
        holder.videoView.setVisibility(View.GONE);
        holder.ivPic.setImageBitmap(current.getPic());

        // Handle image click
        holder.ivPic.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(current);
            }
        });

    }
    public void onBindViewHolderVideo(PostViewHolder holder,int position) {
        final Post current = posts.get(position);
        holder.tvAuthor.setText(current.getAuthor());
        holder.tvContent.setText(current.getContent());
        holder.tvDate.setText(current.getDate());

        holder.ivPic.setVisibility(View.GONE);
        holder.videoView.setVisibility(View.VISIBLE);

        // Set video path
        String videoPath = "android.resource://" + holder.itemView.getContext().getPackageName() + "/raw/" + current.getContent();
        holder.videoView.setVideoURI(Uri.parse(videoPath));

        // Handle video click
        holder.videoView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(current);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public List<Post> getPosts() {
        return posts;
    }
    public void setPosts(List<Post> newPosts) {
        posts = newPosts;
        notifyDataSetChanged();
    }
    public void addPost(Post post) {
        posts.add(post);
        notifyDataSetChanged();
    }
    public void deletePost(Post post) {
        Iterator<Post> iterator = posts.iterator();

        while (iterator.hasNext()) {
            Post currentPost = iterator.next();

            if (currentPost.getAuthor().equals(post.getAuthor()) &&
                    currentPost.getContent().equals(post.getContent())) {
                // Remove the current post from the list
                iterator.remove();

                // Update the post list
                setPosts(posts);
                notifyDataSetChanged();
                return;
            }
        }
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAuthor;
        private final TextView tvContent;
        private final ImageView ivPic;
        private final TextView tvDate;
        private final VideoView videoView;

        private PostViewHolder(View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvContent = itemView.findViewById(R.id.tvContent);
            ivPic = itemView.findViewById(R.id.ivPic);
            tvDate = itemView.findViewById(R.id.tvDate);
            videoView = itemView.findViewById(R.id.videoView);
        }
    }
}