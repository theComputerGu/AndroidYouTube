package com.example.myapplication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Entities.Video;
import com.example.myapplication.Helper;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Video> videos;

    public interface OnVideoClickListener {
        void onVideoClick(Video video);
    }
    private OnVideoClickListener onVideoClickListener;

    public VideoAdapter(List<Video> videos,  OnVideoClickListener onVideoClickListener) {
        this.videos = videos != null ? videos : new ArrayList<>();
        this.onVideoClickListener = onVideoClickListener;
    }

    // Return appropriate ViewHolder based on view type
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout, parent, false);
        return new MainViewHolder(view);
    }

    // Bind data to the ViewHolder based on view type
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Video video = videos.get(position);
        ((MainViewHolder) holder).bind(video);
        holder.itemView.setOnClickListener(v -> {
            if (onVideoClickListener != null) {
                onVideoClickListener.onVideoClick(video);
            }
        });

    }
    @Override
    public int getItemCount() {
        if (videos == null) {
            return 0; // Return 0 if videos list is null
        }
        return videos.size();
    }


    // Update the dataset and notify the adapter
    public void updateVideos(List<Video> newVideos) {
        videos.clear(); // Clear existing data
        if (newVideos != null) {
            videos.addAll(newVideos);
        }
        notifyDataSetChanged(); // Notify adapter that data set has changed
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
            tvDate.setText(video.calculateTimeElapsed());
            videoView.setVideoPath(video.getPath());

            Helper.loadPhotoIntoImageView(itemView.getContext(), ivPic, video.getPhoto());
            //Helper.loadVideoIntoVideoView(itemView.getContext(), videoView, video.getPath());
        }
    }
}
