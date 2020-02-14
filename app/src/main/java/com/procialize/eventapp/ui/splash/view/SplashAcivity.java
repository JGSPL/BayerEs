package com.procialize.eventapp.ui.splash.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.procialize.eventapp.R;
import com.procialize.eventapp.databinding.ActivityLoginBinding;
import com.procialize.eventapp.databinding.ActivitySplashAcivityBinding;
import com.procialize.eventapp.ui.login.view.LoginActivity;
import com.procialize.eventapp.ui.login.viewmodel.LoginViewModel;
import com.procialize.eventapp.ui.splash.model.Splash;
import com.procialize.eventapp.ui.splash.viewmodel.SplashViewModel;

public class SplashAcivity extends AppCompatActivity implements Splash {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashAcivityBinding activitysplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_acivity);
        activitysplashBinding.setViewModel(new SplashViewModel());
        activitysplashBinding.executePendingBindings();
    }

    @Override
    public void openLoginActivity() {

        Intent intent = new Intent(SplashAcivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void openMainActivity() {


    }
}
