package com.procialize.eventapp.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.procialize.eventapp.MainActivity;
import com.procialize.eventapp.ui.eventList.model.EventList;
import com.procialize.eventapp.ui.login.view.LoginActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class SessionManager {
    // User name (make variable public to access from outside)
    public static final String MY_PREFS_NAME = "ProcializeInfo";
    public static final String KEY_FNAME = "name";
    public static final String KEY_LNAME = "lname";
    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    // Email address (make variable public to access from outside)
    public static final String KEY_PASSWORD = "password";
    // Designation  (make variable public to access from outside)
    public static final String KEY_DESIGNATION = "designation";
    // Company  (make variable public to access from outside)
    public static final String KEY_COMPANY = "company";
    // mobile (make variable public to access from outside)
    public static final String KEY_MOBILE = "mobile";
    // TOKEN (make variable public to access from outside)
    public static final String KEY_TOKEN = "api_access_token";
    public static final String EVENT_ID = "event_id";

    public static final String KEY_CITY = "city";
    public static final String KEY_GCM_ID = "gcm_id";
    // country (make variable public to access from outside)
    // PIC (make variable public to access from outside)
    public static final String KEY_PROFILE_PIC = "profile_pic";
    public static final String KEY_ATTENDEE_ID = "id";
    public static final String ATTENDEE_STATUS = "attendee_status";
    public static final String AUTHERISATION_KEY = "autherisationKey";

    // Sharedpref file name
    private static final String PREF_NAME = "Pref";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
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

        // Storing name in pref
        editor.putString(KEY_FNAME, fstname);
        editor.putString(KEY_LNAME, lstname);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // Storing mobile in pref
        editor.putString(KEY_MOBILE, mobile);

        // Storing company in pref
        editor.putString(KEY_COMPANY, company);


        // Storing designation in pref
        editor.putString(KEY_DESIGNATION, designation);

        // Storing token in pref
        editor.putString(KEY_TOKEN, token);

        // Storing city in pref
        editor.putString(KEY_CITY, city);


        // Storing pic in pref
        editor.putString(KEY_PROFILE_PIC, pic);

        // Storing pic in pref
        editor.putString(KEY_ATTENDEE_ID, id);


        // Storing password in pref
        editor.putString(KEY_PASSWORD, password);
        editor.putString(ATTENDEE_STATUS, attendee_status);

        // commit changes
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

        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_FNAME, name);

        // Storing company in pref
        editor.putString(KEY_COMPANY, company);

        // Storing designation in pref
        editor.putString(KEY_DESIGNATION, designation);
        editor.putString(KEY_LNAME, lastname);

        // Storing pic in pref
        editor.putString(KEY_PROFILE_PIC, pic);

        editor.putString(KEY_CITY, city);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, mobno);
        editor.putString(ATTENDEE_STATUS, attendee_type);

        // commit changes
        editor.commit();
    }

    public String getGcmID() {

        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        String gcmRegID = pref.getString(KEY_GCM_ID, "");

        return gcmRegID;
    }

    public void storeGcmID(String gcmRegID) {
        // Storing eventId in pref
        editor.putString(KEY_GCM_ID, gcmRegID);
        // commit changes
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
        SharedPreferences prefs = _context.getSharedPreferences(SessionManager.MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(EVENT_ID, userEvent.getEvent_id()).commit();
        editor.putString("eventnamestr", userEvent.getName()).commit();
        editor.putString("logoImg", userEvent.getHeader_image()).commit();
        editor.putString("eventback", userEvent.getBackground_image()).commit();
        editor.putString("color1", userEvent.getColor_one()).commit();
        editor.putString("color2", userEvent.getColor_two()).commit();
        editor.putString("color3", userEvent.getColor_three()).commit();
        editor.putString("color4", userEvent.getColor_four()).commit();
        editor.putString("color5", userEvent.getColor_five()).commit();

        editor.apply();
    }
}