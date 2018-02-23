package com.pointproject.pointproject.di;

import android.app.Application;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ApplicationModule {
    @Binds
    abstract Context bindContext(Application application);
}
