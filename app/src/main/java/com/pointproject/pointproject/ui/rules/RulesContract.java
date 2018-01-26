package com.pointproject.pointproject.ui.rules;


import com.pointproject.pointproject.BasePresenter;
import com.pointproject.pointproject.BaseView;

public interface RulesContract {
    interface Presenter extends BasePresenter<View>{

    }
    interface View extends BaseView<Presenter>{

    }
}
