package com.pointproject.pointproject.ui.login.doubleAuth;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.pointproject.pointproject.AbstractFragment;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.data.Constants;
import com.pointproject.pointproject.model.User;
import com.pointproject.pointproject.network.ApiClient;
import com.pointproject.pointproject.network.callback.UserCallback;
import com.pointproject.pointproject.network.response.NetworkError;
import com.pointproject.pointproject.ui.maps.MapsActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static com.pointproject.pointproject.data.Constants.KEY_USER;
import static com.pointproject.pointproject.data.Constants.NAME_SHARED_PREFERENCES;

public class AuthFragment extends AbstractFragment implements
        AuthContract.View,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String EXTRA_CREDENTIALS = "extra_credentials";
    public static final String EXTRA_LOGIN = "extra_login";
    public static final String TAG = "AuthFragment";

    private final static int LAYOUT = R.layout.fragment_auth;
    private final static String KEY_CODE_TEXT_EDIT_SHOWN = "key_text_shown";
    private static final String KEY_PHONE_EDIT_TEXT_SHOWN = "phone_text_shown";
    private static final int RESOLVE_HINT = 1;

    @Inject AuthPresenter presenter;

    @Inject
    ApiClient serverApiClient;

    @BindView(R.id.enter_auth_code_layout)
    LinearLayout authCodeLinearLayout;

    @BindView(R.id.auth_code_text)
    EditText codeText;

    @BindView(R.id.enter_phone_layout)
    LinearLayout authPhoneLinearLayout;

    @BindView(R.id.auth_phone_text)
    EditText phoneText;

    private String credentials, login;

    private User user;

    private Configuration orientationConfig;

    private AuthMethod authMethod;
    private GoogleApiClient googleApiClient;

    @Inject
    public AuthFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        ButterKnife.bind(this,view);

        googleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.CREDENTIALS_API)
                .build();

        orientationConfig = getResources().getConfiguration();

        if (savedInstanceState!=null) {
            if (savedInstanceState.getBoolean(KEY_CODE_TEXT_EDIT_SHOWN))
                showCodeField();
            if(savedInstanceState.getBoolean(KEY_PHONE_EDIT_TEXT_SHOWN))
                showPhoneField();
        }

        Bundle bundle = getArguments();
        assert bundle != null;
        if(!bundle.isEmpty()){
            credentials = bundle.getString(EXTRA_CREDENTIALS);
            login = bundle.getString(EXTRA_LOGIN, "");
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), login);
        serverApiClient.secureLogin(requestBody, new UserCallback() {
            @Override
            public void onSuccess(User serverUser) {
                user = serverUser;
            }

            @Override
            public void onError(NetworkError error) {
                Log.e(TAG, error.getMessage());
                Toast.makeText(getContext(), R.string.msg_no_internet, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        presenter.dropView();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.takeView(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(KEY_CODE_TEXT_EDIT_SHOWN, authCodeLinearLayout.isShown());
        outState.putBoolean(KEY_PHONE_EDIT_TEXT_SHOWN, authPhoneLinearLayout.isShown());
    }

    @OnClick(R.id.auth_telegram_button)
    public void authTelegram(View view){
        authMethod = AuthMethod.TELEGRAM;
        presenter.authTelegram(credentials);

        Intent telegram = new Intent(Intent.ACTION_VIEW , Uri.parse(Constants.URI_TELEGRAM_BOT+credentials.hashCode()));
        startActivity(telegram);
    }

//    TODO replace int phone with string phone
    @OnClick(R.id.auth_sms_button)
    public void authSms(View view){
        authMethod = AuthMethod.PHONE;
        if(user != null){
            int phone = user.getPhone();
            if(phone != 0){
                presenter.authSms(getContext(), String.valueOf(phone));
            } else{
                requestHint();
            }
        }
    }

    @OnClick(R.id.auth_enter_code_button)
    public void enterCode(View view){
        String code = codeText.getText().toString();
        if(!code.isEmpty())
            presenter.checkCode(code, authMethod);
    }

    @OnClick(R.id.auth_enter_phone_button)
    public void enterPhone(View view){
        String phone = phoneText.getText().toString();
        if(!phone.isEmpty() && !(phone.length()<10))
            /**TODO check if phone is already in user, otherwise add to user
             * apiClient.checkPhone(phone, UserCallback{
             *     @Override
             *     public void onSuccess(User user){
             *         if(user != null){
             *             Toast.makeText(getContext(), "This phone is already wired to
             *             another account, please add another number", Toast.LONG).show();
             *         } else{
             *             user.addPhone(phone);
             *             presenter.authSms(getContext(), phone);
             *         }
             *     }
             *     @Override
             *     public void onError(NetworkError error){
             *         if(error.getMessage().equals("Null pointer exception"){
             *             user.addPhone(phone);
             *             presenter.authSms(getContext(), phone);
             *         } else{
             *             Toast.makeText(getContext(), "Error occured, please try later", Toast.SHORT)
             *             .show();
             *         }
             *     }
             * })
            * */
            presenter.authSms(getContext(), phone);
    }

    @Override
    public void showCodeField() {
        if(orientationConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if(authPhoneLinearLayout.isShown()){
                hidePhoneField();
            }
            authCodeLinearLayout.setVisibility(View.VISIBLE);
            authCodeLinearLayout.animate()
                    .translationY(authCodeLinearLayout.getHeight())
                    .alpha(1.0F)
                    .setListener(null);
        }
    }

    @Override
    public void hideCodeField(){
        if(orientationConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            authCodeLinearLayout.animate()
                    .translationY(0)
                    .alpha(0.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            authCodeLinearLayout.setVisibility(View.INVISIBLE);
                        }
                    });
        }
    }

    @Override
    public void showError(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(int stringResId) {
        Toast.makeText(getContext(), stringResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void next() {
        SharedPreferences prefs = getContext().getSharedPreferences(NAME_SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_USER, login).apply();
        Intent intent = new Intent(getContext(), MapsActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void showPhoneField() {
        if(orientationConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if(authCodeLinearLayout.isShown()){
                hideCodeField();
            }
            authPhoneLinearLayout.setVisibility(View.VISIBLE);
            authPhoneLinearLayout.animate()
                    .translationY(authPhoneLinearLayout.getHeight())
                    .alpha(1.0F)
                    .setListener(null);
        }
    }

    @Override
    public void hidePhoneField(){
        if(orientationConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            authPhoneLinearLayout.animate()
                    .translationY(0)
                    .alpha(0.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            authPhoneLinearLayout.setVisibility(View.INVISIBLE);
                        }
                    });

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.takeView(this);
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                String phone = credential.getId();
//                TODO check if phone is already used, otherwise add phone to user account
//                user.setPhone(phone);
                presenter.authSms(getActivity(), phone);
                Toast.makeText(getContext(), R.string.sms_will_arrive, Toast.LENGTH_SHORT).show();
            } else{
                showPhoneField();
            }
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showPhoneField();
    }

    // Construct a request for phone numbers and show the picker
    private void requestHint() {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();

        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(
                googleApiClient, hintRequest);
        IntentSender sender = intent.getIntentSender();
        try {
            startIntentSenderForResult(sender,
                    RESOLVE_HINT, null, 0, 0, 0, new Bundle());
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            showPhoneField();
        }
    }
}
