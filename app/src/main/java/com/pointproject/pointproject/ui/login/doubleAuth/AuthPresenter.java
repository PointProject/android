package com.pointproject.pointproject.ui.login.doubleAuth;


import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.pointproject.pointproject.R;

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
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;

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
    public void authSms(Context context, String phone) {
        initializePhoneAuthCallback();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60L,
                TimeUnit.SECONDS,
                (Activity) context,
                mCallbacks
        );
    }

    @Override
    public void checkCode(String userCode, AuthMethod authMethod){
        switch (authMethod) {
            case TELEGRAM:
                if (code == Integer.valueOf(userCode)) {
                    authView.next();
                } else {
                    authView.showError(R.string.wrong_auth_code);
                }
                break;
            case PHONE:
                verifyPhoneNumberWithCode(mVerificationId, String.valueOf(userCode));
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

    private void initializePhoneAuthCallback(){
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
//     user action.
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);


                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    authView.showError(R.string.invalid_phone_number);
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Log.d(TAG, "Quota exceeded");
                    authView.showError(R.string.sms_quota_exceeded);
                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                mVerificationId = verificationId;
                authView.showCodeField();
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        authView.next();
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            authView.showError(R.string.wrong_auth_code);
                        }
                    }
                });
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        signInWithPhoneAuthCredential(credential);
    }
}