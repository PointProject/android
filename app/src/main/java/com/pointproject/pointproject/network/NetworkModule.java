package com.pointproject.pointproject.network;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pointproject.pointproject.PointProjectApplication;
import com.pointproject.pointproject.data.Constants;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.pointproject.pointproject.data.Constants.KEY_TOKEN;
import static com.pointproject.pointproject.data.Constants.NAME_SHARED_PREFERENCES;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    Retrofit provideRetrofit(PointProjectApplication application){
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder();
        httpClient.readTimeout(30, TimeUnit.SECONDS);
        httpClient.connectTimeout(30, TimeUnit.SECONDS);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            SharedPreferences prefs = application.getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE);
            String token = prefs.getString(KEY_TOKEN, "");


            Request.Builder requestBuilder = original.newBuilder()
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("authorization", token)

                    .method(original.method(), original.body());

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        httpClient.addInterceptor(logging);

        Gson gson = new GsonBuilder()
                 .setLenient()
                .create();

        return new Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl(Constants.SERVER_HOST)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    ApiLinks provideLinks(Retrofit retrofit){
        return retrofit.create(ApiLinks.class);
    }

    @Provides
    @Singleton
    ApiClient provideApiClient(ApiLinks links){
        return new ApiClient(links);
    }
}
