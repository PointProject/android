package com.pointproject.pointproject.ui.leaders;


import javax.inject.Inject;

public class LeadersPresenter implements LeadersContract.Presenter {

    LeadersContract.View leadersView;

    @Inject
    LeadersPresenter(){

    }

    @Override
    public void takeView(LeadersContract.View view) {
        leadersView = view;
    }

    @Override
    public void dropView() {
        leadersView = null;
    }
}
