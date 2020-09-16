package com.procialize.eventapp.ui.attendee.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.Utility;

public class AttendeeDetailActivity extends AppCompatActivity implements View.OnClickListener {
    String fname, lname, company, city, designation, prof_pic, attendee_type;
    TextView tv_attendee_name, tv_attendee_designation, tv_attendee_company_name, tv_attendee_city;
    EditText et_message;
    LinearLayout ll_send_message, ll_save_contact,ll_main;
    ImageView iv_profile;
    ProgressBar progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_detail);

        getIntentData();

        progressView = findViewById(R.id.progressView);
        iv_profile = findViewById(R.id.iv_profile);
        tv_attendee_name = findViewById(R.id.tv_attendee_name);
        tv_attendee_designation = findViewById(R.id.tv_attendee_designation);
        tv_attendee_company_name = findViewById(R.id.tv_attendee_company_name);
        tv_attendee_city = findViewById(R.id.tv_attendee_city);
        et_message = findViewById(R.id.et_message);
        ll_send_message = findViewById(R.id.ll_send_message);
        ll_save_contact = findViewById(R.id.ll_save_contact);
        ll_main = findViewById(R.id.ll_main);

        tv_attendee_name.setText(fname + " " + lname);
        tv_attendee_designation.setText(designation);
        tv_attendee_company_name.setText(company);
        tv_attendee_city.setText(city);

        if (prof_pic.trim() != null) {
            Glide.with(AttendeeDetailActivity.this)
                    .load(prof_pic.trim())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressView.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressView.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(iv_profile);
        }
    }

    public void getIntentData() {
        Intent intent = getIntent();
        fname = intent.getStringExtra("fname");
        lname = intent.getStringExtra("lname");
        company = intent.getStringExtra("company");
        city = intent.getStringExtra("city");
        designation = intent.getStringExtra("designation");
        prof_pic = intent.getStringExtra("prof_pic");
        attendee_type = intent.getStringExtra("attendee_type");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_send_message:
                String message = et_message.getText().toString().trim();
                if(message.isEmpty())
                {
                    Utility.createShortSnackBar(ll_main,"Please enter message..");
                }
                else
                {
                    if(ConnectionDetector.getInstance(this).isConnectingToInternet())
                    {

                    }
                    else
                    {
                        Utility.createShortSnackBar(ll_main,"No internet connection..!");
                    }
                }
                break;
            case R.id.ll_save_contact:
                break;
        }
    }
}