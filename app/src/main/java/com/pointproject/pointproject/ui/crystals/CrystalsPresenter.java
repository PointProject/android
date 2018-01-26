package com.pointproject.pointproject.ui.crystals;

import javax.inject.Inject;

public class CrystalsPresenter implements CrystalsContract.Presenter {

    CrystalsContract.View crystalsView;

    @Inject
    CrystalsPresenter(){

    }
    @Override
    public void takeView(CrystalsContract.View view) {
        crystalsView = view;
    }

    @Override
    public void dropView() {
        crystalsView = null;
    }
}
