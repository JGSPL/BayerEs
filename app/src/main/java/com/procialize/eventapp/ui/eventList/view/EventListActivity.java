package com.procialize.eventapp.ui.eventList.view;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.procialize.eventapp.BuildConfig;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.SharedPreferencesConstant;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.session.SessionManager;
import com.procialize.eventapp.ui.eventList.adapter.EventAdapter;
import com.procialize.eventapp.ui.eventList.model.Event;
import com.procialize.eventapp.ui.eventList.model.EventList;
import com.procialize.eventapp.ui.eventList.model.LoginUserInfo;
import com.procialize.eventapp.ui.eventList.model.UpdateDeviceInfo;
import com.procialize.eventapp.ui.eventList.viewModel.EventListViewModel;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.ATTENDEE_STATUS;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
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

public class EventListActivity extends AppCompatActivity implements EventAdapter.EventAdapterListner {

    EventListViewModel eventListViewModel;
    ConnectionDetector cd;
    LinearLayout ll_main;
    RecyclerView rv_event_list;
    EventAdapter eventAdapter;
    EditText et_search;
    String event_id , device_token = "111111", platform, device, osVersion, appVersion, deviceId;
    SessionManager session;
    String api_token="";
    boolean result;

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

        ll_main = findViewById(R.id.ll_main);
        et_search = findViewById(R.id.et_search);
        rv_event_list = findViewById(R.id.rv_event_list);
        cd = ConnectionDetector.getInstance(this);
        eventListViewModel = ViewModelProviders.of(this).get(EventListViewModel.class);

        if (cd.isConnectingToInternet()) {
            String expirytime = SharedPreference.getPref(EventListActivity.this, SharedPreferencesConstant.EXPIRY_TIME);
            Timestamp timestamp_expiry = new Timestamp(Long.parseLong(expirytime));
//            int isvalidtoken = Utility.getTimeDifferenceInMillis(String.valueOf(timestamp_expiry));
           boolean isvalidtoken= Utility.isTimeGreater(String.valueOf(timestamp_expiry));

            if (isvalidtoken == false) {
                RefreashToken refreashToken = new RefreashToken(EventListActivity.this);
                refreashToken.callGetRefreashToken(EventListActivity.this);
            } else {
                Log.d("TAG", "Token is already refreashed");
            }


            eventListViewModel.getEvent(api_token,"0", "");

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
    }

    public void setupEventAdapter(List<EventList> commentList) {
        eventAdapter = new EventAdapter(EventListActivity.this, commentList, EventListActivity.this);
        rv_event_list.setLayoutManager(new LinearLayoutManager(this));
        rv_event_list.setAdapter(eventAdapter);
        eventAdapter.notifyDataSetChanged();
    }


    @Override
    public void onMoreSelected(EventList event, int position) {
        if (cd.isConnectingToInternet()) {

            if(result) {
                String eventId = event.getEvent_id();
                CommonFunction.saveBackgroundImage(EventListActivity.this, event.getBackground_image());
                session.saveCurrentEvent(event);
                eventListViewModel.updateUserData(api_token, eventId, device_token, platform, device, osVersion, appVersion, session);

                eventListViewModel.getupdateUserdatq().observe(this, new Observer<UpdateDeviceInfo>() {
                    @Override
                    public void onChanged(UpdateDeviceInfo event) {
                        List<LoginUserInfo> userData = event.getLoginUserInfoList();
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
                        map.put(KEY_GCM_ID, "");
                        map.put(KEY_PROFILE_PIC, userData.get(0).getProfile_picture());
                        map.put(KEY_ATTENDEE_ID, userData.get(0).getAttendee_id());
                        map.put(ATTENDEE_STATUS, userData.get(0).getIs_god());
                        map.put(IS_LOGIN, "true");
                        map.put(EVENT_ID, eventId);
                        SharedPreference.putPref(EventListActivity.this, map);
                        //session.createLoginSession(fname, lName, emailId, "", company, designation, "", city, profilePic, attnId, "", is_god);


                        if (eventListViewModel != null && eventListViewModel.getupdateUserdatq().hasObservers()) {
                            eventListViewModel.getupdateUserdatq().removeObservers(EventListActivity.this);
                        }

                        eventListViewModel.openProfilePage(EventListActivity.this, userData, position);
                    }
                });
            }
        } else {
            Utility.createShortSnackBar(ll_main, "No Internet Connection..!");
        }
    }

}