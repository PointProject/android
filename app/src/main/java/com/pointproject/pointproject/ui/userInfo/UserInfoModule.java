package com.pointproject.pointproject.ui.userInfo;


import com.pointproject.pointproject.di.ActivityScope;
import com.pointproject.pointproject.di.FragmentScope;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class UserInfoModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract UserInfoFragment userInfoFragment();

    @ActivityScope
    @Binds
    abstract UserInfoContract.Presenter userInfoPresenter(UserInfoPresenter presenter);
}
