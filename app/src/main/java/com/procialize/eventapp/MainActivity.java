package com.procialize.eventapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.SharedPreferencesConstant;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.session.SessionManager;
import com.procialize.eventapp.ui.agenda.view.AgendaFragment;
import com.procialize.eventapp.ui.attendee.view.AttendeeFragment;
import com.procialize.eventapp.ui.eventList.view.EventListActivity;
import com.procialize.eventapp.ui.home.view.HomeFragment;
import com.procialize.eventapp.ui.login.view.LoginActivity;
import com.procialize.eventapp.ui.newsfeed.view.NewsFeedFragment;
import com.procialize.eventapp.ui.profile.view.ProfileActivity;
import com.procialize.eventapp.ui.quiz.view.QuizFragment;
import com.procialize.eventapp.ui.speaker.view.SpeakerFragment;

import java.util.HashMap;
import java.util.Map;

import static com.procialize.eventapp.Utility.Constant.colorunselect;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_LOGO;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.IS_LOGIN;


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
    TableRow tr_switch_event;
    LinearLayout ll_main;
    DatabaseReference mDatabaseReference;
    FirebaseAuth mauth;
    private DatabaseReference mDatabase;

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
        ll_main = findViewById(R.id.ll_main);
        mauth=FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabase=FirebaseDatabase.getInstance().getReference().child("users");


        CommonFunction.showBackgroundImage(this, ll_main);


        String profilePic = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_PROFILE_PIC);
        String fName = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_FNAME);
        String lName = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_LNAME);
        String designation = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_DESIGNATION);
        String city = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_CITY);
        String email = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_EMAIL);
        String event_id = SharedPreference.getPref(this, EVENT_ID);;
        String attendee_id = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_ATTENDEE_ID);


        LinearLayout outer = findViewById(R.id.my);
        ImageView iv_profile = outer.findViewById(R.id.iv_profile);
        TextView tv_name = outer.findViewById(R.id.tv_name);
        ImageView iv_edit = outer.findViewById(R.id.iv_edit);
        iv_edit.setOnClickListener(this);
        TextView tv_designation = outer.findViewById(R.id.tv_designation);
        tv_name.setText(fName + " " + lName);
        tv_designation.setText(designation + " - " + city);
        String fireEmail;
        if(email.equalsIgnoreCase("")) {
             fireEmail = fName + "_" + attendee_id + "_" + event_id + "@procialize.in";
        }else{
            String[] domains = email.split("@");
            fireEmail = fName + "_" + attendee_id + "_" + event_id+"@" + domains[1];

        }

        //Chat related process
        register_user(fName,fireEmail,"12345678");


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
        tr_switch_event = findViewById(R.id.tr_switch_event);
        tr_switch_event.setOnClickListener(this);

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
            mToolbar.showOverflowMenu();
            headerlogoIv = findViewById(R.id.headerlogoIv);

            String eventLogo = SharedPreference.getPref(MainActivity.this, EVENT_LOGO);
            String eventListMediaPath = SharedPreference.getPref(MainActivity.this, EVENT_LIST_MEDIA_PATH);
            Glide.with(MainActivity.this)
                    .load(eventListMediaPath + eventLogo)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(headerlogoIv);

        }
    }

    private void setUpNavDrawer() {
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_drawer);
            mToolbar.getNavigationIcon().setTint(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)));
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
                            Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4))
                    });

            ColorStateList textColorStates = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_checked},
                            new int[]{android.R.attr.state_checked}
                    },
                    new int[]{
                            Color.parseColor(colorunselect),
                            Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4))
                    });

            navView.setItemIconTintList(iconsColorStates);
            navView.setItemTextColor(textColorStates);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            ActivityCompat.finishAffinity(MainActivity.this);
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
            case R.id.tr_switch_event:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, EventListActivity.class));
                SessionManager.clearCurrentEvent(MainActivity.this);
                SessionManager.logoutUser(MainActivity.this);
                //SharedPreference.clearAllPref(this);
                break;
            case R.id.tv_home:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                break;
            case R.id.tv_profile:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
            case R.id.tr_logout:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                SessionManager.clearCurrentEvent(MainActivity.this);
                SessionManager.logoutUser(MainActivity.this);
                SharedPreference.clearPref(this, AUTHERISATION_KEY);
                SharedPreference.clearPref(this, IS_LOGIN);

                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }

    //-----REGISTERING THE NEW USER------
    private void register_user(final String displayname, final String email, final String password) {

        mauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //------IF USER IS SUCCESSFULLY REGISTERED-----
                if(task.isSuccessful()){

                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    final String uid=current_user.getUid();
                    String token_id = FirebaseInstanceId.getInstance().getToken();
                    Map userMap=new HashMap();
                    userMap.put("device_token",token_id);
                    userMap.put("name",displayname);
                    userMap.put("status","Hello Events");
                    userMap.put("image","default");
                    userMap.put("thumb_image","default");
                    userMap.put("online","true");

                    mDatabase.child(uid).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task1) {
                            if(task1.isSuccessful()){

                               // Toast.makeText(getApplicationContext(), "New User is created", Toast.LENGTH_SHORT).show();
                               /* Intent intent=new Intent(MainActivity.this,MainActivity.class);

                                //----REMOVING THE LOGIN ACTIVITY FROM THE QUEUE----
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();*/



                            }
                            else{
                                login_user(email,password);
                              //  Toast.makeText(MainActivity.this, "YOUR NAME IS NOT REGISTERED... MAKE NEW ACCOUNT-- ", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });


                }
                //---ERROR IN ACCOUNT CREATING OF NEW USER---
                else{
                    login_user(email,password);

                    //Toast.makeText(getApplicationContext(), "ERROR REGISTERING USER....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Login User
    private void login_user(String email, String password) {

        //---SIGN IN FOR THE AUTHENTICATE EMAIL-----
        mauth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            //---ADDING DEVICE TOKEN ID AND SET ONLINE TO BE TRUE---
                            //---DEVICE TOKEN IS USED FOR SENDING NOTIFICATION----
                            String user_id=mauth.getCurrentUser().getUid();
                            String token_id= FirebaseInstanceId.getInstance().getToken();
                            Map addValue = new HashMap();
                            addValue.put("device_token",token_id);
                            addValue.put("online","true");

                            //---IF UPDATE IS SUCCESSFULL , THEN OPEN MAIN ACTIVITY---
                            mDatabaseReference.child(user_id).updateChildren(addValue, new DatabaseReference.CompletionListener(){

                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                    if(databaseError==null){

                                        //---OPENING MAIN ACTIVITY---
                                        Log.e("Login : ","Logged in Successfully" );
                                        Toast.makeText(getApplicationContext(), "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                        /*Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();*/
                                    }
                                    else{
                                        Toast.makeText(MainActivity.this, databaseError.toString()  , Toast.LENGTH_SHORT).show();
                                        Log.e("Error is : ",databaseError.toString());

                                    }
                                }
                            });



                        }
                        else{
                            //---IF AUTHENTICATION IS WRONG----
                            Toast.makeText(MainActivity.this, "Wrong Credentials" +
                                    "", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}