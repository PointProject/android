package com.pointproject.pointproject.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class ActivityUtils {
    public static void addSupportFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                                    @NonNull Fragment fragment, int frameId,
                                                    @Nullable String tag) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment, tag);
        transaction.commit();
    }

    public static void addFragmentToActivity(@NonNull android.app.FragmentManager fragmentManager,
                                             @NonNull android.app.Fragment fragment,
                                             int frameId,
                                             @Nullable String tag){
        android.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment, tag);
        transaction.commit();
    }
}
