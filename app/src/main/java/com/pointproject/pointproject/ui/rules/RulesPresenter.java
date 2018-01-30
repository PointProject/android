package com.pointproject.pointproject.ui.rules;

import javax.inject.Inject;

public class RulesPresenter implements RulesContract.Presenter {

    RulesContract.View rulesView;

    @Inject
    RulesPresenter(){

    }

    @Override
    public void takeView(RulesContract.View view) {
        rulesView = view;
    }

    @Override
    public void dropView() {
        rulesView = null;
    }
}
