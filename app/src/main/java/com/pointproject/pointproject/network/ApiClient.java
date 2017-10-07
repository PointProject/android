package com.pointproject.pointproject.network;

import android.content.Context;
import android.provider.SyncStateContract;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pointproject.pointproject.data.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xdewnik on 07.10.2017.
 */

public class ApiClient {


    final String LOG_TAG = getClass().getSimpleName();

    private static ApiClient instance;
    private String url;
    private Retrofit retrofit;
    private ApiLinks requestsLinks;


    public ApiClient(Context context) {
        url = Constants.SERVER_HOST;
        OkHttpClient.Builder httpClient = createClient();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        //.header("Authorization", token)
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        httpClient.addInterceptor(logging);
        Gson gson = new GsonBuilder()
                .create();
        retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        requestsLinks = retrofit.create(ApiLinks.class);
    }

    public static ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context);
        }
        return instance;
    }

    private OkHttpClient.Builder createClient() {
        final OkHttpClient.Builder client = new OkHttpClient().newBuilder();
        client.readTimeout(30, TimeUnit.SECONDS);
        client.connectTimeout(30, TimeUnit.SECONDS);
        return client;
    }



}
