package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VideoListManager {
    private static VideoListManager instance;
    private List<Video> videos;
    private Context context;

    // Private constructor to prevent instantiation
    private VideoListManager(Context context) {
        this.context = context.getApplicationContext();;
        videos = new ArrayList<>();
        initializeDefaultVideos();
    }

    // Synchronized method to ensure only one instance is created
    public static synchronized VideoListManager getInstance(Context context) {
        if (instance == null) {
            instance = new VideoListManager(context);
        }
        return instance;
    }

    private void initializeDefaultVideos() {
        videos.add(new Video("boat", "Author1", "4/09/2004", getBitmapFromUri(R.drawable.boat), getUriFromRawResource(R.raw.boat)));
        videos.add(new Video("buildings", "Author2", "3/04/2022", getBitmapFromUri(R.drawable.buildings), getUriFromRawResource(R.raw.buildings)));
        videos.add(new Video("ferris_wheel", "Author3", "3/03/2021", getBitmapFromUri(R.drawable.ferris_wheel), getUriFromRawResource(R.raw.ferris_wheel)));
        videos.add(new Video("girl_flowers", "Author4", "5/08/2001", getBitmapFromUri(R.drawable.girl_flowers), getUriFromRawResource(R.raw.girl_flowers)));
        videos.add(new Video("hilton", "Author5", "6/07/2003", getBitmapFromUri(R.drawable.hilton), getUriFromRawResource(R.raw.hilton)));
        videos.add(new Video("hard_rock_cafe", "Author6", "5/04/2018", getBitmapFromUri(R.drawable.hard_rock_cafe), getUriFromRawResource(R.raw.hard_rock_cafe)));
        videos.add(new Video("keyboard", "Author7", "5/05/2001", getBitmapFromUri(R.drawable.keyboard), getUriFromRawResource(R.raw.keyboard)));
        videos.add(new Video("kite", "Author8", "6/07/2020", getBitmapFromUri(R.drawable.kite), getUriFromRawResource(R.raw.kite)));
        videos.add(new Video("men_sunset", "Author9", "3/01/2020", getBitmapFromUri(R.drawable.men_sunset), getUriFromRawResource(R.raw.men_sunset)));
        videos.add(new Video("road", "Author10", "1/01/2011", getBitmapFromUri(R.drawable.road), getUriFromRawResource(R.raw.road)));
        videos.add(new Video("women_reading", "Author11", "3/01/2021", getBitmapFromUri(R.drawable.women_reading), getUriFromRawResource(R.raw.women_reading)));
    }

    private Bitmap getBitmapFromUri(int drawableId) {
        return BitmapFactory.decodeResource(context.getResources(), drawableId);
    }

    private Uri getUriFromRawResource(int rawResourceId) {
        return Uri.parse("android.resource://" + context.getPackageName() + "/" + rawResourceId);
    }

    // Method to get the video list
    public List<Video> getVideos() {
        return videos;
    }

    // Method to set the video list
    public void setVideos(List<Video> videoList) {
        this.videos = videoList;
    }

    // Method to add a video to the list
    public void addVideo(Video video) {
        videos.add(video);
    }

    // Method to remove a video from the list
    public void removeVideo(Video video) {
        videos.remove(video);
    }

    public List<Video> getVideosByUser(User user) {
        return videos.stream()
                .filter(video -> video.getUsername().equals(user))
                .collect(Collectors.toList());
    }

    public List<Video> searchVideos(String query) {
        return videos.stream()
                .filter(video -> video.getTitle().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Video> getVideosExcluding(Video video) {
        return videos.stream()
                .filter(v -> !v.equals(video))
                .collect(Collectors.toList());
    }
}
