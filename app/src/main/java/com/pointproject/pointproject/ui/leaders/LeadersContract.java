package com.pointproject.pointproject.ui.leaders;

import com.pointproject.pointproject.BasePresenter;
import com.pointproject.pointproject.BaseView;


public interface LeadersContract {
    interface Presenter extends BasePresenter<View> {

    }
    interface View extends BaseView<Presenter> {

    }
}
