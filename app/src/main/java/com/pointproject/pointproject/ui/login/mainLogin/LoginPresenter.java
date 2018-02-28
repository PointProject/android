package com.pointproject.pointproject.ui.login.mainLogin;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.pointproject.pointproject.R;
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
import static com.pointproject.pointproject.util.CredentialUtils.checkCredentials;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View loginView;

    @Inject
    ApiClient apiClient;


    @Inject
    LoginPresenter(){}

    @Override
    public void takeView(LoginContract.View view) {
        loginView = view;
    }

    @Override
    public void dropView() {
        loginView = null;
    }

    @Override
    public void checkAccount(String login, String password, Context context) {
        if(!checkCredentials(login, password, loginView))
            return;

        User userN = new User();
        userN.setLogin(login);
        userN.setPassword(password);


        login(userN, context);
    }

    private void login(User userN, Context context) {
        loginView.showProgressBar();

        apiClient.login(userN, new UserTokenCallback() {
            @Override
            public void onSuccess(Token token) {
                SharedPreferences prefs = context.getSharedPreferences(NAME_SHARED_PREFERENCES,
                        Context.MODE_PRIVATE);
                prefs.edit().putString(KEY_TOKEN, token.getToken()).apply();

                loginView.loginIn(userN);
            }

            @Override
            public void onError(NetworkError error) {
                if(error.getAppErrorMessage().equals(NETWORK_ERROR_MESSAGE)){
                    loginView.hideProgressBar();
                    loginView.showNoInternetError();
                    return;
                }

                loginView.hideProgressBar();
                loginView.showLoginError(R.string.error_invalid_login);
            }
        });
    }
}
