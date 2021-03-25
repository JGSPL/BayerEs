package com.procialize.bayer2020.ui.splash.viewmodel;

import android.content.Context;
import android.os.Handler;

import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.session.SessionManager;
import com.procialize.bayer2020.ui.splash.view.SplashAcivity;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.ISPROFILE_COMPLETE;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.IS_LOGIN;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_FNAME;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.USER_TYPE;

public class SplashViewModel extends SplashAcivity{

    private static int SPLASH_TIME_OUT = 3000;
    Context context;
    SessionManager session;

    public SplashViewModel(Context context) {
        this.context=context;
        this.movetonext();
        session = new SessionManager(context);
    }

    public void movetonext() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                String isLogin = SharedPreference.getPref(context,IS_LOGIN);
                String iscomplete = SharedPreference.getPref(context,ISPROFILE_COMPLETE);
                String userType = SharedPreference.getPref(context,USER_TYPE);
                String fName = SharedPreference.getPref(context,KEY_FNAME);

                if(isLogin.equalsIgnoreCase("true")) {

                    if(iscomplete.equalsIgnoreCase("true")) {
                        if(fName.equalsIgnoreCase("")|| fName.equalsIgnoreCase("null")|| fName.equalsIgnoreCase(null)){
                            if(userType.equalsIgnoreCase("D")){
                                openProfileActivity(context);
                            }else {
                                openProfilePCOActivity(context);
                            }
                        }else{
                            openMainActivity(context);
                        }

                    }else{
                        if(userType.equalsIgnoreCase("D")){
                            openProfileActivity(context);
                        }else {
                            openProfilePCOActivity(context);
                        }

                    }

                }else{
                    openLoginActivity(context);

                }

            }

        }, SPLASH_TIME_OUT);
    }
}
