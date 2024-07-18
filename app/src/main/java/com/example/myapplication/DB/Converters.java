package com.example.myapplication.DB;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Converters {
    static Gson gson = new Gson();
    @TypeConverter
    public static String fromList(List<String> list) {
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<String> stringToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<String>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String fromDateToJson(Date date) {
        if (date == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(date);
    }

    @TypeConverter
    public static Date fromJsonToDate(String json) {
        if (json == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(json, Date.class);
    }

    @TypeConverter
    public static Bitmap base64ToBitmap(String base64) {
        if (base64 == null) {
            return null;
        }
        byte[] decodedBytes = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    @TypeConverter
    public static String bitmapToBase64(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String base64String = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return "data:image/png;base64," + base64String;
    }
}