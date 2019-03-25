package com.example.stahi.findmyordercourier.API;

import com.example.stahi.findmyordercourier.Models.FinalizeCommand;
import com.example.stahi.findmyordercourier.Models.GetUserRelationshipResponse;
import com.example.stahi.findmyordercourier.Models.GetUserResponse;
import com.example.stahi.findmyordercourier.Models.MyLocation;
import com.example.stahi.findmyordercourier.Models.ReportProblemForm;
import com.example.stahi.findmyordercourier.Models.User;
import com.example.stahi.findmyordercourier.Models.UserRelationship;

import java.util.Date;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService{

    @GET("User/GetAll")
    Call<GetUserResponse> GetAll();

    @GET("User/GetById/{id}")
    Call<User> GetById(@Path("id") long id);

    @GET("User/LoginCourier/{email}/{password}")
    Call<User> LoginCourier(@Path("email") String email, @Path("password") String password);

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


    @POST("ReportProblem/Create")
    Call<String> Create(@Body ReportProblemForm reportProblemForm);


    @GET("UserRelationship/GetAllClientsBtCourierId/{id}")
    Call<GetUserRelationshipResponse> GetAllClientsBtCourierId(@Path("id") int id);

    @PUT("UserRelationship/FinalizeDelivery")
    Call<UserRelationship> FinalizeDelivery(@Body FinalizeCommand myObj);


    @POST("ChatMessages/SaveMessageToDatabase/{senderId}/{senderMessage}/{receiverId}")
    Call<String> SaveMessageToDatabase(@Path("senderId") int senderId, @Path("senderMessage") String senderMessage, @Path("receiverId") int receiverId);


    @POST("CourierLocation/SaveLocationToDatabase")
    Call<MyLocation> SaveLocationToDatabase(@Body MyLocation myLocation);




}