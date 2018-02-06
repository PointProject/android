package com.pointproject.pointproject.ui.userInfo;

import com.pointproject.pointproject.BasePresenter;
import com.pointproject.pointproject.BaseView;
import com.pointproject.pointproject.model.User;

public interface UserInfoContract {
    interface Presenter extends BasePresenter<View> {
        void getUserInfo();
    }

    interface View extends BaseView<Presenter> {
        void showUserInfo(User user);
    }
}
