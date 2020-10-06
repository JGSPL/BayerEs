package com.procialize.eventapp.ui.speaker.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
;
import com.google.firebase.database.DatabaseReference;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.ui.attendee.viewmodel.AttendeeDetailsViewModel;
import com.procialize.eventapp.ui.speaker.adapter.PdfAdapter;
import com.procialize.eventapp.ui.speaker.model.FetchSpeaker;
import com.procialize.eventapp.ui.speaker.model.Speaker;
import com.procialize.eventapp.ui.speaker.model.Speaker_Doc;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;


public class SpeakerDetailActivity extends AppCompatActivity implements View.OnClickListener, PdfAdapter.PdfListAdapterListner {
    String fname, lname, company, city, designation, prof_pic, attendee_type, mobile, email,description;
    TextView tv_attendee_name, tv_attendee_designation, tv_attendee_company_name, tv_attendee_city,
            tv_mobile, tv_email, tv_header, tv_contact,tvdesc, tvRateHeader;
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
    RelativeLayout ratinglayout, layoutTop;
    RatingBar ratingbar;
    RecyclerView rv_pdf_list;
    Button ratebtn;
    float ratingval;
    APIService updateApi;
    MutableLiveData<FetchSpeaker> speakerDetail = new MutableLiveData<>();
    MutableLiveData<LoginOrganizer> speakerRate = new MutableLiveData<>();
    List<Speaker_Doc> pdf_list;
    private ConnectionDetector cd;
    String api_token, eventid;
     View vwRateLine;
     LinearLayout llMAin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker_detail);
        cd = ConnectionDetector.getInstance(this);
        new RefreashToken(this).callGetRefreashToken(this);


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
        tvdesc = findViewById(R.id.tvdesc);
        tvRateHeader = findViewById(R.id.tvRateHeader);
        ratinglayout = findViewById(R.id.ratinglayout);
        ratingbar = findViewById(R.id.ratingbar);
        layoutTop = findViewById(R.id.layoutTop);
        rv_pdf_list = findViewById(R.id.rv_pdf_list);
        ratebtn = findViewById(R.id.ratebtn);
        ll_main = findViewById(R.id.ll_main);
        vwRateLine = findViewById(R.id.vwRateLine);
        llMAin = findViewById(R.id.ll_main);

        tv_attendee_name.setText(fname + " " + lname);
        tv_attendee_designation.setText(designation);
        tv_attendee_company_name.setText(company);
        tv_attendee_city.setText(city);
        tv_mobile.setText(mobile);
        tv_email.setText(email);
        bgLinear = findViewById(R.id.bgLinear);
        bgView = findViewById(R.id.bgView);
        tvdesc.setText(description);

        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(this, EVENT_ID);

        if(cd.isConnectingToInternet()){
            getSpeakerDetail(api_token,eventid,attendeeid);
        }


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
        tvRateHeader.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        tvdesc.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        LinearLayout rate2 = findViewById(R.id.rate2);
        rate2.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)));
        ratebtn.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)));

        ratebtn.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)));

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

        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingval = rating;
            }
        });


        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ratingval > 0) {
                    setSpeakerRate(api_token,eventid,attendeeid,String.valueOf(ratingval));
                } else {
                    Utility.createShortSnackBar(llMAin, "Please rate on a scale of 1-5 stars");

                }
            }
        });

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
        description = speaker.getFirebase_status();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }





    //Update Api
    public MutableLiveData<FetchSpeaker> getSpeakerDetail(final String token, final String event_id, String speaker_id) {
        updateApi = ApiUtils.getAPIService();

        updateApi.SpeakerDetailFetch(token, event_id, speaker_id).enqueue(new Callback<FetchSpeaker>() {
            @Override
            public void onResponse(Call<FetchSpeaker> call,
                                   Response<FetchSpeaker> response) {
                if (response.isSuccessful()) {
                    speakerDetail.setValue(response.body());
                    String strCommentList =response.body().getDetail();
                    RefreashToken refreashToken = new RefreashToken(SpeakerDetailActivity.this);
                    String data = refreashToken.decryptedData(strCommentList);
                    Gson gson = new Gson();
                    List<Speaker> eventLists = gson.fromJson(data, new TypeToken<ArrayList<Speaker>>() {}.getType());
                    if(eventLists!=null) {
                        pdf_list = eventLists.get(0).getSpeakerDocList();
                    }

                    if(pdf_list!=null) {
                        vwRateLine.setVisibility(View.VISIBLE);

                        String pdfurl = eventLists.get(0).getSpeaker_document_path();

                        PdfAdapter pdfListAdapter = new PdfAdapter(SpeakerDetailActivity.this, pdf_list, SpeakerDetailActivity.this, pdfurl);
                        rv_pdf_list.setLayoutManager(new GridLayoutManager(SpeakerDetailActivity.this, 2));
                        rv_pdf_list.setAdapter(pdfListAdapter);
                    }else{
                        vwRateLine.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailure(Call<FetchSpeaker> call, Throwable t) {
                speakerDetail.setValue(null);
            }
        });
        return speakerDetail;
    }



    //Update Api
    public MutableLiveData<LoginOrganizer> setSpeakerRate(final String token, final String event_id, String speaker_id, String rate) {
        updateApi = ApiUtils.getAPIService();

        updateApi.RateSpeaker(token, event_id, speaker_id,rate).enqueue(new Callback<LoginOrganizer>() {
            @Override
            public void onResponse(Call<LoginOrganizer> call,
                                   Response<LoginOrganizer> response) {
                if (response.isSuccessful()) {
                    speakerRate.setValue(response.body());
                    Utility.createShortSnackBar(llMAin, response.body().getHeader().get(0).getMsg());



                }
            }

            @Override
            public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                speakerRate.setValue(null);
            }
        });
        return speakerRate;
    }


    @Override
    public void onContactSelected(Speaker_Doc Speaker_Doc, String path) {
        Intent pdfview = new Intent(SpeakerDetailActivity.this, DownloadPdfActivity.class);
        pdfview.putExtra("url", "https://docs.google.com/gview?embedded=true&url=" + path + Speaker_Doc.getPdf_file());
        pdfview.putExtra("url1", path + Speaker_Doc.getPdf_file());
        pdfview.putExtra("doc_name",  Speaker_Doc.getPdf_name());
        pdfview.putExtra("page_id",  "0");
        pdfview.putExtra("file_id",  Speaker_Doc.getId());
        startActivity(pdfview);
    }
}
