package com.example.myapplication.API;

import com.example.myapplication.Entities.Comment;
import com.example.myapplication.Entities.Result;
import com.example.myapplication.Entities.UpdateComment;
import com.example.myapplication.Entities.User;
import com.example.myapplication.Entities.UserCredentials;
import com.example.myapplication.Entities.Video;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface WebServiceAPI {

    // Comment-related API calls
    @GET("comments/videos/{id}")
    Call<List<Comment>> getComments(@Path("id") String videoId);

    @POST("comments/videos/{id}")
    Call<Comment> createComment(@Path("id") String videoId, @Body Comment comment);

    @PUT("videos/{id}/comments/{cid}")
    Call<Comment> updateComment(@Header("Authorization") String token, @Path("cid") String commentId, @Body UpdateComment comment);

    @DELETE("videos/{id}/comments/{cid}")
    Call<ResponseBody> deleteComment(@Path("id") String videoId, @Path("cid") String commentId);

    @DELETE("videos/{id}/comments")
    Call<ResponseBody> deleteComments(@Header("Authorization") String token, @Path("id") String videoId);

    // User-related API calls
    @POST("users/")
    Call<User> createUser(@Body User user);

    @POST("tokens")
    Call<ResponseBody> login(@Body UserCredentials credentials);

    @GET("users/{username}")
    Call<User> getUser(@Header("Authorization") String token, @Path("username") String username);

    @GET("users/{username}/username")
    Call<User> getUserByUsername(@Path("username") String username);

    @GET("users/id/{id}")
    Call<User> getUserById(@Path("id") String id);

    @GET("users/id/{id}/password")
    Call<User> getUserByIdWithPassword(@Path("id") String id);
    @FormUrlEncoded
    @PATCH("users/{id}")
    Call<User> updateUser(@Path("id") String userId, @Field("displayName") String displayName);

    @HTTP(method = "DELETE", path = "users/{id}", hasBody = true)
    Call<ResponseBody> deleteUser(
            @Path("id") String userId,
            @Body RequestBody body
    );
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
            @Part MultipartBody.Part video,
            @Part("photo") RequestBody photo
    );


    @PATCH("users/{id}/videos/{pid}")
    @FormUrlEncoded
    Call<Result> updateUserVideo(
            @Path("id") String userId,
            @Path("pid") String videoId,
            @Field("title") String title
    );


    @DELETE("users/{id}/videos/{pid}")
    Call<List<Video>> deleteUserVideo(
            @Path("id") String userId,
            @Path("pid") String videoId
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
