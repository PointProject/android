package com.pointproject.pointproject.ui.settings;

import android.os.Bundle;

import com.pointproject.pointproject.AbstractActivity;
import com.pointproject.pointproject.R;


public class SettingsActivity extends AbstractActivity{

    private final static String SETTINGS_FRAGMENT_TAG = "SettingsFragment";
    private static final int NAV_ITEM = R.id.menu_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            // The Activity is NOT being re-created so we can instantiate a new Fragment
            // and add it to the Activity
            SettingsFragment fragment = SettingsFragment.getInstance();
            getFragmentManager()
                    .beginTransaction()
                    // It's almost always a good idea to use .replace instead of .add so that
                    // you never accidentally layer multiple Fragments on top of each other
                    // unless of course that's your intention
                    .replace(R.id.content_container, fragment, SETTINGS_FRAGMENT_TAG)
                    .commit();
        } else {
            // The Activity IS being re-created so we don't need to instantiate the Fragment or add it,
            // but if we need a reference to it, we can use the tag we passed to .replace
            getSupportFragmentManager().findFragmentByTag(SETTINGS_FRAGMENT_TAG);
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
