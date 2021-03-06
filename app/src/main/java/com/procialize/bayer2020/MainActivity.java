package com.procialize.bayer2020;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
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
import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.Constants.Constant;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.Database.EventAppDB;
import com.procialize.bayer2020.GetterSetter.BaseResponse;
import com.procialize.bayer2020.GetterSetter.LoginOrganizer;
import com.procialize.bayer2020.Utility.CommonFirebase;
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.Utility.KeyboardUtility;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.SharedPreferencesConstant;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.session.SessionManager;
import com.procialize.bayer2020.ui.Contactus.ContactUsActivity;
import com.procialize.bayer2020.ui.EULA.EulaActivity;
import com.procialize.bayer2020.ui.Privacypolicy.PrivacypolicyActivity;
import com.procialize.bayer2020.ui.agenda.model.FetchAgenda;
import com.procialize.bayer2020.ui.agenda.view.AgendaFragment;
import com.procialize.bayer2020.ui.attendee.model.Attendee;
import com.procialize.bayer2020.ui.attendee.model.FetchAttendee;
import com.procialize.bayer2020.ui.attendee.viewmodel.AttendeeDatabaseViewModel;
import com.procialize.bayer2020.ui.attendee.viewmodel.AttendeeViewModel;
import com.procialize.bayer2020.ui.catalogue.view.CatalogueFragment;
import com.procialize.bayer2020.ui.document.view.DocumentActivity;
import com.procialize.bayer2020.ui.eventList.view.EventListActivity;
import com.procialize.bayer2020.ui.eventinfo.view.EventInfoActivity;
import com.procialize.bayer2020.ui.faq.view.FAQActivity;
import com.procialize.bayer2020.ui.livepoll.view.LivePollActivity;
import com.procialize.bayer2020.ui.login.view.LoginActivity;
import com.procialize.bayer2020.ui.loyalityleap.model.FetchRedeemStatusBasicData;
import com.procialize.bayer2020.ui.loyalityleap.model.My_point;
import com.procialize.bayer2020.ui.loyalityleap.model.redeem_history_item;
import com.procialize.bayer2020.ui.loyalityleap.model.redeem_history_status_item;
import com.procialize.bayer2020.ui.loyalityleap.view.LoyalityLeapFragment;
import com.procialize.bayer2020.ui.loyalityleap.view.RedemptionHistoryList;
import com.procialize.bayer2020.ui.newsfeed.view.NewsFeedFragment;
import com.procialize.bayer2020.ui.notification.view.NotificationActivity;
import com.procialize.bayer2020.ui.profile.model.Profile;
import com.procialize.bayer2020.ui.profile.model.ProfileDetails;
import com.procialize.bayer2020.ui.profile.view.ProfileActivity;
import com.procialize.bayer2020.ui.profile.view.ProfilePCOActivity;
import com.procialize.bayer2020.ui.profile.viewModel.ProfileActivityViewModel;
import com.procialize.bayer2020.ui.qa.view.QnADirectActivity;
import com.procialize.bayer2020.ui.quiz.view.QuizListingActivity;
import com.procialize.bayer2020.ui.speaker.view.SpeakerFragment;
import com.procialize.bayer2020.ui.storelocator.view.StoreLocatorActivity;
import com.procialize.bayer2020.ui.storelocator.view.StorelocatorMoveActivity;
import com.procialize.bayer2020.ui.survey.view.SurveyActivity;
import com.procialize.bayer2020.ui.upskill.view.UpskillFragment;
import com.yanzhenjie.album.mvp.BaseFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Constants.Constant.FOLDER_DIRECTORY;
import static com.procialize.bayer2020.Utility.CommonFunction.setNotification;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.ATTENDEE_STATUS;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.ENROLL_LEAP_FLAG;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.IS_LOGIN;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_ATTENDEE_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_CITY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_COMPANY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_DESIGNATION;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_EMAIL;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_FNAME;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_GCM_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_LNAME;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_MOBILE;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_PASSWORD;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_PROFILE_PIC;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_TOKEN;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.USER_TYPE;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.notification_count;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int RequestPermissionCode = 7;
    private DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    BottomNavigationView navView;
    FrameLayout mContentFrame;
    Toolbar mToolbar;
    ImageView headerlogoIv,iv_notification;
    RecyclerView rv_side_menu;
    boolean doubleBackToExitPressedOnce = false;
    TableRow tr_switch_event, tr_home, tr_profile, tr_logout, tr_event_info, tr_quiz, tr_live_poll,
            tr_contact_us, tr_survey, tr_eula, tr_privacy_policy, tr_downloads, tr_faq, tr_storeLocator, tr_qna;
    TextView txt_version;
    LinearLayout ll_main;
    public static LinearLayout ll_notification_count;
    public static TextView tv_notification;
    DatabaseReference mDatabaseReference;
    FirebaseAuth mauth;
    private DatabaseReference mDatabase;
    APIService updateApi;
    MutableLiveData<LoginOrganizer> chatUpdate = new MutableLiveData<>();
    String api_token, eventid;
    String fName;
    String fireEmail;
    MenuItem menuItem;
    String storeFireid, storeFirename, stoeUsername, enrollleapFlag, company, userType;
    Menu mMenu;
    Dialog myDialog;
    MutableLiveData<LoginOrganizer> FetchenleepStatusList = new MutableLiveData<>();
    TextView tv_designation;
    public static NotificationCountReciever notificationCountReciever;
    public static IntentFilter notificationCountFilter;
    TextView tv_city;
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
        ll_notification_count = findViewById(R.id.ll_notification_count);
        tv_notification = findViewById(R.id.tv_notification);
        iv_notification = findViewById(R.id.iv_notification);
        iv_notification.setOnClickListener(this);
        FirebaseApp.initializeApp(this);
        mauth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(this, EVENT_ID);

        Log.e("token===>", api_token);
        CommonFirebase.crashlytics("MainActivity", api_token);
        CommonFirebase.firbaseAnalytics(this, "MainActivity", api_token);
        getProfileDetails();

        String profilePic = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_PROFILE_PIC);
        fName = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_FNAME);
        String lName = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_LNAME);
        String designation = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_DESIGNATION);
        String city = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_CITY);
        String email = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_EMAIL);
        String event_id = SharedPreference.getPref(this, EVENT_ID);
        String attendee_id = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_ATTENDEE_ID);
        String tot_event = SharedPreference.getPref(this, SharedPreferencesConstant.TOTAL_EVENT);
        company = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_COMPANY);
        userType = SharedPreference.getPref(this, USER_TYPE);
        enrollleapFlag = SharedPreference.getPref(this, ENROLL_LEAP_FLAG);

        String notificationCount = SharedPreference.getPref(this, notification_count);
        //tv_notification.setText(notificationCount);


        getNotiCount(this);
        //CommonFunction.saveBackgroundImage(MainActivity.this, SharedPreference.getPref(this, SharedPreferencesConstant.EVENT_BACKGROUD));
