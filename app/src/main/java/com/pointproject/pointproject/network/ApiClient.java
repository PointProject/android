package com.pointproject.pointproject.network;

import com.pointproject.pointproject.model.Token;
import com.pointproject.pointproject.model.User;
import com.pointproject.pointproject.model.Zone;
import com.pointproject.pointproject.network.callback.GetZoneCallback;
import com.pointproject.pointproject.network.callback.UserCallback;
import com.pointproject.pointproject.network.callback.UserTokenCallback;
import com.pointproject.pointproject.network.response.NetworkError;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static java.util.concurrent.TimeUnit.SECONDS;


public class ApiClient {


    final String LOG_TAG = getClass().getSimpleName();

    private final ApiLinks requestsLinks;


    public ApiClient(ApiLinks links) {
        requestsLinks = links;
    }

    public void login(final User user, final UserTokenCallback callback) {
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

    public void register(final User user, final UserTokenCallback callback){
        requestsLinks.register(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Token>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Token token) {
                        callback.onSuccess(token);
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

/** Get Zones, on Error repeat request 3 times,
 * if all 3 times failed, call onError
 *
 * */
    public void getZone(GetZoneCallback callback){

        final int ATTEMPTS = 3;

        requestsLinks.getZones()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(errors->
                        errors
                                .zipWith(Observable.range(1, ATTEMPTS), (n, i) ->
                                        i < ATTEMPTS ?
                                                Observable.timer((long)Math.pow(5,i), SECONDS) :
                                                Observable.error(n))
                                .flatMap(x->x))
                .subscribe(new Observer<List<Zone>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Zone> zones) {
                        callback.onSuccess(zones);
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

    public void secureLogin(RequestBody login, UserCallback callback){
        requestsLinks.loginUser(login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(User user) {
                        callback.onSuccess(user);
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
