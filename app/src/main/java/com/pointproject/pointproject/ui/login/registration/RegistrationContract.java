package com.pointproject.pointproject.ui.login.registration;


import android.content.Context;

import com.pointproject.pointproject.BasePresenter;
import com.pointproject.pointproject.LoginBaseView;
import com.pointproject.pointproject.model.User;

public interface RegistrationContract {
    interface Presenter extends BasePresenter<View> {
        void register(String login, String password, String phone, Context context);
    }

    interface View extends LoginBaseView<Presenter> {

        void showNoInternetError(int resId);

        void showProgressBar();

        void hideProgressBar();

        void startAuth(User user);
    }
}
