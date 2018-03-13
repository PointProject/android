package com.pointproject.pointproject.ui.login.registration;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationFragment extends AbstractFragment implements RegistrationContract.View{

    public static final String TAG = RegistrationFragment.class.getSimpleName();
    private static final int LAYOUT = R.layout.fragment_registration;

    private OnFragmentLoginAuthInteraction mCallback;

    public interface OnFragmentLoginAuthInteraction{
        void showLogin();

        void showAuth(User user, AuthReason reason);
    }

    @Inject RegistrationPresenter regPresenter;

    @BindView(R.id.input_login)
    EditText loginField;
    @BindView(R.id.input_password)
    EditText passwordField;
    @BindView(R.id.input_phone)
    EditText phoneField;

    @BindView(R.id.login_text_input_layout_reg)
    TextInputLayout tilLogin;
    @BindView(R.id.password_text_input_layout_reg)
    TextInputLayout tilPassword;
    @BindView(R.id.phone_text_input_layout_reg)
    TextInputLayout tilPhone;

    private ProgressDialog progressDialog;

    @Inject
    public RegistrationFragment(){}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setContext(context);

        try{
            mCallback = (OnFragmentLoginAuthInteraction) context;
        } catch(ClassCastException e){
            throw new ClassCastException(context.toString() +
                    " must implement OnFragmentLoginAuthInteraction interface");
        }
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
        mCallback.showLogin();
    }

    @Override
    public void showError(int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginError(int resId) {
        tilLogin.setErrorEnabled(true);
        tilLogin.setError(getString(resId));
    }

    @Override
    public void showEmptyLoginError() {
        tilLogin.setErrorEnabled(true);
        tilLogin.setError(getString(R.string.empty_login));
    }

    @Override
    public void showEmptyPasswordError() {
        tilPassword.setErrorEnabled(true);
        tilPassword.setError(getString(R.string.empty_password));
    }

    @Override
    public void showPasswordError(int resId) {
        tilPassword.setErrorEnabled(true);
        tilPassword.setError(getString(resId));
    }

    @Override
    public void showPhoneError(int resId) {
        tilPhone.setErrorEnabled(true);
        tilPhone.setError(getString(resId));
    }

    @Override
    public void showNoInternetError(int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }


    @Override
    public void resetErrors(){
        tilLogin.setError(null);
        tilPassword.setError(null);
        tilPhone.setError(null);
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
        mCallback.showAuth(user, AuthReason.REGISTRATION);
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