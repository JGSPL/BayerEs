package com.procialize.eventapp.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.procialize.eventapp.MainActivity;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.SharedPreferencesConstant;
import com.procialize.eventapp.ui.eventList.model.EventList;
import com.procialize.eventapp.ui.login.view.LoginActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.ATTENDEE_STATUS;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_BACKGROUD;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_5;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_LOGO;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_NAME;
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
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.SHARED_PREF;


public class SessionManager {
    public static final String MY_PREFS_NAME = SHARED_PREF;
    // Sharedpref file name
    private static final String PREF_NAME = SHARED_PREF;
    // Shared Preferences
    static SharedPreferences pref;
    // Editor for Shared preferences
    static SharedPreferences.Editor editor;

    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public SessionManager(Context context) {
        try {
            this._context = context;
            pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            editor = pref.edit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* public static void saveSharedPreferencesEventList(List<EventSettingList> callLog) {
        Gson gson = new Gson();
        String json = gson.toJson(callLog);
        editor.putString("eventlist", json);
        editor.commit();
    }

    public static void saveSharedPreferencesMenuEventList(List<EventMenuSettingList> callLog) {

        Gson gson = new Gson();
        String json = gson.toJson(callLog);
        editor.putString("menueventlist", json);
        editor.commit();
    }

   public static List<EventSettingList> loadEventList() {
        List<EventSettingList> callLog = new ArrayList<EventSettingList>();
        Gson gson = new Gson();
        String json = pref.getString("eventlist", "");
        if (json.isEmpty()) {
            callLog = new ArrayList<EventSettingList>();
        } else {
            Type type = new TypeToken<List<EventSettingList>>() {
            }.getType();
            callLog = gson.fromJson(json, type);
        }
        return callLog;
    }

    public static List<EventMenuSettingList> loadMenuEventList() {
        List<EventMenuSettingList> callLog = new ArrayList<EventMenuSettingList>();
        Gson gson = new Gson();
        String json = pref.getString("menueventlist", "");
        if (json.isEmpty()) {
            callLog = new ArrayList<EventMenuSettingList>();
        } else {
            Type type = new TypeToken<List<EventMenuSettingList>>() {
            }.getType();
            callLog = gson.fromJson(json, type);
        }
        return callLog;
    }*/

    /**
     * Create login session
     */
    public void createLoginSession(String fstname, String lstname, String email, String mobile, String company,
                                   String designation, String token, String city, String pic, String id,
                                   String password, String attendee_status) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_FNAME, fstname);
        editor.putString(KEY_LNAME, lstname);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_COMPANY, company);
        editor.putString(KEY_DESIGNATION, designation);
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_CITY, city);
        editor.putString(KEY_PROFILE_PIC, pic);
        editor.putString(KEY_ATTENDEE_ID, id);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(ATTENDEE_STATUS, attendee_status);
        editor.commit();
    }


    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        } else {
            Intent i = new Intent(_context, MainActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_FNAME, pref.getString(KEY_FNAME, null));

        user.put(KEY_LNAME, pref.getString(KEY_LNAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // user mobile
        user.put(KEY_MOBILE, pref.getString(KEY_MOBILE, null));

        // user designation
        user.put(KEY_DESIGNATION, pref.getString(KEY_DESIGNATION, null));

        // user company
        user.put(KEY_COMPANY, pref.getString(KEY_COMPANY, null));


        // user token
        user.put(KEY_TOKEN, pref.getString(KEY_TOKEN, null));

        // user city
        user.put(KEY_CITY, pref.getString(KEY_CITY, null));


        // user pic
        user.put(KEY_PROFILE_PIC, pref.getString(KEY_PROFILE_PIC, null));

        //user Id
        user.put(KEY_ATTENDEE_ID, pref.getString(KEY_ATTENDEE_ID, null));

        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        user.put(ATTENDEE_STATUS, pref.getString(ATTENDEE_STATUS, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void createProfileSession(String name, String company, String designation, String pic, String lastname, String city,
                                     String email, String mobno, String attendee_type) {

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_FNAME, name);
        editor.putString(KEY_COMPANY, company);
        editor.putString(KEY_DESIGNATION, designation);
        editor.putString(KEY_LNAME, lastname);
        editor.putString(KEY_PROFILE_PIC, pic);
        editor.putString(KEY_CITY, city);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, mobno);
        editor.putString(ATTENDEE_STATUS, attendee_type);
        editor.commit();
    }

    public String getGcmID() {

        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        String gcmRegID = pref.getString(KEY_GCM_ID, "");

        return gcmRegID;
    }

    public void storeGcmID(String gcmRegID) {
        editor.putString(KEY_GCM_ID, gcmRegID);
        editor.commit();
    }

    public String getAuthHeaderKey() {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        String authKey = pref.getString(AUTHERISATION_KEY, "");
        return authKey;
    }

    public void storeAuthHeaderkey(String authKey) {
        // Storing eventId in pref
        editor.putString(AUTHERISATION_KEY, authKey);
        // commit changes
        editor.commit();
    }

    public EventList getUserEvent() {
        SharedPreferences prefs = _context.getSharedPreferences(SessionManager.MY_PREFS_NAME, MODE_PRIVATE);
        return new EventList(
                prefs.getString(EVENT_ID, "1"),
                prefs.getString("eventnamestr", ""),
                prefs.getString("logoImg", ""),
                prefs.getString("eventback", ""),
                prefs.getString("color1", ""),
                prefs.getString("color2", ""),
                prefs.getString("color3", ""),
                prefs.getString("color4", ""),
                prefs.getString("color5", "")

        );
    }


    public void saveCurrentEvent(EventList userEvent) {
        HashMap<String,String> map = new HashMap<>();
        map.put(EVENT_ID, userEvent.getEvent_id());
        map.put(EVENT_NAME, userEvent.getName());
        map.put(EVENT_LOGO, userEvent.getHeader_image());
        map.put(EVENT_BACKGROUD, userEvent.getBackground_image());
        map.put(EVENT_COLOR_1, userEvent.getColor_one());
        map.put(EVENT_COLOR_2, userEvent.getColor_two());
        map.put(EVENT_COLOR_3, userEvent.getColor_three());
        map.put(EVENT_COLOR_4, userEvent.getColor_four());
        map.put(EVENT_COLOR_5, userEvent.getColor_five());
        SharedPreference.putPref(_context,map);
    }


}