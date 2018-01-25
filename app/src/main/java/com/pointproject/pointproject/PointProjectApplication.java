package com.pointproject.pointproject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;


public class PointProjectApplication extends DaggerApplication {
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }
}
