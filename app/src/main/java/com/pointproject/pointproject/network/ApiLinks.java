package com.pointproject.pointproject.network;


import com.pointproject.pointproject.model.User;
import com.pointproject.pointproject.network.Response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiLinks {

    @Headers("Cache-Control: no-cache")
    @POST("/user/login")
    Call<UserResponse> login(@Body User user);


}
