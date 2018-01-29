package com.pointproject.pointproject.ui.login;

import android.os.Bundle;

import com.pointproject.pointproject.R;
import com.pointproject.pointproject.util.ActivityUtils;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;


public class LoginActivity extends DaggerAppCompatActivity {

    private final static int LAYOUT = R.layout.login_activity;
    private final static String LOGIN_FRAGMENT_TAG = "LoginFragment";

    @Inject LoginFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().
                findFragmentById(R.id.content_container);
        if(loginFragment == null){
            loginFragment = mFragment;
            ActivityUtils.addSupportFragmentToActivity(getSupportFragmentManager(),
                    loginFragment,
                    R.id.content_container,
                    LOGIN_FRAGMENT_TAG);
        }
    }
}

