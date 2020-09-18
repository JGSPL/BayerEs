package com.procialize.eventapp.ui.splash.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.procialize.eventapp.MainActivity;
import com.procialize.eventapp.R;
import com.procialize.eventapp.databinding.ActivitySplashAcivityBinding;
import com.procialize.eventapp.session.SessionManager;
import com.procialize.eventapp.ui.login.view.LoginActivity;
import com.procialize.eventapp.ui.splash.model.Splash;
import com.procialize.eventapp.ui.splash.viewmodel.SplashViewModel;

public class SplashAcivity extends AppCompatActivity implements Splash {
    public static SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);

        ActivitySplashAcivityBinding activitysplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_acivity);
        activitysplashBinding.setViewModel(new SplashViewModel(SplashAcivity.this));
        activitysplashBinding.executePendingBindings();
    }

    @Override
    public void openLoginActivity(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));

    }

    @Override
    public void openMainActivity(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));

    }
}
