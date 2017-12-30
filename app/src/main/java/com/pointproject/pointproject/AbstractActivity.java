package com.pointproject.pointproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;

import com.pointproject.pointproject.ui.maps.MapsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xdewnik on 30.12.2017.
 */

public abstract class AbstractActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    //TODO сделать ебалу с demins для hdpi,mdpi etc

    @BindView(R.id.nav_view) NavigationView navigationView;

    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }


    @Override
    public void onPause() {
        super.onPause();
        //animation
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        ButterKnife.bind(this);
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

    protected abstract int getContentViewId();

    protected abstract int getNavigationMenuItemId();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigationView.postDelayed(() -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_play)
                startActivity(new Intent(this, MapsActivity.class));
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
