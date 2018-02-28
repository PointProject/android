package com.pointproject.pointproject.ui.login;

import android.content.Intent;
import android.os.Bundle;

import com.pointproject.pointproject.R;
import com.pointproject.pointproject.model.User;
import com.pointproject.pointproject.ui.login.doubleAuth.AuthFragment;
import com.pointproject.pointproject.ui.login.mainLogin.LoginFragment;
import com.pointproject.pointproject.ui.login.registration.RegistrationFragment;
import com.pointproject.pointproject.ui.maps.MapsActivity;
import com.pointproject.pointproject.util.ActivityUtils;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

import static com.pointproject.pointproject.ui.login.doubleAuth.AuthFragment.EXTRA_AUTH_REASON;
import static com.pointproject.pointproject.ui.login.doubleAuth.AuthFragment.EXTRA_USER;


public class LoginActivity extends DaggerAppCompatActivity
        implements LoginFragment.OnFragmentRegAuthInteraction,
            RegistrationFragment.OnFragmentLoginAuthInteraction,
            AuthFragment.OnSuccessfulAuth{

    private final static int LAYOUT = R.layout.login_activity;
    private final static String LOGIN_FRAGMENT_TAG = "LoginFragment";

    @Inject LoginFragment mFragment;

    @Inject RegistrationFragment regFragment;

    @Inject AuthFragment authFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        if(savedInstanceState == null) {
            LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().
                    findFragmentById(R.id.content_container);
            if (loginFragment == null) {
                loginFragment = mFragment;
                ActivityUtils.addSupportFragmentToActivity(getSupportFragmentManager(),
                        loginFragment,
                        R.id.content_container,
                        LOGIN_FRAGMENT_TAG);
            }
        }
    }

    @Override
    public void showRegistration() {
        if(regFragment != null)
            regFragment = new RegistrationFragment();
        ActivityUtils.addSupportFragmentToActivity(
                getSupportFragmentManager(),
                regFragment,
                R.id.content_container,
                RegistrationFragment.TAG);
    }

    @Override
    public void showAuthentication(User user, AuthReason reason) {
        if(authFragment != null)
            authFragment = new AuthFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_AUTH_REASON, reason);
        bundle.putSerializable(EXTRA_USER, user);
        authFragment.setArguments(bundle);
        ActivityUtils.addSupportFragmentToActivity(
                getSupportFragmentManager(),
                authFragment,
                R.id.content_container,
                AuthFragment.TAG);
    }

    @Override
    public void showLogin() {
        if(mFragment != null)
            mFragment = new LoginFragment();
        ActivityUtils.addSupportFragmentToActivity(
                getSupportFragmentManager(),
                mFragment,
                R.id.content_container,
                LoginFragment.TAG);
    }

    @Override
    public void showAuth(User user, AuthReason reason) {
        if(authFragment != null){
            authFragment = new AuthFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_AUTH_REASON, reason);
        bundle.putSerializable(EXTRA_USER, user);
        AuthFragment authFragment = new AuthFragment();
        authFragment.setArguments(bundle);
        ActivityUtils.addSupportFragmentToActivity(
                getSupportFragmentManager(),
                authFragment,
                R.id.content_container,
                AuthFragment.TAG);
    }

    @Override
    public void startMainMap() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
        finish();
    }
}

