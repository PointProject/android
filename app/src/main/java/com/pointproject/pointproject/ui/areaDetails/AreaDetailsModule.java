package com.pointproject.pointproject.ui.areaDetails;

import com.pointproject.pointproject.di.ActivityScope;
import com.pointproject.pointproject.di.FragmentScope;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class AreaDetailsModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract AreaDetailsFragment areaDetailsFragment();

    @Binds
    @ActivityScope
    abstract AreaDetailsContract.Presenter areaDetailsPresenter(AreaDetailsPresenter presenter);
}
