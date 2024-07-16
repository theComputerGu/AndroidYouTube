package com.example.myapplication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Entities.Video;
import com.example.myapplication.Helper;
import com.example.myapplication.R;

import java.util.List;

public class UserVideosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Video> videos;
    public interface OnVideoClickListener {
        void onVideoDelete(Video video);
        void onVideoUpdate(Video video);
    }
    private OnVideoClickListener onVideoClickListener;

    public UserVideosAdapter(List<Video> videos,  OnVideoClickListener onVideoClickListener) {
        this.videos = videos;
        this.onVideoClickListener = onVideoClickListener;
    }

    // Return appropriate ViewHolder based on view type
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_post, parent, false);
        return new AddViewHolder(view);
    }

    // Bind data to the ViewHolder based on view type
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((AddViewHolder) holder).bind(videos.get(position), onVideoClickListener);
    }
    @Override
    public int getItemCount() {
        return videos.size();
    }


    // Update the dataset and notify the adapter
    public void updateVideos(List<Video> newVideos) {
        this.videos = newVideos;
        notifyDataSetChanged();
    }


    // ViewHolder for AddVideoActivity
    public class AddViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvViews;
        private final TextView tvContent;
        private final ImageView ivPic;
        private final TextView tvDate;
        private final Button btnDelete;
        private final ImageButton btnUpdate;

        private AddViewHolder(View itemView) {
            super(itemView);
            tvViews = itemView.findViewById(R.id.tvViews);
            tvContent = itemView.findViewById(R.id.tvContent);
            ivPic = itemView.findViewById(R.id.ivPic);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
        }


        public void bind(Video video, OnVideoClickListener onVideoDelete) {
            tvViews.setText("Views: " + video.getViews());
            tvContent.setText(video.getTitle());
            tvDate.setText(video.calculateTimeElapsed());

            String photoPath = video.getPhoto();
            Helper.loadPhotoIntoImageView(itemView.getContext(), ivPic, photoPath);

            btnDelete.setOnClickListener(v -> {
                if (onVideoDelete != null) {
                    onVideoDelete.onVideoDelete(video);
                }
            });
            btnUpdate.setOnClickListener(v -> {
                if (onVideoClickListener != null) {
                    onVideoClickListener.onVideoUpdate(video);
                }
            });
        }
    }
}
