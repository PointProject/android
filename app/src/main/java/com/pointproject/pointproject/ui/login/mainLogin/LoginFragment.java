package com.pointproject.pointproject.ui.login.mainLogin;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pointproject.pointproject.AbstractFragment;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.ui.login.doubleAuth.AuthFragment;
import com.pointproject.pointproject.util.ActivityUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pointproject.pointproject.ui.login.doubleAuth.AuthFragment.EXTRA_CREDENTIALS;

public class LoginFragment extends AbstractFragment implements LoginContract.View{

    public  final static String TAG = "LoginFragment";
    private final static int LAYOUT = R.layout.login_fragment;

    @BindView(R.id.sign_in_login)
    Button loginBtn;

//    @BindView(R.id.register_login)
//    Button regBtn;

    @BindView(R.id.password_login)
    EditText passwordField;

    @BindView(R.id.login_login)
    EditText loginField;

    @Inject LoginContract.Presenter presenter;

    private String login, password;

    private ProgressDialog progressDialog;


    @Inject
    public LoginFragment(){

    }

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
            presenter.checkAccount(login, password);
        });

//        regBtn.setOnClickListener(view1 -> {
//            getCredentials();
//            presenter.register(login, password);
//        });
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
    public void showNoInternetError() {
        Toast.makeText(getContext(), R.string.msg_no_internet, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginIn() {
//        hideProgressBar();
//        startActivity(new Intent(context, MapsActivity.class));
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_CREDENTIALS, login+password);
        AuthFragment authFragment = new AuthFragment();
        authFragment.setArguments(bundle);
        ActivityUtils.addSupportFragmentToActivity(
                getActivity().getSupportFragmentManager(),
                authFragment,
                ID_CONTENT_CONTAINER,
                AuthFragment.TAG);
    }

    @Override
    public void register(){

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
}