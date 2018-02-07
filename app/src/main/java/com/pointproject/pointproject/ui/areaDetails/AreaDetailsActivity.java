package com.pointproject.pointproject.ui.areaDetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.widget.ImageView;

import com.pointproject.pointproject.AbstractActivity;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.data.Values;
import com.pointproject.pointproject.util.ActivityUtils;

import javax.inject.Inject;

import butterknife.BindView;


public class AreaDetailsActivity extends AbstractActivity {

    private static final int LAYOUT = R.layout.activity_area_details;
    private static final int NAV_ITEM = -1;
    private static final String TAG = AreaDetailsActivity.class.getSimpleName();
    private static final String MAPS_FRAGMENT_TAG = "AreaDetailsFragment";

    private static final String EXTRA_AREA_NAME = "areaName";

    @Inject AreaDetailsFragment mFragment;

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

        AreaDetailsFragment areaDetailsFragment =
                (AreaDetailsFragment) getSupportFragmentManager().
                        findFragmentById(ID_CONTENT_CONTAINER);

        if (areaDetailsFragment == null) {
            areaDetailsFragment = mFragment;

            ActivityUtils.addSupportFragmentToActivity(getSupportFragmentManager(),
                    areaDetailsFragment, ID_CONTENT_CONTAINER, MAPS_FRAGMENT_TAG);
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

        collapsingToolbar.setTitle(name);
        collapsingToolbar.setTitleEnabled(true);

        areaImage.setImageResource(Values.toolbarImageId);
    }
}
