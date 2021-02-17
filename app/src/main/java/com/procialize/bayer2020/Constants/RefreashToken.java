package com.procialize.bayer2020.Constants;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.procialize.bayer2020.ConnectionDetector;
import com.procialize.bayer2020.Database.EventAppDB;
import com.procialize.bayer2020.GetterSetter.validateOTP;
import com.procialize.bayer2020.MainActivity;
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.SharedPreferencesConstant;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.session.SessionManager;
import com.procialize.bayer2020.ui.eventList.view.EventListActivity;
import com.procialize.bayer2020.ui.login.view.LoginActivity;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.ENROLL_LEAP_FLAG;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EXPIRY_TIME;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.IS_GOD;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_EMAIL;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_FNAME;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_LNAME;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_MOBILE;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_TOKEN;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.MIDDLE_NAME;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.PROFILE_PIC;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.PROFILE_STATUS;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.TIME;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.TOTAL_EVENT;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.USER_TYPE;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.VERIFY_OTP;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.vToken;

public class RefreashToken {

    Context context;
    CryptLib cryptLib;
    APIService mApiService;
    String vtoken;

    public RefreashToken(Context context) {
        this.context = context;
        try {
            cryptLib = new CryptLib();
        } catch (Exception e) {

        }
    }

    public String decryptedData(String encrypteddata) {
        String decrypteddata = "";
        try {
            decrypteddata = cryptLib.decryptCipherTextWithRandomIV(encrypteddata, "512832C564C8C15535382F023B50B427");

        } catch (Exception e) {

        }
        return decrypteddata;
    }

    public void decodeRefreashToken(String data) {
        String authorizationtoken= CommonFunction.stripquotes(data);
        HashMap<String, String> map_token = new HashMap<>();
        map_token.put(AUTHERISATION_KEY, authorizationtoken);
        SharedPreference.putPref(context, map_token);

        JWT jwt = new JWT(data);

        Log.d("Response", jwt.toString());

        HashMap<String, String> map = new HashMap<>();
        map.put(KEY_ID, jwt.getClaim("id").asString());
        map.put(KEY_FNAME, jwt.getClaim("first_name").asString());
        //map.put(MIDDLE_NAME, jwt.getClaim("middle_name").asString());
        map.put(KEY_LNAME, jwt.getClaim("last_name").asString());
        map.put(KEY_MOBILE, jwt.getClaim("mobile").asString());
        map.put(KEY_EMAIL, jwt.getClaim("email").asString());
        map.put(KEY_TOKEN, jwt.getClaim("refresh_token").asString());
        map.put(USER_TYPE, jwt.getClaim("user_type").asString());
        map.put(VERIFY_OTP, jwt.getClaim("verify_otp").asString());
        map.put(PROFILE_STATUS, jwt.getClaim("profile_status").asString());
        map.put(ENROLL_LEAP_FLAG, jwt.getClaim("enrollleapflag").asString());
        map.put(PROFILE_PIC, jwt.getClaim("profile_pic").asString());
        map.put(IS_GOD, jwt.getClaim("is_god").asString());
      //  map.put(VERIFY_OTP, jwt.getClaim("verify_otp").asString());
        map.put(TOTAL_EVENT, jwt.getClaim("total_event").asString());
        map.put(TIME, jwt.getClaim("time").asString());

        map.put(EXPIRY_TIME, jwt.getClaim("expiry_time").asString());
        SharedPreference.putPref(context, map);
    }

