package com.pointproject.pointproject.ui.login.doubleAuth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.pointproject.pointproject.AbstractFragment;
import com.pointproject.pointproject.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthFragment extends AbstractFragment implements AuthContract.View{

    public static final String EXTRA_CREDENTIALS = "extra_credentials";
    public static final String TAG = "AuthFragment";
    private final static int LAYOUT = R.layout.fragment_auth;

    @Inject AuthPresenter presenter;

    @BindView(R.id.auth_code)
    EditText codeText;

    @BindView(R.id.auth_enter_code)
    Button enterCodeButton;

    @BindView(R.id.auth_sms)
    Button authSms;

    @BindView(R.id.auth_telegram)
    Button authTelegram;

    private String credentials;

    public AuthFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        ButterKnife.bind(this,view);

        Bundle bundle = getArguments();
        assert bundle != null;
        if(!bundle.isEmpty())
            credentials = bundle.getString(EXTRA_CREDENTIALS);

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

    @OnClick(R.id.auth_telegram)
    public void authTelegram(View view){
        presenter.authTelegram(credentials);
        Intent telegram = new Intent(Intent.ACTION_VIEW , Uri.parse("https://telegram.me/pay_point_bot?start="+credentials));
        startActivity(telegram);
    }

    @Override
    public void showEnterCode(int code) {
        codeText.setText(code+"");
    }

    @Override
    public void wrongCode() {

    }

    @Override
    public void next() {

    }
}
