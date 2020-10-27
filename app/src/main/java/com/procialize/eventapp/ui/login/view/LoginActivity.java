package com.procialize.eventapp.ui.login.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.eventapp.BuildConfig;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.Database.EventAppDB;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFirebase;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.ATTENDEE_STATUS;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.FIREBASEUSER_NAME;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.FIREBASE_ID;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.FIREBASE_NAME;
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
import static com.procialize.eventapp.ui.eventList.adapter.EventAdapter.isClickable;

public class LoginActivity extends AppCompatActivity {

    public static ActivityLoginBinding activityLoginBinding;
    public static SessionManager sessionManager;
    public static RefreashToken refreashToken;
    private static String device_token;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static CountDownTimer countdowntimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        activityLoginBinding.setViewModel(new LoginViewModel(LoginActivity.this, activityLoginBinding));
        activityLoginBinding.executePendingBindings();

        if (checkPlayServices()) {


            device_token = SharedPreference.getPref(this, KEY_GCM_ID);;

            if (device_token.isEmpty()) {

                new getGCMRegId().execute();
            }

        } else {
            Log.i("GCMDemo", "No valid Google Play Services APK found.");
        }

        CommonFirebase.crashlytics("Login Screen", "");
        CommonFirebase.firbaseAnalytics(this, "Login Screen", "");
    }

    @BindingAdapter({"toastMessage"})
    public static void runMe(View view, String message) {
        if (message != null) {
            if (message.equalsIgnoreCase("user found")) {
                Utility.hideKeyboard(view);
                activityLoginBinding.linearLoginView.setVisibility(View.GONE);
                activityLoginBinding.linearOTPView.setVisibility(View.VISIBLE);
            } else if (message.equalsIgnoreCase("back")) {
                Utility.hideKeyboard(view);
                activityLoginBinding.editOtp.setText("");
                activityLoginBinding.linearLoginView.setVisibility(View.VISIBLE);
                activityLoginBinding.linearOTPView.setVisibility(View.GONE);
            } else if (message.equalsIgnoreCase("Successfully Login")) {
                Utility.hideKeyboard(view);
                final String tot_event = SharedPreference.getPref(view.getContext(), SharedPreferencesConstant.TOTAL_EVENT);
                if (tot_event.equalsIgnoreCase("1")) {
                    boolean result = Utility.checkWritePermission(view.getContext());
                    getEventDetails(view);
                } else {
                    view.setClickable(false);
                    view.getContext().startActivity(new Intent(view.getContext(), EventListActivity.class));
                    ((Activity) view.getContext()).finish();
                }
                //finish();
            } else if (message.equalsIgnoreCase("DesignAndDevelopedby")) {
                Utility.hideKeyboard(view);
                String url = "https://www.theeventapp.in/terms-of-use";
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                //finish();
            } else if (message.equalsIgnoreCase("OTP sent on register email id/mobile no")) {
                Utility.hideKeyboard(view);
                Utility.displayToast(view.getContext(), message);
                countdowntimer = new CountDownTimerClass(30000, 1000);
                countdowntimer.start();
            } else {
//                Constant.displayToast(view.getContext(), message);
                Utility.hideKeyboard(view);
//                Utility.createShortSnackBar(view, message);
                Utility.displayToast(view.getContext(), message);

            }
        }
    }

    public static class CountDownTimerClass extends CountDownTimer {
        public CountDownTimerClass(long startTime, long interval) {
            super(startTime, interval);

        }

        @Override
        public void onFinish() {
            activityLoginBinding.btnResendOTP.setClickable(true);
            activityLoginBinding.btnResendOTP.setText("Resend OTP");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int progress = (int) (millisUntilFinished / 1000);
            activityLoginBinding.btnResendOTP.setText("Resend OTP : " + Integer.toString(progress));
            activityLoginBinding.btnResendOTP.setClickable(false);
        }
    }


    public static void getEventDetails(final View view) {

        final Context context = view.getContext();
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
                refreashToken = new RefreashToken(view.getContext());
                String decrypteventdetail = refreashToken.decryptedData(event.getDetail());
                String strFilePath = CommonFunction.stripquotes(refreashToken.decryptedData(event.getFile_path()));

                Gson gson = new Gson();
                List<EventList> eventLists = gson.fromJson(decrypteventdetail, new TypeToken<ArrayList<EventList>>() {
                }.getType());
//                List<EventList> eventLists = event.getEventLists();
                HashMap<String, String> map1 = new HashMap<>();
                map1.put(EVENT_LIST_MEDIA_PATH, strFilePath.replace("\\/","/"));
                SharedPreference.putPref(context, map1);
                final String eventId = eventLists.get(0).getEvent_id();
                final String eventBg = eventLists.get(0).getBackground_image();
                CommonFunction.saveBackgroundImage(context, eventBg);
                new SessionManager(context).saveCurrentEvent(eventLists.get(0));

                eventListViewModel.updateUserData(api_token, eventId, device_token, platform, device, osVersion, appVersion, new SessionManager(context));
                eventListViewModel.getupdateUserdatq().observe((LifecycleOwner) view.getContext(),new Observer<UpdateDeviceInfo>() {
                    @Override
                    public void onChanged(UpdateDeviceInfo updateDeviceInfo) {
                        String decrypteventdetail = refreashToken.decryptedData(updateDeviceInfo.getDetail());

                        Gson gson = new Gson();
                        List<LoginUserInfo> userData = gson.fromJson(decrypteventdetail, new TypeToken<ArrayList<LoginUserInfo>>() {
                        }.getType());
//                        final List<LoginUserInfo> userData = updateDeviceInfo.getLoginUserInfoList();
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
                        map.put(KEY_GCM_ID, device_token);
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
                        SharedPreference.putPref(context, map);

                        if (eventListViewModel != null && eventListViewModel.getupdateUserdatq().hasObservers()) {
                            eventListViewModel.getupdateUserdatq().removeObservers((LifecycleOwner) context);
                        }
                        EventAppDB eventAppDB = EventAppDB.getDatabase(context);
                        if (eventListViewModel != null && eventListViewModel.getEventList().hasObservers()) {
                            eventListViewModel.getEventList().removeObservers((LifecycleOwner) context);
                        }
                        String strAttendeeId = userData.get(0).getAttendee_id();
                        List<ProfileEventId> profileDataUpdated = eventAppDB.profileUpdateDao().
                                getProfileWithEventId(eventId,strAttendeeId);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAffinity(LoginActivity.this);
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
                SharedPreference.putPref(LoginActivity.this, map);

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
