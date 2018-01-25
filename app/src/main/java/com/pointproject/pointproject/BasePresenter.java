package com.pointproject.pointproject;

public interface BasePresenter<T> {

    void takeView(T view);

    void dropView();
}
