package com.pointproject.pointproject;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.pointproject.pointproject.model.User;
import com.pointproject.pointproject.ui.crystals.CrystalsActivity;
import com.pointproject.pointproject.ui.leaders.LeadersActivity;
import com.pointproject.pointproject.ui.login.LoginActivity;
import com.pointproject.pointproject.ui.maps.MapsActivity;
import com.pointproject.pointproject.ui.rules.RulesActivity;
import com.pointproject.pointproject.ui.settings.SettingsActivity;
import com.pointproject.pointproject.ui.userInfo.UserInfoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;
import me.leolin.shortcutbadger.ShortcutBadger;

import static com.pointproject.pointproject.data.Constants.KEY_TOKEN;
import static com.pointproject.pointproject.data.Constants.KEY_USER;
import static com.pointproject.pointproject.data.Constants.NAME_SHARED_PREFERENCES;

public abstract class AbstractActivity extends DaggerAppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected static final int LAYOUT = R.layout.activity_maps;
    protected static final int ID_CONTENT_CONTAINER = R.id.content_container;

    protected static User currentUser;

    //TODO сделать ебалу с demins для hdpi,mdpi etc

    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private TextView navHeaderName;
    private ImageView navHeaderImage;
    private ImageButton navHeaderCrystal;

    private ActionBarDrawerToggle mDrawerToggle;

//    private TextView badgeCrystalItem;

//    TODO Delete this afterwards
    private static int mockBadgeCounter = 1;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        ButterKnife.bind(this);

        SharedPreferences sp = getBaseContext()
                .getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String login = sp.getString(KEY_USER, "");
        String token = sp.getString(KEY_TOKEN, "");

        if(login.isEmpty() || token.isEmpty()){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);

//        badgeCrystalItem = (TextView) navigationView.getMenu().
//                findItem(R.id.menu_crystals).getActionView();

        navHeaderName = navigationView.getHeaderView(0)
                .findViewById(R.id.nav_header_name);

        navHeaderCrystal = navigationView.getHeaderView(0)
                .findViewById(R.id.nav_header_crystal);

        navHeaderImage = navigationView.getHeaderView(0)
                .findViewById(R.id.nav_header_image_view);

//      display burger icon on toolbar
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        setupDrawer();
        if(currentUser != null)
            setupDrawerUser(currentUser);
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
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //sync drawer state with burger icon state
        mDrawerToggle.syncState();


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    //        sync state when configuration changed(landscape mode, etc)
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //check if burger was clicked
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigationView.postDelayed(() -> {
            int itemId = item.getItemId();
            switch(itemId){
                case R.id.menu_play:
                    startActivity(new Intent(this, MapsActivity.class));
                    break;
                case R.id.menu_settings:
                    startActivity(new Intent(this, SettingsActivity.class));
                    break;
                case R.id.menu_leaders:
                    startActivity(new Intent(this, LeadersActivity.class));
                    break;
                case R.id.menu_rules:
                    startActivity(new Intent(this, RulesActivity.class));
                    break;
                case R.id.menu_exit:
                    SharedPreferences prefs = getBaseContext().
                            getSharedPreferences(NAME_SHARED_PREFERENCES,Context.MODE_PRIVATE);
                    prefs.edit().putString(KEY_TOKEN, "").apply();
                    prefs.edit().putString(KEY_USER, "").apply();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                    return;
                case R.id.menu_mock_notification:
                    mockNotification();
                    break;
                default:
//            TODO Commented only for menu mock notification presentation. Uncomment finish() line
//                  finish();
                    DrawerLayout drawer = findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    drawerLayout.closeDrawer(GravityCompat.START);
            }
        }, 300);
        return true;
    }

    void selectBottomNavigationBarItem(int itemId) {
        Menu menu = navigationView.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);

            boolean shouldBeChecked = item.getItemId() == itemId;
            if (shouldBeChecked) {
                item.setChecked(true);

                setToolbarTitle(item.getTitle());

                break;
            }
        }
    }

    protected abstract int getContentViewId();

    protected abstract int getNavigationMenuItemId();

    protected void setToolbarTitle(CharSequence title) {
        assert getSupportActionBar()!=null;
        getSupportActionBar().setTitle(title);
    }

    protected void setupDrawerUser(User user){
        currentUser = user;

        navHeaderName.setText(user.getLogin());

        navHeaderImage.setOnClickListener(v -> {

            Intent intent = UserInfoActivity.getIntent(this, user);
            startActivity(intent);
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
        R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(mDrawerToggle);
        navHeaderCrystal.setOnClickListener(v ->
                startActivity(new Intent(getBaseContext(), CrystalsActivity.class)));
    }

    private void updateNavigationBarState(){
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

/**   mock notification for presentation purpose only; show counter on app launcher */
    private void mockNotification(){
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(getApplicationContext())
                .setContentTitle("Test app launcher counter")
                .setContentText("Minimize app and look at app launcher")
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
