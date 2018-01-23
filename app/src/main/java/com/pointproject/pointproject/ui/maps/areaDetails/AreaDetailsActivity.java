package com.pointproject.pointproject.ui.maps.areaDetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.pointproject.pointproject.AbstractActivity;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.data.Values;

import butterknife.BindView;


public class AreaDetailsActivity extends AbstractActivity {



    private static final int LAYOUT = R.layout.activity_area_details;
    private static final int NAV_ITEM = -1;
    private static final String TAG = AreaDetailsActivity.class.getSimpleName();
    private static final String MAPS_FRAGMENT_TAG = "AreaDetailsFragment";

    private static final String EXTRA_AREA_NAME = "areaName";

    @BindView(R.id.toolbar_image) ImageView areaImage;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;

    public static Intent getIntent(Context context, String name){
        Intent intent = new Intent(context, AreaDetailsActivity.class);
        intent.putExtra(EXTRA_AREA_NAME, name);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupCollapsingToolbar();

        if (savedInstanceState == null) {
            // The Activity is NOT being re-created so we can instantiate a new Fragment
            // and add it to the Activity

            AreaDetailsFragment fragment = AreaDetailsFragment.getInstance(this);
            getSupportFragmentManager()
                    .beginTransaction()
                    // It's almost always a good idea to use .replace instead of .add so that
                    // you never accidentally layer multiple Fragments on top of each other
                    // unless of course that's your intention
                    .replace(R.id.content_container, fragment, MAPS_FRAGMENT_TAG)
                    .commit();
        } else {
            // The Activity IS being re-created so we don't need to instantiate the Fragment or add it,
            // but if we need a reference to it, we can use the tag we passed to .replace
            getSupportFragmentManager().findFragmentByTag(MAPS_FRAGMENT_TAG);
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

    private void setupCollapsingToolbar() {
        String name = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString(EXTRA_AREA_NAME);


        }

//        collapsingToolbar.setContentScrimColor(ContextCompat.getColor(this, R.color.colorPrimary));
        collapsingToolbar.setTitle(name);
        collapsingToolbar.setTitleEnabled(true);

        areaImage.setImageResource(Values.toolvarImageId);
    }
}
