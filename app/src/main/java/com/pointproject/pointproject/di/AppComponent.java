package com.pointproject.pointproject.di;

import com.pointproject.pointproject.PointProjectApplication;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules={ApplicationModule.class,
        ActivityBuilder.class,
        AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<PointProjectApplication> {
    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<PointProjectApplication>{
    }
}
