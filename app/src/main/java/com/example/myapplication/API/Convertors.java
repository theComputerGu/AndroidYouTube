package com.example.myapplication.API;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Convertors {
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
    public static byte[] fromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    @TypeConverter
    public static Bitmap toBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
}