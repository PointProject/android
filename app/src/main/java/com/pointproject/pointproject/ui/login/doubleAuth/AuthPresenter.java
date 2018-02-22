package com.pointproject.pointproject.ui.login.doubleAuth;


import android.util.Log;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;

public class AuthPresenter implements AuthContract.Presenter{

    private static String TAG = AuthPresenter.class.getSimpleName();

    private int code;

    private AuthContract.View authView;

    @Inject
    AuthPresenter(){}

    @Override
    public void takeView(AuthContract.View view) {
        authView = view;
    }

    @Override
    public void dropView() {
        authView = null;
    }

    @Override
    public void authTelegram(String credentials) {
        code = generateCode(credentials.hashCode());
        authView.showCodeField();
    }

    @Override
    public void authSms() {

    }

    @Override
    public void checkCode(int userCode){
        if(code == userCode){
            authView.next();
        } else{
            authView.wrongCode();
        }
    }

    private int generateCode(int intCredentials){
        String credentials = String.valueOf(intCredentials);
        TimeBasedOneTimePasswordGenerator totp = null;
        SecretKey secretKey = null;

        try {
            totp = new TimeBasedOneTimePasswordGenerator(5, TimeUnit.MINUTES);

            byte[] salt = {
                    (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
                    (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99
            };

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(credentials.toCharArray(), salt, 20, 512);
            SecretKey tmp = factory.generateSecret(spec);
            secretKey = new SecretKeySpec(tmp.getEncoded(), totp.getAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.toString());
        } catch (InvalidKeySpecException e) {
            Log.e(TAG, e.toString());
        }

        int code = 0;
        try {
            assert totp != null;
            code = totp.generateOneTimePassword(secretKey, new Date());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Auth telegram one time password: " + code);
        return code;
    }
}
