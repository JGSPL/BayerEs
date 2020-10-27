package com.procialize.eventapp.ui.quiz.view;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.procialize.eventapp.App;
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
import com.procialize.eventapp.ui.quiz.model.QuizList;
import com.procialize.eventapp.ui.quiz.model.QuizListing;
import com.procialize.eventapp.ui.quiz.model.QuizQuestion;
import com.procialize.eventapp.ui.quiz.viewmodel.QuizDetailViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.QUIZLOGO_MEDIA_PATH;

public class QuizDetailActivity extends AppCompatActivity {

    SessionManager session;
    String event_id, api_token;
    RelativeLayout ll_main;
    public static String folder_id, foldername = "null", strTimer = "0";
    QuizDetailViewModel quizDetailViewModel;
    public String getQuizUrl, untimed_quiz = "", timed_quiz = "";
    ConnectionDetector cd;
    ProgressDialog pDialog;
    public static Button submit, btnNext;
    TextView txt_count, questionTv, txt_time, txtSkip, txtHeaderQ;
    CustomViewPager pager;
    ProgressBar progressBarCircle;
    TextView textViewTime;
    public int time = 10, timerForQuiz;
    RelativeLayout rv_timer;
    QuizPagerAdapter quizPagerAdapter;
    public static boolean submitflag = false, isBackePressed = false;
    CountDownTimer timercountdown;
    QuizPagerAdapter pagerAdapter;
    public static int count1 = 1;
    List<QuizList> quizList;
    List<QuizQuestion> questionList;
    public static App app;
    int count;
    public static int countpage = 1;

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
                count = 1;
                pagerAdapter.selectopt = 0;
                try {
                    if (timercountdown != null) {
                        timercountdown.cancel();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                onBackPressed();

            }
        });

        app = (App) getApplicationContext();
        foldername = getIntent().getExtras().getString("folder");
        strTimer = getIntent().getExtras().getString("timer");
        folder_id = getIntent().getExtras().getString("folder_id");
        timerForQuiz = Integer.parseInt(getIntent().getExtras().getString("timer"));
        if (timerForQuiz == 0) {
            timed_quiz = "0";
            untimed_quiz = "1";
        } else {
            timed_quiz = "1";
            untimed_quiz = "0";
        }
        time = timerForQuiz;
        time = Integer.parseInt(strTimer);

        ll_main = findViewById(R.id.relative);
        submit = (Button) findViewById(R.id.submit);
        btnNext = (Button) findViewById(R.id.btnNext);
        txt_time = (TextView) findViewById(R.id.txt_time);
        txtSkip = (TextView) findViewById(R.id.txtSkip);
        txt_count = (TextView) findViewById(R.id.txt_count);
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
        quizDetailViewModel = ViewModelProviders.of(this).get(QuizDetailViewModel.class);

        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(this, EVENT_ID);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                time = timerForQuiz;
                txt_count.setText("Questions " + (position + 1) + "/" + quizList.size());
               /* timercountdown.cancel();
                timercountdown.start();*/
                //startCountDownTimer(time * 1000);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int option = pagerAdapter.getSelectedOption();

//                String correctOption = quizList.get(llm.findLastVisibleItemPosition()).getCorrect_answer();
                int i = pagerAdapter.getItemCount();

                pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                countpage = pager.getCurrentItem();
                if (quizList.size() == pager.getCurrentItem() + 1) {
                    btnNext.setVisibility(View.GONE);
                    submit.setVisibility(View.VISIBLE);
                    submitflag = true;
                    //                    txt_time.setText(String.format(Locale.getDefault(), "%d", time));
//                    if (time > 0)
//                        time -= 1;
                } else if (quizList.size() >= pager.getCurrentItem() + 1) {
                    btnNext.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.GONE);
                    //time = 0;
                    if (timercountdown != null) {
                        time = timerForQuiz;
                        timercountdown.cancel();
                        Log.e("btn_next_click==>", "timer start");
                        timercountdown.start();
                        txt_time.setText("" + ":" + checkdigit(time));
                    }
//                    txt_time.setText(String.format(Locale.getDefault(), "%d", time));
//                    if (time > 0)
//                        time -= 1;

                }
            }
        });

        if (cd.isConnectingToInternet()) {
            quizDetailViewModel.getQuizList(api_token, event_id);

            quizDetailViewModel.getQuizList().observe(this, new Observer<QuizListing>() {
                @Override
                public void onChanged(QuizListing event) {
                    RefreashToken refreashToken = new RefreashToken(QuizDetailActivity.this);
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
                    quizList = gson.fromJson(je4, new TypeToken<ArrayList<QuizList>>() {
                    }.getType());
                    HashMap<String, String> map = new HashMap<>();
                    map.put(QUIZLOGO_MEDIA_PATH, strFilePath.replace("\\/", "/"));
                    SharedPreference.putPref(QuizDetailActivity.this, map);

                    for (int i = 0; i < quizList.size(); i++) {
                        if (folder_id.equalsIgnoreCase(quizList.get(i).getFolder_id())) {
                            questionList = quizList.get(i).getQuiz_question();
                        }
                    }

                    progressBarCircle.setMax(timerForQuiz);
                    progressBarCircle.setProgress(10);

                    if (timed_quiz.equalsIgnoreCase("1")) {
                        rv_timer.setVisibility(View.VISIBLE);
                        int timeFQ = time * 1000;
                        timercountdown = new CountDownTimer(timeFQ, 1000) {
                            public void onTick(long millisUntilFinished) {
                                if (time == 0) {
                                    time = timerForQuiz;
                                    //time = 10;
                                }

                                txt_time.setText("" + ":" + checkdigit(time));
                                textViewTime.setText(String.valueOf(time));
                                progressBarCircle.setProgress(time);
                                time--;
                            }

                            public void onFinish() {
                                time = 0;
                                if (timercountdown != null) {
                                    timercountdown.cancel();
                                }

                                if (quizList.size() == pager.getCurrentItem() + 1) {
                                    if (!isBackePressed) {
                                        submitflag = true;
//                                        submitQuiz();
                                    }
                                } else {
                                    Log.e("getQuizList==>", "timer start");
                                    if (timercountdown != null) {
                                        timercountdown.start();
                                    }
                                    txt_time.setText("" + ":" + checkdigit(time));
                                    submitflag = false;
                                    //submitQuiz();
                                }
                            }
                        }.start();
                    } else {
                        rv_timer.setVisibility(View.GONE);
                    }

                    txt_count.setText("Questions 1/" + questionList.size());
                    pagerAdapter = new QuizPagerAdapter(QuizDetailActivity.this, questionList);
                    pager.setAdapter(pagerAdapter);
                    pager.setPagingEnabled(false);
                    if (questionList.size() > 1) {
                        btnNext.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.GONE);

                    } else {
                        btnNext.setVisibility(View.GONE);
                        submit.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else {
            Utility.createShortSnackBar(ll_main, "No Internet Connection");
        }


        CommonFirebase.crashlytics("QuizDetail", api_token);
        CommonFirebase.firbaseAnalytics(this, "QuizDetail", api_token);
        CommonFunction.showBackgroundImage(QuizDetailActivity.this, ll_main);
    }

    public String checkdigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        count1 = 1;
//        pagerAdapter.selectopt = 0;
        submitflag = false;
        isBackePressed = true;
        try {
            if (timercountdown != null) {
                timercountdown.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*Intent intent = new Intent(QuizActivity.this, FolderQuizActivity.class);
        startActivity(intent);
        finish();*/

    }

}


