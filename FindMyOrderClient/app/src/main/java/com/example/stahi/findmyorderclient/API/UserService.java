package com.example.stahi.findmyorderclient.API;

import com.example.stahi.findmyorderclient.Models.Contact;
import com.example.stahi.findmyorderclient.Models.DocumentPhotos;
import com.example.stahi.findmyorderclient.Models.GetDocumentPhotosRelationshipResponse;
import com.example.stahi.findmyorderclient.Models.GetUserRelationshipResponse;
import com.example.stahi.findmyorderclient.Models.GetUserResponse;
import com.example.stahi.findmyorderclient.Models.MyLocation;
import com.example.stahi.findmyorderclient.Models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by stahi on 4/15/2018.
 */

public interface UserService {

    @GET("User/GetAll")
    Call<GetUserResponse> GetAll();

    @GET("User/GetById/{id}")
    Call<User> GetById(@Path("id") long id);

    @GET("User/LoginClient/{email}/{password}")
    Call<User> LoginClient(@Path("email") String email, @Path("password") String password);

    @PUT("User/Update/{id}")
    Call<User> Update(@Path("id") long id, @Body User user);

    @PUT("User/ChangePassword/{id}/{password}")
    Call<User> ChangePassword(@Path("id") long id, @Path("password") String newPassword);

    @PUT("User/UpdateFirebaseToken/{uid}/{firebaseToken}/{testString}")
    Call<User> UpdateFirebaseToken(@Path("uid") String uid, @Path("firebaseToken") String firebaseToken, @Path("testString") String testString);

    @POST("User/Create")
    Call<User> Create(@Body User user);

    @GET("User/ResetPass/{email}")
    Call<User> ResetPass(@Path("email") String email);


    @POST("Contact/Create")
    Call<String> Create(@Body Contact contact);


    @GET("UserRelationship/GetAllCouriersByClientId/{id}")
    Call<GetUserRelationshipResponse> GetAllCouriersByClientId(@Path("id") int id);


    @POST("ChatMessages/SaveMessageToDatabase/{senderId}/{senderMessage}/{receiverId}")
    Call<String> SaveMessageToDatabase(@Path("senderId") int senderId, @Path("senderMessage") String senderMessage, @Path("receiverId") int receiverId);


    @POST("CourierLocation/SaveLocationToDatabase")
    Call<MyLocation> SaveLocationToDatabase(@Body MyLocation myLocation);

    @GET("CourierLocation/GetCourierLocationById/{Id}")
    Call<MyLocation> GetCourierLocationById(@Path("Id") int courierId);


    @POST("DocumentPhotos/Create")
    Call<String> Create(@Body DocumentPhotos docPhoto);

    @GET("DocumentPhotos/GetAllPicturesByClientId/{Id}")
    Call<GetDocumentPhotosRelationshipResponse> GetAllPicturesByClientId(@Path("Id") int Id);
}