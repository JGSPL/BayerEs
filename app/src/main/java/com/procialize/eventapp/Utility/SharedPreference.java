package com.procialize.eventapp.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Iterator;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.SHARED_PREF;


public class SharedPreference {

    Context context;

    public SharedPreference(Context context) {
        this.context = context;
    }


    /*------------------------------------------------------*
     *             Shared Preferences Functions              *
     *-------------------------------------------------------*/
    /*HashMap<String, String> map = new HashMap<String, String>();
       map.put("key", "value");
       putPref(map);*/
    //------ Write Shared Preferences
    public void putPref(HashMap<String, String> map) {
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
    public String getPref(String prefKey) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return preferences.getString(prefKey.trim(), "");
    }

    //--- Clear All Preferences ---
    public void clearAllPref() {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
    }

    //--- Clear single preferences ---
    public void clearPref(String prefKey) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        preferences.edit().remove(prefKey).commit();
    }

}
