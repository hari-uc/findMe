package com.example.myapplication.Interface;

//import com.agrotech.karyfresh.Model.ProductResponseModel_spinner;

import com.example.myapplication.Model.DirectionModel.DirectionResponseModel;
import com.example.myapplication.Model.UserRequest;
import com.example.myapplication.Model.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface WebServiceInterface {


    String weburl = "http://192.168.1.6:5050/";



    @POST("register")
    Call<UserResponse>saveuser(@Body UserRequest userRequest);



    @GET("data")
    Call<String> getSlotData(
            @Query("email") String email,
            @Query("password") String password
    );


    @GET
    Call<DirectionResponseModel> getDirection(@Url String url);





}


