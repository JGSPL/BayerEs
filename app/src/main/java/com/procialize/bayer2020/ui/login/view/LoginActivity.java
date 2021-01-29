package com.procialize.bayer2020.ui.login.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.procialize.bayer2020.BuildConfig;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.Database.EventAppDB;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.CommonFirebase;
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.SharedPreferencesConstant;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.databinding.ActivityLoginBinding;
import com.procialize.bayer2020.session.SessionManager;
import com.procialize.bayer2020.ui.eventList.model.Event;
import com.procialize.bayer2020.ui.eventList.model.EventList;
import com.procialize.bayer2020.ui.eventList.model.LoginUserInfo;
import com.procialize.bayer2020.ui.eventList.model.UpdateDeviceInfo;
import com.procialize.bayer2020.ui.eventList.view.EventListActivity;
import com.procialize.bayer2020.ui.eventList.viewModel.EventListViewModel;
import com.procialize.bayer2020.ui.login.viewmodel.LoginViewModel;
import com.procialize.bayer2020.ui.profile.roomDB.ProfileEventId;
import com.procialize.bayer2020.ui.profile.view.ProfileActivity;
import com.procialize.bayer2020.ui.profile.view.ProfilePCOActivity;

import java.io.Serializable;
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
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.USER_TYPE;
import static com.procialize.bayer2020.ui.eventList.adapter.EventAdapter.isClickable;

public class LoginActivity extends AppCompatActivity {

    public static ActivityLoginBinding activityLoginBinding;
    public static SessionManager sessionManager;
    public static RefreashToken refreashToken;
    private static String device_token;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static CountDownTimer countdowntimer;
    public static Dialog passcodeDialog;

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
//                activityLoginBinding.linearLoginView.setVisibility(View.GONE);
//                activityLoginBinding.linearOTPView.setVisibility(View.VISIBLE);
                passcodeDialog(view.getContext());
            }else if (message.equalsIgnoreCase("user added")) {
                    Utility.hideKeyboard(view);
//                activityLoginBinding.linearLoginView.setVisibility(View.GONE);
//                activityLoginBinding.linearOTPView.setVisibility(View.VISIBLE);
                    passcodeDialog(view.getContext());
                }  else if (message.equalsIgnoreCase("back")) {
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

    public static void passcodeDialog(final Context activity) {


        passcodeDialog = new Dialog(activity);
        passcodeDialog.setContentView(R.layout.passcode_dialog);

        // Font Initialization
//        Typeface typeFace = Typeface.createFromAsset(getAssets(), "DINPro-Light_13935.ttf");

        // Set Font
        SpannableString title = new SpannableString("Enter Passcode");
//        title.setSpan(typeFace, 0, 12, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        passcodeDialog.setTitle(title);
        passcodeDialog.setCancelable(false);

        final EditText submitPasscode = (EditText) passcodeDialog
                .findViewById(R.id.message_edt_send__dialog);
//        submitPasscode.setTypeface(typeFace);

        Button submit = (Button) passcodeDialog
                .findViewById(R.id.btn_send_dialog);
        Button btn_resend = (Button) passcodeDialog
                .findViewById(R.id.btn_resend);
        ProgressBar progressBar = (ProgressBar) passcodeDialog
                .findViewById(R.id.progressBar);

        btn_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityLoginBinding.getViewModel().passcodeResendOTP();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                if (submitPasscode.length() == 0) {
                    Toast.makeText(activity, "Please enter passcode",
                            Toast.LENGTH_SHORT).show();
                } else {
                    String passcode = submitPasscode.getText().toString();

                    activityLoginBinding.getViewModel().PasscodeValidateOTP(passcode);

//                    if (passwordkey.equalsIgnoreCase(passcode)) {
//
//                        if (passcodeDialog.isShowing())
//                            passcodeDialog.dismiss();
//
////                        sendEventList(access_token);
//
//
//                    } else {
//                        Toast.makeText(activity, "Invalid Passcode",
//                                Toast.LENGTH_SHORT).show();
//
//                    }

                }

            }
        });

        ImageView close = (ImageView) passcodeDialog
                .findViewById(R.id.cross);
        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                passcodeDialog.dismiss();

            }
        });

        passcodeDialog.show();

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
        //eventListViewModel.getEvent(api_token, "1", "");
        ApiUtils.getAPIService().getEventList(api_token, "1", "")
                .enqueue(new Callback<Event>() {
                    @Override
                    public void onResponse(Call<Event> call, Response<Event> response) {
                        if (response.isSuccessful()) {
                            refreashToken = new RefreashToken(view.getContext());
                            String decrypteventdetail = refreashToken.decryptedData(response.body().getDetail());
                            String strFilePath = CommonFunction.stripquotes(refreashToken.decryptedData(response.body().getFile_path()));

                            Gson gson = new Gson();
                            final List<EventList> eventLists = gson.fromJson(decrypteventdetail, new TypeToken<ArrayList<EventList>>() {
                            }.getType());
//                List<EventList> eventLists = event.getEventLists();
                            HashMap<String, String> map1 = new HashMap<>();
                            map1.put(EVENT_LIST_MEDIA_PATH, strFilePath.replace("\\/", "/"));
                            SharedPreference.putPref(context, map1);
                            final String eventId = eventLists.get(0).getEvent_id();
                            final String eventBg = eventLists.get(0).getBackground_image();
                            CommonFunction.saveBackgroundImage(context, eventBg);
                            new SessionManager(context).saveCurrentEvent(eventLists.get(0));
                            ApiUtils.getAPIService().updateDeviceInfo(api_token, eventId, device_token, platform, device, osVersion, appVersion)
                                    .enqueue(new Callback<UpdateDeviceInfo>() {
                                        @Override
                                        public void onResponse(Call<UpdateDeviceInfo> call, Response<UpdateDeviceInfo> response) {
                                            if (response.isSuccessful()) {
                                                try {
                                                    String decrypteventdetail = refreashToken.decryptedData(response.body().getDetail());

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


                                                    map.put(IS_LOGIN, "true");
                                                    map.put(EVENT_ID, eventId);
                                                    SharedPreference.putPref(context, map);


                                                    EventAppDB eventAppDB = EventAppDB.getDatabase(context);

                                                    String strAttendeeId = userData.get(0).getAttendee_id();
                                                    List<ProfileEventId> profileDataUpdated = eventAppDB.profileUpdateDao().
                                                            getProfileWithEventId(eventId, strAttendeeId);
                                                    if (profileDataUpdated.size() > 0) {
                                                        isClickable = true;
                                                        eventListViewModel.openMainPage((Activity) context, eventLists.get(0));
                                                    } else {
                                                        isClickable = true;
                                                        String userType = SharedPreference.getPref(context, USER_TYPE);
                                                        if(userType.equalsIgnoreCase("D")) {
                                                            eventListViewModel.openProfilePage((Activity) context, userData, 0, eventBg, eventLists.get(0));
                                                        }else{
                                                            eventListViewModel.openProfilePCOPage((Activity) context, userData, 0, eventBg, eventLists.get(0));

                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<UpdateDeviceInfo> call, Throwable t) {
                                            //updateLoginUserList.setValue(null);
                                        }
                                    });

                        }
                    }

                    @Override
                    public void onFailure(Call<Event> call, Throwable t) {
                        //eventList.setValue(null);
                    }
                });

/*
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
*/
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
