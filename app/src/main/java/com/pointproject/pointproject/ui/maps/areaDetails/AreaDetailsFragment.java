package com.pointproject.pointproject.ui.maps.areaDetails;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pointproject.pointproject.AbstractFragment;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.data.Values;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AreaDetailsFragment extends AbstractFragment {

    @BindView(R.id.participants_text_view) TextView participants;
    @BindView(R.id.reward_text_view) TextView revard;
    @BindView(R.id.area_text_view) TextView area;
    @BindView(R.id.time_alive_text_view) TextView timeAlive;
    @BindView(R.id.start_time_text_view) TextView startTime;

    public AreaDetailsFragment() {
        // Required empty public constructor
    }

    //тот самый getInstance
    public static AreaDetailsFragment getInstance(Context context) {
        AreaDetailsFragment fragmentInstance = new AreaDetailsFragment();

        Bundle args = new Bundle();
        fragmentInstance.setArguments(args);
        fragmentInstance.setContext(context);
        return fragmentInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_area_details, container, false);
        ButterKnife.bind(this, view);
        populateMockData();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void populateMockData() {
        participants.setText(Values.participants);
        revard.setText(Values.revard);
        area.setText(Values.area);
        timeAlive.setText(Values.timeAlive);
        startTime.setText(Values.start);
    }
}
