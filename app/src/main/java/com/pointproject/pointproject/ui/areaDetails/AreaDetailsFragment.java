package com.pointproject.pointproject.ui.areaDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pointproject.pointproject.AbstractFragment;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.data.Values;
import com.pointproject.pointproject.model.RaceModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AreaDetailsFragment extends AbstractFragment implements AreaDetailsContract.View{

    @BindView(R.id.participants_text_view) TextView participants;
    @BindView(R.id.reward_text_view) TextView reward;
    @BindView(R.id.area_text_view) TextView area;
    @BindView(R.id.time_alive_text_view) TextView timeAlive;
    @BindView(R.id.start_time_text_view) TextView startTime;

    @Inject AreaDetailsContract.Presenter presenter;

    @Inject
    public AreaDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.takeView(this);
        presenter.loadAreaDetails();
    }

    @Override
    public void onPause() {
        presenter.dropView();
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_area_details, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void populateAreaDetails(RaceModel race) {
        participants.setText(getString(R.string.raceParticipantsNumber, race.getParticipants()));
        reward.setText(getString(R.string.raceReward, race.getReward()));
        area.setText(getString(R.string.raceArea, race.getArea()));
        timeAlive.setText(getString(R.string.pointLifetime,race.getTimeAlive()));
        startTime.setText(getString(R.string.startTime, race.getStartTime()));
    }
}
