package com.pointproject.pointproject.ui.settings;


import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.pointproject.pointproject.R;


public class SettingsFragment extends PreferenceFragment {

    public static String TAG = SettingsFragment.class.getSimpleName();


    public SettingsFragment() {
        // Required empty public constructor
    }

    //тот самый getInstance
    public static SettingsFragment getInstance() {
        SettingsFragment fragmentInstance = new SettingsFragment();

        Bundle args = new Bundle();
        fragmentInstance.setArguments(args);
        return fragmentInstance;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }
}
