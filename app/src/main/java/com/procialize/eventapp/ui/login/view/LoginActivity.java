package com.procialize.eventapp.ui.login.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.procialize.eventapp.BuildConfig;
import com.procialize.eventapp.Constants.Constant;
import com.procialize.eventapp.Database.EventAppDB;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.SharedPreferencesConstant;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.databinding.ActivityLoginBinding;
import com.procialize.eventapp.session.SessionManager;
import com.procialize.eventapp.ui.eventList.model.Event;
import com.procialize.eventapp.ui.eventList.model.EventList;
import com.procialize.eventapp.ui.eventList.model.LoginUserInfo;
import com.procialize.eventapp.ui.eventList.model.UpdateDeviceInfo;
import com.procialize.eventapp.ui.eventList.view.EventListActivity;
import com.procialize.eventapp.ui.eventList.viewModel.EventListViewModel;
import com.procialize.eventapp.ui.login.viewmodel.LoginViewModel;
import com.procialize.eventapp.ui.profile.roomDB.ProfileEventId;

import java.util.HashMap;
import java.util.List;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.ATTENDEE_STATUS;
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
import static com.procialize.eventapp.ui.eventList.adapter.EventAdapter.isClickable;

public class LoginActivity extends AppCompatActivity {

    public static ActivityLoginBinding activityLoginBinding;
    public static SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        activityLoginBinding.setViewModel(new LoginViewModel(LoginActivity.this));
        activityLoginBinding.executePendingBindings();


    }

    @BindingAdapter({"toastMessage"})
    public static void runMe(View view, String message) {
        if (message != null) {
            if (message.equalsIgnoreCase("user found")) {
                activityLoginBinding.linearLoginView.setVisibility(View.GONE);
                activityLoginBinding.linearOTPView.setVisibility(View.VISIBLE);
            } else if (message.equalsIgnoreCase("back")) {
                activityLoginBinding.linearLoginView.setVisibility(View.VISIBLE);
                activityLoginBinding.linearOTPView.setVisibility(View.GONE);
            } else if (message.equalsIgnoreCase("Successfully Login")) {

                final String tot_event = SharedPreference.getPref(view.getContext(), SharedPreferencesConstant.TOTAL_EVENT);
                if (tot_event.equalsIgnoreCase("1")) {
                    boolean result = Utility.checkWritePermission(view.getContext());
                    getEventDetails(view);
                } else {
                    view.getContext().startActivity(new Intent(view.getContext(), EventListActivity.class));
                }
                //finish();
            } else {
                Constant.displayToast(view.getContext(), message);
            }
        }
    }

    public static void getEventDetails(View view) {

        final Context context = view.getContext();
        final String device_token = "11111";
        final String platform = "android";
        final String device = Build.MODEL;
        final String osVersion = Build.VERSION.RELEASE;
        final String appVersion = "Version" + BuildConfig.VERSION_NAME;
        final String deviceId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        final EventListViewModel eventListViewModel = ViewModelProviders.of((FragmentActivity) context).get(EventListViewModel.class);
        final String api_token = SharedPreference.getPref(context, SharedPreferencesConstant.AUTHERISATION_KEY);
        eventListViewModel.getEvent(api_token, "0", "");

        eventListViewModel.getEventList().observeForever( new Observer<Event>() {
            @Override
            public void onChanged(Event event) {
                List<EventList> eventLists = event.getEventLists();
                String strFilePath = event.getFile_path();
                HashMap<String,String> map1 = new HashMap<>();
                map1.put(EVENT_LIST_MEDIA_PATH, strFilePath);
                SharedPreference.putPref(context, map1);
                final String eventId = eventLists.get(0).getEvent_id();
                final String eventBg = eventLists.get(0).getBackground_image();
                CommonFunction.saveBackgroundImage(context, eventBg);
                new SessionManager(context).saveCurrentEvent(eventLists.get(0));

                eventListViewModel.updateUserData(api_token, eventId, device_token, platform, device, osVersion, appVersion, new SessionManager(context));
                eventListViewModel.getupdateUserdatq().observeForever(new Observer<UpdateDeviceInfo>() {
                    @Override
                    public void onChanged(UpdateDeviceInfo updateDeviceInfo) {
                        final List<LoginUserInfo> userData = updateDeviceInfo.getLoginUserInfoList();
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
                        SharedPreference.putPref(context, map);

                        if (eventListViewModel != null && eventListViewModel.getupdateUserdatq().hasObservers()) {
                            eventListViewModel.getupdateUserdatq().removeObservers((LifecycleOwner) context);
                        }
                        EventAppDB eventAppDB = EventAppDB.getDatabase(context);
                        List<ProfileEventId> profileDataUpdated = eventAppDB.profileUpdateDao().getProfileWithEventId(eventId);
                        if (profileDataUpdated.size() > 0) {
                            isClickable = true;
                            eventListViewModel.openMainPage((Activity) context);
                        } else {
                            isClickable = true;
                            eventListViewModel.openProfilePage((Activity) context, userData, 0, eventBg);
                        }
                    }
                });
            }
        });
    }

}
