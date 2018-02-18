package com.pointproject.pointproject;

import android.content.Context;
import android.view.View;

import dagger.android.support.DaggerFragment;


public abstract class AbstractFragment extends DaggerFragment {

    protected static final int ID_CONTENT_CONTAINER = R.id.content_container;

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