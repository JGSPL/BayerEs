package com.procialize.eventapp.ui.eventList.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;
import com.procialize.eventapp.BuildConfig;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.session.SessionManager;
import com.procialize.eventapp.ui.eventList.adapter.EventAdapter;
import com.procialize.eventapp.ui.eventList.model.Event;
import com.procialize.eventapp.ui.eventList.model.EventList;
import com.procialize.eventapp.ui.eventList.model.LoginUserInfo;
import com.procialize.eventapp.ui.eventList.model.UpdateDeviceInfo;
import com.procialize.eventapp.ui.eventList.viewModel.EventListViewModel;

import java.util.HashMap;
import java.util.List;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;

public class EventListActivity extends AppCompatActivity implements EventAdapter.EventAdapterListner {

    EventListViewModel eventListViewModel;
    ConnectionDetector cd;
    LinearLayout ll_main;
    RecyclerView rv_event_list;
    EventAdapter eventAdapter;
    EditText et_search;
    String eventid = "1",  device_token = "111111",  platform,  device, osVersion,  appVersion, deviceId;
    SessionManager session;
    String gcmRegID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        platform = "android";
        device = Build.MODEL;
        osVersion = Build.VERSION.RELEASE;
        appVersion = "Version" + BuildConfig.VERSION_NAME;
        deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        session = new SessionManager(getApplicationContext());


        ll_main = findViewById(R.id.ll_main);
        et_search = findViewById(R.id.et_search);
        rv_event_list = findViewById(R.id.rv_event_list);
        cd = ConnectionDetector.getInstance(this);
        eventListViewModel = ViewModelProviders.of(this).get(EventListViewModel.class);

        if (cd.isConnectingToInternet()) {
            eventListViewModel.getEvent("0", "");
            eventListViewModel.getEventList().observe(this, new Observer<Event>() {
                @Override
                public void onChanged(Event event) {
                    List<EventList> eventLists = event.getEventLists();
                    String strFilePath = event.getFile_path();
                    HashMap<String,String> map = new HashMap<>();
                    map.put(EVENT_LIST_MEDIA_PATH,strFilePath);
                    SharedPreference.putPref(EventListActivity.this,map);
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
            eventListViewModel.updateUserData(eventid,device_token,platform,device,osVersion,appVersion,session);


            eventListViewModel.getupdateUserdatq().observe(this, new Observer<UpdateDeviceInfo>() {
                @Override
                public void onChanged(UpdateDeviceInfo event) {
                    List<LoginUserInfo> userData = event.getLoginUserInfoList();
                    String fname = userData.get(0).getFirst_name();
                    String lName = userData.get(0).getLast_name();
                    String designation = userData.get(0).getDesignation();
                    String company = userData.get(0).getCompany_name();
                    String attnId = userData.get(0).getAttendee_id();
                    String profilePic = userData.get(0).getProfile_picture();
                    String city = userData.get(0).getCity();
                    String is_god = userData.get(0).getIs_god();
                    String emailId = userData.get(0).getEmail();
                    session.createLoginSession(fname,lName,emailId,"",company,designation, "",city,profilePic,attnId,"",is_god);

                    eventListViewModel.openProfilePage(EventListActivity.this,userData,position);

                }
            });
        } else {
            Utility.createShortSnackBar(ll_main, "No Internet Connection..!");
        }


    }

}