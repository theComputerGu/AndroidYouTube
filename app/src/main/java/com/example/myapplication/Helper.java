package com.example.myapplication;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Entities.CustomMediaController;
import com.example.myapplication.Entities.User;

public class Helper extends Application {
    public static Context context;
    public static String token;
    public static User signedInUser;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    public static String getToken() {
        return token;
    }
    public static void setToken(String token) {
        Helper.token = token;
    }
    public static User getSignedInUser() {
        return signedInUser;
    }
    public static void setSignedInUser(User signedInUser) {
        Helper.signedInUser = signedInUser;
    }
    public static boolean isSignedIn() {
        return signedInUser != null;
    }
    public static void loadPhotoIntoImageView(Context context, ImageView imageView, String photo) {
        if (photo.startsWith("data:image")) {
            // Base64-encoded image
            String base64String = photo.split(",")[1];
            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedByte);
        } else {
            // Image URL
            String imageUrl = Helper.context.getString(R.string.baseServerURL) + photo;
            Glide.with(context).load(imageUrl).into(imageView);
        }
    }

    public static void loadVideoIntoVideoView(Context context, VideoView videoView, String videoPath) {
        String videoUrl;
        String baseServerURL = context.getString(R.string.baseServerURL);
        videoUrl = baseServerURL + videoPath;


        videoView.setVideoURI(Uri.parse(videoUrl));
        videoView.setOnPreparedListener(mp -> {
            videoView.start();
        });

        // Optional: Set a media controller for video controls
        CustomMediaController mediaController = new CustomMediaController(context, videoView);
        videoView.setMediaController(mediaController);
    }
}
