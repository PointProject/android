package com.pointproject.pointproject;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.pointproject.pointproject.ui.crystals.CrystalsActivity;
import com.pointproject.pointproject.ui.maps.MapsActivity;
import com.pointproject.pointproject.ui.settings.SettingsActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leolin.shortcutbadger.ShortcutBadger;

public abstract class AbstractActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    protected static final int LAYOUT = R.layout.activity_maps;

    //TODO сделать ебалу с demins для hdpi,mdpi etc

    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;

    @BindView(R.id.counter_application) TextView counterApplication;
    @BindView(R.id.counter_race) TextView counterRace;

    private TextView badgeCrystalItem;

//    TODO Delete this afterwards
    private long mockTimer = 10_800_000;
    private static int mockBadgeCounter = 1;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        ButterKnife.bind(this);
        navigationView.setNavigationItemSelectedListener(this);

        badgeCrystalItem = (TextView) navigationView.getMenu().
                findItem(R.id.menu_crystals).getActionView();

        setupDrawer();

        startFakeCounter();
    }


    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }


    @Override
    public void onPause() {
        super.onPause();
        //animation
     //   overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    protected void onStop() {
//        Remove fake badge
        ShortcutBadger.removeCount(AbstractActivity.this);

        super.onStop();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigationView.postDelayed(() -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_play)
                startActivity(new Intent(this, MapsActivity.class));
            if(itemId == R.id.menu_settings)
                startActivity(new Intent(this, SettingsActivity.class));
            if(itemId == R.id.menu_crystals)
                startActivity(new Intent(this, CrystalsActivity.class));

            if(itemId == R.id.menu_mock_notification)
                mockNotification();

//            TODO Commented only for menu mock notification presentation. Uncomment finish() line
//            finish();
            drawerLayout.closeDrawer(GravityCompat.START);
        }, 300);
        return true;
    }

    @OnClick(R.id.button_menu_burger)
    public void burgerClicked(View view){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    void selectBottomNavigationBarItem(int itemId) {
        Menu menu = navigationView.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            boolean shouldBeChecked = item.getItemId() == itemId;
            if (shouldBeChecked) {
                item.setChecked(true);
                break;
            }
        }
    }

    protected abstract int getContentViewId();

    protected abstract int getNavigationMenuItemId();

    private void setupDrawer() {
//        add badges to drawer items
        //Gravity property aligns the text
        badgeCrystalItem.setGravity(Gravity.CENTER_VERTICAL);
        badgeCrystalItem.setTypeface(null, Typeface.BOLD);
        badgeCrystalItem.setTextColor(getResources().getColor(R.color.colorAccent));
        badgeCrystalItem.setText("69");
    }

    private void updateNavigationBarState(){
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
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

/**   mock notification for presentation purpose only; show counter on app launcher */
    private void mockNotification(){
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(getApplicationContext())
                .setContentTitle("")
                .setContentText("")
                .setSmallIcon(R.mipmap.ic_launcher_round);

        //Vibration
        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        //LED
        builder.setLights(Color.YELLOW, 3000, 3000);
        //Sound
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

        Notification notification = builder.build();
        ShortcutBadger.applyCount(AbstractActivity.this, mockBadgeCounter);
        ShortcutBadger.applyNotification(getApplicationContext(), notification, mockBadgeCounter);
        mockBadgeCounter++;
        assert mNotificationManager != null;
        mNotificationManager.notify(0, notification);
    }
}
