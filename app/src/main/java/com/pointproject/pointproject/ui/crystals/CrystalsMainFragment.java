package com.pointproject.pointproject.ui.crystals;

import android.content.Context;
import android.os.Bundle;

import com.pointproject.pointproject.AbstractFragment;
import com.pointproject.pointproject.R;

/**
 * Created by xdewnik on 30.12.2017.
 */

public class CrystalsMainFragment extends AbstractFragment {

    private final static int LAYOUT = R.layout.crystals_fragment;

    public static CrystalsMainFragment getInstance(Context context) {
        CrystalsMainFragment fragmentInstance = new CrystalsMainFragment();

        Bundle args = new Bundle();
        fragmentInstance.setArguments(args);
        fragmentInstance.setContext(context);
        return fragmentInstance;
    }



}
