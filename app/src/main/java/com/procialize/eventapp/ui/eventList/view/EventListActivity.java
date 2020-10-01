package com.procialize.eventapp.ui.eventList.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.procialize.eventapp.BuildConfig;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.Database.EventAppDB;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.session.SessionManager;
import com.procialize.eventapp.ui.eventList.adapter.EventAdapter;
import com.procialize.eventapp.ui.eventList.model.Event;
import com.procialize.eventapp.ui.eventList.model.EventList;
import com.procialize.eventapp.ui.eventList.model.LoginUserInfo;
import com.procialize.eventapp.ui.eventList.model.UpdateDeviceInfo;
import com.procialize.eventapp.ui.eventList.viewModel.EventListViewModel;
import com.procialize.eventapp.ui.login.view.LoginActivity;
import com.procialize.eventapp.ui.profile.roomDB.ProfileEventId;

import java.util.HashMap;
import java.util.List;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.ATTENDEE_STATUS;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.FIREBASEUSER_NAME;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.FIREBASE_ID;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.FIREBASE_NAME;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.FIREBASE_STATUS;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.IS_LOGIN;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_ATTENDEE_ID;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_CITY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_COMPANY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_DESIGNATION;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_EMAIL;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_FNAME;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_GCM_ID;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_LNAME;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_MOBILE;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_PASSWORD;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_PROFILE_PIC;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_TOKEN;
import static com.procialize.eventapp.ui.eventList.adapter.EventAdapter.isClickable;
import static com.procialize.eventapp.ui.newsfeed.adapter.PaginationListener.PAGE_START;

public class EventListActivity extends AppCompatActivity implements EventAdapter.EventAdapterListner , View.OnClickListener {

    EventListViewModel eventListViewModel;
    ConnectionDetector cd;
    public static LinearLayout ll_main;
    RecyclerView rv_event_list;
    EventAdapter eventAdapter;
    EditText et_search;
    String event_id, device_token = "111111", platform, device, osVersion, appVersion, deviceId;
    SessionManager session;
    String api_token = "";
    boolean result;
    ImageView iv_logout;
    SwipeRefreshLayout refresh;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        result = Utility.checkWritePermission(EventListActivity.this);

        platform = "android";
        device = Build.MODEL;
        osVersion = Build.VERSION.RELEASE;
        appVersion = "Version" + BuildConfig.VERSION_NAME;
        deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        session = new SessionManager(getApplicationContext());

        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(this, EVENT_ID);

