package com.pointproject.pointproject.ui.rules;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.pointproject.pointproject.AbstractActivity;
import com.pointproject.pointproject.R;

public class RulesActivity extends AbstractActivity {
    
    private final static int LAYOUT = R.layout.activity_settings;
    private final static int NAV_MENU = R.id.menu_rules;
    private final static String RULES_MAIN_FRAGMENT = "RulesFragment";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            // The Activity is NOT being re-created so we can instantiate a new Fragment
            // and add it to the Activity
            RulesFragment fragment = RulesFragment.getInstance(this);
            getSupportFragmentManager()
                    .beginTransaction()
                    // It's almost always a good idea to use .replace instead of .add so that
                    // you never accidentally layer multiple Fragments on top of each other
                    // unless of course that's your intention
                    .replace(R.id.content_container, fragment, RULES_MAIN_FRAGMENT)
                    .commit();
        } else {
            // The Activity IS being re-created so we don't need to instantiate the Fragment or add it,
            // but if we need a reference to it, we can use the tag we passed to .replace
            getSupportFragmentManager().findFragmentByTag(RULES_MAIN_FRAGMENT);
        }
    }

    @Override
    protected int getContentViewId() {
        return LAYOUT;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return NAV_MENU;
    }
}
