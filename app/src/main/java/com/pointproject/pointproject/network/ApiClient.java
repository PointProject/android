package com.pointproject.pointproject.network;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.pointproject.pointproject.data.Constants;
import com.pointproject.pointproject.model.User;
import com.pointproject.pointproject.network.Event.ErrorEvent;
import com.pointproject.pointproject.network.Event.UserEvent;
import com.pointproject.pointproject.network.Response.UserResponse;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



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



    private void successResponse(ApiName apiName, Response response) {
        switch (apiName) {
            case USER_LOGIN:
                getUserAccessToken((UserResponse) response.body());
                break;
            default:
                break;
        }
    }

    private void getUserAccessToken(UserResponse body) {
        UserEvent event = new UserEvent(body);
        EventBus.getDefault().postSticky(event);
    }


    private void onFail(ApiName apiName) {
        switch (apiName) {
            case USER_LOGIN:
                break;
            default:
                break;
        }
    }



    private void errorResponse(ApiName apiName, Response response) {
/*        if (apiName == ApiName.SEARCH_PRODUCTS) {
            SearchProductsError error = new SearchProductsError();
            error.code = response.code();
            EventBus.getDefault().postSticky(error);
        } else {
            JsonParser parser = new JsonParser();
            JsonElement mJson = null;
            try {
                mJson = parser.parse(response.errorBody().string());
                Gson gson = new Gson();
                ErrorEvent errorResponse = gson.fromJson(mJson, ErrorEvent.class);
                EventBus.getDefault().postSticky(errorResponse);
            } catch (IOException ex) {
                ex.printStackTrace();}


        }*/
    }

    private class RequestCalls extends AsyncTask<Call, Void, Void> {

        private ApiName apiName;

        public void setApiName(ApiName apiName) {
            this.apiName = apiName;
        }

        @Override
        protected Void doInBackground(Call... baseRequests) {

            baseRequests[0].enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.code() == 200) {
                        successResponse(apiName, response);
                        Log.d(LOG_TAG, String.valueOf(apiName));
                    } else {
                        errorResponse(apiName, response);
                        Log.d(LOG_TAG, String.valueOf(apiName) + " = error");
                    }
                    //doRetry();
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    ErrorEvent errorEvent = new ErrorEvent();
                    errorEvent.setMessage(t.getMessage());
                    EventBus.getDefault().postSticky(errorEvent);
                    onFail(apiName);
                }
            });
            return null;
        }
    }

}
