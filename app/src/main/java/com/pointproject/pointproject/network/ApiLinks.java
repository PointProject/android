package com.pointproject.pointproject.network;


import com.pointproject.pointproject.model.Token;
import com.pointproject.pointproject.model.User;
import com.pointproject.pointproject.network.response.TokenResponse;
import com.pointproject.pointproject.network.response.UserResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
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
    Observable<UserResponse> updateUser(@Body User user);


}
