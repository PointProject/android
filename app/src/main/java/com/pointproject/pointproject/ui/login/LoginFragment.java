package com.pointproject.pointproject.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.pointproject.pointproject.AbstractFragment;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.ui.maps.MapsActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginFragment extends AbstractFragment implements LoginContract.View{

    private final static int LAYOUT = R.layout.login_fragment;


    @BindView(R.id.login_progress)
    ProgressBar progressBar;

    @BindView(R.id.sign_in_login)
    Button loginBtn;

    @BindView(R.id.password_login)
    EditText passwordField;

    @BindView(R.id.login_login)
    EditText loginField;

    @Inject LoginContract.Presenter presenter;

    private String login, password;


    @Inject
    public LoginFragment(){

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.takeView(this);
    }

    @Override
    public void onPause() {
        presenter.dropView();
        super.onPause();
    }

    //    Context is lost on screen rotation. Resetting it onAttach solve rotation crash problem
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        setContext(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        ButterKnife.bind(this,view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        passwordField.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {

                getCredentials();
                presenter.checkAccount(login, password);
                return true;
            }
            return false;
        });
        loginBtn.setOnClickListener(view1 -> {
            getCredentials();
            presenter.checkAccount(login, password);;
        });

    }

    @Override
    public void resetErrors() {
        loginField.setError(null);
        passwordField.setError(null);
    }

    @Override
    public void showPasswordError() {
        passwordField.setError(getString(R.string.error_invalid_password));
        passwordField.requestFocus();
    }

    @Override
    public void showEmptyLoginError() {
        loginField.setError(getString(R.string.error_field_required));
        loginField.requestFocus();
    }

    @Override
    public void showInvalidLoginError() {
        loginField.setError(getString(R.string.error_invalid_login));
        loginField.requestFocus();
    }

    @Override
    public void loginIn() {
        startActivity(new Intent(context, MapsActivity.class));
    }

    private void getCredentials(){
        login = loginField.getText().toString();
        password = passwordField.getText().toString();
    }
}
