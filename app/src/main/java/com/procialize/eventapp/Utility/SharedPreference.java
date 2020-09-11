package com.procialize.eventapp.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.procialize.eventapp.Constants.AutherisationKey;

import java.util.HashMap;
import java.util.Iterator;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.SHARED_PREF;


public class SharedPreference {


    /*------------------------------------------------------*
     *             Shared Preferences Functions              *
     *-------------------------------------------------------*/

    //------ Write Shared Preferences
    public static void putPref(Context context,HashMap<String, String> map) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) iterator.next();
            //System.out.println(pair.getKey()+" : "+pair.getValue());
            editor.putString(pair.getKey().toString().trim(), pair.getValue().toString().trim());
            iterator.remove();
        }
        editor.commit();
    }

    //--- Read Preferences -----
    public static String getPref(Context context,String prefKey) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return preferences.getString(prefKey.trim(), "");
    }

    //--- Clear All Preferences ---
    public static void clearAllPref(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
    }

    //--- Clear single preferences ---
    public static void clearPref(Context context,String prefKey) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        preferences.edit().remove(prefKey).commit();
    }

}
