package com.pointproject.pointproject.ui.rules;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pointproject.pointproject.AbstractFragment;
import com.pointproject.pointproject.R;

import javax.inject.Inject;


public class RulesFragment extends AbstractFragment implements RulesContract.View{

    private final static int LAYOUT = R.layout.rules_fragment;

    @Inject RulesContract.Presenter presenter;

    @Inject
    public RulesFragment(){

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.takeView(this);
    }

    @Override
    public void onPause() {
        presenter.dropView();
        super.onPause();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT,container,false);
        return view;
    }

}
