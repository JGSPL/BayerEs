package com.procialize.eventapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.auth0.android.jwt.JWT;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.SharedPreferencesConstant;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.ui.agenda.view.AgendaFragment;
import com.procialize.eventapp.ui.attendee.view.AttendeeFragment;
import com.procialize.eventapp.ui.eventList.view.EventListActivity;
import com.procialize.eventapp.ui.home.view.HomeFragment;
import com.procialize.eventapp.ui.login.view.LoginActivity;
import com.procialize.eventapp.ui.newsfeed.view.NewsFeedFragment;
import com.procialize.eventapp.ui.profile.view.ProfileActivity;
import com.procialize.eventapp.ui.quiz.view.QuizFragment;
import com.procialize.eventapp.ui.speaker.view.SpeakerFragment;

import java.sql.Timestamp;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.procialize.eventapp.Utility.Constant.colorSecondary;
import static com.procialize.eventapp.Utility.Constant.colorunselect;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int RequestPermissionCode = 7;
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

/*        if (CheckingPermissionIsEnabledOrNot()) {
//            Toast.makeText(MainActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
        }
        else {
            //Calling method to enable permission.
            RequestMultiplePermission();
        }*/

        String profilePic = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_PROFILE_PIC);
        String fName = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_FNAME);
        String lName = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_LNAME);
        String designation = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_DESIGNATION);
        String city = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_CITY);
        //
        LinearLayout outer = findViewById(R.id.my);
        ImageView iv_profile = outer.findViewById(R.id.iv_profile);
        TextView tv_name = outer.findViewById(R.id.tv_name);
        ImageView iv_edit = outer.findViewById(R.id.iv_edit);
        iv_edit.setOnClickListener(this);
        TextView tv_designation = outer.findViewById(R.id.tv_designation);
        tv_name.setText(fName + " " + lName);
        tv_designation.setText(designation + " - " + city);

        Glide.with(MainActivity.this)
                .load(profilePic)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(iv_profile);

        setUpToolbar();
        setUpNavDrawer();

        //String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjYiLCJmaXJzdF9uYW1lIjoiQXBhcm5hIiwibWlkZGxlX25hbWUiOiIiLCJsYXN0X25hbWUiOiJCYWRoYW4iLCJtb2JpbGUiOiI4ODMwNDE2NzkwIiwiZW1haWwiOiJhcGFybmFAcHJvY2lhbGl6ZS5pbiIsInJlZnJlc2hfdG9rZW4iOiJjYWU1MzljNGViMzcwMTI4ZTc3MDIwY2M0ZTdmOTRlMmRhZDIxYjUwIiwidXNlcl90eXBlIjoiQSIsInZlcmlmeV9vdHAiOiIxIiwicHJvZmlsZV9waWMiOiIgaHR0cHM6XC9cL3N0YWdlLWFkbWluLnByb2NpYWxpemUubGl2ZVwvYmFzZWFwcFwvdXBsb2Fkc1wvdXNlclwvZGVmYXVsdC5wbmciLCJpc19nb2QiOiIwIiwidGltZSI6MTU5OTI4MjIxNCwiZXhwaXJ5X3RpbWUiOjE1OTkyODU4MTR9.kXZylcC-mT6sv94wGQJbzyEhTAF63plIsglnU0GFZOQ";
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjYiLCJmaXJzdF9uYW1lIjoiQXBhcm5hIiwibWlkZGxlX25hbWUiOiIiLCJsYXN0X25hbWUiOiJCYWRoYW4iLCJtb2JpbGUiOiI4ODMwNDE2NzkwIiwiZW1haWwiOiJhcGFybmFAcHJvY2lhbGl6ZS5pbiIsInJlZnJlc2hfdG9rZW4iOiIxYTE0NDljYzM5ZDVhZjFlMTkwYzRkNGQ3ZGJmMzc2ZGE4NmQ3ZjE2IiwidXNlcl90eXBlIjoiQSIsInZlcmlmeV9vdHAiOiIxIiwicHJvZmlsZV9waWMiOiJodHRwczpcL1wvc3RhZ2UtYWRtaW4ucHJvY2lhbGl6ZS5saXZlXC9iYXNlYXBwXC91cGxvYWRzXC91c2VyXC8xNTk5NTczNjM0ODMzNC5qcGciLCJpc19nb2QiOiIwIiwidGltZSI6MTU5OTY0NDg3MCwiZXhwaXJ5X3RpbWUiOjE1OTk2NDg0NzB9.2wPbwyM65sb0ipQKMZgbTQlBtPNEhIjzHCdKR6VXgRg";
        JWT jwt = new JWT(token);
        //String mobile = jwt.getClaim("mobile").asString();
        String first_name = jwt.getClaim("first_name").asString();
        String middle_name = jwt.getClaim("middle_name").asString();
        String last_name = jwt.getClaim("last_name").asString();
        String mobile = jwt.getClaim("mobile").asString();
        String email = jwt.getClaim("email").asString();
        String refresh_token = jwt.getClaim("refresh_token").asString();
        String user_type = jwt.getClaim("user_type").asString();
        String verify_otp = jwt.getClaim("verify_otp").asString();
        String profile_pic = jwt.getClaim("profile_pic").asString();
        String is_god = jwt.getClaim("is_god").asString();
        String time = jwt.getClaim("time").asString();
        String expiry_time = jwt.getClaim("expiry_time").asString();

        Timestamp timestamp_open = new Timestamp(Long.parseLong(time));
        Timestamp timestamp_expiry = new Timestamp(Long.parseLong(expiry_time));

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
                    case R.id.navigation_logout:
                        //Logout from app
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finishAffinity();
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
        Utility.createShortSnackBar(mDrawerLayout, "Please click BACK again to exit");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_edit:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
        }
    }
}