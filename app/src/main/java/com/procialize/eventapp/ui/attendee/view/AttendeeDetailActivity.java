package com.procialize.eventapp.ui.attendee.view;

import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.SharedPreferencesConstant;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.ui.attendee.viewmodel.AttendeeDetailsViewModel;
import com.procialize.eventapp.ui.attendeeChat.ChatActivity;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;

public class AttendeeDetailActivity extends AppCompatActivity implements View.OnClickListener {
    String fname, lname, company, city, designation, prof_pic, attendee_type,mobile,email;
    TextView tv_attendee_name, tv_attendee_designation, tv_attendee_company_name, tv_attendee_city,tv_mobile,tv_email,tv_sendmess;
    EditText et_message;
    LinearLayout ll_send_message, ll_save_contact,ll_main;
    ImageView iv_profile,iv_back;
    ProgressBar progressView;
    public static final int RequestPermissionCode = 100;
    AttendeeDetailsViewModel attendeeDetailsViewModel;
    private DatabaseReference mConvDatabase;
    private DatabaseReference mUsersDatabase;
    private DatabaseReference mMessageDatabase;
    private FirebaseAuth mAuth;
    String  mCurrent_user_id,attendeeid, firebase_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_detail);

        try {
            Intent intent = getIntent();
            CommonFunction.saveBackgroundImage(AttendeeDetailActivity.this,intent.getStringExtra("eventBg"));
        }catch (Exception e) {

        }
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
        tv_sendmess = findViewById(R.id.tv_sendmess);

        ll_send_message = findViewById(R.id.ll_send_message);
        ll_save_contact = findViewById(R.id.ll_save_contact);
        ll_main = findViewById(R.id.ll_main);
        ll_save_contact.setOnClickListener(this);
        ll_send_message.setOnClickListener(this);
        tv_sendmess.setOnClickListener(this);

        tv_attendee_name.setText(fname + " " + lname);
        tv_attendee_designation.setText(designation);
        tv_attendee_company_name.setText(company);
        tv_attendee_city.setText(city);
        tv_mobile.setText(mobile);
        tv_email.setText(email);

        iv_back.setOnClickListener(this);
        CommonFunction.showBackgroundImage(this, ll_main);
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

        //--GETTING CURRENT USER ID---
        mAuth= FirebaseAuth.getInstance();
        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        //---REFERENCE TO CHATS CHILD IN FIREBASE DATABASE-----
        mConvDatabase = FirebaseDatabase.getInstance().getReference().child("chats").child(mCurrent_user_id);

        //---OFFLINE FEATURE---
        mConvDatabase.keepSynced(true);

        mUsersDatabase=FirebaseDatabase.getInstance().getReference().child("users");
        mUsersDatabase.keepSynced(true);

        mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrent_user_id);
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
        attendeeid = intent.getStringExtra("attendeeid");
        firebase_id = intent.getStringExtra("firebase_id");
    }

    @Override
    public void onClick(View v) {
        final String SprofilePic = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_PROFILE_PIC);
        final String SUserNmae = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_FNAME);
        final String SlName = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_LNAME);

        final String message = et_message.getText().toString().trim();
        switch (v.getId()) {
            case R.id.ll_send_message:

                if(message.isEmpty())
                {
                    Utility.createShortSnackBar(ll_main,"Please enter message..");

                }
                else
                {
                    if(firebase_id.equalsIgnoreCase("0")){
                        et_message.setText("");
                        Utility.createShortSnackBar(ll_main, "User not valid for chat...!");

                    }else {
                        if (ConnectionDetector.getInstance(this).isConnectingToInternet()) {
                            mUsersDatabase.child(firebase_id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                               /* final String userName = dataSnapshot.child("name").getValue().toString();
                                String userThumb = dataSnapshot.child("thumb_image").getValue().toString();

                                if(dataSnapshot.hasChild("online")){

                                    String userOnline = dataSnapshot.child("online").getValue().toString();
                                    //  convViewHolder.setUserOnline(userOnline);

                                }*/
                                    //  convViewHolder.setName(userName);
                                    // convViewHolder.setUserImage(userThumb,this);

                                    //--OPENING CHAT ACTIVITY FOR CLICKED USER----

                                    Intent chatIntent = new Intent(AttendeeDetailActivity.this, ChatActivity.class);
                                    chatIntent.putExtra("user_id", firebase_id);
                                    chatIntent.putExtra("user_name", fname + " " + lname);
                                    chatIntent.putExtra("loginUser_name", SUserNmae + " " + SlName);
                                    chatIntent.putExtra("sProfilePic", SprofilePic);
                                    chatIntent.putExtra("rProfilepic", prof_pic);
                                    chatIntent.putExtra("attendeeid", attendeeid);
                                    chatIntent.putExtra("firebase_id", firebase_id);
                                    chatIntent.putExtra("Message", message);
                                    chatIntent.putExtra("page", "AttendeeDetail");

                                    chatIntent.putExtra("lname", lname);
                                    chatIntent.putExtra("company", company);
                                    chatIntent.putExtra("city", city);
                                    chatIntent.putExtra("designation", designation);
                                    chatIntent.putExtra("attendee_type", attendee_type);
                                    chatIntent.putExtra("mobile", mobile);
                                    chatIntent.putExtra("email", email);

                                    startActivity(chatIntent);
                                    finish();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        } else {
                            Utility.createShortSnackBar(ll_main, "No internet connection..!");
                        }
                    }
                }
                break;
            case R.id.tv_sendmess:

                if(message.isEmpty())
                {
                    Utility.createShortSnackBar(ll_main,"Please enter message..");

                }
                else
                {
                    if(firebase_id.equalsIgnoreCase("0")){
                        et_message.setText("");
                        Utility.createShortSnackBar(ll_main, "User not valid for chat...!");

                    }else {
                        if (ConnectionDetector.getInstance(this).isConnectingToInternet()) {
                            mUsersDatabase.child(firebase_id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                    Intent chatIntent = new Intent(AttendeeDetailActivity.this, ChatActivity.class);
                                    chatIntent.putExtra("user_id", firebase_id);
                                    chatIntent.putExtra("user_name", fname + " " + lname);
                                    chatIntent.putExtra("loginUser_name", SUserNmae + " " + SlName);
                                    chatIntent.putExtra("sProfilePic", SprofilePic);
                                    chatIntent.putExtra("rProfilepic", prof_pic);
                                    chatIntent.putExtra("attendeeid", attendeeid);
                                    chatIntent.putExtra("firebase_id", firebase_id);
                                    chatIntent.putExtra("Message", message);
                                    chatIntent.putExtra("page", "AttendeeDetail");

                                    chatIntent.putExtra("lname", lname);
                                    chatIntent.putExtra("company", company);
                                    chatIntent.putExtra("city", city);
                                    chatIntent.putExtra("designation", designation);
                                    chatIntent.putExtra("attendee_type", attendee_type);
                                    chatIntent.putExtra("mobile", mobile);
                                    chatIntent.putExtra("email", email);

                                    startActivity(chatIntent);
                                    finish();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        } else {
                            Utility.createShortSnackBar(ll_main, "No internet connection..!");
                        }
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
                        try {
                            attendeeDetailsViewModel.saveContact(this, fname + " " + lname, company, mobile, designation, email);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {

                        Toast.makeText(AttendeeDetailActivity.this, "We need your permission so you can enjoy full features of app", Toast.LENGTH_LONG).show();
                        RequestMultiplePermission();

                    }
                }

                break;
        }
    }



}