package com.pointproject.pointproject.ui.areaDetails;


import com.pointproject.pointproject.data.Values;
import com.pointproject.pointproject.model.RaceModel;

import javax.inject.Inject;

class AreaDetailsPresenter implements AreaDetailsContract.Presenter {

    @Inject
    AreaDetailsPresenter(){}

    private AreaDetailsContract.View areaDetailsView;

    @Override
    public void takeView(AreaDetailsContract.View view){
        areaDetailsView = view;
    }

    @Override
    public void dropView() {
        areaDetailsView = null;
    }

    @Override
    public void loadAreaDetails() {
        RaceModel raceModel = Values.mockRaceModel;

        areaDetailsView.populateAreaDetails(raceModel);
    }
}
