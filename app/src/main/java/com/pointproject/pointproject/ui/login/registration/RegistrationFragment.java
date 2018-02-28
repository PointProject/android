package com.pointproject.pointproject.ui.login.registration;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.pointproject.pointproject.AbstractFragment;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.model.User;
import com.pointproject.pointproject.ui.login.AuthReason;
import com.pointproject.pointproject.ui.login.doubleAuth.AuthFragment;
import com.pointproject.pointproject.ui.login.mainLogin.LoginFragment;
import com.pointproject.pointproject.util.ActivityUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pointproject.pointproject.ui.login.doubleAuth.AuthFragment.EXTRA_AUTH_REASON;
import static com.pointproject.pointproject.ui.login.doubleAuth.AuthFragment.EXTRA_USER;

public class RegistrationFragment extends AbstractFragment implements RegistrationContract.View{

    public static final String TAG = RegistrationFragment.class.getSimpleName();
    private static final int LAYOUT = R.layout.fragment_registration;

    @Inject RegistrationPresenter regPresenter;

    @BindView(R.id.input_login)
    EditText loginField;
    @BindView(R.id.input_password)
    EditText passwordField;
    @BindView(R.id.input_phone)
    EditText phoneField;

    private ProgressDialog progressDialog;

    @Inject
    public RegistrationFragment(){}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setContext(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        phoneField.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                register();
                return true;
            }
            return false;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        regPresenter.takeView(this);
    }

    @Override
    public void onPause() {
        regPresenter.dropView();
        super.onPause();
    }

    @OnClick(R.id.btn_signup)
    public void signUp(View view){
        register();
    }

    @OnClick(R.id.link_login)
    public void showLogin(View view){
        LoginFragment loginFragment = new LoginFragment();
        ActivityUtils.addSupportFragmentToActivity(getFragmentManager(),
                loginFragment,
                ID_CONTENT_CONTAINER,
                LoginFragment.TAG);
    }

    @Override
    public void showError(int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginError(int resId) {
        loginField.setError(getString(resId));
        loginField.requestFocus();
    }

    @Override
    public void showEmptyLoginError() {
        loginField.setError(getString(R.string.empty_login));
        loginField.requestFocus();
    }

    @Override
    public void showEmptyPasswordError() {
        passwordField.setError(getString(R.string.empty_password));
        passwordField.requestFocus();
    }

    @Override
    public void showPasswordError(int resId) {
        passwordField.setError(getString(resId));
        passwordField.requestFocus();
    }

    @Override
    public void showPhoneError(int resId) {
        phoneField.setError(getString(resId));
        phoneField.requestFocus();
    }

    @Override
    public void showNoInternetError(int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }


    @Override
    public void resetErrors(){
        loginField.setError(null);
        passwordField.setError(null);
        phoneField.setError(null);
    }

    @Override
    public void showProgressBar() {
        progressDialog = new ProgressDialog(context,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.authentication_progress));
        progressDialog.show();
    }

    @Override
    public void hideProgressBar() {
        progressDialog.dismiss();
    }

    @Override
    public void startAuth(User user) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_AUTH_REASON, AuthReason.REGISTRATION);
        bundle.putSerializable(EXTRA_USER, user);
        AuthFragment authFragment = new AuthFragment();
        authFragment.setArguments(bundle);
        ActivityUtils.addSupportFragmentToActivity(
                getActivity().getSupportFragmentManager(),
                authFragment,
                ID_CONTENT_CONTAINER,
                AuthFragment.TAG);
    }

    private void register(){
        regPresenter.register(
                loginField.getText().toString(),
                passwordField.getText().toString(),
                phoneField.getText().toString(),
                context
        );
    }
}