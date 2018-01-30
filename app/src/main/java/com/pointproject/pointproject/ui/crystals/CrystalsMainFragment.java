package com.pointproject.pointproject.ui.crystals;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pointproject.pointproject.AbstractFragment;
import com.pointproject.pointproject.R;

import javax.inject.Inject;

public class CrystalsMainFragment extends AbstractFragment implements CrystalsContract.View{

    private final static int LAYOUT = R.layout.crystals_fragment;

    @Inject CrystalsContract.Presenter presenter;

    @Inject
    public CrystalsMainFragment() {

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
        view = inflater.inflate(LAYOUT, container,false);

        return view;
    }

}
