package com.procialize.eventapp.ui.attendee.view;

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
import androidx.lifecycle.MutableLiveData;
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
import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFirebase;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.SharedPreferencesConstant;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.ui.attendee.viewmodel.AttendeeDetailsViewModel;
import com.procialize.eventapp.ui.attendeeChat.ChatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;

public class AttendeeDetailActivity extends AppCompatActivity implements View.OnClickListener {
    String fname, lname, company, city, designation, prof_pic, attendee_type, mobile, email;
    TextView tv_header, tv_contact, tv_attendee_name, tv_attendee_designation, tv_attendee_company_name, tv_attendee_city, tv_mobile, tv_email, tv_sendmess;
    EditText et_message;
    LinearLayout ll_send_message, ll_save_contact, ll_main, ll_save_contact_inner;
    ImageView iv_profile, iv_back, ic_email, iv_contact;
    ProgressBar progressView;
    public static final int RequestPermissionCode = 100;
    AttendeeDetailsViewModel attendeeDetailsViewModel;
    private DatabaseReference mConvDatabase;
    private DatabaseReference mUsersDatabase;
    private DatabaseReference mMessageDatabase;
    private FirebaseAuth mAuth;
    String mCurrent_user_id, attendeeid, firebase_id;
    APIService updateApi;
    MutableLiveData<LoginOrganizer> chatUpdate = new MutableLiveData<>();
    String api_token, eventid;
    LinearLayout bgLinear;
    View bgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_detail);

        try {
            Intent intent = getIntent();
            CommonFunction.saveBackgroundImage(AttendeeDetailActivity.this, intent.getStringExtra("eventBg"));
        } catch (Exception e) {

        }
        getIntentData();
        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(this, EVENT_ID);
        CommonFirebase.crashlytics("AttendeeDetail", api_token);
        CommonFirebase.firbaseAnalytics(this, "AttendeeDetail", api_token);

        attendeeDetailsViewModel = ViewModelProviders.of(this).get(AttendeeDetailsViewModel.class);
        progressView = findViewById(R.id.progressView);
        iv_profile = findViewById(R.id.iv_profile);
        iv_back = findViewById(R.id.iv_back);
        ic_email = findViewById(R.id.ic_email);
        tv_attendee_name = findViewById(R.id.tv_attendee_name);
        tv_attendee_designation = findViewById(R.id.tv_attendee_designation);
        tv_attendee_company_name = findViewById(R.id.tv_attendee_company_name);
        tv_attendee_city = findViewById(R.id.tv_attendee_city);
        tv_mobile = findViewById(R.id.tv_mobile);
        tv_email = findViewById(R.id.tv_email);
        et_message = findViewById(R.id.et_message);
        tv_sendmess = findViewById(R.id.tv_sendmess);
        tv_header = findViewById(R.id.tv_header);
        ll_save_contact_inner = findViewById(R.id.ll_save_contact_inner);
        iv_contact = findViewById(R.id.iv_contact);
        tv_contact = findViewById(R.id.tv_contact);

        bgLinear = findViewById(R.id.bgLinear);
        bgView = findViewById(R.id.bgView);


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

        CommonFunction.showBackgroundImage(this, ll_main);


        iv_back.setOnClickListener(this);

        CommonFunction.showBackgroundImage(AttendeeDetailActivity.this, ll_main);
        tv_attendee_name.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_1)));
        tv_header.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)));
        iv_back.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)), PorterDuff.Mode.SRC_ATOP);
        ll_send_message.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_1)));
        ic_email.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)), PorterDuff.Mode.SRC_ATOP);
        tv_sendmess.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)));
        ll_save_contact.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_1)));
        ll_save_contact_inner.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)));
        iv_contact.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_1)), PorterDuff.Mode.SRC_ATOP);
        tv_contact.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_1)));

        bgLinear.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)));
        bgView.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)));

        String eventColor3 = SharedPreference.getPref(this, EVENT_COLOR_3);
        et_message.setHintTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)));
        et_message.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)));

        String eventColor3Opacity40 = eventColor3.replace("#", "");

        tv_attendee_designation.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        tv_attendee_company_name.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        tv_attendee_city.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        tv_mobile.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        tv_email.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));

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
        try {
            mAuth = FirebaseAuth.getInstance();
            mCurrent_user_id = mAuth.getCurrentUser().getUid();

            //---REFERENCE TO CHATS CHILD IN FIREBASE DATABASE-----
            mConvDatabase = FirebaseDatabase.getInstance().getReference().child("chats").child(mCurrent_user_id);

            //---OFFLINE FEATURE---
            mConvDatabase.keepSynced(true);

            mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users");
            mUsersDatabase.keepSynced(true);

            mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrent_user_id);
        } catch (Exception e) {
            e.printStackTrace();
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
        attendeeid = intent.getStringExtra("attendeeid");
        firebase_id = intent.getStringExtra("firebase_id");
    }

    @Override
    public void onClick(View v) {
        final String SprofilePic = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_PROFILE_PIC);
        final String SUserNmae = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_FNAME);
        final String SlName = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_LNAME);
        final String FireEmaile = SharedPreference.getPref(this, SharedPreferencesConstant.FIREBASEUSER_NAME);

        final String message = et_message.getText().toString().trim();
        switch (v.getId()) {
            case R.id.ll_send_message:

                if (message.isEmpty()) {
                    Utility.createShortSnackBar(ll_main, "Please enter message..");

                } else {
                    if (firebase_id.equalsIgnoreCase("0")) {
                        et_message.setText("");
                        Utility.createShortSnackBar(ll_main, "User not valid for chat...!");

                    } else {
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
                                    getChatUpdate(api_token, eventid, attendeeid);

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

                if (message.isEmpty()) {
                    Utility.createShortSnackBar(ll_main, "Please enter message..");

                } else {
                    if (firebase_id.equalsIgnoreCase("0")) {
                        et_message.setText("");
                        Utility.createShortSnackBar(ll_main, "User not valid for chat...!");

                    } else {
                        if (ConnectionDetector.getInstance(this).isConnectingToInternet()) {
                            mUsersDatabase.child(firebase_id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    getChatUpdate(api_token, eventid, attendeeid);

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
                                    getChatUpdate(api_token,eventid,firebase_id);

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
                        attendeeDetailsViewModel.saveContact(this, fname + " " + lname, company, mobile, designation, email);
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

    //Update Api
    public MutableLiveData<LoginOrganizer> getChatUpdate(final String token, final String event_id, String receiver_id) {
        updateApi = ApiUtils.getAPIService();

        updateApi.UpdateChatStatus(token, event_id, receiver_id).enqueue(new Callback<LoginOrganizer>() {
            @Override
            public void onResponse(Call<LoginOrganizer> call,
                                   Response<LoginOrganizer> response) {
                if (response.isSuccessful()) {
                    chatUpdate.setValue(response.body());
                    // Utility.createShortSnackBar(ll_main,"Chat info updated");

                }
            }

            @Override
            public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                chatUpdate.setValue(null);

            }
        });
        return chatUpdate;
    }


}