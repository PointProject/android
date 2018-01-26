package com.pointproject.pointproject.di;

import com.pointproject.pointproject.ui.areaDetails.AreaDetailsActivity;
import com.pointproject.pointproject.ui.areaDetails.AreaDetailsModule;
import com.pointproject.pointproject.ui.crystals.CrystalsActivity;
import com.pointproject.pointproject.ui.crystals.CrystalsModule;
import com.pointproject.pointproject.ui.leaders.LeadersActivity;
import com.pointproject.pointproject.ui.leaders.LeadersModule;
import com.pointproject.pointproject.ui.login.LoginActivity;
import com.pointproject.pointproject.ui.login.LoginModule;
import com.pointproject.pointproject.ui.maps.MapsActivity;
import com.pointproject.pointproject.ui.maps.MapsModule;
import com.pointproject.pointproject.ui.rules.RulesActivity;
import com.pointproject.pointproject.ui.rules.RulesModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(modules = AreaDetailsModule.class)
    abstract AreaDetailsActivity areaDetailsActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract LoginActivity loginActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = MapsModule.class)
    abstract MapsActivity mapsActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = CrystalsModule.class)
    abstract CrystalsActivity crystalsActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = LeadersModule.class)
    abstract LeadersActivity leadersActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = RulesModule.class)
    abstract RulesActivity rulesActivity();
}
