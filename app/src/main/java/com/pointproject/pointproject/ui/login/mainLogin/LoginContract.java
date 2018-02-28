package com.pointproject.pointproject.ui.login.mainLogin;

import android.content.Context;

import com.pointproject.pointproject.BasePresenter;
import com.pointproject.pointproject.BaseView;
import com.pointproject.pointproject.LoginBaseView;
import com.pointproject.pointproject.model.User;

public interface LoginContract {
    interface Presenter extends BasePresenter<View>{

        void checkAccount(String login, String password, Context context);

//        void register(String login, String password);

    }

    interface View extends LoginBaseView<Presenter> {

        void showNoInternetError();

        void loginIn(User userN);

        void register();

        void showProgressBar();

        void hideProgressBar();
    }
}
