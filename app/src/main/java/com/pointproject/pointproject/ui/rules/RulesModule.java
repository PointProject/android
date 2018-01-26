package com.pointproject.pointproject.ui.rules;

import com.pointproject.pointproject.di.ActivityScope;
import com.pointproject.pointproject.di.FragmentScope;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class RulesModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract RulesFragment rulesFragment();

    @Binds
    @ActivityScope
    abstract RulesContract.Presenter rulesPresenter(RulesPresenter presenter);

}
