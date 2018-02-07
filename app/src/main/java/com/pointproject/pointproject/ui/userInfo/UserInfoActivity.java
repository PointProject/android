package com.pointproject.pointproject.ui.userInfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.pointproject.pointproject.AbstractActivity;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.util.ActivityUtils;

import javax.inject.Inject;


public class UserInfoActivity extends AbstractActivity {

    private final static String MAPS_FRAGMENT_TAG = "UserInfoFragment";
    private static final int NAV_ITEM = -1;

    @Inject UserInfoFragment mFragment;

    public static Intent getIntent(Context context){
        return new Intent(context, UserInfoActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbarTitle(getString(R.string.user_info_title));

        UserInfoFragment userInfoFragment = (UserInfoFragment) getSupportFragmentManager()
                .findFragmentById(ID_CONTENT_CONTAINER);

        if(userInfoFragment == null){
            userInfoFragment = mFragment;

            ActivityUtils.addSupportFragmentToActivity(getSupportFragmentManager(),
                    userInfoFragment,
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
