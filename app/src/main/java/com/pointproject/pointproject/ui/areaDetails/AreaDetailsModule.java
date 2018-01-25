package com.pointproject.pointproject.ui.areaDetails;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class AreaDetailsModule {
    @ContributesAndroidInjector
    abstract AreaDetailsFragment areaDetailsFragment();

    @Binds
    abstract AreaDetailsContract.Presenter areaDetailsPresenter(AreaDetailsPresenter presenter);
}
