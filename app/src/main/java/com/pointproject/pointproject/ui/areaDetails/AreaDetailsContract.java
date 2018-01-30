package com.pointproject.pointproject.ui.areaDetails;


import com.pointproject.pointproject.BasePresenter;
import com.pointproject.pointproject.BaseView;
import com.pointproject.pointproject.model.RaceModel;


public interface AreaDetailsContract {
    interface Presenter extends BasePresenter<View>{

        void loadAreaDetails();
    }

    interface View extends BaseView<Presenter> {

        void populateAreaDetails(RaceModel raceModel);
    }
}
