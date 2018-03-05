package com.pointproject.pointproject.ui.login.doubleAuth;


import android.content.Context;

import com.pointproject.pointproject.BasePresenter;
import com.pointproject.pointproject.BaseView;
import com.pointproject.pointproject.model.User;
import com.pointproject.pointproject.ui.login.AuthReason;

public interface AuthContract {
    interface Presenter extends BasePresenter<View> {

        void takeView(View view, AuthReason reason, User user);

        void authSms(Context context, String phone);

        void checkCode(String userCode);

        void resendVerificationCode(Context context);
    }

    interface View extends BaseView<Presenter> {
        void showMessage(int stringResId);

        void showMapsActivity();

        void showPhoneField();

        void showCodeField();

        void showResendButton();

        void showEmptyPhoneFieldError();

        void showEmptyCodeError();

        void showWrongCodeError();

        void showInvalidPhoneError();

//        TODO remove
        Context getContext();
    }
}
