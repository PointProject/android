package com.pointproject.pointproject.ui.login.doubleAuth;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.pointproject.pointproject.AbstractFragment;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.model.User;
import com.pointproject.pointproject.network.ApiClient;
import com.pointproject.pointproject.network.callback.UserCallback;
import com.pointproject.pointproject.network.response.NetworkError;
import com.pointproject.pointproject.ui.login.AuthReason;

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

    private OnSuccessfulAuth mCallback;
    public interface OnSuccessfulAuth{
        void startMainMap();
    }

    public static final String EXTRA_USER = "extra_user";
    public static final String EXTRA_AUTH_REASON = "extra_reason";
    public static final String TAG = "AuthFragment";

    private static final int LAYOUT = R.layout.fragment_auth;
    private static final String KEY_PHONE_EDIT_TEXT_SHOWN = "phone_text_shown";
    private static final String KEY_AUTH_REASON = "key_auth_reason";
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
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

    @BindView(R.id.til_code)
    TextInputLayout tilCode;
    @BindView(R.id.til_phone)
    TextInputLayout tilPhone;

    @BindView(R.id.auth_msg_text)
    TextView authMsg;

    @BindView(R.id.auth_resend_button)
    Button resendBtn;

    private User user;

    private AuthReason authReason;
    private GoogleApiClient googleApiClient;

    private boolean mVerificationInProgress = false;

    @Inject
    public AuthFragment(){}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setContext(context);

        try{
            mCallback = (OnSuccessfulAuth) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    " must implement OnSuccessfulAuth interface");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        ButterKnife.bind(this, view);

//      googleApiClient for phone hint request
        googleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage((FragmentActivity) context, this)
                .addApi(Auth.CREDENTIALS_API)
                .build();

        Bundle bundle = getArguments();
        assert bundle != null;
        if(!bundle.isEmpty()){
            user = (User) bundle.getSerializable(EXTRA_USER);
            authReason = (AuthReason) bundle.getSerializable(EXTRA_AUTH_REASON);
        }

        presenter.takeView(this, authReason, user);

        if(authReason == AuthReason.LOGIN) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), user.getLogin());
            serverApiClient.secureLogin(requestBody, new UserCallback() {
                @Override
                public void onSuccess(User serverUser) {
                    user = serverUser;
                    authSms();
                }

                @Override
                public void onError(NetworkError error) {
                    Log.e(TAG, error.getMessage());
                    authMsg.setText(R.string.no_internet_msg);
                }
            });
        }

        if (savedInstanceState!=null) {
            if(savedInstanceState.getBoolean(KEY_PHONE_EDIT_TEXT_SHOWN))
                showPhoneField();
            else
                showCodeField();
            mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS, false);
            authReason = (AuthReason) savedInstanceState.getSerializable(KEY_AUTH_REASON);
        }

        phoneText.setOnFocusChangeListener((v, hasFocus) -> {if(hasFocus) requestHint();});

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(!mVerificationInProgress && authReason == AuthReason.REGISTRATION){
            authSms();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(KEY_PHONE_EDIT_TEXT_SHOWN, authPhoneLinearLayout.isShown());
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
        outState.putSerializable(KEY_AUTH_REASON, authReason);
    }

//    TODO replace int phone with string phone
    private void authSms(){
        if(user != null){
            String phone = user.getPhone();
            if(phone!=null && !phone.isEmpty()){
                presenter.authSms(getContext(), phone);
                mVerificationInProgress = true;
            } else{
                showEmptyPhoneFieldError();
                showPhoneField();
            }
        }
    }

    @OnClick(R.id.auth_enter_code_button)
    public void enterCode(View view){
        String code = codeText.getText().toString();
        presenter.checkCode(code);
    }

    @Override
    public void showEmptyPhoneFieldError() {
        tilPhone.setError(" ");
        authMsg.setText(R.string.msg_empty_number);

        mVerificationInProgress = false;
    }

    @Override
    public void showEmptyCodeError() {
        tilCode.setError(" ");
        authMsg.setText(R.string.msg_empty_code);
    }

    @Override
    public void showWrongCodeError() {
        tilCode.setError(" ");
        authMsg.setText(R.string.msg_wrong_code);
    }

    @Override
    public void showInvalidPhoneError() {
        tilPhone.setError(" ");
        authMsg.setText(R.string.msg_invalid_phone_number);
        mVerificationInProgress = false;
    }

    @OnClick(R.id.auth_enter_phone_button)
    public void enterPhone(View view){
        String phone = phoneText.getText().toString();
            /**TODO: check if user have phone, otherwise add phone to user
             * TODO: and move this check to presenter
             *
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
        mVerificationInProgress = true;
        presenter.authSms(getContext(), phone);
    }

    @Override
    public void showMessage(int stringResId) {
        authMsg.setText(stringResId);
    }

    @Override
    public void showMapsActivity() {

        SharedPreferences prefs = context.getSharedPreferences(NAME_SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_USER, user.getLogin()).apply();

        mVerificationInProgress = false;
        mCallback.startMainMap();
    }

    @Override
    public void showCodeField() {
        authPhoneLinearLayout.setVisibility(View.GONE);
        authCodeLinearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPhoneField() {
        authCodeLinearLayout.setVisibility(View.GONE);
        authPhoneLinearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showResendButton(){
        resendBtn.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.auth_resend_button)
    public void resend(View view){
        presenter.resendVerificationCode(context);
        view.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.takeView(this, authReason, user);
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                String phone = credential.getId();
//                TODO check if phone is already used, otherwise add phone to user account
//                user.setPhone(phone);
                mVerificationInProgress = true;
                presenter.authSms(getActivity(), phone);
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
