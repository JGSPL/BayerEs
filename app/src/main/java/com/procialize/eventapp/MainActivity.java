package com.procialize.eventapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.Database.EventAppDB;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.Utility.CommonFirebase;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.SharedPreferencesConstant;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.session.SessionManager;
import com.procialize.eventapp.ui.agenda.view.AgendaFragment;
import com.procialize.eventapp.ui.attendee.model.Attendee;
import com.procialize.eventapp.ui.attendee.model.FetchAttendee;
import com.procialize.eventapp.ui.attendee.view.AttendeeFragment;
import com.procialize.eventapp.ui.attendee.viewmodel.AttendeeDatabaseViewModel;
import com.procialize.eventapp.ui.attendee.viewmodel.AttendeeViewModel;
import com.procialize.eventapp.ui.eventList.view.EventListActivity;
import com.procialize.eventapp.ui.eventinfo.view.EventInfoActivity;
import com.procialize.eventapp.ui.home.view.HomeFragment;
import com.procialize.eventapp.ui.livepoll.view.LivePollActivity;
import com.procialize.eventapp.ui.login.view.LoginActivity;
import com.procialize.eventapp.ui.newsfeed.view.NewsFeedFragment;
import com.procialize.eventapp.ui.profile.model.Profile;
import com.procialize.eventapp.ui.profile.model.ProfileDetails;
import com.procialize.eventapp.ui.profile.view.ProfileActivity;
import com.procialize.eventapp.ui.profile.viewModel.ProfileActivityViewModel;
import com.procialize.eventapp.ui.quiz.view.QuizListingActivity;
import com.procialize.eventapp.ui.speaker.view.SpeakerFragment;
import com.yanzhenjie.album.mvp.BaseFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.eventapp.Constants.Constant.FOLDER_DIRECTORY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.ATTENDEE_STATUS;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_LOGO;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.IS_LOGIN;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_ATTENDEE_ID;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_CITY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_COMPANY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_DESIGNATION;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_EMAIL;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_FNAME;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_LNAME;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_MOBILE;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_PASSWORD;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_PROFILE_PIC;


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
    TableRow tr_switch_event, tr_home, tr_profile, tr_logout, tr_event_info, tr_quiz, tr_live_poll;
    TextView txt_version;
    LinearLayout ll_main;
    DatabaseReference mDatabaseReference;
    FirebaseAuth mauth;
    private DatabaseReference mDatabase;
    APIService updateApi;
    MutableLiveData<LoginOrganizer> chatUpdate = new MutableLiveData<>();
    String api_token, eventid;
    String fName;
    String fireEmail;
    MenuItem menuItem;
    String storeFireid, storeFirename, stoeUsername;
    Menu mMenu;

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
        mauth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(this, EVENT_ID);

        CommonFirebase.crashlytics("MainActivity", api_token);
        CommonFirebase.firbaseAnalytics(this, "MainActivity", api_token);

        String profilePic = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_PROFILE_PIC);
        fName = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_FNAME);
        String lName = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_LNAME);
        String designation = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_DESIGNATION);
        String city = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_CITY);
        String email = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_EMAIL);
        String event_id = SharedPreference.getPref(this, EVENT_ID);
        String attendee_id = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_ATTENDEE_ID);
        String tot_event = SharedPreference.getPref(this, SharedPreferencesConstant.TOTAL_EVENT);
        storeFireid = SharedPreference.getPref(this, SharedPreferencesConstant.FIREBASE_ID);
        storeFirename = SharedPreference.getPref(this, SharedPreferencesConstant.FIREBASE_NAME);
        stoeUsername = SharedPreference.getPref(this, SharedPreferencesConstant.FIREBASEUSER_NAME);


        getProfileDetails();
        CommonFunction.saveBackgroundImage(MainActivity.this, SharedPreference.getPref(this, SharedPreferencesConstant.EVENT_BACKGROUD));
        CommonFunction.showBackgroundImage(this, ll_main);

        LinearLayout outer = findViewById(R.id.my);
        ImageView iv_profile = outer.findViewById(R.id.iv_profile);
        TextView tv_name = outer.findViewById(R.id.tv_name);
        ImageView iv_edit = outer.findViewById(R.id.iv_edit);
        iv_edit.setOnClickListener(this);
        TextView tv_designation = outer.findViewById(R.id.tv_designation);
        tv_name.setText(fName + " " + lName);
        tv_designation.setText(designation + " - " + city);


        if ((stoeUsername != null) && !(stoeUsername.equalsIgnoreCase(""))) {
            fireEmail = stoeUsername;
        } else {
            if (email.equalsIgnoreCase("")) {
                fireEmail = fName + "_" + attendee_id + "_" + event_id + "@procialize.in";
            } else {
                String[] domains = email.split("@");
                fireEmail = fName + "_" + attendee_id + "_" + event_id + "@" + domains[1];

            }
        }

        //Chat related process
        if (ConnectionDetector.getInstance(this).isConnectingToInternet()) {
            if (storeFireid.equalsIgnoreCase("0")) {
                register_user(fName, fireEmail, "12345678");

            } else {
                if (stoeUsername != null) {
                    normal_login_user(stoeUsername, "12345678");

                } else {
                    register_user(fName, fireEmail, "12345678");

                }
            }
        }


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
        tr_home = findViewById(R.id.tr_home);
        tr_profile = findViewById(R.id.tr_profile);
        tr_event_info = findViewById(R.id.tr_event_info);
        tr_logout = findViewById(R.id.tr_logout);
        txt_version = findViewById(R.id.txt_version);
        tr_live_poll = findViewById(R.id.tr_live_poll);
        tr_quiz = findViewById(R.id.tr_quiz);

        txt_version.setText(BuildConfig.VERSION_NAME);
        tr_switch_event.setOnClickListener(this);
        tr_home.setOnClickListener(this);
        tr_profile.setOnClickListener(this);
        tr_event_info.setOnClickListener(this);
        tr_logout.setOnClickListener(this);
        tr_live_poll.setOnClickListener(this);
        tr_quiz.setOnClickListener(this);

        if (tot_event.equalsIgnoreCase("1")) {
            tr_switch_event.setVisibility(View.GONE);
        } else {
            tr_switch_event.setVisibility(View.VISIBLE);
        }


       /* mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
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


                    default:
                        return true;
                }
            }
        });*/

        //
        NewsFeedFragment newsFeedFragment = NewsFeedFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("isFrom", "MainActivity");
        newsFeedFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_frame, newsFeedFragment, "")
                .commit();

        //To set icon on agenda when live streaming is going on
        /*BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) navView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(1);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(this)
                .inflate(R.layout.notification_badge, itemView, true);
       */
        /*itemView.removeViewAt(itemView.getChildCount()-1);*/

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                menuItem = item;

                switch (item.getItemId()) {

                    case R.id.navigation_home:
                        // Switch to page one
                        NewsFeedFragment myFragment = (NewsFeedFragment) getSupportFragmentManager().findFragmentByTag("NewsFeed");
                        if (myFragment != null && myFragment.isVisible()) {
                        } else {
                            JzvdStd.releaseAllVideos();
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_frame, NewsFeedFragment.newInstance(), "NewsFeed")
                                    .commit();
                        }
                        break;
                    case R.id.navigation_agenda:
                        // Switch to page two
                        JzvdStd.releaseAllVideos();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_frame, AgendaFragment.newInstance(), "")
                                .commit();
                        break;
                    case R.id.navigation_attendee:
                        // Switch to page three
                        JzvdStd.releaseAllVideos();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_frame, AttendeeFragment.newInstance(), "")
                                .commit();
                        break;
                    case R.id.navigation_speaker:
                        // Switch to page four
                        JzvdStd.releaseAllVideos();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_frame, SpeakerFragment.newInstance(), "")
                                .commit();
                        break;
                    case R.id.navigation_logout:
                        //Logout from app
                        JzvdStd.releaseAllVideos();
                        EventAppDB.getDatabase(MainActivity.this).profileUpdateDao().deleteData();
                        EventAppDB.getDatabase(MainActivity.this).newsFeedDao().deleteNewsFeed();
                        EventAppDB.getDatabase(MainActivity.this).newsFeedDao().deleteNewsFeedMedia();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finishAffinity();
                        break;

                }

                return true;
            }
        });

        getAttendeeAndInsertIntoDB();
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
            headerlogoIv.setOnClickListener(this);

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
                            //Color.parseColor(colorunselect),
                            Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)),
                            Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4))
                    });

            ColorStateList textColorStates = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_checked},
                            new int[]{android.R.attr.state_checked}
                    },
                    new int[]{
                            //Color.parseColor(colorunselect),
                            Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)),
                            Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4))
                    });

            navView.setItemIconTintList(iconsColorStates);
