package com.pointproject.pointproject.ui.login;


import android.text.TextUtils;

import javax.inject.Inject;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View loginView;

    @Inject
    LoginPresenter(){

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
        loginView.resetErrors();

        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            loginView.showPasswordError();
        }else if (TextUtils.isEmpty(login)) {
            loginView.showEmptyLoginError();
        } else if (!isEmailValid(login)) {
            loginView.showInvalidLoginError();
        } else{
            loginView.loginIn();
        }
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
