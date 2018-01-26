package com.pointproject.pointproject.ui.rules;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.pointproject.pointproject.AbstractActivity;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.util.ActivityUtils;

import javax.inject.Inject;

public class RulesActivity extends AbstractActivity {

    private final static int NAV_MENU = R.id.menu_rules;
    private final static String RULES_MAIN_FRAGMENT = "RulesFragment";

    @Inject RulesFragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RulesFragment rulesFragment = (RulesFragment) getSupportFragmentManager()
                .findFragmentById(ID_CONTENT_CONTAINER);
        if(rulesFragment == null){
            rulesFragment = mFragment;

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    rulesFragment,
                    ID_CONTENT_CONTAINER,
                    RULES_MAIN_FRAGMENT);
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
