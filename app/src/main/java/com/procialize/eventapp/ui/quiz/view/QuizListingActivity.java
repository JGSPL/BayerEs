package com.procialize.eventapp.ui.quiz.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFirebase;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.session.SessionManager;
import com.procialize.eventapp.ui.quiz.viewmodel.QuizListingViewModel;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;

public class QuizListingActivity extends AppCompatActivity {

    QuizListingViewModel quizlistingviewmodel;
    ConnectionDetector cd;
    public static LinearLayout ll_main;
    RecyclerView quizrecycler;
    SessionManager session;
    String api_token = "",event_id;
    SwipeRefreshLayout quizRefreash;
    TextView tv_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_listing);

        new RefreashToken(QuizListingActivity.this).callGetRefreashToken(QuizListingActivity.this);

        quizrecycler=findViewById(R.id.quizrecycler);
        quizRefreash=findViewById(R.id.quizrefresher);
        ll_main=findViewById(R.id.ll_main);
        tv_header=findViewById(R.id.tv_header);

        session = new SessionManager(getApplicationContext());

        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(this, EVENT_ID);

        CommonFirebase.crashlytics("QuizListing", api_token);
        CommonFirebase.firbaseAnalytics(this, "QuizListing", api_token);
        cd = ConnectionDetector.getInstance(this);
        quizlistingviewmodel = ViewModelProviders.of(this).get(QuizListingViewModel.class);

        if(cd.isConnectingToInternet()){
            quizlistingviewmodel.getQuizList(api_token, event_id);
        }else {
            Utility.createShortSnackBar(ll_main, "No Internet Connection..!");
        }
    }
}