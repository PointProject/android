package com.pointproject.pointproject.ui.crystals;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.pointproject.pointproject.AbstractActivity;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.util.ActivityUtils;

import javax.inject.Inject;

public class CrystalsActivity extends AbstractActivity {

    private final static String MAPS_FRAGMENT_TAG = "CrystalsMainFragment";
    private static final int NAV_ITEM = R.id.menu_crystals;

    @Inject CrystalsMainFragment mainFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CrystalsMainFragment crystalsMainFragment = (CrystalsMainFragment)
                getSupportFragmentManager().findFragmentById(ID_CONTENT_CONTAINER);

        if(crystalsMainFragment == null){
            crystalsMainFragment = mainFragment;

            ActivityUtils.addSupportFragmentToActivity(getSupportFragmentManager(),
                    crystalsMainFragment,
                    ID_CONTENT_CONTAINER,
                    MAPS_FRAGMENT_TAG);
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
