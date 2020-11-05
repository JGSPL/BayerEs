package com.procialize.eventapp.ui.quiz.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFirebase;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.session.SessionManager;
import com.procialize.eventapp.ui.quiz.adapter.QuizListAdapter;
import com.procialize.eventapp.ui.quiz.model.QuizList;
import com.procialize.eventapp.ui.quiz.model.QuizListing;
import com.procialize.eventapp.ui.quiz.viewmodel.QuizListingViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.QUIZLOGO_MEDIA_PATH;

public class QuizListingActivity extends AppCompatActivity implements QuizListAdapter.QuizListAdapterListner {

    QuizListingViewModel quizlistingviewmodel;
    ConnectionDetector cd;
    public static LinearLayout ll_main;
    RecyclerView quizrecycler;
    SessionManager session;
    String api_token = "", event_id;
    SwipeRefreshLayout quizRefreash;
    TextView tv_header;
    QuizListAdapter quizAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_listing);

        new RefreashToken(QuizListingActivity.this).callGetRefreashToken(QuizListingActivity.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        quizrecycler=findViewById(R.id.quizrecycler);
        quizRefreash=findViewById(R.id.quizrefresher);
        ll_main=findViewById(R.id.ll_main);
        tv_header=findViewById(R.id.tv_header);
        CommonFunction.showBackgroundImage(QuizListingActivity.this, ll_main);
        tv_header.setTextColor(Color.parseColor(SharedPreference.getPref(QuizListingActivity.this, EVENT_COLOR_1)));
        session = new SessionManager(getApplicationContext());

        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(this, EVENT_ID);

        CommonFirebase.crashlytics("QuizListing", api_token);
        CommonFirebase.firbaseAnalytics(this, "QuizListing", api_token);
        cd = ConnectionDetector.getInstance(this);
        quizlistingviewmodel = ViewModelProviders.of(this).get(QuizListingViewModel.class);

        if (cd.isConnectingToInternet()) {
            quizlistingviewmodel.getQuizList(api_token, event_id);

            quizlistingviewmodel.getQuizList().observe(this, new Observer<QuizListing>() {
                @Override
                public void onChanged(QuizListing event) {
                    RefreashToken refreashToken = new RefreashToken(QuizListingActivity.this);
                    String decrypteventdetail = refreashToken.decryptedData(event.getDetail());
//                    String strFilePath = CommonFunction.stripquotes(refreashToken.decryptedData(event.getDetailpreencrypt().getLogo_url_path()));
//                    String strFilePath = CommonFunction.stripquotes(event.getDetailpreencrypt().getLogo_url_path());
                    JsonParser jp = new JsonParser();
                    JsonElement je = jp.parse(decrypteventdetail);
                    JsonElement je2 = je.getAsJsonObject().get("logo_url_path");
                    JsonElement je3 = je.getAsJsonObject().get("quiz_logo");
                    JsonElement je4 = je.getAsJsonObject().get("quiz_list");
                    JsonElement je5 = je3.getAsJsonObject().get("app_quiz_logo");
                    String strFilePath = String.valueOf(je5);
                    Gson gson = new Gson();
                    List<QuizList> gsonevent = gson.fromJson(je4, new TypeToken<ArrayList<QuizList>>() {
                    }.getType());
                    HashMap<String, String> map = new HashMap<>();
                    map.put(QUIZLOGO_MEDIA_PATH, strFilePath.replace("\\/", "/"));
                    SharedPreference.putPref(QuizListingActivity.this, map);
                    setupQuizAdapter(gsonevent);
                }
            });
        } else {
            Utility.createShortSnackBar(ll_main, "No Internet Connection..!");
        }

        quizRefreash.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                quizRefreash.setRefreshing(false);
                if (cd.isConnectingToInternet()) {
                    quizlistingviewmodel.getQuizList(api_token, event_id);

                    quizlistingviewmodel.getQuizList().observe(QuizListingActivity.this, new Observer<QuizListing>() {
                        @Override
                        public void onChanged(QuizListing event) {
                            RefreashToken refreashToken = new RefreashToken(QuizListingActivity.this);
                            String decrypteventdetail = refreashToken.decryptedData(event.getDetail());
//                    String strFilePath = CommonFunction.stripquotes(refreashToken.decryptedData(event.getDetailpreencrypt().getLogo_url_path()));
//                    String strFilePath = CommonFunction.stripquotes(event.getDetailpreencrypt().getLogo_url_path());
                            JsonParser jp = new JsonParser();
                            JsonElement je = jp.parse(decrypteventdetail);
                            JsonElement je2 = je.getAsJsonObject().get("logo_url_path");
                            JsonElement je3 = je.getAsJsonObject().get("quiz_logo");
                            JsonElement je4 = je.getAsJsonObject().get("quiz_list");
                            JsonElement je5 = je3.getAsJsonObject().get("app_quiz_logo");
                            String strFilePath = String.valueOf(je5);
                            Gson gson = new Gson();
                            List<QuizList> gsonevent = gson.fromJson(je4, new TypeToken<ArrayList<QuizList>>() {
                            }.getType());
                            HashMap<String, String> map = new HashMap<>();
                            map.put(QUIZLOGO_MEDIA_PATH, strFilePath.replace("\\/", "/"));
                            SharedPreference.putPref(QuizListingActivity.this, map);
                            setupQuizAdapter(gsonevent);
                        }
                    });
                } else {
                    Utility.createShortSnackBar(ll_main, "No Internet Connection..!");
                }
            }
        });
    }

    public void setupQuizAdapter(List<QuizList> commentList) {
        quizAdapter = new QuizListAdapter(QuizListingActivity.this, commentList, QuizListingActivity.this);
        quizrecycler.setLayoutManager(new LinearLayoutManager(this));
        quizrecycler.setAdapter(quizAdapter);
        quizAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMoreSelected(QuizList quiz, final int position) {
        if(quiz.getQuiz_question().get(0).getReplied().equalsIgnoreCase("0")) {
            QuizDetailActivity.count1 = 1;
            QuizDetailActivity.submitflag = false;
            Intent intent = new Intent(QuizListingActivity.this, QuizDetailActivity.class);
            intent.putExtra("timer", quiz.getTimer());
            intent.putExtra("folder_id", quiz.getFolder_id());
            intent.putExtra("folder_name", quiz.getFolder_name());
            startActivity(intent);
        }else {
            Intent intent = new Intent(QuizListingActivity.this, QuizSubmittedActivity.class);
            intent.putExtra("timer", quiz.getTimer());
            intent.putExtra("folder_id", quiz.getFolder_id());
            intent.putExtra("folder_name", quiz.getFolder_name());
            startActivity(intent);
        }
    }
}