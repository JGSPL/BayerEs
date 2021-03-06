package com.procialize.bayer2020.ui.eventList.view;

import android.app.Activity;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.bayer2020.BuildConfig;
import com.procialize.bayer2020.ConnectionDetector;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.Database.EventAppDB;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.CommonFirebase;
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.session.SessionManager;
import com.procialize.bayer2020.ui.eventList.adapter.EventAdapter;
import com.procialize.bayer2020.ui.eventList.model.Event;
import com.procialize.bayer2020.ui.eventList.model.EventList;
import com.procialize.bayer2020.ui.eventList.model.LoginUserInfo;
import com.procialize.bayer2020.ui.eventList.model.UpdateDeviceInfo;
import com.procialize.bayer2020.ui.eventList.viewModel.EventListViewModel;
import com.procialize.bayer2020.ui.login.view.LoginActivity;
import com.procialize.bayer2020.ui.profile.roomDB.ProfileEventId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.ATTENDEE_STATUS;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.FIREBASEUSER_NAME;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.FIREBASE_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.FIREBASE_NAME;
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
import static com.procialize.bayer2020.ui.eventList.adapter.EventAdapter.isClickable;

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

        CommonFirebase.crashlytics("EventListing", api_token);
        CommonFirebase.firbaseAnalytics(this, "EventListing", api_token);
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
                    RefreashToken refreashToken = new RefreashToken(EventListActivity.this);
                    String decrypteventdetail = refreashToken.decryptedData(event.getDetail());
                    String strFilePath = CommonFunction.stripquotes(refreashToken.decryptedData(event.getFile_path()));
                    Gson gson = new Gson();
                    List<EventList> gsonevent = gson.fromJson(decrypteventdetail, new TypeToken<ArrayList<EventList>>() {
                    }.getType());
                    HashMap<String, String> map = new HashMap<>();
                    map.put(EVENT_LIST_MEDIA_PATH, strFilePath.replace("\\/","/"));
                    SharedPreference.putPref(EventListActivity.this, map);
                    setupEventAdapter(gsonevent);
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
                            RefreashToken refreashToken = new RefreashToken(EventListActivity.this);
                            String decrypteventdetail = refreashToken.decryptedData(event.getDetail());
                            String strFilePath = CommonFunction.stripquotes(refreashToken.decryptedData(event.getFile_path()));

                            Gson gson = new Gson();
                            List<EventList> eventLists = gson.fromJson(decrypteventdetail, new TypeToken<ArrayList<EventList>>() {
                            }.getType());
                            HashMap<String, String> map = new HashMap<>();
                            map.put(EVENT_LIST_MEDIA_PATH, strFilePath.replace("\\/","/"));
                            SharedPreference.putPref(EventListActivity.this, map);
                            setupEventAdapter(eventLists);
                        }
                    });
                } else {
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
            //eventListViewModel.updateUserData(api_token, eventId, device_token, platform, device, osVersion, appVersion, session);

            ApiUtils.getAPIService().updateDeviceInfo(api_token, eventId, device_token, platform, device, osVersion, appVersion)
                    .enqueue(new Callback<UpdateDeviceInfo>() {
                        @Override
                        public void onResponse(Call<UpdateDeviceInfo> call, Response<UpdateDeviceInfo> response) {
                            if (response.isSuccessful()) {
                                try {
                                    RefreashToken refreashToken = new RefreashToken(EventListActivity.this);
                                    String decrypteventdetail = refreashToken.decryptedData(response.body().getDetail());

                                    Gson gson = new Gson();
                                    List<LoginUserInfo> userData = gson.fromJson(decrypteventdetail, new TypeToken<ArrayList<LoginUserInfo>>() {
                                    }.getType());
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put(KEY_FNAME, userData.get(0).getFirst_name());
                                    map.put(KEY_LNAME, userData.get(0).getLast_name());
                                    map.put(KEY_EMAIL, userData.get(0).getEmail());
                                    map.put(KEY_PASSWORD, "");
                                    map.put(KEY_DESIGNATION, userData.get(0).getDesignation());
                                    map.put(KEY_COMPANY, userData.get(0).getCompany_name());
                                    map.put(KEY_MOBILE, userData.get(0).getMobile());
                                    //map.put(KEY_TOKEN, "");
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
                                    List<ProfileEventId> profileDataUpdated = eventAppDB.profileUpdateDao().getProfileWithEventId(eventId,userData.get(0).getAttendee_id());
                                    /*if (profileDataUpdated.size() > 0) {
                                        isClickable = true;
                                        eventListViewModel.openMainPage(EventListActivity.this);
                                    } else {
                                        isClickable = true;
                                        eventListViewModel.openProfilePage(EventListActivity.this, userData, position, eventBg);
                                    }*/
                                   /* if (profileDataUpdated.size() > 0) {
                                        isClickable = true;
                                        eventListViewModel.openMainPage(EventListActivity.this, eventLists.get(0));
                                    } else {
                                        isClickable = true;
                                        eventListViewModel.openProfilePage(EventListActivity.this, userData, 0, eventBg, eventLists.get(0));
                                    }*/
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UpdateDeviceInfo> call, Throwable t) {
                            Utility.createShortSnackBar(ll_main, "Failure...!!");
                        }
                    });
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