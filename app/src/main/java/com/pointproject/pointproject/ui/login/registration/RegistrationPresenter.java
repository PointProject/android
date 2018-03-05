package com.pointproject.pointproject.ui.login.registration;

import android.content.Context;

import com.pointproject.pointproject.model.User;
import com.pointproject.pointproject.network.ApiClient;

import javax.inject.Inject;

import static com.pointproject.pointproject.util.CredentialUtils.checkCredentials;

public class RegistrationPresenter implements RegistrationContract.Presenter{

    private RegistrationContract.View regView;

    @Inject
    ApiClient apiClient;

    @Inject
    RegistrationPresenter(){}

    @Override
    public void takeView(RegistrationContract.View view) {
        regView = view;
    }

    @Override
    public void dropView() {
        regView = null;
    }

    @Override
    public void register(String login, String password, String phone, Context context) {
//        TODO check if login and phone are available after appropriate methods appear on the server
        if(!checkCredentials(login, password, phone, regView))
            return;

        User userN = new User();
        userN.setLogin(login);
        userN.setPassword(password);
        userN.setPhone(phone);

        regView.startAuth(userN);
    }
}
