package com.pointproject.pointproject.ui.login.mainLogin;

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
import android.widget.Button;
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

public class LoginFragment extends AbstractFragment implements LoginContract.View{

    public  final static String TAG = "LoginFragment";
    private final static int LAYOUT = R.layout.fragment_login;

    private OnFragmentRegAuthInteraction mCallback;

    public interface OnFragmentRegAuthInteraction{
        void showRegistration();

        void showAuthentication(User user, AuthReason reason);
    }

    @BindView(R.id.sign_in_login)
    Button loginBtn;

    @BindView(R.id.password_login)
    EditText passwordField;

    @BindView(R.id.login_login)
    EditText loginField;

    @BindView(R.id.login_text_input_layout_login)
    TextInputLayout tilLogin;

    @BindView(R.id.password_text_input_layout_login)
    TextInputLayout tilPassword;

    @Inject LoginContract.Presenter presenter;

    private String login, password;

    private ProgressDialog progressDialog;


    @Inject
    public LoginFragment(){    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.takeView(this);

        if(progressDialog!=null)
            progressDialog.dismiss();
    }

    @Override
    public void onPause() {
        presenter.dropView();
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        setContext(context);

        try {
            mCallback = (OnFragmentRegAuthInteraction) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    " must implement OnFragmentRegAuthInteraction");
        }
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
                presenter.checkAccount(login, password, context);
                return true;
            }
            return false;
        });
        loginBtn.setOnClickListener(view1 -> {
            getCredentials();
            presenter.checkAccount(login, password, getContext());
        });
    }

    @Override
    public void resetErrors() {
        tilLogin.setError(null);
        tilPassword.setError(null);
    }

    @Override
    public void showPasswordError(int resId) {
        tilPassword.setErrorEnabled(true);
        tilPassword.setError(getString(resId));
    }

    @Override
    public void showLoginError(int resId) {
        tilLogin.setErrorEnabled(true);
        tilLogin.setError(getString(resId));
    }

    @Override
    public void showEmptyLoginError() {
        tilLogin.setErrorEnabled(true);
        tilLogin.setError(getString(R.string.error_field_required));
    }

    @Override
    public void showEmptyPasswordError() {
        tilPassword.setError(getString(R.string.error_incorrect_password));
    }

    @Override
    public void showError(int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNoInternetError() {
        Toast.makeText(getContext(), R.string.msg_no_internet, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void loginIn(User userN) {
        mCallback.showAuthentication(userN, AuthReason.LOGIN);
        hideProgressBar();
    }

    @Override
    public void register(){
        mCallback.showRegistration();
    }

    @Override
    public void showProgressBar() {

        progressDialog = new ProgressDialog(context,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.authentication_progress));
        progressDialog.show();
    }

    @Override
    public void hideProgressBar() {
        progressDialog.dismiss();
    }

    private void getCredentials(){
        login = loginField.getText().toString();
        password = passwordField.getText().toString();
    }

    @OnClick(R.id.register_text_view)
    public void register(View view){
        register();
    }

    @Override
    public void showPhoneError(int resId) {}
}