//        CommonFunction.showBackgroundImage(this, ll_main);

        try {
            notificationCountReciever = new NotificationCountReciever();
            notificationCountFilter = new IntentFilter(Constant.BROADCAST_ACTION_FOR_NOTIFICATION_COUNT);
            LocalBroadcastManager.getInstance(this).registerReceiver(notificationCountReciever, notificationCountFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (notificationCount.equalsIgnoreCase("0")) {
            tv_notification.setVisibility(View.GONE);
            ll_notification_count.setVisibility(View.GONE);
        }else if (notificationCount.equalsIgnoreCase(null)) {
            tv_notification.setVisibility(View.GONE);
            ll_notification_count.setVisibility(View.GONE);
        }else if (notificationCount.equalsIgnoreCase("null")) {
            tv_notification.setVisibility(View.GONE);
            ll_notification_count.setVisibility(View.GONE);
        }else if (notificationCount.equalsIgnoreCase("")) {
            tv_notification.setVisibility(View.GONE);
            ll_notification_count.setVisibility(View.GONE);
        }  else {
            tv_notification.setVisibility(View.VISIBLE);
            ll_notification_count.setVisibility(View.VISIBLE);
        }
        String device_token = SharedPreference.getPref(this, KEY_GCM_ID);;
        Log.e("device_token===>",device_token);
        LinearLayout outer = findViewById(R.id.my);
        ImageView iv_profile = outer.findViewById(R.id.iv_profile);
        TextView tv_name = outer.findViewById(R.id.tv_name);
        ImageView iv_edit = outer.findViewById(R.id.iv_edit);
        iv_edit.setOnClickListener(this);
         tv_designation = outer.findViewById(R.id.tv_designation);
         tv_city = outer.findViewById(R.id.tv_city);
        String userType = SharedPreference.getPref(this, USER_TYPE);;

        if(!fName.equalsIgnoreCase("false")&&!lName.equalsIgnoreCase("false")) {
            tv_name.setText(fName + " " + lName);
        }
        else
        {
            tv_name.setText("");
        }
       // tv_designation.setText(designation);
        if(userType.equalsIgnoreCase("PO") || userType.equalsIgnoreCase("D")) {
            tv_designation.setVisibility(View.VISIBLE);

            tv_designation.setText(company);
        }else{
            tv_designation.setVisibility(View.GONE);
        }

        /*if (enrollleapFlag.equalsIgnoreCase("1")) {

        } else {
            showLeepdialouge();
        }*/


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
        tr_contact_us = findViewById(R.id.tr_contact_us);
        tr_survey = findViewById(R.id.tr_survey);
        tr_eula = findViewById(R.id.tr_eula);
        tr_privacy_policy = findViewById(R.id.tr_privacy_policy);
        tr_downloads = findViewById(R.id.tr_downloads);
        tr_faq = findViewById(R.id.tr_faq);
        tr_storeLocator = findViewById(R.id.tr_storeLocator);
        tr_qna = findViewById(R.id.tr_qna);

        new RefreashToken(this).callGetRefreashToken(this);


        txt_version.setText("Version: "+ BuildConfig.VERSION_NAME);
        tr_switch_event.setOnClickListener(this);
        tr_home.setOnClickListener(this);
        tr_profile.setOnClickListener(this);
        tr_event_info.setOnClickListener(this);
        tr_logout.setOnClickListener(this);
        tr_live_poll.setOnClickListener(this);
        tr_quiz.setOnClickListener(this);
        tr_contact_us.setOnClickListener(this);
        tr_survey.setOnClickListener(this);
        tr_eula.setOnClickListener(this);
        tr_privacy_policy.setOnClickListener(this);
        tr_downloads.setOnClickListener(this);
        tr_faq.setOnClickListener(this);
        tr_storeLocator.setOnClickListener(this);
        tr_qna.setOnClickListener(this);

        if (tot_event.equalsIgnoreCase("1")) {
            tr_switch_event.setVisibility(View.GONE);
        } else {
            tr_switch_event.setVisibility(View.VISIBLE);
        }

        try {
            String from = getIntent().getStringExtra("from");
            if(from.equalsIgnoreCase("postNewsFeed")){
                NewsFeedFragment newsFeedFragment = NewsFeedFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putString("isFrom", "MainActivity");
                newsFeedFragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_frame, newsFeedFragment, "")
                        .commit();
                navView.setSelectedItemId(R.id.navigation_home);
            }
            else {
                enrollleapFlag = SharedPreference.getPref(this, ENROLL_LEAP_FLAG);
                if (enrollleapFlag.equalsIgnoreCase("1")) {

                } else {
                    showLeepdialouge();
                }

                LoyalityLeapFragment newsFeedFragment = LoyalityLeapFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putString("isFrom", "MainActivity");
                newsFeedFragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_frame, newsFeedFragment, "")
                        .commit();

                navView.setSelectedItemId(R.id.navigation_leap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            enrollleapFlag = SharedPreference.getPref(this, ENROLL_LEAP_FLAG);
            if (enrollleapFlag.equalsIgnoreCase("1")) {
              // showLeepdialouge();

            } else {
                showLeepdialouge();
            }


            LoyalityLeapFragment newsFeedFragment = LoyalityLeapFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString("isFrom", "MainActivity");
            newsFeedFragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_frame, newsFeedFragment, "")
                    .commit();

            navView.setSelectedItemId(R.id.navigation_leap);
        }

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
                    case R.id.navigation_leap:
                        // Switch to page two
                        enrollleapFlag = SharedPreference.getPref(MainActivity.this, ENROLL_LEAP_FLAG);
                        if (enrollleapFlag.equalsIgnoreCase("1")) {

                        } else {
                            showLeepdialouge();
                        }

                        JzvdStd.releaseAllVideos();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_frame, LoyalityLeapFragment.newInstance(), "")
                                .commit();
                        break;
                    case R.id.navigation_catalogue:
                        // Switch to page three
                        JzvdStd.releaseAllVideos();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_frame, CatalogueFragment.newInstance(), "")
                                .commit();
                        break;
                    case R.id.navigation_upskill:
                        // Switch to page four
                        JzvdStd.releaseAllVideos();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_frame, UpskillFragment.newInstance(), "")
                                .commit();
                        break;
                    case R.id.navigation_logout:
                        //Logout from app
                       // LogoutFun();
                        /*JzvdStd.releaseAllVideos();
                        EventAppDB.getDatabase(MainActivity.this).profileUpdateDao().deleteData();
                        EventAppDB.getDatabase(MainActivity.this).newsFeedDao().deleteNewsFeed();
                        EventAppDB.getDatabase(MainActivity.this).newsFeedDao().deleteNewsFeedMedia();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finishAffinity();*/
                        break;

                }

                return true;
            }
        });

        getAttendeeAndInsertIntoDB();
    }


    private void setUpToolbar() {
        /*mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            mToolbar.showOverflowMenu();*/
            headerlogoIv = findViewById(R.id.headerlogoIv);
            //headerlogoIv.setOnClickListener(this);

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

        /*}*/
    }

    private void setUpNavDrawer_old() {
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.menuicon);
//            mToolbar.getNavigationIcon().setTint(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)));
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
                            Color.parseColor("#ffffff"),
                            Color.parseColor("#ffffff")

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

    private void setUpNavDrawer() {
        if (mToolbar != null) {
            /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_drawer);
            mToolbar.getNavigationIcon().setTint(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)));
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                    KeyboardUtility.hideSoftKeyboard(MainActivity.this);

                }
            });*/
            ImageView iv_drawer = findViewById(R.id.iv_drawer);

            iv_drawer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                    KeyboardUtility.hideSoftKeyboard(MainActivity.this);

                }
            });
            int color4 = Color.parseColor(SharedPreference.getPref(MainActivity.this, EVENT_COLOR_4));
            //iv_drawer.setColorFilter(color4, PorterDuff.Mode.SRC_ATOP);
            ColorStateList iconsColorStates = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_checked},
                            new int[]{android.R.attr.state_checked}
                    },
                    new int[]{
                            //Color.parseColor(colorunselect),
                            Color.parseColor( SharedPreference.getPref(this, EVENT_COLOR_4)),
                            Color.parseColor("#ffffff")
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
                if (userType.equalsIgnoreCase("D")) {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, ProfilePCOActivity.class));

                }
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
            case R.id.tr_contact_us:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, ContactUsActivity.class));
                break;
            case R.id.tr_survey:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, SurveyActivity.class));
                break;
            case R.id.tr_eula:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, EulaActivity.class));
                break;
            case R.id.tr_privacy_policy:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, PrivacypolicyActivity.class));
                break;
            case R.id.tr_downloads:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, DocumentActivity.class));
                break;
            case R.id.tr_faq:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, FAQActivity.class));
                break;
            case R.id.tr_profile:
                JzvdStd.releaseAllVideos();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, ProfilePCOActivity.class));
                break;
            case R.id.tr_qna:
                JzvdStd.releaseAllVideos();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, QnADirectActivity.class));
                break;
            case R.id.tr_storeLocator:
                JzvdStd.releaseAllVideos();
                mDrawerLayout.closeDrawer(GravityCompat.START);
               // Utility.createShortSnackBar(mDrawerLayout, "Coming soon");

                startActivity(new Intent(MainActivity.this, StorelocatorMoveActivity.class));
                break;
            case R.id.iv_notification:
                JzvdStd.releaseAllVideos();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(MainActivity.this, NotificationActivity.class));
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
                LogoutFun();
              /*  SessionManager.clearCurrentEvent(MainActivity.this);
                SessionManager.logoutUser(MainActivity.this);
                SharedPreference.clearPref(this, AUTHERISATION_KEY);
                SharedPreference.clearPref(this, IS_LOGIN);
                EventAppDB.getDatabase(MainActivity.this).newsFeedDao().deleteNewsFeedMedia();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();*/
                break;
        }
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
            ApiUtils.getAPIService().getProfile(api_token, eventid).enqueue(new Callback<Profile>() {
                @Override
                public void onResponse(Call<Profile> call, Response<Profile> response) {
                    if (response.isSuccessful()) {
                        String strEventList = response.body().getProfileDetailsEncrypted();
                        RefreashToken refreashToken = new RefreashToken(MainActivity.this);
                        String data = refreashToken.decryptedData(strEventList);
                        JsonArray jsonArray = new JsonParser().parse(data).getAsJsonArray();
                        ArrayList<ProfileDetails> profileDetails = new Gson().fromJson(jsonArray, new TypeToken<List<ProfileDetails>>() {
                        }.getType());

                        //List<ProfileDetails> profileDetails = profile.getProfileDetails();
                        if (profileDetails.size() > 0) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put(KEY_ATTENDEE_ID, profileDetails.get(0).getAttendee_id());
                            map.put(KEY_FNAME, profileDetails.get(0).getFirst_name());
                            map.put(KEY_LNAME, profileDetails.get(0).getLast_name());
                            map.put(KEY_EMAIL, profileDetails.get(0).getEmail());
                            map.put(KEY_PASSWORD, "");
                            map.put(KEY_DESIGNATION, profileDetails.get(0).getDesignation());
                            map.put(KEY_COMPANY, profileDetails.get(0).getCompany_name());
                            map.put(KEY_MOBILE, profileDetails.get(0).getMobile());
                            map.put(KEY_TOKEN, profileDetails.get(0).getAccess_token());
                            map.put(KEY_CITY, profileDetails.get(0).getCity());
                            //map.put(KEY_GCM_ID, "");
                            map.put(KEY_PROFILE_PIC, profileDetails.get(0).getProfile_picture());
                            map.put(KEY_ATTENDEE_ID, profileDetails.get(0).getAttendee_id());
                            map.put(ATTENDEE_STATUS, profileDetails.get(0).getIs_god());
                            map.put(ENROLL_LEAP_FLAG, profileDetails.get(0).getEnrollleapflag());
                            map.put(USER_TYPE, profileDetails.get(0).getUser_type());

                            map.put(IS_LOGIN, "true");

                            SharedPreference.putPref(MainActivity.this, map);

                            //  tv_name.setText(profileDetails.get(0).getFirst_name() + " " + profileDetails.get(0).getLast_name());
                            //tv_designation.setText(profileDetails.get(0).getDesignation() + " - " + profileDetails.get(0).getCity());

                            if(profileDetails.get(0).getUser_type().equalsIgnoreCase("PO") || profileDetails.get(0).getUser_type().equalsIgnoreCase("D")) {
                                tv_designation.setText(profileDetails.get(0).getCompany_name());
                                tv_designation.setVisibility(View.VISIBLE);

                            }else{
                                tv_designation.setVisibility(View.GONE);
                            }
                            if(profileDetails.get(0).getCity()!=null) {
                                tv_city.setText(profileDetails.get(0).getCity());
                            }

                           /* Glide.with(getApplicationContext())
                                    .load(profileDetails.get(0).getProfile_picture())
                                    .skipMemoryCache(true)
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            return false;
                                        }
                                    }).into(iv_profile);*/
                        }


                    }else {
                       /* if (response.body() != null) {
                        } else {
                            SessionManager.clearCurrentEvent(MainActivity.this);
                            SessionManager.logoutUser(MainActivity.this);
                            //EventAppDB.getDatabase(MainActivity.this).profileUpdateDao().deleteData();
                            EventAppDB.getDatabase(MainActivity.this).newsFeedDao().deleteNewsFeed();
                            EventAppDB.getDatabase(MainActivity.this).newsFeedDao().deleteNewsFeedMedia();
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        }*/
                    }
                }

                @Override
                public void onFailure(Call<Profile> call, Throwable t) {
                    try {
                        //profileMutableLiveData.setValue(null);
                    } catch (Exception e) {
                    }
                }
            });

        }
    }

