package com.pointproject.pointproject.ui.maps.areaDetails;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pointproject.pointproject.AbstractFragment;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.data.Values;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AreaDetailsFragment extends AbstractFragment {

    @BindView(R.id.toolbar_image)
    ImageView areaImage;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.participants_textview)
    TextView participants;
    @BindView(R.id.revard_textview)
    TextView revard;
    @BindView(R.id.area_textview)
    TextView area;
    @BindView(R.id.time_alive_textview)
    TextView timeAlive;
    @BindView(R.id.start_time_textview)
    TextView startTime;
    @BindView(R.id.toolbar)
    @Nullable
    Toolbar toolbar;

    private static final String KEY_POLYGON_NAME = "polygonName";

    String polygonName;

    public AreaDetailsFragment() {
        // Required empty public constructor
    }

    //тот самый getInstance
    public static AreaDetailsFragment getInstance(Context context, String name) {
        AreaDetailsFragment fragmentInstance = new AreaDetailsFragment();

        Bundle args = new Bundle();
        args.putString(KEY_POLYGON_NAME, name);
        fragmentInstance.setArguments(args);
        fragmentInstance.setContext(context);
        return fragmentInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_area_details, container, false);
        ButterKnife.bind(this, view);
//        setToolbar();
        populateMockData();
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        polygonName = getArguments().getString(KEY_POLYGON_NAME);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void setToolbar()
    {
        collapsingToolbar.setContentScrimColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        collapsingToolbar.setTitle(polygonName);
        collapsingToolbar.setTitleEnabled(true);

        if (toolbar != null)
        {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null)
            {
//                actionBar.setDisplayHomeAsUpEnabled(true);

            }
        } else
        {
            // Don't inflate. Tablet is in landscape mode.
        }
    }

    private void populateMockData() {
        areaImage.setImageResource(Values.toolvarImageId);
        participants.setText(Values.participants);
        revard.setText(Values.revard);
        area.setText(Values.area);
        timeAlive.setText(Values.timeAlive);
        startTime.setText(Values.start);
    }
}
