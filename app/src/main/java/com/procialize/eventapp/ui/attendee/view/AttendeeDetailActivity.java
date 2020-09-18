package com.procialize.eventapp.ui.attendee.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.ui.attendee.viewmodel.AttendeeDetailsViewModel;
import com.procialize.eventapp.ui.profile.viewModel.ProfileActivityViewModel;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;

public class AttendeeDetailActivity extends AppCompatActivity implements View.OnClickListener {
    String fname, lname, company, city, designation, prof_pic, attendee_type,mobile,email;
    TextView tv_attendee_name, tv_attendee_designation, tv_attendee_company_name, tv_attendee_city,tv_mobile,tv_email;
    EditText et_message;
    LinearLayout ll_send_message, ll_save_contact,ll_main;
    ImageView iv_profile,iv_back;
    ProgressBar progressView;
    public static final int RequestPermissionCode = 100;
    AttendeeDetailsViewModel attendeeDetailsViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_detail);

        getIntentData();
        attendeeDetailsViewModel = ViewModelProviders.of(this).get(AttendeeDetailsViewModel.class);
        progressView = findViewById(R.id.progressView);
        iv_profile = findViewById(R.id.iv_profile);
        iv_back = findViewById(R.id.iv_back);
        tv_attendee_name = findViewById(R.id.tv_attendee_name);
        tv_attendee_designation = findViewById(R.id.tv_attendee_designation);
        tv_attendee_company_name = findViewById(R.id.tv_attendee_company_name);
        tv_attendee_city = findViewById(R.id.tv_attendee_city);
        tv_mobile = findViewById(R.id.tv_mobile);
        tv_email = findViewById(R.id.tv_email);
        et_message = findViewById(R.id.et_message);
        ll_send_message = findViewById(R.id.ll_send_message);
        ll_save_contact = findViewById(R.id.ll_save_contact);
        ll_main = findViewById(R.id.ll_main);
        ll_save_contact.setOnClickListener(this);
        ll_send_message.setOnClickListener(this);

        tv_attendee_name.setText(fname + " " + lname);
        tv_attendee_designation.setText(designation);
        tv_attendee_company_name.setText(company);
        tv_attendee_city.setText(city);
        tv_mobile.setText(mobile);
        tv_email.setText(email);

        iv_back.setOnClickListener(this);

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
        mobile = intent.getStringExtra("mobile");
        email = intent.getStringExtra("email");
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
                try {
                    if (!CheckingPermissionIsEnabledOrNot()) {
                        RequestMultiplePermission();
                    } else {
                        attendeeDetailsViewModel.saveContact(this,fname+" "+lname,company,mobile,designation,email);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    public boolean CheckingPermissionIsEnabledOrNot() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_CONTACTS);
        int ForthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ForthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestMultiplePermission() {
        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(AttendeeDetailActivity.this, new String[]
                {
                        WRITE_CONTACTS,
                        READ_CONTACTS
                }, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {
                    boolean readContactPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeContactpermjission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (readContactPermission && writeContactpermjission) {
//                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(AttendeeDetailActivity.this, "We need your permission so you can enjoy full features of app", Toast.LENGTH_LONG).show();
                        RequestMultiplePermission();

                    }
                }

                break;
        }
    }



}