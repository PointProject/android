package com.pointproject.pointproject.ui.maps;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.pointproject.pointproject.AbstractActivity;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.util.ActivityUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;

public class MapsActivity extends AbstractActivity  {

    @BindView(R.id.counter_application) TextView counterApplication;
    @BindView(R.id.counter_race) TextView counterRace;

    private static final int LAYOUT = R.layout.activity_main_map;
    private static final int NAV_ITEM = R.id.menu_play;
    private static final String TAG = MapsActivity.class.getSimpleName();
    private final static String MAPS_FRAGMENT_TAG = "MapsMainFragment";

    @Inject
    MapsMainFragment mFragment;

//    TODO Delete
    private long mockTimer = 10_800_000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startFakeCounter();

        MapsMainFragment mainFragment = (MapsMainFragment) getSupportFragmentManager()
                .findFragmentById(ID_CONTENT_CONTAINER);

        if(mainFragment == null){
            mainFragment = mFragment;
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    mainFragment,
                    ID_CONTENT_CONTAINER,
                    MAPS_FRAGMENT_TAG);
        }
    }

    @Override
    protected int getContentViewId() {
        return LAYOUT;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return NAV_ITEM;
    }

    private void startFakeCounter(){
        new CountDownTimer(mockTimer, 1_000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String  hmsApplication =  (TimeUnit.MILLISECONDS.toHours(millisUntilFinished))+":"+
                        (TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)))+":"+
                        (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

//                provides hour difference between counters
                long millisUntilFinishedSecondTimer = millisUntilFinished + 3_600_000;

                String  hmsRace =  (TimeUnit.MILLISECONDS.toHours(millisUntilFinishedSecondTimer))+":"+
                        (TimeUnit.MILLISECONDS.toMinutes(millisUntilFinishedSecondTimer) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinishedSecondTimer)))+":"+
                        (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinishedSecondTimer) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinishedSecondTimer)));

                counterApplication.setText(hmsApplication);
                counterRace.setText(hmsRace);

                mockTimer = millisUntilFinished;
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }
}


