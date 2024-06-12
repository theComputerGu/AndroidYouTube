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

import java.util.List;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Post post);
    }

    private final LayoutInflater mInflater;
    private List<Post> posts;
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
    public void onBindViewHolder(PostViewHolder holder, int position) {
        if (posts != null) {
            final Post current = posts.get(position);
            holder.tvAuthor.setText(current.getAuthor());
            holder.tvContent.setText(current.getContent());
            holder.tvDate.setText(current.getDate());

            // Display image or video based on the post type
            if (current.getType() == Post.TYPE_IMAGE) {
                holder.ivPic.setVisibility(View.VISIBLE);
                holder.videoView.setVisibility(View.GONE);
                holder.ivPic.setImageResource(current.getPic());

                // Handle image click
                holder.ivPic.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onItemClick(current);
                    }
                });
            } else if (current.getType() == Post.TYPE_VIDEO) {
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
        }
    }

    public void setPosts(List<Post> s) {
        posts = s;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (posts != null) {
            return posts.size();
        } else {
            return 0;
        }
    }

    public List<Post> getPosts() {
        return posts;
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
