package com.pointproject.pointproject.ui.leaders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.pointproject.pointproject.AbstractActivity;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.util.ActivityUtils;

import javax.inject.Inject;

import butterknife.BindView;

public class LeadersActivity extends AbstractActivity {

    private static final int NAV_ITEM = R.id.menu_leaders;
    private static final String TAG = LeadersActivity.class.getSimpleName();
    private final static String LEADERS_MAIN_FRAGMENT = "LeadersMainFragment";

    @Inject LeadersMainFragment mFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LeadersMainFragment leadersMainFragment = (LeadersMainFragment) getSupportFragmentManager().
                findFragmentById(ID_CONTENT_CONTAINER);

        if(leadersMainFragment == null){
            leadersMainFragment = mFragment;

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    leadersMainFragment,
                    ID_CONTENT_CONTAINER,
                    LEADERS_MAIN_FRAGMENT);
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
