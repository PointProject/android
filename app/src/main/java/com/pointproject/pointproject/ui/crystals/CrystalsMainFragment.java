package com.pointproject.pointproject.ui.crystals;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pointproject.pointproject.AbstractFragment;
import com.pointproject.pointproject.R;

public class CrystalsMainFragment extends AbstractFragment {

    private final static int LAYOUT = R.layout.crystals_fragment;

    public static CrystalsMainFragment getInstance(Context context) {
        CrystalsMainFragment fragmentInstance = new CrystalsMainFragment();

        Bundle args = new Bundle();
        fragmentInstance.setArguments(args);
        fragmentInstance.setContext(context);
        return fragmentInstance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container,false);

        return view;
    }

}
