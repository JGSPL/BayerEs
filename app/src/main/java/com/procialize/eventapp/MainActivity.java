package com.procialize.eventapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.auth0.android.jwt.JWT;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.procialize.eventapp.ui.agenda.view.AgendaFragment;
import com.procialize.eventapp.ui.attendee.view.AttendeeFragment;
import com.procialize.eventapp.ui.home.view.HomeFragment;
import com.procialize.eventapp.ui.newsfeed.view.NewsFeedFragment;
import com.procialize.eventapp.ui.quiz.view.QuizFragment;
import com.procialize.eventapp.ui.speaker.view.SpeakerFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import static com.procialize.eventapp.Utility.Constant.*;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    BottomNavigationView navView;
    FrameLayout mContentFrame;
    Toolbar mToolbar;
    ImageView headerlogoIv;
    RecyclerView rv_side_menu;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.bottom_navigation);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        mNavigationView = findViewById(R.id.nav_view);
        mContentFrame = findViewById(R.id.fragment_frame);
        rv_side_menu = findViewById(R.id.rv_side_menu);
        mToolbar = findViewById(R.id.toolbar);


        setUpToolbar();
        setUpNavDrawer();

        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjYiLCJmaXJzdF9uYW1lIjoiQXBhcm5hIiwibWlkZGxlX25hbWUiOiIiLCJsYXN0X25hbWUiOiJCYWRoYW4iLCJtb2JpbGUiOiI4ODMwNDE2NzkwIiwiZW1haWwiOiJhcGFybmFAcHJvY2lhbGl6ZS5pbiIsInJlZnJlc2hfdG9rZW4iOiJjYWU1MzljNGViMzcwMTI4ZTc3MDIwY2M0ZTdmOTRlMmRhZDIxYjUwIiwidXNlcl90eXBlIjoiQSIsInZlcmlmeV9vdHAiOiIxIiwicHJvZmlsZV9waWMiOiIgaHR0cHM6XC9cL3N0YWdlLWFkbWluLnByb2NpYWxpemUubGl2ZVwvYmFzZWFwcFwvdXBsb2Fkc1wvdXNlclwvZGVmYXVsdC5wbmciLCJpc19nb2QiOiIwIiwidGltZSI6MTU5OTI4MjIxNCwiZXhwaXJ5X3RpbWUiOjE1OTkyODU4MTR9.kXZylcC-mT6sv94wGQJbzyEhTAF63plIsglnU0GFZOQ";
        JWT jwt = new JWT(token);
        String mobile = jwt.getClaim("mobile").asString();
        boolean isExpired = jwt.isExpired(10);

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
                        navView.setVisibility(View.VISIBLE);
                        return true;

                    case R.id.navigation_eventinfo:

                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;


                    case R.id.navigation_quiz:

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_frame, QuizFragment.newInstance(), "")
                                .commit();
                        navView.setVisibility(View.GONE);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    default:
                        return true;
                }
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_frame, NewsFeedFragment.newInstance(), "")
                .commit();

        /*
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_frame, HomeFragment.newInstance(), "")
                .commit();*/

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        // Switch to page one

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_frame, NewsFeedFragment.newInstance(), "")
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
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            headerlogoIv = findViewById(R.id.headerlogoIv);
        }
    }

    private void setUpNavDrawer() {
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_drawer);
            mToolbar.getNavigationIcon().setTint(Color.parseColor(colorSecondary));
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });

            ColorStateList iconsColorStates = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_checked},
                            new int[]{android.R.attr.state_checked}
                    },
                    new int[]{
                            Color.parseColor(colorunselect),
                            Color.parseColor(colorSecondary)
                    });

            ColorStateList textColorStates = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_checked},
                            new int[]{android.R.attr.state_checked}
                    },
                    new int[]{
                            Color.parseColor(colorunselect),
                            Color.parseColor(colorSecondary)
                    });

            navView.setItemIconTintList(iconsColorStates);
            navView.setItemTextColor(textColorStates);
        }

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(mDrawerLayout, "Please click BACK again to exit", Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}