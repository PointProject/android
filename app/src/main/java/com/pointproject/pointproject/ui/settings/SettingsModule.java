package com.pointproject.pointproject.ui.settings;

import com.pointproject.pointproject.di.FragmentScope;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SettingsModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract SettingsFragment settingsFragment();
}
