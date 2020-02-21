package com.procialize.eventapp;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.procialize.eventapp.ui.agenda.view.AgendaFragment;
import com.procialize.eventapp.ui.attendee.view.AttendeeFragment;
import com.procialize.eventapp.ui.home.HomeFragment;
import com.procialize.eventapp.ui.speaker.view.SpeakerFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    BottomNavigationView navView;
    FrameLayout mContentFrame;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.bottom_navigation);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        mNavigationView = findViewById(R.id.nav_view);
        mContentFrame = findViewById(R.id.fragment_frame);
        mToolbar = findViewById(R.id.toolbar);


        setUpToolbar();
        setUpNavDrawer();

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_frame, HomeFragment.newInstance(), "")
                                .commit();
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    case R.id.navigation_eventinfo:

                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    default:
                        return true;
                }
            }
        });


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_frame, HomeFragment.newInstance(), "")
                .commit();

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        // Switch to page one
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_frame, HomeFragment.newInstance(), "")
                                .commit();
                        break;
                    case R.id.navigation_agenda:
                        // Switch to page two
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_frame, AgendaFragment.newInstance(), "")
                                .commit();
                        break;
                    case R.id.navigation_attendee:
                        // Switch to page three
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_frame, AttendeeFragment.newInstance(), "")
                                .commit();
                        break;
                    case R.id.navigation_speaker:
                        // Switch to page four
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_frame, SpeakerFragment.newInstance(), "")
                                .commit();
                        break;
                }

                return true;
            }
        });
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    private void setUpNavDrawer() {
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_drawer);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

    }

}