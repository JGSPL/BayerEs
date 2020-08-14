package com.procialize.eventapp.Utility;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Utility {


    FragmentManager fragmentManager;

    //--- For fragments
    public Utility(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public static void createShortToast(Context context,String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void createLongToast(Context context,String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /*------------------------------------------------------*
     *                    General Functions                  *
     *-------------------------------------------------------*/

    /*--- Change Fragments with Previous Fragment
         fragment = Fragment Object,
         Tag =  Fragment TAG
         addToBackStack = true/false
    */
    public void replaceFragment(Fragment fragment, int layout, String TAG, Boolean addToBackStack, int anim) {
        //--- Fragment Manager initialization
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(layout, fragment, TAG);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(TAG);
        }
        fragmentTransaction.commit();
    }

    public void addFragment(Fragment fragment, int layout, String TAG, Boolean addToBackStack, int anim) {
        //--- Fragment Manager initialization
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(layout, fragment, TAG);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(TAG);
        }
        fragmentTransaction.commit();
    }

    //--- get unique Device id --
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    //--- get IMEI number of Device
    public static  String getIMEINumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId().toString();

    }


    //--- get ip address of mobile
    public static String getIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {

        }
        return null;
    }

    //---- Px to Dp Converter
    public static int pxToDp(Context context,int px) {
        float density = context.getResources().getDisplayMetrics().density;
        int dp = (int) (px * density + 0.5f);
        return dp;
    }


    //--- Clear full BackStack
    public void clearFullStack() {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    //--- get Email ID from Device
    public static String getDeviceEmailId(Context context) {
        String email_id = null;
        AccountManager accManager = AccountManager.get(context);
        Account acc[] = accManager.getAccountsByType("com.google");
        int accCount = acc.length;

        for (int i = 0; i < accCount; i++) {
            email_id = acc[i].name;
        }
        return email_id;
    }

}
