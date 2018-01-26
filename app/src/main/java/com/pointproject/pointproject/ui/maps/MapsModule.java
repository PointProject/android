package com.pointproject.pointproject.ui.maps;


import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.res.Resources;
import android.location.LocationManager;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.di.ActivityScope;
import com.pointproject.pointproject.di.FragmentScope;

import javax.inject.Inject;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MapsModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract MapsMainFragment mapsMainFragment();

    @Binds
    @ActivityScope
    abstract MapsContract.Presenter mapsPresenter(MapsPresenter presenter);


    @ActivityScope
    @Provides
    public static LocationManager provideLocationManager(MapsActivity activity){
        return (LocationManager) activity.getBaseContext()
                .getSystemService(Service.LOCATION_SERVICE);
    }

    @Provides
    public static Resources provideResources(MapsActivity activity){
        return activity.getBaseContext().getResources();
    }

    @ActivityScope
    @Provides
    public static LocationRequest provideLocationRequest(Resources res){
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(res.getInteger(R.integer.location_request_update_interval));
        locationRequest.setFastestInterval(res.getInteger(R.integer.location_request_fastest_interval));
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        return locationRequest;
    }

    @ActivityScope
    @Provides
    public static LocationSettingsRequest.Builder provideLocationSettingsBuilder(LocationRequest locationRequest){
       LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest);

       //always show dialog window when gps is disabled
        builder.setAlwaysShow(true);

        return builder;
    }
}
