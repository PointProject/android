package com.pointproject.pointproject.ui.login;

import com.pointproject.pointproject.BasePresenter;
import com.pointproject.pointproject.BaseView;

public interface LoginContract {
    interface Presenter extends BasePresenter<View>{

        void checkAccount(String login, String password);

    }

    interface View extends BaseView<Presenter> {
        void resetErrors();

        void showPasswordError();

        void showEmptyLoginError();

        void showInvalidLoginError();

        void loginIn();
    }
}
