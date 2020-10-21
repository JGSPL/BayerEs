package com.procialize.eventapp.ui.quiz.view;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFirebase;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.costumTools.CustomViewPager;
import com.procialize.eventapp.session.SessionManager;
import com.procialize.eventapp.ui.quiz.adapter.QuizPagerAdapter;
import com.procialize.eventapp.ui.quiz.viewmodel.QuizDetailViewModel;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;

public class QuizDetailActivity extends AppCompatActivity {

    SessionManager session;
    String event_id, api_token;
    RelativeLayout ll_main;
    public static String foldername = "null", strTimer = "0";
    QuizDetailViewModel quizDetailViewModel;
    String getQuizUrl,untimed_quiz = "", timed_quiz = "";
    ConnectionDetector cd;
    ProgressDialog pDialog;
    public static Button submit, btnNext;
    TextView questionTv, txt_time, txtSkip, txtHeaderQ;
    CustomViewPager pager;
    ProgressBar progressBarCircle;
    TextView textViewTime;
    public int time = 10, timerForQuiz;
    RelativeLayout rv_timer;
    QuizPagerAdapter quizPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_detail);

        new RefreashToken(QuizDetailActivity.this).callGetRefreashToken(QuizDetailActivity.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });


        ll_main = findViewById(R.id.relative);
        submit = (Button) findViewById(R.id.submit);
        btnNext = (Button) findViewById(R.id.btnNext);
        txt_time = (TextView) findViewById(R.id.txt_time);
        txtSkip = (TextView) findViewById(R.id.txtSkip);
//        txtHeaderQ = (TextView) findViewById(R.id.txtHeaderQ);
//        txtSkip.setOnClickListener(this);
        pager = findViewById(R.id.pager);
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        rv_timer = (RelativeLayout) findViewById(R.id.rv_timer);

//        submit.setOnClickListener(this);


        session = new SessionManager(getApplicationContext());
        cd = ConnectionDetector.getInstance(this);
        quizDetailViewModel= ViewModelProviders.of(this).get(QuizDetailViewModel.class);

        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(this, EVENT_ID);

        if (cd.isConnectingToInternet()) {
            quizDetailViewModel.getQuizList(api_token, event_id);
        }else {
            Utility.createShortSnackBar(ll_main,"No Internet Connection");
        }


        CommonFirebase.crashlytics("QuizDetail", api_token);
        CommonFirebase.firbaseAnalytics(this, "QuizDetail", api_token);
        CommonFunction.showBackgroundImage(QuizDetailActivity.this, ll_main);
    }



}