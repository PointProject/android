package com.pointproject.pointproject;


public interface LoginBaseView<T> extends BaseView<T> {
    void resetErrors();

    void showPasswordError(int resId);

    void showLoginError(int resId);

    void showEmptyLoginError();

    void showEmptyPasswordError();

    void showError(int resId);

    void showPhoneError(int resId);
}
