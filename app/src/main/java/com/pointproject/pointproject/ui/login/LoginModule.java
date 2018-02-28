package com.pointproject.pointproject.ui.login;

import com.pointproject.pointproject.di.ActivityScope;
import com.pointproject.pointproject.di.FragmentScope;
import com.pointproject.pointproject.ui.login.doubleAuth.AuthContract;
import com.pointproject.pointproject.ui.login.doubleAuth.AuthFragment;
import com.pointproject.pointproject.ui.login.doubleAuth.AuthPresenter;
import com.pointproject.pointproject.ui.login.mainLogin.LoginContract;
import com.pointproject.pointproject.ui.login.mainLogin.LoginFragment;
import com.pointproject.pointproject.ui.login.mainLogin.LoginPresenter;
import com.pointproject.pointproject.ui.login.registration.RegistrationContract;
import com.pointproject.pointproject.ui.login.registration.RegistrationFragment;
import com.pointproject.pointproject.ui.login.registration.RegistrationPresenter;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class LoginModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract LoginFragment loginFragment();

    @ActivityScope
    @Binds
    abstract LoginContract.Presenter loginPresenter(LoginPresenter presenter);

    @FragmentScope
    @ContributesAndroidInjector
    abstract AuthFragment authFragment();

    @ActivityScope
    @Binds
    abstract AuthContract.Presenter authPresenter(AuthPresenter presenter);

    @FragmentScope
    @ContributesAndroidInjector
    abstract RegistrationFragment registrationFragment();

    @ActivityScope
    @Binds
    abstract RegistrationContract.Presenter regPresenter(RegistrationPresenter presenter);
}
