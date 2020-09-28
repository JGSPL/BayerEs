package com.procialize.eventapp.ui.speaker.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
;
import com.google.firebase.database.DatabaseReference;

import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.SharedPreferencesConstant;
import com.procialize.eventapp.ui.attendee.viewmodel.AttendeeDetailsViewModel;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;
import com.procialize.eventapp.ui.speaker.model.Speaker;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_4;

public class SpeakerDetailActivity extends AppCompatActivity implements View.OnClickListener {
    String fname, lname, company, city, designation, prof_pic, attendee_type, mobile, email;
    TextView tv_attendee_name, tv_attendee_designation, tv_attendee_company_name, tv_attendee_city,
            tv_mobile, tv_email, tv_header, tv_contact;
    EditText et_message;
    LinearLayout  ll_main;
    ImageView iv_profile, iv_back, ic_email, iv_contact;
    ProgressBar progressView;
    public static final int RequestPermissionCode = 100;
    AttendeeDetailsViewModel attendeeDetailsViewModel;
    private DatabaseReference mConvDatabase;
    private DatabaseReference mUsersDatabase;
    private DatabaseReference mMessageDatabase;
    private FirebaseAuth mAuth;
    String mCurrent_user_id, attendeeid, firebase_id;
    LinearLayout bgLinear;
    View bgView;
    private Speaker speaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker_detail);

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
        tv_header = findViewById(R.id.tv_header);
        iv_contact = findViewById(R.id.iv_contact);
        tv_contact = findViewById(R.id.tv_contact);


        ll_main = findViewById(R.id.ll_main);

        tv_attendee_name.setText(fname + " " + lname);
        tv_attendee_designation.setText(designation);
        tv_attendee_company_name.setText(company);
        tv_attendee_city.setText(city);
        tv_mobile.setText(mobile);
        tv_email.setText(email);
        bgLinear = findViewById(R.id.bgLinear);
        bgView = findViewById(R.id.bgView);


        iv_back.setOnClickListener(this);

        CommonFunction.showBackgroundImage(SpeakerDetailActivity.this, ll_main);
        tv_attendee_name.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_1)));
        tv_header.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)));
        iv_back.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)), PorterDuff.Mode.SRC_ATOP);
        bgLinear.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this,EVENT_COLOR_2)));
        bgView.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this,EVENT_COLOR_2)));

        String eventColor3 = SharedPreference.getPref(this, EVENT_COLOR_3);

        String eventColor3Opacity40 = eventColor3.replace("#", "");

        tv_attendee_designation.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        tv_attendee_company_name.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        tv_attendee_city.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        tv_mobile.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        tv_email.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        if (prof_pic.trim() != null) {
            Glide.with(SpeakerDetailActivity.this)
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

        //--GETTING CURRENT USER ID---
      /*  mAuth = FirebaseAuth.getInstance();
        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        //---REFERENCE TO CHATS CHILD IN FIREBASE DATABASE-----
        mConvDatabase = FirebaseDatabase.getInstance().getReference().child("chats").child(mCurrent_user_id);

        //---OFFLINE FEATURE---
        mConvDatabase.keepSynced(true);

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mUsersDatabase.keepSynced(true);

        mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrent_user_id);*/
    }

    public void getIntentData() {
        Intent intent = getIntent();
        speaker = (Speaker) getIntent().getSerializableExtra("Speaker");

        fname = speaker.getFirst_name();
        lname = speaker.getLast_name();
        company = speaker.getCompany_name();
        city = speaker.getCity();
        designation = speaker.getDesignation();
        prof_pic = speaker.getProfile_picture();
        attendee_type = speaker.getAttendee_type();
        mobile = speaker.getMobile();
        email = speaker.getEmail();
        attendeeid = speaker.getAttendee_id();
        firebase_id = speaker.getFirebase_id();
    }

    @Override
    public void onClick(View v) {
        final String SprofilePic = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_PROFILE_PIC);
        final String SUserNmae = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_FNAME);
        final String SlName = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_LNAME);

        final String message = et_message.getText().toString().trim();
        switch (v.getId()) {
            case R.id.ll_send_message:

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
        ActivityCompat.requestPermissions(SpeakerDetailActivity.this, new String[]
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
                        try {
                            attendeeDetailsViewModel.saveContact(this, fname + " " + lname, company, mobile, designation, email);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        Toast.makeText(SpeakerDetailActivity.this, "We need your permission so you can enjoy full features of app", Toast.LENGTH_LONG).show();
                        RequestMultiplePermission();

                    }
                }

                break;
        }
    }


}
