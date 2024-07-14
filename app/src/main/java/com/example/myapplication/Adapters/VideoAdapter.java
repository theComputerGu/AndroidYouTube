package com.example.myapplication.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Entities.Video;
import com.example.myapplication.Helper;
import com.example.myapplication.R;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_MAIN = 1;
    public static final int VIEW_TYPE_WATCH = 2;
    public static final int VIEW_TYPE_ADD = 3;
    private List<Video> videos;
    private final int viewType;
    public interface OnVideoClickListener {
        void onVideoClick(Video video);
    }
    private OnVideoClickListener onVideoClickListener;

    public VideoAdapter(List<Video> videos, int viewType,  OnVideoClickListener onVideoClickListener) {
        this.videos = videos;
        this.viewType = viewType;
        this.onVideoClickListener = onVideoClickListener;
    }

    // Return appropriate ViewHolder based on view type
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MAIN || viewType == VIEW_TYPE_WATCH) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout, parent, false);
            return new MainViewHolder(view);
        }
        else { // VIEW_TYPE_ADD
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_post, parent, false);
            return new AddViewHolder(view);
        }
    }

    // Bind data to the ViewHolder based on view type
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Video video = videos.get(position);
        if (holder instanceof MainViewHolder) {
            ((MainViewHolder) holder).bind(video);
            holder.itemView.setOnClickListener(v -> {
                if (onVideoClickListener != null) {
                    onVideoClickListener.onVideoClick(video);
                }
            });
        }
        else if (holder instanceof AddViewHolder) {
            ((AddViewHolder) holder).bind(videos.get(position), onVideoClickListener);
        }
    }
    @Override
    public int getItemCount() {
        return videos.size();
    }

    // Return the view type based on the adapter's viewType field
    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    // Update the dataset and notify the adapter
    public void updateVideos(List<Video> newVideos) {
        this.videos = newVideos;
        notifyDataSetChanged();
    }

    // ViewHolder for MainActivity
    class MainViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAuthor;
        private final TextView tvContent;
        private final ImageView ivPic;
        private final TextView tvDate;
        private final VideoView videoView;

        private MainViewHolder(View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvContent = itemView.findViewById(R.id.tvContent);
            ivPic = itemView.findViewById(R.id.ivPic);
            tvDate = itemView.findViewById(R.id.tvDate);
            videoView = itemView.findViewById(R.id.videoView);
        }
        public void bind(Video video) {
            tvAuthor.setText(video.getAuthorDisplayName());
            tvContent.setText(video.getTitle());
            tvDate.setText(video.getTimeAgo().toString());
            videoView.setVideoPath(video.getPath());

            String photoPath = video.getPhoto();
            String baseUrl = Helper.context.getString(R.string.baseServerURL);
            String fullUrl = baseUrl + photoPath;

            Log.d("VideoAdapter", "Full URL: " + fullUrl);

            Glide.with(itemView.getContext()).load(fullUrl).into(ivPic);
        }
    }

    // ViewHolder for AddVideoActivity
    public class AddViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAuthor;
        private final TextView tvContent;
        private final ImageView ivPic;
        private final TextView tvDate;
        private Button btnDelete;

        private AddViewHolder(View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvContent = itemView.findViewById(R.id.tvContent);
            ivPic = itemView.findViewById(R.id.ivPic);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(Video video, OnVideoClickListener onVideoClickListener) {
            tvAuthor.setText(video.getAuthorDisplayName());
            tvContent.setText(video.getTitle());
            tvDate.setText(video.getTimeAgo().toString());

            String photoPath = video.getPhoto();
            String baseUrl = Helper.context.getString(R.string.baseServerURL);
            String fullUrl = baseUrl + photoPath;

            Glide.with(itemView.getContext()).load(fullUrl).into(ivPic);

            btnDelete.setOnClickListener(v -> {
                if (onVideoClickListener != null) {
                    onVideoClickListener.onVideoClick(video);
                }
            });
        }
    }
}
