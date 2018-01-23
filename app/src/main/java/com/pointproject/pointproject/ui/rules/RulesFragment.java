package com.pointproject.pointproject.ui.rules;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pointproject.pointproject.AbstractFragment;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.ui.leaders.LeadersMainFragment;

/**
 * Created by xdewnik on 30.12.2017.
 */

public class RulesFragment extends AbstractFragment {

    private final static int LAYOUT = R.layout.rules_fragment;

    public static RulesFragment getInstance(Context context) {
        RulesFragment fragmentInstance = new RulesFragment();

        Bundle args = new Bundle();
        fragmentInstance.setArguments(args);
        fragmentInstance.setContext(context);
        return fragmentInstance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT,container,false);
        return view;
    }

}
