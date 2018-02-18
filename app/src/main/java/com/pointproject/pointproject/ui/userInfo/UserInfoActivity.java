package com.pointproject.pointproject.ui.userInfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.pointproject.pointproject.AbstractActivity;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.model.User;
import com.pointproject.pointproject.ui.areaDetails.AreaDetailsActivity;
import com.pointproject.pointproject.util.ActivityUtils;

import javax.inject.Inject;


public class UserInfoActivity extends AbstractActivity {

    private final static String MAPS_FRAGMENT_TAG = "UserInfoFragment";
    private static final int NAV_ITEM = -1;
    private static final String EXTRA_USER = "user";

    @Inject UserInfoFragment mFragment;

    public static Intent getIntent(Context context, User user){
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra(EXTRA_USER, user);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolbarTitle(getString(R.string.user_info_title));

        User user = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (User)extras.getSerializable(EXTRA_USER);
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(UserInfoFragment.EXTRA_USER, user);

        UserInfoFragment userInfoFragment = (UserInfoFragment) getSupportFragmentManager()
                .findFragmentById(ID_CONTENT_CONTAINER);

        if(userInfoFragment == null){
            userInfoFragment = mFragment;
            userInfoFragment.setArguments(bundle);

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
