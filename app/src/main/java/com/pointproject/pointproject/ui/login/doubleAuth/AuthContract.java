package com.pointproject.pointproject.ui.login.doubleAuth;


import android.content.Context;

import com.pointproject.pointproject.BasePresenter;
import com.pointproject.pointproject.BaseView;
import com.pointproject.pointproject.model.User;
import com.pointproject.pointproject.ui.login.AuthReason;

public interface AuthContract {
    interface Presenter extends BasePresenter<View> {

        void takeView(View view, AuthReason reason);

        void authSms(Context context, String phone);

        void checkCode(String userCode);
    }

    interface View extends BaseView<Presenter> {
        void showCodeField();

        void hideCodeField();

        void showError(String errorText);

        void showError(int stringResId);

        void showMapsActivity();

        void showPhoneField();

        void hidePhoneField();

        User getRegisteredUser();

        void showEmptyPhoneFieldError();

        void showEmptyCodeError();

        void showWrongCodeError();

        void showInvalidPhoneError();

//        TODO remove
        Context getContext();
    }
}
