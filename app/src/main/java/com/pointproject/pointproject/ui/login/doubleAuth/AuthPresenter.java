package com.pointproject.pointproject.ui.login.doubleAuth;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.model.Token;
import com.pointproject.pointproject.model.User;
import com.pointproject.pointproject.network.ApiClient;
import com.pointproject.pointproject.network.callback.UserCallback;
import com.pointproject.pointproject.network.callback.UserTokenCallback;
import com.pointproject.pointproject.network.response.NetworkError;
import com.pointproject.pointproject.ui.login.AuthReason;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import static com.pointproject.pointproject.data.Constants.KEY_TOKEN;
import static com.pointproject.pointproject.data.Constants.NAME_SHARED_PREFERENCES;
import static com.pointproject.pointproject.network.response.NetworkError.NETWORK_ERROR_MESSAGE;

public class AuthPresenter implements AuthContract.Presenter{

    private static String TAG = AuthPresenter.class.getSimpleName();

    @Inject
    ApiClient apiClient;

    private AuthContract.View authView;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private AuthReason authReason;

    @Inject
    AuthPresenter(){}

    @Override
    public void takeView(AuthContract.View view) {
        authView = view;
    }

    @Override
    public void takeView(AuthContract.View view, AuthReason reason) {
        authView = view;
        authReason = reason;
    }

    @Override
    public void dropView() {
        authView = null;
    }

    @Override
    public void authSms(Context context, String phone) {
        if(phone.isEmpty()){
            authView.showEmptyPhoneFieldError();
            return;
        }

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
    public void checkCode(String userCode){
        if(userCode.isEmpty()) {
            authView.showEmptyCodeError();
            return;
        }
        if(userCode.length() < 10){
            authView.showInvalidPhoneError();
            return;
        }

        verifyPhoneNumberWithCode(mVerificationId, String.valueOf(userCode));
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
                    authView.showInvalidPhoneError();
                    authView.showPhoneField();
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
                        codeIsRight();
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            authView.showWrongCodeError();
                        }
                    }
                });
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        signInWithPhoneAuthCredential(credential);
    }

    private void codeIsRight(){
        switch (authReason){
            case LOGIN:
                authView.showMapsActivity();
                break;
            case REGISTRATION:
                apiClient.register(authView.getRegisteredUser(), new UserCallback() {
                    @Override
                    public void onSuccess(User user) {
//                        TODO: remove
                        login(user, authView.getContext());
                        authView.showMapsActivity();
                    }

                    @Override
                    public void onError(NetworkError error) {
                        if(error.getAppErrorMessage().equals(NETWORK_ERROR_MESSAGE)){
                            authView.showError(R.string.msg_no_internet);
                        }
                    }
                });
        }
    }

//    TODO: remove this unholy thing when server implements token return on registration
    private void login(User userN, Context context) {

        apiClient.login(userN, new UserTokenCallback() {
            @Override
            public void onSuccess(Token token) {
                SharedPreferences prefs = context.getSharedPreferences(NAME_SHARED_PREFERENCES,
                        Context.MODE_PRIVATE);
                prefs.edit().putString(KEY_TOKEN, token.getToken()).apply();
            }

            @Override
            public void onError(NetworkError error) {
            }
        });
    }
}