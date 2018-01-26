package com.pointproject.pointproject.ui.login;

import android.app.Activity;
import android.content.Context;

import com.pointproject.pointproject.di.ActivityScope;
import com.pointproject.pointproject.di.FragmentScope;

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
}
