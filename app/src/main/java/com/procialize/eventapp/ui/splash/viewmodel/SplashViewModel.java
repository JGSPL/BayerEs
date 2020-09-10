package com.procialize.eventapp.ui.splash.viewmodel;

import android.content.Context;
import android.os.Handler;

import com.procialize.eventapp.session.SessionManager;
import com.procialize.eventapp.ui.splash.view.SplashAcivity;

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
                if(session.isLoggedIn() == true) {
                    openMainActivity(context);

                }else{
                    openLoginActivity(context);

                }

            }

        }, SPLASH_TIME_OUT);
    }
}