        iv_logout = findViewById(R.id.iv_logout);
        ll_main = findViewById(R.id.ll_main);
        et_search = findViewById(R.id.et_search);
        refresh = findViewById(R.id.refresh);
        rv_event_list = findViewById(R.id.rv_event_list);
        cd = ConnectionDetector.getInstance(this);
        eventListViewModel = ViewModelProviders.of(this).get(EventListViewModel.class);
        iv_logout.setOnClickListener(this);
        if (cd.isConnectingToInternet()) {
            new RefreashToken(EventListActivity.this).callGetRefreashToken(EventListActivity.this);
            eventListViewModel.getEvent(api_token, "0", "");

            eventListViewModel.getEventList().observe(this, new Observer<Event>() {
                @Override
                public void onChanged(Event event) {
                    List<EventList> eventLists = event.getEventLists();
                    String strFilePath = event.getFile_path();
                    HashMap<String, String> map = new HashMap<>();
                    map.put(EVENT_LIST_MEDIA_PATH, strFilePath);
                    SharedPreference.putPref(EventListActivity.this, map);
                    setupEventAdapter(eventLists);
                }
            });
        } else {
            Utility.createShortSnackBar(ll_main, "No Internet Connection..!");
        }

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                et_search.setText("");
                refresh.setRefreshing(false);
                if (cd.isConnectingToInternet()) {
                    new RefreashToken(EventListActivity.this).callGetRefreashToken(EventListActivity.this);
                    eventListViewModel.getEvent(api_token, "0", "");

                    eventListViewModel.getEventList().observe(EventListActivity.this, new Observer<Event>() {
                        @Override
                        public void onChanged(Event event) {
                            List<EventList> eventLists = event.getEventLists();
                            String strFilePath = event.getFile_path();
                            HashMap<String, String> map = new HashMap<>();
                            map.put(EVENT_LIST_MEDIA_PATH, strFilePath);
                            SharedPreference.putPref(EventListActivity.this, map);
                            setupEventAdapter(eventLists);
                        }
                    });
                }
                else
                {
                    Utility.createShortSnackBar(ll_main, "No Internet Connection");
                }
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    eventAdapter.getFilter().filter(s.toString());
                } catch (Exception e) {

                }

            }
        });

        if (checkPlayServices()) {


            device_token = SharedPreference.getPref(this, KEY_GCM_ID);;

            if (device_token.isEmpty()) {

                new getGCMRegId().execute();
            }

        } else {
            Log.i("GCMDemo", "No valid Google Play Services APK found.");
        }

    }



    public void setupEventAdapter(List<EventList> commentList) {
        eventAdapter = new EventAdapter(EventListActivity.this, commentList, EventListActivity.this);
        rv_event_list.setLayoutManager(new LinearLayoutManager(this));
        rv_event_list.setAdapter(eventAdapter);
        eventAdapter.notifyDataSetChanged();
    }


    @Override
    public void onMoreSelected(EventList event, final int position) {
        if (cd.isConnectingToInternet()) {
            // if(result) {
            final String eventId = event.getEvent_id();
            final String eventBg = event.getBackground_image();
            CommonFunction.saveBackgroundImage(EventListActivity.this, event.getBackground_image());
            session.saveCurrentEvent(event);
            eventListViewModel.updateUserData(api_token, eventId, device_token, platform, device, osVersion, appVersion, session);
            eventListViewModel.getupdateUserdatq().observeForever(new Observer<UpdateDeviceInfo>() {
                @Override
                public void onChanged(UpdateDeviceInfo event) {
                    final List<LoginUserInfo> userData = event.getLoginUserInfoList();
                  /*  String fname = userData.get(0).getFirst_name();
                    String lName = userData.get(0).getLast_name();
                    String designation = userData.get(0).getDesignation();
                    String company = userData.get(0).getCompany_name();
                    String attnId = userData.get(0).getAttendee_id();
                    String profilePic = userData.get(0).getProfile_picture();
                    String city = userData.get(0).getCity();
                    String is_god = userData.get(0).getIs_god();
                    String emailId = userData.get(0).getEmail();*/

                    HashMap<String, String> map = new HashMap<>();
                    map.put(KEY_FNAME, userData.get(0).getFirst_name());
                    map.put(KEY_LNAME, userData.get(0).getLast_name());
                    map.put(KEY_EMAIL, userData.get(0).getEmail());
                    map.put(KEY_PASSWORD, "");
                    map.put(KEY_DESIGNATION, userData.get(0).getDesignation());
                    map.put(KEY_COMPANY, userData.get(0).getCompany_name());
                    map.put(KEY_MOBILE, userData.get(0).getMobile());
                    map.put(KEY_TOKEN, "");
                    map.put(KEY_CITY, userData.get(0).getCity());
                   // map.put(KEY_GCM_ID, device_token);
                    map.put(KEY_PROFILE_PIC, userData.get(0).getProfile_picture());
                    map.put(KEY_ATTENDEE_ID, userData.get(0).getAttendee_id());
                    map.put(ATTENDEE_STATUS, userData.get(0).getIs_god());
                    if( userData.get(0).getFirebase_id()==null){
                        map.put(FIREBASE_ID, "0");

                    }else{
                        map.put(FIREBASE_ID, userData.get(0).getFirebase_id());

                    }
                    if( userData.get(0).getFirebase_name()==null){
                        map.put(FIREBASE_NAME, "");

                    }else{
                        map.put(FIREBASE_NAME, userData.get(0).getFirebase_name());

                    }
                    if( userData.get(0).getFirebase_username()==null){
                        map.put(FIREBASEUSER_NAME, "");

                    }else{
                        map.put(FIREBASEUSER_NAME, userData.get(0).getFirebase_username());

                    }
                  //  map.put(FIREBASE_STATUS, userData.get(0).getFirebase_status());

                    map.put(IS_LOGIN, "true");
                    map.put(EVENT_ID, eventId);
                    SharedPreference.putPref(EventListActivity.this, map);
                    //session.createLoginSession(fname, lName, emailId, "", company, designation, "", city, profilePic, attnId, "", is_god);


                    if (eventListViewModel != null && eventListViewModel.getupdateUserdatq().hasObservers()) {
                        eventListViewModel.getupdateUserdatq().removeObservers(EventListActivity.this);
                    }
                    EventAppDB eventAppDB = EventAppDB.getDatabase(EventListActivity.this);
                    List<ProfileEventId> profileDataUpdated = eventAppDB.profileUpdateDao().getProfileWithEventId(eventId);
                    if (profileDataUpdated.size() > 0) {
                        isClickable = true;
                        eventListViewModel.openMainPage(EventListActivity.this);
                    } else {
                        isClickable = true;
                        eventListViewModel.openProfilePage(EventListActivity.this, userData, position, eventBg);
                    }
                }
            });
            // }
        } else {
            isClickable = true;
            Utility.createShortSnackBar(ll_main, "No Internet Connection..!");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.iv_logout:
                isClickable = true;
                SessionManager.clearCurrentEvent(EventListActivity.this);
                SessionManager.logoutUser(EventListActivity.this);
                SharedPreference.clearAllPref(EventListActivity.this);
                startActivity(new Intent(EventListActivity.this, LoginActivity.class));
                finish();
            break;
        }
    }

    @Override
    public void onBackPressed() {
        isClickable = true;
        SessionManager.clearCurrentEvent(EventListActivity.this);
        SessionManager.logoutUser(EventListActivity.this);
        SharedPreference.clearAllPref(EventListActivity.this);
        startActivity(new Intent(EventListActivity.this, LoginActivity.class));
        finish();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("GCMDemo", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private class getGCMRegId extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {


            try {
                //gcmRegID = gcmRegistrationHelper.GCMRegister(REG_ID);
                String token = FirebaseInstanceId.getInstance().getToken();
                Log.d("MYTAG", "This is your Firebase token" + token);

                device_token = token;

                HashMap<String, String> map = new HashMap<>();
                map.put(KEY_GCM_ID, device_token);
                SharedPreference.putPref(EventListActivity.this, map);

                // storeRegistrationId(getApplicationContext(), gcmRegID);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // session = new SessionManagement(getApplicationContext());
            // if (session.isLoggedIn()) {
            // Update GCM ID to Server
            // new updateGCMRegId().execute();
            // }

        }
    }


}