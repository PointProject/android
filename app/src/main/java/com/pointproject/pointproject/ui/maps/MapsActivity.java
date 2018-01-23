package com.pointproject.pointproject.ui.maps;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.pointproject.pointproject.AbstractActivity;
import com.pointproject.pointproject.R;

import java.util.concurrent.TimeUnit;

public class MapsActivity extends AbstractActivity  {

//    Toolbar toolbarM;
//    CoordinatorLayout coordinatorLayout;
//    AppBarLayout appComm;
//    AppBarLayout appCount;
    TextView counterApplication;
    TextView counterRace;

    private static final int LAYOUT = R.layout.activity_main_map;
    private static final int NAV_ITEM = R.id.menu_play;
    private static final String TAG = MapsActivity.class.getSimpleName();
    private final static String MAPS_FRAGMENT_TAG = "MapsMainFragment";

//    TODO Delete
    private long mockTimer = 10_800_000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        coordinatorLayout = findViewById(R.id.overview_coordinator_layout);
//        appComm = coordinatorLayout.findViewById(R.id.appbar_common);
//        appCount = drawer.findViewById(R.id.appbar_timers);

//        coordinatorLayout.removeView(appComm);
//        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = vi.inflate(R.layout.appbar_main_map, null);
//        coordinatorLayout.addView(v);

//        toolbarM = findViewById(R.id.toolbar_main_map);
//        appComm = findViewById(R.id.appbar_common);
//        appCount = findViewById(R.id.appbar_timers);
        counterApplication = findViewById(R.id.counter_application);
        counterRace = findViewById(R.id.counter_race);

//        setSupportActionBar(toolbarM);

        startFakeCounter();

        if (savedInstanceState == null) {
            // The Activity is NOT being re-created so we can instantiate a new Fragment
            // and add it to the Activity
            MapsMainFragment fragment = MapsMainFragment.getInstance(this);
            getSupportFragmentManager()
                    .beginTransaction()
                    // It's almost always a good idea to use .replace instead of .add so that
                    // you never accidentally layer multiple Fragments on top of each other
                    // unless of course that's your intention
                    .replace(R.id.content_container, fragment, MAPS_FRAGMENT_TAG)
                    .commit();
        } else {
            // The Activity IS being re-created so we don't need to instantiate the Fragment or add it,
            // but if we need a reference to it, we can use the tag we passed to .replace
            getSupportFragmentManager().findFragmentByTag(MAPS_FRAGMENT_TAG);
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


