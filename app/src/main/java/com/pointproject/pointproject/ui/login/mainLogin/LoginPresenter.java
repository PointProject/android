package com.pointproject.pointproject.ui.login.mainLogin;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.pointproject.pointproject.model.Token;
import com.pointproject.pointproject.model.User;
import com.pointproject.pointproject.network.ApiClient;
import com.pointproject.pointproject.network.callback.UserTokenCallback;
import com.pointproject.pointproject.network.response.NetworkError;
import com.pointproject.pointproject.ui.login.LoginActivity;

import javax.inject.Inject;

import static com.pointproject.pointproject.data.Constants.KEY_TOKEN;
import static com.pointproject.pointproject.data.Constants.KEY_USER;
import static com.pointproject.pointproject.data.Constants.NAME_SHARED_PREFERENCES;
import static com.pointproject.pointproject.network.response.NetworkError.NETWORK_ERROR_MESSAGE;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View loginView;

    @Inject
    ApiClient apiClient;

    private LoginActivity activity;

    @Inject
    LoginPresenter(LoginActivity activity){
        this.activity = activity;
    }

    @Override
    public void takeView(LoginContract.View view) {
        loginView = view;
    }

    @Override
    public void dropView() {
        loginView = null;
    }

    @Override
    public void checkAccount(String login, String password) {
        if(!checkCredentials(login, password))
            return;

        User userN = new User();
        userN.setLogin(login);
        userN.setPassword(password);


        login(userN);
    }

//    @Override
//    public void register(String login, String password){
//        if(!checkCredentials(login, password))
//            return;
//
//        loginView.showProgressBar();
//
//        User userN = new User();
//        userN.setLogin(login);
//        userN.setPassword(password);
//
//        apiClient.register(userN, new UserUserCallback() {
//            @Override
//            public void onSuccess(User user) {
//                login(user);
//            }
//
//            @Override
//            public void onError(NetworkError error) {
//                if(error.getAppErrorMessage().equals(NETWORK_ERROR_MESSAGE)){
//                    loginView.hideProgressBar();
//                    loginView.showNoInternetError();
//                }
//            }
//        });
//    }

    private boolean checkCredentials(String login, String password) {
        loginView.resetErrors();

        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            loginView.showPasswordError();
            return false;
        }else if (TextUtils.isEmpty(login)) {
            loginView.showEmptyLoginError();
            return false;
        } else if (!isEmailValid(login)) {
            loginView.showInvalidLoginError();
            return false;
        }

        return true;
    }

    private void login(User userN) {
        loginView.showProgressBar();

        apiClient.login(userN, new UserTokenCallback() {
            @Override
            public void onSuccess(Token token) {
                SharedPreferences prefs = activity.getSharedPreferences(NAME_SHARED_PREFERENCES,
                        Context.MODE_PRIVATE);
                prefs.edit().putString(KEY_TOKEN, token.getToken()).apply();

                loginView.loginIn();
            }

            @Override
            public void onError(NetworkError error) {
                if(error.getAppErrorMessage().equals(NETWORK_ERROR_MESSAGE)){
                    loginView.hideProgressBar();
                    loginView.showNoInternetError();
                    return;
                }

                loginView.hideProgressBar();
                loginView.showInvalidLoginError();
            }
        });
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.length()>3;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}
