package com.pointproject.pointproject;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by xdewnik on 30.12.2017.
 */

public abstract class AbstractFragment extends Fragment {


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