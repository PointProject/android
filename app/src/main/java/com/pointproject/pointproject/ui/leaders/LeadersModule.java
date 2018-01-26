package com.pointproject.pointproject.ui.leaders;

import com.pointproject.pointproject.di.ActivityScope;
import com.pointproject.pointproject.di.FragmentScope;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class LeadersModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract LeadersMainFragment leadersMainFragment();

    @Binds
    @ActivityScope
    abstract LeadersContract.Presenter leadersPresenter(LeadersPresenter presenter);
}
