package com.pointproject.pointproject.network;


import com.pointproject.pointproject.model.Token;
import com.pointproject.pointproject.model.User;
import com.pointproject.pointproject.model.Zone;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiLinks {

    @Headers("Cache-Control: no-cache")
    @POST("/user/login")
    Observable<Token> login(@Body User user);

    @Headers("Cache-Control: no-cache")
    @POST("/user/register")
    Observable<User> register(@Body User user);

    @Headers("Cache-Control: no-cache")
    @POST("/secure/user/update")
    Observable<User> updateUser(@Body User user);

    @GET("/secure/zone/list")
    Observable<List<Zone>> getZones();

//    retrofit adds double quotes to string, so that's why i'm using request body
    @Headers({"Cache-Control: no-cache",
            "Content-Type: text/plain"})
    @POST("/secure/user/login")
    Observable<User> loginUser(@Body RequestBody login);

}
