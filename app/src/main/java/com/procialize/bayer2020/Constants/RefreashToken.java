package com.procialize.bayer2020.Constants;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.procialize.bayer2020.ConnectionDetector;
import com.procialize.bayer2020.GetterSetter.validateOTP;
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.SharedPreferencesConstant;
import com.procialize.bayer2020.Utility.Utility;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EXPIRY_TIME;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.IS_GOD;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_EMAIL;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_FNAME;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_LNAME;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_MOBILE;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_TOKEN;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.TIME;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.TOTAL_EVENT;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.USER_TYPE;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.VERIFY_OTP;

public class RefreashToken {

    Context context;
    CryptLib cryptLib;
    APIService mApiService;

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
        map.put(KEY_FNAME, jwt.getClaim("first_name").asString());
        map.put(KEY_LNAME, jwt.getClaim("last_name").asString());
        map.put(KEY_EMAIL, jwt.getClaim("email").asString());
        map.put(KEY_MOBILE, jwt.getClaim("mobile").asString());
        map.put(KEY_TOKEN, jwt.getClaim("refresh_token").asString());
        map.put(USER_TYPE, jwt.getClaim("user_type").asString());
        map.put(IS_GOD, jwt.getClaim("is_god").asString());
        map.put(VERIFY_OTP, jwt.getClaim("verify_otp").asString());
        map.put(EXPIRY_TIME, jwt.getClaim("expiry_time").asString());
        map.put(TIME, jwt.getClaim("time").asString());
        map.put(TOTAL_EVENT, jwt.getClaim("total_event").asString());
        SharedPreference.putPref(context, map);

    }

    public void GetRefreashToken(String username, String otp, String access_token) {
        mApiService.getRefreashToken("0", username, otp, access_token).enqueue(new Callback<validateOTP>() {
            @Override
            public void onResponse(Call<validateOTP> call, Response<validateOTP> response) {
                if (response.isSuccessful()) {
                    RefreashToken refreashToken = new RefreashToken(context);
                    String data = refreashToken.decryptedData(response.body().getToken().toString().trim());
                    refreashToken.decodeRefreashToken(data);

                } else {
                    if (response.body() != null) {

                        Toast.makeText(context, "Invalid Token", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Invalid Token", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<validateOTP> call, Throwable t) {

            }
        });
    }

    public void callGetRefreashToken(Context context) {
        if(ConnectionDetector.getInstance(context).isConnectingToInternet()) {
            String expirytime = SharedPreference.getPref(context, SharedPreferencesConstant.EXPIRY_TIME);
            String timestamp_expiry = Utility.getDate(Long.parseLong(expirytime));
            boolean isvalidtoken = Utility.isTimeGreater(String.valueOf(timestamp_expiry));
            String accesstoken = SharedPreference.getPref(context, KEY_TOKEN);
            String accesstoken1 =accesstoken;
            if (isvalidtoken == false) {
                String username = SharedPreference.getPref(context, SharedPreferencesConstant.KEY_EMAIL);
                String otp = SharedPreference.getPref(context, SharedPreferencesConstant.OTP);
                String accesstoken2 = SharedPreference.getPref(context, KEY_TOKEN);
                mApiService = ApiUtils.getAPIService();
                GetRefreashToken(username, otp, accesstoken2);
            } else {
                Log.d("TAG", "Token is already refreashed");
            }
        }
    }
}