package com.procialize.eventapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {

    private static Context _context;

    public static ConnectionDetector connectionDetector = null;

    public static ConnectionDetector getInstance(Context context)
    {
        _context = context;
        if (connectionDetector == null)
            connectionDetector = new ConnectionDetector();

        return connectionDetector;
    }
    /*private ConnectionDetector(Context context) {
        this._context = context;
    }*/

    /**
     * Checking for all possible internet providers
     **/
    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }
}