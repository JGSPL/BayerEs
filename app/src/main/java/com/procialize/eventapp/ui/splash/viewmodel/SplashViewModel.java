package com.procialize.eventapp.ui.splash.viewmodel;

import android.os.Handler;

import com.procialize.eventapp.ui.splash.view.SplashAcivity;

public class SplashViewModel extends SplashAcivity{

    private static int SPLASH_TIME_OUT = 3000;


    public SplashViewModel() {
        this.movetonext();
    }

    public void movetonext() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                openLoginActivity();

            }

        }, SPLASH_TIME_OUT);
    }
}
