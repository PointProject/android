package com.pointproject.pointproject;

import android.content.Context;
import android.view.View;

import dagger.android.support.DaggerFragment;


public abstract class AbstractFragment extends DaggerFragment {

    private String title;

    protected Context context;
    protected View view;


    protected void setContext(Context context){
        this.context = context;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}