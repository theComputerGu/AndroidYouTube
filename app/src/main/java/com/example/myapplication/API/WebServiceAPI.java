package com.example.myapplication.API;

import com.example.myapplication.Entities.Comment;
import com.example.myapplication.Entities.UpdateComment;
import com.example.myapplication.Entities.UpdateUser;
import com.example.myapplication.Entities.User;
import com.example.myapplication.Entities.UserCredentials;
import com.example.myapplication.Entities.UserLoginResponse;
import com.example.myapplication.Entities.Video;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface WebServiceAPI {

    // Comment-related API calls
    @GET("videos/{id}/comments")
    Call<List<Comment>> getComments(@Path("id") String videoId);

    @POST("videos/{id}/comments")
    Call<Comment> createComment(@Header("Authorization") Comment comment, @Path("id") String videoId);

    @PUT("videos/{id}/comments/{cid}")
    Call<Comment> updateComment(@Header("Authorization") String token, @Path("cid") String commentId, @Body UpdateComment comment);

    @DELETE("videos/{id}/comments/{cid}")
    Call<ResponseBody> deleteComment(@Path("id") String videoId, @Path("cid") String commentId);

    @DELETE("videos/{id}/comments")
    Call<ResponseBody> deleteComments(@Header("Authorization") String token, @Path("id") String videoId);

    // User-related API calls
    @POST("users")
    Call<ResponseBody> createUser(@Body User user);

    @POST("login")
    Call<UserLoginResponse> login(@Body UserCredentials credentials);

    @GET("users/{username}")
    Call<User> getUser(@Header("Authorization") String token, @Path("username") String username);

    @GET("users/{username}")
    Call<User> getUserByUsername(@Path("username") String username);

    @GET("users/id/{id}")
    Call<User> getUserById(@Path("id") String id);

    @GET("users/id/{id}/password")
    Call<User> getUserByIdWithPassword(@Path("id") String id);

    @PUT("users/{id}")
    Call<User> updateUser(@Header("Authorization") String token, @Path("id") String id, @Body UpdateUser update);

    @DELETE("users/{id}")
    Call<ResponseBody> deleteUser(@Header("Authorization") String token, @Path("id") String id);

    @GET("videos/{pid}")
    Call<Video> getUserVideo(@Header("Authorization") String token, @Path("pid") String videoId);

    @GET("users/{id}/videos")
    Call<List<Video>> getUserVideos(@Header("Authorization") String token, @Path("id") String userId);

    @Multipart
    @POST("users/{id}/videos")
    Call<Video> createUserVideo(
            @Path("id") String userId,
            @Part("title") RequestBody title,
            @Part("author") RequestBody author,
            @Part MultipartBody.Part videoFile,
            @Part("photo") RequestBody photo
    );

    @PUT("videos/{id}/{pid}")
    Call<Video> updateUserVideo(
            @Header("Authorization") String token,
            @Path("pid") String videoId,
            @Path("id") String userId,
            @Path("title") String title
    );

    @DELETE("users/{id}/videos/{pid}")
    Call<ResponseBody> deleteUserVideo(
            @Path("id") String userId,
            @Path("pid") String videoId,
            @Header("Authorization") String token
    );

    @POST("videos")
    Call<ResponseBody> createVideo(@Body Video video);

    @GET("videos")
    Call<List<Video>> getVideos();

    @GET("videos/{id}")
    Call<Video> getVideoById(@Path("id") String videoId);

    @PUT("videos/{id}")
    Call<Video> updateVideo(@Path("id") String videoId, @Body Video video);

    @DELETE("videos/{id}")
    Call<ResponseBody> deleteVideo(@Path("id") String videoId);

    @GET("videos/prefix/{prefix}")
    Call<List<Video>> getVideosByPrefix(@Path("prefix") String prefix);

    @GET("users/{id}/videos")
    Call<List<Video>> getUserVideos(@Path("id") String userId);

}
