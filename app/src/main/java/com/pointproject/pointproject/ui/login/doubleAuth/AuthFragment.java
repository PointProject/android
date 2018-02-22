package com.pointproject.pointproject.ui.login.doubleAuth;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pointproject.pointproject.AbstractFragment;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.data.Constants;
import com.pointproject.pointproject.ui.maps.MapsActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pointproject.pointproject.data.Constants.KEY_USER;
import static com.pointproject.pointproject.data.Constants.NAME_SHARED_PREFERENCES;

public class AuthFragment extends AbstractFragment implements AuthContract.View{

    public static final String EXTRA_CREDENTIALS = "extra_credentials";
    public static final String EXTRA_LOGIN = "extra_login";
    public static final String TAG = "AuthFragment";

    private final static int LAYOUT = R.layout.fragment_auth;
    private final static String KEY_CODE_TEXT__EDIT_SHOWN = "key_text_shown";

    @Inject AuthPresenter presenter;

    @BindView(R.id.auth_code)
    EditText codeText;

    @BindView(R.id.auth_enter_code)
    Button enterCodeButton;

    @BindView(R.id.auth_sms)
    Button authSms;

    @BindView(R.id.auth_telegram)
    Button authTelegram;

    @BindView(R.id.enter_auth_code_layout)
    LinearLayout authCodeLinearLayout;

    private String credentials, login;

    private Configuration orientationConfig;

    @Inject
    public AuthFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        ButterKnife.bind(this,view);

        orientationConfig = getResources().getConfiguration();

        if (savedInstanceState!=null)
            if(savedInstanceState.getBoolean(KEY_CODE_TEXT__EDIT_SHOWN))
                showCodeField();

        Bundle bundle = getArguments();
        assert bundle != null;
        if(!bundle.isEmpty()){
            credentials = bundle.getString(EXTRA_CREDENTIALS);
            login = bundle.getString(EXTRA_LOGIN, "");
        }

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

        outState.putBoolean(KEY_CODE_TEXT__EDIT_SHOWN, authCodeLinearLayout.isShown());
    }

    @OnClick(R.id.auth_telegram)
    public void authTelegram(View view){
        presenter.authTelegram(credentials);
        Intent telegram = new Intent(Intent.ACTION_VIEW , Uri.parse(Constants.URI_TELEGRAM_BOT+credentials.hashCode()));
        startActivity(telegram);
    }

    @OnClick(R.id.auth_sms)
    public void authSms(View view){
        presenter.authSms();
    }

    @OnClick(R.id.auth_enter_code)
    public void enterCode(View view){
        String code = codeText.getText().toString();
        if(!code.isEmpty())
            presenter.checkCode(Integer.valueOf(code));
    }

    @Override
    public void showCodeField() {
        if(orientationConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
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
    public void wrongCode() {
        Toast.makeText(getContext(), R.string.wrong_auth_code, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void next() {
        SharedPreferences prefs = getContext().getSharedPreferences(NAME_SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_USER, login).apply();
        startActivity(new Intent(getContext(), MapsActivity.class));
    }
}
