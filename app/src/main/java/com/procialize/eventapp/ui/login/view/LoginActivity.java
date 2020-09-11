package com.procialize.eventapp.ui.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import com.procialize.eventapp.Constants.Constant;
import com.procialize.eventapp.R;
import com.procialize.eventapp.databinding.ActivityLoginBinding;
import com.procialize.eventapp.session.SessionManager;
import com.procialize.eventapp.ui.eventList.view.EventListActivity;
import com.procialize.eventapp.ui.login.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    public static ActivityLoginBinding activityLoginBinding;
    public static  SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        activityLoginBinding.setViewModel(new LoginViewModel(LoginActivity.this));
        activityLoginBinding.executePendingBindings();
    }

    @BindingAdapter({"toastMessage"})
    public static void runMe(View view, String message) {
        if (message != null) {
            if (message.equalsIgnoreCase("user found")) {
                activityLoginBinding.linearLoginView.setVisibility(View.GONE);
                activityLoginBinding.linearOTPView.setVisibility(View.VISIBLE);

               /* activityLoginBinding.txtOtp.setVisibility(View.VISIBLE);
                activityLoginBinding.liinearOtp.setVisibility(View.VISIBLE);
                activityLoginBinding.editOtp.setVisibility(View.VISIBLE);
                activityLoginBinding.txtLogin.setVisibility(View.INVISIBLE);
                activityLoginBinding.txtMobileno.setVisibility(View.INVISIBLE);
                activityLoginBinding.btnSubmit.setVisibility(View.INVISIBLE);
                activityLoginBinding.inEmail.setVisibility(View.INVISIBLE);*/
            } else if (message.equalsIgnoreCase("back")) {
                activityLoginBinding.linearLoginView.setVisibility(View.VISIBLE);
                activityLoginBinding.linearOTPView.setVisibility(View.GONE);
            } else if (message.equalsIgnoreCase("Successfully Login")) {
                view.getContext().startActivity(new Intent(view.getContext(), EventListActivity.class));
                //finish();
            } else {
                Constant.displayToast(view.getContext(), message);
            }
        }
    }


}
