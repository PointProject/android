package com.pointproject.pointproject.network;

import com.pointproject.pointproject.model.Token;
import com.pointproject.pointproject.model.User;
import com.pointproject.pointproject.network.callback.RegisterCallback;
import com.pointproject.pointproject.network.callback.UserCallback;
import com.pointproject.pointproject.network.response.BaseResponse;
import com.pointproject.pointproject.network.response.NetworkError;
import com.pointproject.pointproject.network.response.TokenResponse;
import com.pointproject.pointproject.network.response.UserResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class ApiClient {


    final String LOG_TAG = getClass().getSimpleName();

    private final ApiLinks requestsLinks;


    public ApiClient(ApiLinks links) {
        requestsLinks = links;
    }

    public void login(final User user, final UserCallback callback) {
        requestsLinks.login(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Token>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Token response) {
                        callback.onSuccess(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void register(final User user, final RegisterCallback callback){
        requestsLinks.register(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(User userResponse) {
                        callback.onSuccess(userResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
