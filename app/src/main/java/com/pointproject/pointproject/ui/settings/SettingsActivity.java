package com.pointproject.pointproject.ui.settings;

import android.os.Bundle;

import com.pointproject.pointproject.AbstractActivity;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.util.ActivityUtils;

import javax.inject.Inject;


public class SettingsActivity extends AbstractActivity{

    private final static String SETTINGS_FRAGMENT_TAG = "SettingsFragment";
    private static final int NAV_ITEM = R.id.menu_settings;

    @Inject SettingsFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsFragment settingsFragment = (SettingsFragment) getFragmentManager()
                .findFragmentById(ID_CONTENT_CONTAINER);

        if(settingsFragment == null){
            settingsFragment = mFragment;

            ActivityUtils.addFragmentToActivity(getFragmentManager(),
                    settingsFragment,
                    ID_CONTENT_CONTAINER,
                    SETTINGS_FRAGMENT_TAG);
        }
    }

    @Override
    protected int getContentViewId() {
        return LAYOUT;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return NAV_ITEM;
    }
}
