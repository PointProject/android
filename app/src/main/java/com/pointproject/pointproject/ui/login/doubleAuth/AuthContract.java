package com.pointproject.pointproject.ui.login.doubleAuth;


import android.content.Context;

import com.pointproject.pointproject.BasePresenter;
import com.pointproject.pointproject.BaseView;

public interface AuthContract {
    interface Presenter extends BasePresenter<View> {
        void authTelegram(String credentials);

        void authSms(Context context, String phone);

        void checkCode(String userCode, AuthMethod authMethod);
    }

    interface View extends BaseView<Presenter> {
        void showCodeField();

        void hideCodeField();

        void showError(String errorText);

        void showError(int stringResId);

        void next();

        void showPhoneField();

        void hidePhoneField();
    }
}
