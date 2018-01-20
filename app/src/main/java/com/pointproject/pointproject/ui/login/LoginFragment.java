package com.pointproject.pointproject.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginFragment extends AbstractFragment {

    private final static int LAYOUT = R.layout.login_fragment;


    @BindView(R.id.login_progress)
    ProgressBar progressBar;

    @BindView(R.id.sign_in_login)
    Button loginBtn;

    @BindView(R.id.password_login)
    EditText passwordField;

    @BindView(R.id.login_login)
    EditText loginField;




    public static LoginFragment getInstance(Context context) {
        LoginFragment fragmentInstance = new LoginFragment();

        Bundle args = new Bundle();
        fragmentInstance.setArguments(args);
        fragmentInstance.setContext(context);
        return fragmentInstance;
    }

//    Context is lost on screen rotation. Resetting it onAttach solve rotation crach problem
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
                attemptLogin();
                return true;
            }
            return false;
        });
        loginBtn.setOnClickListener(view1 -> {
            attemptLogin();
        });

    }

    private void attemptLogin() {

        // Reset errors.
        loginField.setError(null);
        passwordField.setError(null);

        // Store values at the time of the login attempt.
        String email = loginField.getText().toString();
        String password = passwordField.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordField.setError(getString(R.string.error_invalid_password));
            focusView = passwordField;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            loginField.setError(getString(R.string.error_field_required));
            focusView = loginField;
            cancel = true;
        } else if (!isEmailValid(email)) {
            loginField.setError(getString(R.string.error_invalid_login));
            focusView = loginField;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            startActivity(new Intent(context, MapsActivity.class));
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.length()>3;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


}
