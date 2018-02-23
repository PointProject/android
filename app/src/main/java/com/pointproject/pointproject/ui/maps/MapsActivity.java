package com.pointproject.pointproject.ui.maps;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import com.pointproject.pointproject.AbstractActivity;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.model.User;
import com.pointproject.pointproject.network.ApiClient;
import com.pointproject.pointproject.network.callback.UserCallback;
import com.pointproject.pointproject.network.response.NetworkError;
import com.pointproject.pointproject.util.ActivityUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.pointproject.pointproject.data.Constants.KEY_USER;
import static com.pointproject.pointproject.data.Constants.NAME_SHARED_PREFERENCES;

public class MapsActivity extends AbstractActivity  {

    @BindView(R.id.counter_application) TextView counterApplication;
    @BindView(R.id.counter_race) TextView counterRace;

    private static final int LAYOUT = R.layout.activity_main_map;
    private static final int NAV_ITEM = R.id.menu_play;
    private static final String TAG = MapsActivity.class.getSimpleName();
    private final static String MAPS_FRAGMENT_TAG = "MapsMainFragment";

    @Inject
    MapsMainFragment mFragment;

    @Inject
    ApiClient apiClient;

//    TODO Delete
    private long mockTimer = 10_800_000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getBaseContext()
                .getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String login = sp.getString(KEY_USER, " ");
        if(!login.isEmpty() && !login.equals(" ")) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), login);
            apiClient.secureLogin(requestBody, new UserCallback() {
                @Override
                public void onSuccess(User user) {
                    setupDrawerUser(user);
                }

                @Override
                public void onError(NetworkError error) {
//                Toast.makeText(MapsActivity.this, R.string.user_load_error, Toast.LENGTH_SHORT).show();
                }
            });
        }

        startFakeCounter();

        MapsMainFragment mainFragment = (MapsMainFragment) getSupportFragmentManager()
                .findFragmentById(ID_CONTENT_CONTAINER);

        if(mainFragment == null){
            mainFragment = mFragment;
            ActivityUtils.addSupportFragmentToActivity(getSupportFragmentManager(),
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


