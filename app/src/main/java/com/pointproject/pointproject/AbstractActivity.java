package com.pointproject.pointproject;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.pointproject.pointproject.ui.crystals.CrystalsActivity;
import com.pointproject.pointproject.ui.maps.MapsActivity;
import com.pointproject.pointproject.ui.settings.SettingsActivity;
import com.pointproject.pointproject.ui.settings.SettingsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xdewnik on 30.12.2017.
 */

public abstract class AbstractActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    protected static final int LAYOUT = R.layout.activity_maps;

    //TODO сделать ебалу с demins для hdpi,mdpi etc

    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);


    }



    private void updateNavigationBarState(){
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_crystals:
                startActivity(new Intent(this, CrystalsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected abstract int getContentViewId();

    protected abstract int getNavigationMenuItemId();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigationView.postDelayed(() -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_play)
                startActivity(new Intent(this, MapsActivity.class));
            else if(itemId == R.id.menu_settings){
                startActivity(new Intent(this, SettingsActivity.class));
            }
//                SettingsFragment fragment = SettingsFragment.getInstance();
//                getFragmentManager()
//                        .beginTransaction()
//                        // It's almost always a good idea to use .replace instead of .add so that
//                        // you never accidentally layer multiple Fragments on top of each other
//                        // unless of course that's your intention
//                        .replace(R.id.content_container, fragment)
//                        .addToBackStack(fragment.TAG)
//                        .commit();
//            }
//            else if (itemId == R.id.menu_leaders) {
//                startActivity(new Intent(this, LeadersActivity.class));
//            }
//            else if (itemId == R.id.navigation_notifications) {
//                startActivity(new Intent(this, NotificationsActivity.class));
//            }
            finish();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }, 300);
        return true;
    }
}
