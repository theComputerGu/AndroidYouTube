package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // Define view types as constants
    public static final int VIEW_TYPE_MAIN = 1;
    public static final int VIEW_TYPE_WATCH = 2;
    public static final int VIEW_TYPE_ADD = 3;
    private List<Video> videos;
    private int viewType;
    public interface OnVideoClickListener {
        void onVideoClick(Video video);
    }
    private OnVideoClickListener onVideoClickListener;

    // Constructor with additional viewType parameter
    public VideoAdapter(List<Video> videos, int viewType,  OnVideoClickListener onVideoClickListener) {
        this.videos = videos;
        this.viewType = viewType;
        this.onVideoClickListener = onVideoClickListener;
    }

    // Return appropriate ViewHolder based on view type
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout, parent, false);
        return new MainViewHolder(view);
//        View view;
//        if (viewType == VIEW_TYPE_MAIN) {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout, parent, false);
//            return new MainViewHolder(view);
//        }
//        else if (viewType == VIEW_TYPE_WATCH) {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item_watch, parent, false);
//            return new WatchViewHolder(view);
//        } else { // VIEW_TYPE_ADD
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item_add, parent, false);
//            return new AddViewHolder(view);
//        }
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
    }


    // Return item count
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
            tvAuthor.setText(video.getUsername());
            tvContent.setText(video.getTitle());
            ivPic.setImageBitmap(video.getPic()); // Assuming you have a method to get the thumbnail as Bitmap
            tvDate.setText(video.getDate());

            // Set up the VideoView with the video URI
            videoView.setVideoURI(video.getVideoUri());
            // Optionally, you can set up other properties of the video view such as controls, autoplay, etc.
        }
    }

//    // ViewHolder for WatchVideoActivity
//    public static class WatchViewHolder extends RecyclerView.ViewHolder {
//        public TextView titleTextView;
//
//        public WatchViewHolder(View itemView) {
//            super(itemView);
//            titleTextView = itemView.findViewById(R.id.titleTextView);
//        }
//
//        public void bind(Video video) {
//            titleTextView.setText(video.getTitle());
//        }
//    }

//    // ViewHolder for AddVideoActivity
//    public static class AddViewHolder extends RecyclerView.ViewHolder {
//        public TextView titleTextView;
//
//        public AddViewHolder(View itemView) {
//            super(itemView);
//            titleTextView = itemView.findViewById(R.id.titleTextView);
//        }
//
//        public void bind(Video video) {
//            titleTextView.setText(video.getTitle());
//        }
//    }
}
