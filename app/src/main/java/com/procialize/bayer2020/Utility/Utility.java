package com.procialize.bayer2020.Utility;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.format.DateFormat;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_3;

public class Utility {


    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 124;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 121;
    FragmentManager fragmentManager;

    //--- For fragments
    public Utility(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public static void createShortSnackBar(View view, String message) {
        try {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        }catch (Exception e)
        {}
    }

    public static void createLongSnackBar(View view,String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED )  {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                }
                else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkWritePermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED )  {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                }
                else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkCameraPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)  {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Camera permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                }
                else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getRealPathFromURI(Context context,Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static String decoded(String JWTEncoded) throws Exception {
        String[] split = new String[3];
        try {
            split = JWTEncoded.split("\\.");
            Log.d("JWT_DECODED", "Header: " + getJson(split[0]));
            Log.d("JWT_DECODED", "Body: " + getJson(split[1]));
            Log.d("JWT_DECODED", "Signiture: " + getJson(split[2]));
        } catch (UnsupportedEncodingException e) {
            //Error
        }
        return getJson(split[1]);
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

    public static int getTimeDifferenceInMillis(String dateTime) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String currentTimeString = sdf.format(new Date());

        Calendar now = Calendar.getInstance();
        now.add(Calendar.SECOND, 5);
        Date date = now.getTime();
        long currentTime = date.getTime();
        long endTime = 0;

        try {
            //Parsing the user Inputed time ("yyyy-MM-dd HH:mm:ss")
            endTime = dateFormat.parse(dateTime).getTime();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

        if (endTime > currentTime)
            return 1;
        else
            return 0;
    }

    public static void displayToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean isTimeGreater(String datetime) {
        Date currentdateTime, pickedTime;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTimeString = sdf.format(new Date());

        try {
            currentdateTime = sdf.parse(currentTimeString);
            pickedTime = sdf.parse(datetime);
            Calendar now = Calendar.getInstance();
            now.add(Calendar.SECOND, 5);
            Date date = now.getTime();
            long currentTime = date.getTime();
            long endTime = 0;

            try {
//Parsing the user Inputed time ("yyyy-MM-dd HH:mm:ss")
                endTime = sdf.parse(datetime).getTime();

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            Log.e("CDATE", "Current Date: " + currentdateTime);
            Log.e("PDATE", "Picked Date: " + pickedTime);


/*compare > 0, if date1 is greater than date2
compare = 0, if date1 is equal to date2
compare < 0, if date1 is smaller than date2*/

            if (pickedTime.compareTo(currentdateTime) > 0) {
                Log.e("TAG", "Picked Date is Greater than Current Date");
                if (endTime < currentTime)
                    return false;
                else
                    return true;
            } else if (pickedTime.compareTo(currentdateTime) == 0) {
                Log.e("TAG", "Picked Date is equal to Current Date");
                return false;
            } else {
                Log.e("TAG", "Picked Date is Less than Current Date");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }



    public static void setupPagerIndidcatorDots(Context context,int currentPage, LinearLayout ll_dots, int size) {

        TextView[] dots = new TextView[size];
        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(context);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(24);
           // dots[i].setTextColor(Color.parseColor("#343434"));
            String eventColor3 = SharedPreference.getPref(context, EVENT_COLOR_3);
            String eventColor3Opacity40 = eventColor3.replace("#", "");
            dots[i].setTextColor(Color.parseColor("#A2A2A2"));
            ll_dots.addView(dots[i]);
        }

        try {
            if (dots.length > 0) {
                if (dots.length != currentPage) {
                    dots[currentPage].setTextColor(Color.parseColor("#002e46"));
                    //dots[currentPage].setTextColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_1)));
                }
            }
        } catch (Exception e) {

        }
    }

    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("yyyy-MM-dd HH:mm:ss", cal).toString();
        return date;
    }

    public static void hideKeyboard(View view){
        try {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch(Exception ignored) {
            ignored.printStackTrace();
        }
    }

    public static CharSequence trimTrailingWhitespace(CharSequence source) {

        if(source == null)
            return "";

        int i = source.length();

        // loop back to the first non-whitespace character
        while(--i >= 0 && Character.isWhitespace(source.charAt(i))) {
        }

        return source.subSequence(0, i+1);
    }
}