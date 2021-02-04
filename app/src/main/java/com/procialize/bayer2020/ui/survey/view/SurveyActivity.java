package com.procialize.bayer2020.ui.survey.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.bayer2020.ConnectionDetector;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.agenda.model.FetchAgenda;
import com.procialize.bayer2020.ui.survey.adapter.SurveyAdapter;
import com.procialize.bayer2020.ui.survey.model.Survey;
import com.procialize.bayer2020.ui.survey.viewmodel.SurveyViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.procialize.bayer2020.Utility.CommonFunction.setNotification;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;

public class SurveyActivity extends AppCompatActivity implements SurveyAdapter.SurveyAdapterListner {
    SurveyViewModel surveyViewModel;
    String api_token,event_id;
    ImageView iv_back;
    LinearLayout ll_main;
    SurveyAdapter surevyAdapter;
    RecyclerView surevyrecycler;
    ConnectionDetector cd;
    SwipeRefreshLayout surveyrefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        new RefreashToken(SurveyActivity.this).callGetRefreashToken(SurveyActivity.this);
        surveyViewModel = ViewModelProviders.of(this).get(SurveyViewModel.class);
        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(this, EVENT_ID);
        //event_id = "1";
        iv_back = findViewById(R.id.iv_back);
        ll_main = findViewById(R.id.ll_main);
        surveyrefresh = findViewById(R.id.surveyrefresh);
        surevyrecycler = findViewById(R.id.surevyrecycler);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //--------------------------------------------------------------------------------------
       /* GetUserActivityReport getUserActivityReport = new GetUserActivityReport(this,api_token,
                event_id,
                Constant.pageVisited,
                "39",
                "0");
        getUserActivityReport.userActivityReport();*/
        //--------------------------------------------------------------------------------------

        setUpToolbar();

        //-----------------------------For Notification count-----------------------------
        try {
            LinearLayout ll_notification_count = findViewById(R.id.ll_notification_count);
            TextView tv_notification = findViewById(R.id.tv_notification);
            setNotification(this, tv_notification, ll_notification_count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //----------------------------------------------------------------------------------

        cd = ConnectionDetector.getInstance(this);
        if (cd.isConnectingToInternet()) {
            getDataFromApi();
        } else {
            if (surveyrefresh.isRefreshing()) {
                surveyrefresh.setRefreshing(false);
            }
            Utility.createShortSnackBar(ll_main, "No internet connection");
        }
        surveyrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                surveyrefresh.setRefreshing(false);
                if (cd.isConnectingToInternet()) {
                    getDataFromApi();
                } else {
                    if (surveyrefresh.isRefreshing()) {
                        surveyrefresh.setRefreshing(false);
                    }
                    Utility.createShortSnackBar(ll_main, "No internet connection");
                }
            }
        });
    }

    private void getDataFromApi()
    {
        surveyViewModel.getSurvey(api_token,event_id,"1000","1");
        surveyViewModel.getSurvey().observeForever(new Observer<FetchAgenda>() {
            @Override
            public void onChanged(FetchAgenda fetchAgenda) {
                if(fetchAgenda!=null)
                {
                    if(fetchAgenda.getHeader().get(0).getType().equalsIgnoreCase("success"))
                    {
                        String contactUsLink = fetchAgenda.getDetail();
                        RefreashToken refreashToken = new RefreashToken(SurveyActivity.this);
                        String data = refreashToken.decryptedData(contactUsLink);
                        try {
                            Gson gson = new Gson();
                            List<Survey> contactUsList = gson.fromJson(data, new TypeToken<ArrayList<Survey>>() {
                            }.getType());
                            if (contactUsList != null) {
                                setupEventAdapter(contactUsList);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Utility.createShortSnackBar(ll_main,fetchAgenda.getHeader().get(0).getMsg());
                    }
                }
            }
        });
    }

    public void setupEventAdapter(List<Survey> commentList) {
        if(commentList!=null) {

            surevyAdapter = new SurveyAdapter(SurveyActivity.this, commentList, SurveyActivity.this);
            surevyrecycler.setLayoutManager(new LinearLayoutManager(SurveyActivity.this));
            surevyrecycler.setAdapter(surevyAdapter);
            surevyAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onContactSelected(Survey survey) {
        Uri webpage = Uri.parse(survey.getSurvey_url());
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(webIntent);
    }

    private void setUpToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
           /* setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            mToolbar.showOverflowMenu();*/
            ImageView headerlogoIv = findViewById(R.id.headerlogoIv);

            String eventLogo = SharedPreference.getPref(this, EVENT_LOGO);
            String eventListMediaPath = SharedPreference.getPref(this, EVENT_LIST_MEDIA_PATH);
            Glide.with(this)
                    .load(eventListMediaPath + eventLogo)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(headerlogoIv);

        }
    }
}