//            navView.setItemTextColor(textColorStates);
//            navView.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_1)));
        }
    }

    @Override
    public void onBackPressed() {

        JzvdStd.releaseAllVideos();
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
            case R.id.headerlogoIv:
                JzvdStd.releaseAllVideos();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, EventInfoActivity.class));
                break;
            case R.id.iv_edit:
                JzvdStd.releaseAllVideos();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
            case R.id.tr_switch_event:
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/" + FOLDER_DIRECTORY);
                if (!myDir.exists()) {
                    myDir.mkdirs();
                }
                String name = "background.jpg";
                File fdelete = new File(Uri.parse(myDir + "/" + name).getPath());
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        System.out.println("file Deleted :" + Uri.parse(myDir + "/" + name).getPath());
                    } else {
                        System.out.println("file not Deleted :" + Uri.parse(myDir + "/" + name).getPath());
                    }
                }
                JzvdStd.releaseAllVideos();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                SessionManager.clearCurrentEvent(MainActivity.this);
                SessionManager.logoutUser(MainActivity.this);
                //EventAppDB.getDatabase(MainActivity.this).profileUpdateDao().deleteData();
                EventAppDB.getDatabase(MainActivity.this).newsFeedDao().deleteNewsFeed();
                EventAppDB.getDatabase(MainActivity.this).newsFeedDao().deleteNewsFeedMedia();
                startActivity(new Intent(MainActivity.this, EventListActivity.class));
                //SharedPreference.clearAllPref(this);
                break;
            case R.id.tr_home:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                break;
            case R.id.tr_profile:
                JzvdStd.releaseAllVideos();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
            case R.id.tr_event_info:
                JzvdStd.releaseAllVideos();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, EventInfoActivity.class));
                /*getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_frame, EventInfoActivity.newInstance(), "")
                        .commit();*/
                break;

            case R.id.tr_quiz:
                JzvdStd.releaseAllVideos();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, QuizListingActivity.class));
                /*getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_frame, EventInfoActivity.newInstance(), "")
                        .commit();*/
                break;

            case R.id.tr_live_poll:
                JzvdStd.releaseAllVideos();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, LivePollActivity.class));
                break;
            case R.id.tr_logout:
                JzvdStd.releaseAllVideos();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                SessionManager.clearCurrentEvent(MainActivity.this);
                SessionManager.logoutUser(MainActivity.this);
                SharedPreference.clearPref(this, AUTHERISATION_KEY);
                SharedPreference.clearPref(this, IS_LOGIN);
                EventAppDB.getDatabase(MainActivity.this).newsFeedDao().deleteNewsFeedMedia();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }

    //-----REGISTERING THE NEW USER------
    private void register_user(final String displayname, final String email, final String password) {

        mauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //------IF USER IS SUCCESSFULLY REGISTERED-----
                if (task.isSuccessful()) {

                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    final String uid = current_user.getUid();
                    String token_id = FirebaseInstanceId.getInstance().getToken();
                    Map userMap = new HashMap();
                    userMap.put("device_token", token_id);
                    userMap.put("name", displayname);
                    userMap.put("status", "Hello Events");
                    userMap.put("image", "default");
                    userMap.put("thumb_image", "default");
                    userMap.put("online", "true");

                    mDatabase.child(uid).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task1) {
                            if (task1.isSuccessful()) {

                                // Toast.makeText(getApplicationContext(), "New User is created", Toast.LENGTH_SHORT).show();
                               /* Intent intent=new Intent(MainActivity.this,MainActivity.class);

                                //----REMOVING THE LOGIN ACTIVITY FROM THE QUEUE----
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();*/


                            } else {
                                login_user(email, password);
                                //  Toast.makeText(MainActivity.this, "YOUR NAME IS NOT REGISTERED... MAKE NEW ACCOUNT-- ", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });


                }
                //---ERROR IN ACCOUNT CREATING OF NEW USER---
                else {
                    login_user(email, password);

                    //Toast.makeText(getApplicationContext(), "ERROR REGISTERING USER....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Login User
    private void login_user(String email, String password) {

        //---SIGN IN FOR THE AUTHENTICATE EMAIL-----
        mauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            //---ADDING DEVICE TOKEN ID AND SET ONLINE TO BE TRUE---
                            //---DEVICE TOKEN IS USED FOR SENDING NOTIFICATION----
                            String user_id = mauth.getCurrentUser().getUid();
                            String token_id = FirebaseInstanceId.getInstance().getToken();
                            Map addValue = new HashMap();
                            addValue.put("device_token", token_id);
                            addValue.put("online", "true");

                            //---IF UPDATE IS SUCCESSFULL , THEN OPEN MAIN ACTIVITY---
                            mDatabaseReference.child(user_id).updateChildren(addValue, new DatabaseReference.CompletionListener() {

                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                    if (databaseError == null) {

                                        //---OPENING MAIN ACTIVITY---
                                        Log.e("Login : ", "Logged in Successfully");
                                        Utility.createShortSnackBar(ll_main, "Logged in Successfully");
                                        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        getChatUpdate(api_token, eventid, currentuser, fireEmail, fName, "0");

                                    } else {
                                        Toast.makeText(MainActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                                        Log.e("Error is : ", databaseError.toString());

                                    }
                                }
                            });


                        } else {
                            //---IF AUTHENTICATION IS WRONG----
                           /* Toast.makeText(MainActivity.this, "Wrong Credentials" +
                                    "", Toast.LENGTH_SHORT).show();*/
                        }
                    }
                });
    }

    //Normal Login User
    private void normal_login_user(String email, String password) {

        //---SIGN IN FOR THE AUTHENTICATE EMAIL-----
        mauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            //---ADDING DEVICE TOKEN ID AND SET ONLINE TO BE TRUE---
                            //---DEVICE TOKEN IS USED FOR SENDING NOTIFICATION----
                            String user_id = mauth.getCurrentUser().getUid();
                            String token_id = FirebaseInstanceId.getInstance().getToken();
                            Map addValue = new HashMap();
                            addValue.put("device_token", token_id);
                            addValue.put("online", "true");

                            //---IF UPDATE IS SUCCESSFULL , THEN OPEN MAIN ACTIVITY---
                            mDatabaseReference.child(user_id).updateChildren(addValue, new DatabaseReference.CompletionListener() {

                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                    if (databaseError == null) {

                                        //---OPENING MAIN ACTIVITY---
                                        Log.e("Login : ", "Logged in Successfully");
                                        //   Utility.createShortSnackBar(ll_main,"Logged in Successfully");
                                        // String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        // getChatUpdate(api_token,eventid,currentuser,stoeUsername,storeFirename,"1");

                                    } else {
                                        Toast.makeText(MainActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                                        Log.e("Error is : ", databaseError.toString());

                                    }
                                }
                            });


                        } else {
                            //---IF AUTHENTICATION IS WRONG----
                               /* Toast.makeText(MainActivity.this, "Wrong Credentials" +
                                        "", Toast.LENGTH_SHORT).show();*/
                        }
                    }
                });
    }

    private void getAttendeeAndInsertIntoDB() {
        try {
            final AttendeeDatabaseViewModel attendeeDatabaseViewModel = ViewModelProviders.of(this).get(AttendeeDatabaseViewModel.class);

            if (ConnectionDetector.getInstance(MainActivity.this).isConnectingToInternet()) {
                final AttendeeViewModel attendeeViewModel = ViewModelProviders.of(this).get(AttendeeViewModel.class);

                attendeeViewModel.getAttendee(api_token, eventid, "", "1", "10000");
                attendeeViewModel.getAttendeeList().observe(this, new Observer<FetchAttendee>() {
                    @Override
                    public void onChanged(FetchAttendee event) {
                        if (event != null) {
                            //List<Attendee> attendeeList = event.getAttandeeList();
                            String strCommentList = event.getDetail();
                            RefreashToken refreashToken = new RefreashToken(MainActivity.this);
                            String data = refreashToken.decryptedData(strCommentList);
                            Gson gson = new Gson();
                            List<Attendee> attendeeList = gson.fromJson(data, new TypeToken<ArrayList<Attendee>>() {
                            }.getType());
                            //Delete All attendee from local db and insert attendee
                            if (attendeeList != null) {
                                attendeeDatabaseViewModel.deleteAllAttendee(MainActivity.this);
                                attendeeDatabaseViewModel.insertIntoDb(MainActivity.this, attendeeList);
                            }
                        }

                        if (attendeeViewModel != null && attendeeViewModel.getAttendeeList().hasObservers()) {
                            attendeeViewModel.getAttendeeList().removeObservers(MainActivity.this);
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getProfileDetails() {
        if (ConnectionDetector.getInstance(MainActivity.this).isConnectingToInternet()) {
            final ProfileActivityViewModel profileActivityViewModel = ViewModelProviders.of(this).get(ProfileActivityViewModel.class);
            profileActivityViewModel.getProfile(api_token, eventid);
            profileActivityViewModel.getProfileDetails().observeForever(new Observer<Profile>() {
                @Override
                public void onChanged(Profile profile) {
                    String strEventList = profile.getProfileDetailsEncrypted();
                    RefreashToken refreashToken = new RefreashToken(MainActivity.this);
                    String data = refreashToken.decryptedData(strEventList);
                    JsonArray jsonArray = new JsonParser().parse(data).getAsJsonArray();
                    ArrayList<ProfileDetails> profileDetails = new Gson().fromJson(jsonArray, new TypeToken<List<ProfileDetails>>() {
                    }.getType());

                    //List<ProfileDetails> profileDetails = profile.getProfileDetails();
                    if (profileDetails.size() > 0) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put(KEY_FNAME, profileDetails.get(0).getFirst_name());
                        map.put(KEY_LNAME, profileDetails.get(0).getLast_name());
                        map.put(KEY_EMAIL, profileDetails.get(0).getEmail());
                        map.put(KEY_PASSWORD, "");
                        map.put(KEY_DESIGNATION, profileDetails.get(0).getDesignation());
                        map.put(KEY_COMPANY, profileDetails.get(0).getCompany_name());
                        map.put(KEY_MOBILE, profileDetails.get(0).getMobile());
                        //map.put(KEY_TOKEN, "");
                        map.put(KEY_CITY, profileDetails.get(0).getCity());
                        //map.put(KEY_GCM_ID, "");
                        map.put(KEY_PROFILE_PIC, profileDetails.get(0).getProfile_picture());
                        map.put(KEY_ATTENDEE_ID, profileDetails.get(0).getAttendee_id());
                        map.put(ATTENDEE_STATUS, profileDetails.get(0).getIs_god());
                        map.put(IS_LOGIN, "true");
                        SharedPreference.putPref(MainActivity.this, map);
                    }

                    if (profileActivityViewModel != null && profileActivityViewModel.getProfileDetails().hasObservers()) {
                        profileActivityViewModel.getProfileDetails().removeObservers(MainActivity.this);
                    }
                }
            });
        }
    }

    //Update Api
    public MutableLiveData<LoginOrganizer> getChatUpdate(final String token, final String event_id, String firebase_id, String firEmail, String firebase_username, String id) {
        updateApi = ApiUtils.getAPIService();

        updateApi.UpdateChatUserInfo(token, event_id, firebase_id, firebase_username, firEmail, id).enqueue(new Callback<LoginOrganizer>() {
            @Override
            public void onResponse(Call<LoginOrganizer> call,
                                   Response<LoginOrganizer> response) {
                if (response.isSuccessful()) {
                    chatUpdate.setValue(response.body());
                    //    Utility.createShortSnackBar(ll_main,"Chat info updated");
                }
            }

            @Override
            public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                chatUpdate.setValue(null);
            }
        });
        return chatUpdate;
    }

}