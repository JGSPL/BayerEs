package com.procialize.bayer2020.ui.splash.viewmodel;

import android.content.Context;
import android.os.Handler;

import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.session.SessionManager;
import com.procialize.bayer2020.ui.splash.view.SplashAcivity;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.IS_LOGIN;

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
                if(isLogin.equalsIgnoreCase("true")) {
                    openMainActivity(context);
                }else{
                    openLoginActivity(context);

                }

            }

        }, SPLASH_TIME_OUT);
    }
}