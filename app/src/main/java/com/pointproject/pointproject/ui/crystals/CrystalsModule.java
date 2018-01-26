package com.pointproject.pointproject.ui.crystals;

import com.pointproject.pointproject.di.ActivityScope;
import com.pointproject.pointproject.di.FragmentScope;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class CrystalsModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract CrystalsMainFragment crystalsMainFragment();

    @Binds
    @ActivityScope
    abstract CrystalsContract.Presenter crystalsPresenter(CrystalsPresenter presenter);
}
