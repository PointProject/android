package com.pointproject.pointproject.util;

import android.text.TextUtils;

import com.pointproject.pointproject.LoginBaseView;
import com.pointproject.pointproject.R;


public class CredentialUtils {
    public static boolean checkCredentials(String login, String password, LoginBaseView view) {
        view.resetErrors();

        boolean isGood = true;

        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            view.showPasswordError(R.string.error_invalid_password);
            isGood =  false;
        }
        if (TextUtils.isEmpty(login)) {
            view.showEmptyLoginError();
            isGood = false;
        }
        if (!isLoginValid(login)) {
            view.showLoginError(R.string.error_invalid_login);
            isGood = false;
        }

        return isGood;
    }

    public static boolean checkCredentials(String login, String password, String phone, LoginBaseView view){
        boolean isGood = checkCredentials(login, password, view);

        if(TextUtils.isEmpty(phone) || !isPhoneValid(phone)){
            view.showPhoneError(R.string.msg_invalid_phone);
            isGood = false;
        }
        return isGood;
    }

    private static boolean isPhoneValid(String phone) {
//        TODO Replace with further logic with check phone server implementation
        return phone.length() >= 10;
    }

    private static boolean isLoginValid(String email) {
        //TODO: Replace this with your own logic
        return email.length()>3;
    }

    private static boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}