/*
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
*/

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

    public BaseFragment getActiveFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return (BaseFragment) getSupportFragmentManager().findFragmentByTag(tag);
    }

    private void showLeepdialouge() {

        myDialog = new Dialog(MainActivity.this);
        myDialog.setContentView(R.layout.dialog_enroll_leap);
        myDialog.setCancelable(false);


        Button btnSubmit = myDialog.findViewById(R.id.btnSubmit);
        Button btnCancel = myDialog.findViewById(R.id.btnCancel);
        final CheckBox enrollBox = myDialog.findViewById(R.id.enrollCheckBox);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                JzvdStd.releaseAllVideos();
                navView.setSelectedItemId(R.id.navigation_home);

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enrollBox.isChecked()) {
                    getEnleepStatus(api_token, "1", "1", "");
                    myDialog.dismiss();

                } else {
                    Utility.createShortSnackBar(mDrawerLayout, "Please checked the box");
                }
            }
        });

        myDialog.show();

    }

    public MutableLiveData<LoginOrganizer> getEnleepStatus(String token, String eventid, String id, String text) {
        updateApi = ApiUtils.getAPIService();

        updateApi.enrollLeapFlag(token, eventid, "1", "")
                .enqueue(new Callback<LoginOrganizer>() {
                    @Override
                    public void onResponse(Call<LoginOrganizer> call, Response<LoginOrganizer> response) {
                        if (response.isSuccessful()) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put(ENROLL_LEAP_FLAG, "1");
                            SharedPreference.putPref(MainActivity.this, map);
                            Utility.createShortSnackBar(mDrawerLayout, response.body().getHeader().get(0).getMsg());

                        }
                    }

                    @Override
                    public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                        FetchenleepStatusList.setValue(null);
                    }
                });

        return FetchenleepStatusList;
    }


    public static class NotificationCountReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                getNotiCount(context);
            } catch (Exception e) {
            }


            try {
                notificationCountReciever = new MainActivity.NotificationCountReciever();
                notificationCountFilter = new IntentFilter(Constant.BROADCAST_ACTION_FOR_NOTIFICATION_COUNT);
                LocalBroadcastManager.getInstance(context).registerReceiver(notificationCountReciever, notificationCountFilter);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    static void getNotiCount(Context context){
            try {
                setNotification(context, tv_notification, ll_notification_count);
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

    }
    void LogoutFun( ) {
        ApiUtils.getAPIService().logout(api_token,"1").enqueue(new Callback<LoginOrganizer>() {
            @Override
            public void onResponse(Call<LoginOrganizer> call, Response<LoginOrganizer> response) {
                if (response.isSuccessful()) {
                    JzvdStd.releaseAllVideos();
                    SharedPreference.clearAllPref(getApplicationContext());
                    EventAppDB.getDatabase(MainActivity.this).profileUpdateDao().deleteData();
                    EventAppDB.getDatabase(MainActivity.this).newsFeedDao().deleteNewsFeed();
                    EventAppDB.getDatabase(MainActivity.this).newsFeedDao().deleteNewsFeedMedia();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finishAffinity();

                }
            }

            @Override
            public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                try {

                      Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                }
            }
        });
    }


}