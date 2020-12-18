package com.procialize.bayer2020.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.ui.eventList.model.EventList;

import java.util.HashMap;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.ATTENDEE_STATUS;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_BACKGROUD;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_5;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_NAME;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.IS_GOD;
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
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.SHARED_PREF;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.*;


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

    public static void clearCurrentEvent(Context context) {
        SharedPreference.clearPref(context,EVENT_ID);
        SharedPreference.clearPref(context,EVENT_NAME);
        SharedPreference.clearPref(context,EVENT_LOGO);
        SharedPreference.clearPref(context,EVENT_BACKGROUD);
        SharedPreference.clearPref(context,EVENT_COLOR_1);
        SharedPreference.clearPref(context,EVENT_COLOR_2);
        SharedPreference.clearPref(context,EVENT_COLOR_3);
        SharedPreference.clearPref(context,EVENT_COLOR_4);
        SharedPreference.clearPref(context,EVENT_COLOR_5);
    }

 public static void logoutUser(Context context) {
        SharedPreference.clearPref(context,KEY_FNAME);
        SharedPreference.clearPref(context,KEY_LNAME);
        SharedPreference.clearPref(context,KEY_EMAIL);
        SharedPreference.clearPref(context,KEY_PASSWORD);
        SharedPreference.clearPref(context,KEY_DESIGNATION);
        SharedPreference.clearPref(context,KEY_COMPANY);
        SharedPreference.clearPref(context,KEY_MOBILE);
        SharedPreference.clearPref(context,KEY_TOKEN);
        SharedPreference.clearPref(context,EVENT_ID);
        SharedPreference.clearPref(context,KEY_CITY);
        SharedPreference.clearPref(context,KEY_GCM_ID);
        SharedPreference.clearPref(context,KEY_PROFILE_PIC);
        SharedPreference.clearPref(context,KEY_ATTENDEE_ID);
        SharedPreference.clearPref(context,ATTENDEE_STATUS);
        SharedPreference.clearPref(context,IS_LOGIN);
        SharedPreference.clearPref(context,IS_GOD);
        SharedPreference.clearPref(context,USER_TYPE);
        SharedPreference.clearPref(context,VERIFY_OTP);
        SharedPreference.clearPref(context,MIDDLE_NAME);
    }
}