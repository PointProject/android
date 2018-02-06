package com.pointproject.pointproject;

import com.pointproject.pointproject.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;


public class PointProjectApplication extends DaggerApplication {

    @Inject
    public PointProjectApplication(){

    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().create(this);
    }
}
