package com.pointproject.pointproject.ui.login.doubleAuth;


import com.pointproject.pointproject.BasePresenter;
import com.pointproject.pointproject.BaseView;

public interface AuthContract {
    interface Presenter extends BasePresenter<View> {
        void authTelegram(String credentials);

        void authSms();
    }

    interface View extends BaseView<Presenter> {
        void showEnterCode(int code);

        void wrongCode();

        void next();
    }
}