    public void GetRefreashToken(final String username, final String otp, String access_token) {

        Log.e("Refresh Token===>", access_token);
        Log.e("username===>", username);
        Log.e("otp===>", otp);
        Log.e("access_token===>", access_token);
       /* Toast.makeText(context, "username ====> "+username, Toast.LENGTH_SHORT).show();
        Toast.makeText(context, "otp ====> "+otp, Toast.LENGTH_SHORT).show();
        Toast.makeText(context, "access_token ====> "+access_token, Toast.LENGTH_SHORT).show();*/

        mApiService.getRefreashToken("0", username, otp, access_token).enqueue(new Callback<validateOTP>() {
            @Override
            public void onResponse(Call<validateOTP> call, Response<validateOTP> response) {
                if (response.isSuccessful()) {
                    if(response.body().getHeader().get(0).getType().equalsIgnoreCase("error")) {
                        vtoken = SharedPreference.getPref(context, AUTHERISATION_KEY);

                        otpValidate(username, otp, vtoken);
                    }else {

                        RefreashToken refreashToken = new RefreashToken(context);
                        String data = refreashToken.decryptedData(response.body().getToken().toString().trim());
                        refreashToken.decodeRefreashToken(data);
                        Log.e("token===>", data);
                    }
                } else {
                    if (response.body() != null) {
                        Toast.makeText(context, response.body().toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        SessionManager.clearCurrentEvent(context);
                        SessionManager.logoutUser(context);
                        //EventAppDB.getDatabase(MainActivity.this).profileUpdateDao().deleteData();
                        EventAppDB.getDatabase(context).newsFeedDao().deleteNewsFeed();
                        EventAppDB.getDatabase(context).newsFeedDao().deleteNewsFeedMedia();
                        context.startActivity(new Intent(context, LoginActivity.class));
                    }
                }
            }

            @Override
            public void onFailure(Call<validateOTP> call, Throwable t) {
                //Toast.makeText(context, "Invalid Token", Toast.LENGTH_SHORT).show();
                SessionManager.clearCurrentEvent(context);
                SessionManager.logoutUser(context);
                //EventAppDB.getDatabase(MainActivity.this).profileUpdateDao().deleteData();
                EventAppDB.getDatabase(context).newsFeedDao().deleteNewsFeed();
                EventAppDB.getDatabase(context).newsFeedDao().deleteNewsFeedMedia();
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        });
    }

    public void callGetRefreashToken(Context context) {
        if (ConnectionDetector.getInstance(context).isConnectingToInternet()) {
            String expirytime = SharedPreference.getPref(context, SharedPreferencesConstant.EXPIRY_TIME);
            String timestamp_expiry = Utility.getDate(Long.parseLong(expirytime));
            boolean isvalidtoken = Utility.isTimeGreater(String.valueOf(timestamp_expiry));
            String accesstoken = SharedPreference.getPref(context, KEY_TOKEN);
            Log.e("Access Token===>", accesstoken);
            String accesstoken1 = accesstoken;
            if (isvalidtoken == false) {
                String username = SharedPreference.getPref(context, SharedPreferencesConstant.KEY_EMAIL);
                String otp = SharedPreference.getPref(context, SharedPreferencesConstant.OTP);
                String accesstoken2 = SharedPreference.getPref(context, KEY_TOKEN);
                mApiService = ApiUtils.getAPIService();


                String api_token = SharedPreference.getPref(context, AUTHERISATION_KEY);
                JWT jwt = new JWT(api_token);
                accesstoken2 = jwt.getClaim("refresh_token").asString();
                GetRefreashToken(username, otp, accesstoken2);
            } else {
                Log.d("TAG", "Token is already refreashed");
            }
        }
    }

    private void otpValidate(String username, final String otp, String vtoken) {
        mApiService.validateOTP("0", username, otp, vtoken).enqueue(new Callback<validateOTP>() {
            @Override
            public void onResponse(Call<validateOTP> call, Response<validateOTP> response) {
                if (response.isSuccessful()) {
                    RefreashToken refreashToken = new RefreashToken(context);
                    String data = refreashToken.decryptedData(response.body().getToken().toString().trim());
                    refreashToken.decodeRefreashToken(data);
                    HashMap<String, String> map = new HashMap<>();
                    map.put(SharedPreferencesConstant.OTP, otp);
                    String authorizationtolen= CommonFunction.stripquotes(data);
                    map.put(AUTHERISATION_KEY, authorizationtolen);
                    SharedPreference.putPref(context, map);
                } else {
                }
            }

            @Override
            public void onFailure(Call<validateOTP> call, Throwable t) {
            }
        });
    }
}