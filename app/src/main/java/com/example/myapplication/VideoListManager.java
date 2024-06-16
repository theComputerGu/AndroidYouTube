package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
        videos.add(new Video("boat", "Author1", "4/09/2004", getBitmapFromUri(R.drawable.boat), getFilePathFromRawResource(R.raw.boat, "boat.mp4")));
        videos.add(new Video("buildings", "Author2", "3/04/2022", getBitmapFromUri(R.drawable.buildings), getFilePathFromRawResource(R.raw.buildings, "buildings.mp4")));
        videos.add(new Video("ferris_wheel", "Author3", "3/03/2021", getBitmapFromUri(R.drawable.ferris_wheel), getFilePathFromRawResource(R.raw.ferris_wheel, "ferris_wheel.mp4")));
        videos.add(new Video("girl_flowers", "Author4", "5/08/2001", getBitmapFromUri(R.drawable.girl_flowers), getFilePathFromRawResource(R.raw.girl_flowers, "girl_flowers.mp4")));
        videos.add(new Video("hilton", "Author5", "6/07/2003", getBitmapFromUri(R.drawable.hilton), getFilePathFromRawResource(R.raw.hilton, "hilton.mp4")));
        videos.add(new Video("hard_rock_cafe", "Author6", "5/04/2018", getBitmapFromUri(R.drawable.hard_rock_cafe), getFilePathFromRawResource(R.raw.hard_rock_cafe, "hard_rock_cafe.mp4")));
        videos.add(new Video("keyboard", "Author7", "5/05/2001", getBitmapFromUri(R.drawable.keyboard), getFilePathFromRawResource(R.raw.keyboard, "keyboard.mp4")));
        videos.add(new Video("kite", "Author8", "6/07/2020", getBitmapFromUri(R.drawable.kite), getFilePathFromRawResource(R.raw.kite, "kite.mp4")));
        videos.add(new Video("men_sunset", "Author9", "3/01/2020", getBitmapFromUri(R.drawable.men_sunset), getFilePathFromRawResource(R.raw.men_sunset, "men_sunset.mp4")));
        videos.add(new Video("road", "Author10", "1/01/2011", getBitmapFromUri(R.drawable.road), getFilePathFromRawResource(R.raw.road, "road.mp4")));
        videos.add(new Video("women_reading", "Author11", "3/01/2021", getBitmapFromUri(R.drawable.women_reading), getFilePathFromRawResource(R.raw.women_reading, "women_reading.mp4")));
    }
    private String getFilePathFromRawResource(int rawResourceId, String fileName) {
        File file = copyRawResourceToFile(rawResourceId, fileName);
        return file != null ? file.getAbsolutePath() : null;
    }
    private File copyRawResourceToFile(int rawResourceId, String fileName) {
        try {
            InputStream inputStream = context.getResources().openRawResource(rawResourceId);
            File file = new File(context.getFilesDir(), fileName);
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private Bitmap getBitmapFromUri(int drawableId) {
        return BitmapFactory.decodeResource(context.getResources(), drawableId);
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

    public List<Video> getVideosByUser(String username) {
        return videos.stream()
                .filter(video -> video.getUsername().equals(username))
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
    public Video getVideosByTag(String username, String title) {
        for (Video video : videos) {
            boolean matchesUsername = username == null || username.isEmpty() || video.getUsername().equalsIgnoreCase(username);
            boolean matchesTitle = title == null || title.isEmpty() || video.getTitle().equalsIgnoreCase(title);

            if (matchesUsername && matchesTitle) {
                return video;
            }
        }
        return null;
    }
}
