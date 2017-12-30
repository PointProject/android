package com.pointproject.pointproject.ui.leaders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.pointproject.pointproject.AbstractActivity;
import com.pointproject.pointproject.R;

import butterknife.BindView;

/**
 * Created by xdewnik on 30.12.2017.
 */

public class LeadersActivity extends AbstractActivity {

    private static final int NAV_ITEM = R.id.menu_leaders;
    private static final String TAG = LeadersActivity.class.getSimpleName();
    private final static String LEADERS_MAIN_FRAGMENT = "LeadersMainFragment";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            // The Activity is NOT being re-created so we can instantiate a new Fragment
            // and add it to the Activity
            LeadersMainFragment fragment = LeadersMainFragment.getInstance(this);
            getSupportFragmentManager()
                    .beginTransaction()
                    // It's almost always a good idea to use .replace instead of .add so that
                    // you never accidentally layer multiple Fragments on top of each other
                    // unless of course that's your intention
                    .replace(R.id.content_container, fragment, LEADERS_MAIN_FRAGMENT)
                    .commit();
        } else {
            // The Activity IS being re-created so we don't need to instantiate the Fragment or add it,
            // but if we need a reference to it, we can use the tag we passed to .replace
            getSupportFragmentManager().findFragmentByTag(LEADERS_MAIN_FRAGMENT);
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
