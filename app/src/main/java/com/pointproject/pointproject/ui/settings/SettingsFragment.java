package com.pointproject.pointproject.ui.settings;


import android.os.Bundle;
import android.preference.PreferenceFragment;


import com.pointproject.pointproject.R;

import javax.inject.Inject;


public class SettingsFragment extends PreferenceFragment {

    public static String TAG = SettingsFragment.class.getSimpleName();

    @Inject
    public SettingsFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }
}
