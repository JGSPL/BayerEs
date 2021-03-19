package com.procialize.bayer2020.ui.splash.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.procialize.bayer2020.MainActivity;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.databinding.ActivitySplashAcivityBinding;
import com.procialize.bayer2020.session.SessionManager;
import com.procialize.bayer2020.ui.login.view.LoginActivity;
import com.procialize.bayer2020.ui.profile.view.ProfileActivity;
import com.procialize.bayer2020.ui.profile.view.ProfilePCOActivity;
import com.procialize.bayer2020.ui.splash.model.Splash;
import com.procialize.bayer2020.ui.splash.viewmodel.SplashViewModel;

public class SplashAcivity extends AppCompatActivity implements Splash {
    public static SessionManager sessionManager;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        ActivitySplashAcivityBinding activitysplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_acivity);
        activitysplashBinding.setViewModel(new SplashViewModel(SplashAcivity.this));
        activitysplashBinding.executePendingBindings();
    }

    @Override
    public void openLoginActivity(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
        ((Activity)context).finish();
    }

    @Override
    public void openMainActivity(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
        ((Activity)context).finish();
    }
    @Override
    public void openProfilePCOActivity(Context context) {
        context.startActivity(new Intent(context, ProfilePCOActivity.class));
        ((Activity)context).finish();
    }
    @Override
    public void openProfileActivity(Context context) {
        context.startActivity(new Intent(context, ProfileActivity.class));
        ((Activity)context).finish();
    }
}
