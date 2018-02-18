package com.pointproject.pointproject.ui.userInfo;


import com.pointproject.pointproject.model.User;

import javax.inject.Inject;

public class UserInfoPresenter implements UserInfoContract.Presenter{

    UserInfoContract.View view;

    @Inject
    UserInfoPresenter(){

    }

    @Override
    public void takeView(UserInfoContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        view = null;
    }
}
