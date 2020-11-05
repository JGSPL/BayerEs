package com.procialize.eventapp.ui.quiz.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.procialize.eventapp.GetterSetter.Header;
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
import com.procialize.eventapp.ui.quiz.model.QuizSubmit;
import com.procialize.eventapp.ui.quiz.viewmodel.QuizDetailViewModel;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.QUIZLOGO_MEDIA_PATH;

public class QuizDetailActivity extends AppCompatActivity implements View.OnClickListener {

    SessionManager session;
    String event_id, api_token;
    RelativeLayout ll_main;
    public static String quiz_question_id;
    public static String folder_id, foldername = "null", strTimer = "0";
    QuizDetailViewModel quizDetailViewModel;
    public String getQuizUrl, untimed_quiz = "", timed_quiz = "";
    ConnectionDetector cd;
    ProgressDialog pDialog;
    public static Button submit, btnNext;
    TextView tv_header, txt_count, questionTv, txt_time, txtSkip, txtHeaderQ;
    CustomViewPager pager;
    ProgressBar progressBarCircle;
    TextView textViewTime;
    public int time = 10, timerForQuiz;
    RelativeLayout rv_timer;
    QuizPagerAdapter quizPagerAdapter;
    public static boolean submitflag = false, isBackePressed = false;
    public static CountDownTimer timercountdown;
    QuizPagerAdapter pagerAdapter;
    public static int count1 = 1;
    List<QuizList> quizList;
    List<QuizQuestion> questionList;
    public static App app;
    int count;
    private String quiz_options_id;
    public static int countpage = 1;
    boolean flag = true;
    boolean flag1 = true;
    boolean flag2 = true;
    RelativeLayout relative_main;

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

        String eventColor3 = SharedPreference.getPref(QuizDetailActivity.this, EVENT_COLOR_3);

        String eventColor3Opacity40 = eventColor3.replace("#", "");

        app = (App) getApplicationContext();
        foldername = getIntent().getExtras().getString("folder_name");
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
        tv_header = (TextView) findViewById(R.id.tv_header);
        relative_main = findViewById(R.id.relative_main);
        questionTv = findViewById(R.id.questionTv);
        tv_header.setTextColor(Color.parseColor(SharedPreference.getPref(QuizDetailActivity.this, EVENT_COLOR_1)));
        questionTv.setTextColor(Color.parseColor(SharedPreference.getPref(QuizDetailActivity.this, EVENT_COLOR_1)));
        txt_count.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        btnNext.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)));
        submit.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)));
        relative_main.setBackgroundColor(Color.parseColor(SharedPreference.getPref(QuizDetailActivity.this, EVENT_COLOR_2)));
        btnNext.setBackgroundColor(Color.parseColor(SharedPreference.getPref(QuizDetailActivity.this, EVENT_COLOR_1)));
        submit.setBackgroundColor(Color.parseColor(SharedPreference.getPref(QuizDetailActivity.this, EVENT_COLOR_1)));

        GradientDrawable border = new GradientDrawable();
//        border.setColor(Color.parseColor(SharedPreference.getPref(QuizDetailActivity.this, EVENT_COLOR_1))); //white background
        border.setStroke(1, Color.parseColor(SharedPreference.getPref(QuizDetailActivity.this, EVENT_COLOR_1))); //black border with full opacity
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            txtSkip.setBackgroundDrawable(border);
        } else {
            txtSkip.setBackground(border);
        }
        questionTv.setText(StringEscapeUtils.unescapeJava(foldername));
        pager = findViewById(R.id.pager);
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        rv_timer = (RelativeLayout) findViewById(R.id.rv_timer);

        submit.setOnClickListener(this);
        txtSkip.setOnClickListener(this);


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
                txt_count.setText("Questions " + (position + 1) + "/" + questionList.size());
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
                int i = pagerAdapter.getItemCount();

                pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                countpage = pager.getCurrentItem();
                if (questionList.size() == pager.getCurrentItem() + 1) {
                    btnNext.setVisibility(View.GONE);
                    submit.setVisibility(View.VISIBLE);
                    submitflag = true;
                    //txt_time.setText(String.format(Locale.getDefault(), "%d", time));
//                    if (time > 0)
//                        time -= 1;
                } else if (questionList.size() >= pager.getCurrentItem() + 1) {
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

                                if (questionList.size() == pager.getCurrentItem() + 1) {
                                    if (!isBackePressed) {
                                        submitflag = true;
                                        submitQuiz();
                                    }
                                } else {
                                    timercountdown.start();
                                    txt_time.setText("" + ":" + checkdigit(time));
                                    pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                                    if (questionList.size() == pager.getCurrentItem() + 1) {
                                        btnNext.setVisibility(View.GONE);
                                        submit.setVisibility(View.VISIBLE);
                                        submitflag = true;
                                        //valid = true;
                                    } else if (questionList.size() >= pager.getCurrentItem() + 1) {
                                        btnNext.setVisibility(View.VISIBLE);
                                        submit.setVisibility(View.GONE);
                                        //time = 0;
                                        time = timerForQuiz;
                                        timercountdown.cancel();
                                        timercountdown.start();
                                        txt_time.setText("" + ":" + checkdigit(time));
                                    }
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
    public void onClick(View v) {
        if (v == submit) {
            submitflag = true;
            Boolean valid = true;
            final int[] check = {0};
            int sum = 0;
            final String[] question_id = {""};
            final String[] question_ans = {""};
            final String[] value = {""};
            final RadioGroup[] radioGroup = new RadioGroup[1];
            final EditText[] ans_edit = new EditText[1];
            final RadioButton[] radioButton = new RadioButton[1];
//            Log.e("size", adapter.getItemCount() + "");


            String[] data = pagerAdapter.getselectedData();
            String[] question = pagerAdapter.getselectedquestion();

            if (data != null) {
                for (int i = 0; i < data.length; i++) {
                    if (i != 0) {
                        question_id[0] = question_id[0] + "$#";
                        question_ans[0] = question_ans[0] + "$#";
                    }

                    String id = questionList.get(i).getId();
                    question_id[0] = question_id[0] + id;

                    flag = true;
                    flag1 = true;
                    flag2 = true;
                    if (data[i] != null) {
                        if (questionList.get(i).getReplied() != null) {
                            if (questionList.get(i).getReplied().equalsIgnoreCase("0")) {
                                if (!data[i].equalsIgnoreCase("")) {
                                    question_ans[0] = question_ans[0] + data[i];
                                } else {
                                    valid = false;
                                }
                            } else {

                                if (!data[i].equalsIgnoreCase("")) {

                                    String idno = questionList.get(i).getId();

                                    for (int j = 0; j < questionList.get(i).getQuiz_option().size(); j++) {
                                        if (questionList.get(i).getQuiz_option().get(j).getOption().equals(data[i]) && questionList.get(i).getQuiz_option().get(j).getQuiz_id().equals(idno)) {
                                            question_ans[0] = question_ans[0] + questionList.get(i).getQuiz_option().get(j).getOption_id();
                                        }
                                    }
                                } else {
                                    String idno = questionList.get(i).getId();

                                    for (int j = 0; j < questionList.get(i).getQuiz_option().size(); j++) {
                                        if (questionList.get(i).getQuiz_option().get(j).getQuiz_id().equals(idno)) {
                                            if (flag1 == true) {
                                                question_ans[0] = question_ans[0] + "0";
                                                //question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                                flag1 = false;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {

                            if (!data[i].equalsIgnoreCase("")) {

                                String idno = questionList.get(i).getId();

                                for (int j = 0; j < questionList.get(i).getQuiz_option().size(); j++) {
                                    if (questionList.get(i).getQuiz_option().get(j).getOption().equals(data[i]) && questionList.get(i).getQuiz_option().get(j).getQuiz_id().equals(idno)) {
                                        question_ans[0] = question_ans[0] + questionList.get(i).getQuiz_option().get(j).getOption_id();
                                    }
                                }
                            } else {
                                String idno = questionList.get(i).getId();

                                for (int j = 0; j < questionList.get(i).getQuiz_option().size(); j++) {
                                    if (questionList.get(i).getQuiz_option().get(j).getQuiz_id().equals(idno)) {
                                        if (flag2 == true) {
                                            question_ans[0] = question_ans[0] + "0";
                                            // question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                            flag2 = false;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        String idno = questionList.get(i).getId();


                        for (int j = 0; j < questionList.get(i).getQuiz_option().size(); j++) {
                            if (questionList.get(i).getQuiz_option().get(j).getQuiz_id().equals(idno)) {
                                if (flag == true) {
                                    // question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                    question_ans[0] = question_ans[0] + "0";
                                    flag = false;
                                }
                            }
                        }
                    }
                }

                Log.e("valid_ans", question_ans.toString());
                Log.e("valid_id", question_id.toString());
                Log.e("valid_string", valid.toString());


                //if (valid == true) {
                if (submitflag == true) {
                    quiz_question_id = question_id[0];
                    quiz_options_id = question_ans[0];
                    int answers = pagerAdapter.getCorrectOption();
                    //Toast.makeText(appDelegate, quiz_options_id, Toast.LENGTH_SHORT).show();
                    Log.d("Selected Options==>", quiz_options_id);
                    quizDetailViewModel.quizSubmit(api_token, event_id, quiz_question_id, quiz_options_id);
                    quizDetailViewModel.quizSubmit().observe(QuizDetailActivity.this, new Observer<QuizSubmit>() {
                        @Override
                        public void onChanged(QuizSubmit response) {
                            if (timercountdown != null) {
                                timercountdown.cancel();
                            }
                            pagerAdapter.checkArray = null;
                            pagerAdapter.correctAnswer = "";
                            pagerAdapter.selectedOption = "";
                            pagerAdapter.dataArray = null;
                            pagerAdapter.dataIDArray = null;
                            pagerAdapter.ansArray = null;

                            try {
                                List<Header> header = response.getHeader();

                                if (header.get(0).getType().equalsIgnoreCase("success")) {
                                    int answers = pagerAdapter.getCorrectOption();
                                    Intent intent = new Intent(QuizDetailActivity.this, YourScoreActivity.class);
                                    intent.putExtra("folderName", foldername);
                                    intent.putExtra("Answers", response.getTotal_correct_answer());
                                    intent.putExtra("TotalQue", response.getTotal_questions());
                                    intent.putExtra("Page", "Question");
                                    startActivity(intent);
                                    count1 = 1;
                                    pagerAdapter.selectopt = 0;
                                    submitflag = true;

                                    finish();
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });


                } /*else {
                    Toast.makeText(getApplicationContext(), "Please answer all questions", Toast.LENGTH_SHORT).show();
                }*/
            }
        } else if (v.getId() == R.id.txtSkip) {
            Boolean valid = false;
            countpage = pager.getCurrentItem();

            //submitflag = true;
            //Boolean valid = true;
            final int[] check = {0};
            int sum = 0;
            final String[] question_id = {""};
            final String[] question_ans = {""};
            final String[] value = {""};
            final RadioGroup[] radioGroup = new RadioGroup[1];
            final EditText[] ans_edit = new EditText[1];
            final RadioButton[] radioButton = new RadioButton[1];
//            Log.e("size", adapter.getItemCount() + "");

            String[] data = pagerAdapter.getselectedData();
            String[] question = pagerAdapter.getselectedquestion();

            if (data != null) {
                for (int i = 0; i < data.length; i++) {
                    if (i != 0) {
                        question_id[0] = question_id[0] + "$#";
                        question_ans[0] = question_ans[0] + "$#";
                    }

                    String id = questionList.get(i).getId();
                    question_id[0] = question_id[0] + id;

                    flag = true;
                    flag1 = true;
                    flag2 = true;
                    if (data[i] != null) {
                        if (questionList.get(i).getReplied() != null) {
                            if (questionList.get(i).getReplied().equalsIgnoreCase("0")) {
                                if (!data[i].equalsIgnoreCase("")) {
                                    question_ans[0] = question_ans[0] + data[i];
                                } else {
                                    valid = false;
                                }
                            } else {

                                if (!data[i].equalsIgnoreCase("")) {

                                    String idno = questionList.get(i).getId();

                                    for (int j = 0; j < questionList.get(i).getQuiz_option().size(); j++) {
                                        if (questionList.get(i).getQuiz_option().get(j).getOption().equals(data[i]) && questionList.get(i).getQuiz_option().get(j).getQuiz_id().equals(idno)) {
                                            question_ans[0] = question_ans[0] + questionList.get(i).getQuiz_option().get(j).getOption_id();
                                        }
                                    }
                                } else {
                                    String idno = questionList.get(i).getId();

                                    for (int j = 0; j < questionList.get(i).getQuiz_option().size(); j++) {
                                        if (questionList.get(i).getQuiz_option().get(j).getQuiz_id().equals(idno)) {
                                            if (flag1 == true) {
                                                question_ans[0] = question_ans[0] + "0";
                                                //question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                                flag1 = false;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {

                            if (!data[i].equalsIgnoreCase("")) {

                                String idno = questionList.get(i).getId();

                                for (int j = 0; j < questionList.get(i).getQuiz_option().size(); j++) {
                                    if (questionList.get(i).getQuiz_option().get(j).getOption().equals(data[i]) && questionList.get(i).getQuiz_option().get(j).getQuiz_id().equals(idno)) {
                                        question_ans[0] = question_ans[0] + questionList.get(i).getQuiz_option().get(j).getOption_id();
                                    }
                                }
                            } else {
                                String idno = questionList.get(i).getId();

                                for (int j = 0; j < questionList.get(i).getQuiz_option().size(); j++) {
                                    if (questionList.get(i).getQuiz_option().get(j).getQuiz_id().equals(idno)) {
                                        if (flag2 == true) {
                                            question_ans[0] = question_ans[0] + "0";
                                            // question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                            flag2 = false;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        String idno = questionList.get(i).getId();


                        for (int j = 0; j < questionList.get(i).getQuiz_option().size(); j++) {
                            if (questionList.get(i).getQuiz_option().get(j).getQuiz_id().equals(idno)) {
                                if (flag == true) {
                                    // question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                    question_ans[0] = question_ans[0] + "0";
                                    flag = false;
                                }
                            }
                        }
                    }
                }

                Log.e("valid_ans", question_ans.toString());
                Log.e("valid_id", question_id.toString());
                Log.e("valid_string", valid.toString());

                // if (valid == true && submitflag == true) {
                if (submitflag == true) {
                    quiz_question_id = question_id[0];
                    quiz_options_id = question_ans[0];
                    int answers = pagerAdapter.getCorrectOption();

                    Log.d("Selected Options==>", quiz_options_id);
                    quizDetailViewModel.quizSubmit(api_token, event_id, quiz_question_id, quiz_options_id);
                    quizDetailViewModel.quizSubmit().observe(QuizDetailActivity.this, new Observer<QuizSubmit>() {
                        @Override
                        public void onChanged(QuizSubmit response) {
                            if (timercountdown != null) {
                                timercountdown.cancel();
                            }
                            pagerAdapter.checkArray = null;
                            pagerAdapter.correctAnswer = "";
                            pagerAdapter.selectedOption = "";
                            pagerAdapter.dataArray = null;
                            pagerAdapter.dataIDArray = null;
                            pagerAdapter.ansArray = null;

                            try {
                                List<Header> header = response.getHeader();

                                if (header.get(0).getType().equalsIgnoreCase("success")) {
                                    int answers = pagerAdapter.getCorrectOption();
                                    Intent intent = new Intent(QuizDetailActivity.this, YourScoreActivity.class);
                                    intent.putExtra("folderName", foldername);
                                    intent.putExtra("Answers", response.getTotal_correct_answer());
                                    intent.putExtra("TotalQue", response.getTotal_questions());
                                    intent.putExtra("Page", "Question");
                                    startActivity(intent);
                                    count1 = 1;
                                    pagerAdapter.selectopt = 0;
                                    submitflag = true;
                                    finish();
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    //Toast.makeText(getApplicationContext(), "last question", Toast.LENGTH_SHORT).show();
                }
            }

            pager.setCurrentItem(pager.getCurrentItem() + 1, true);
            if (questionList.size() == pager.getCurrentItem() + 1) {
                btnNext.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
                submitflag = true;
                valid = true;
            } else if (questionList.size() >= pager.getCurrentItem() + 1) {
                btnNext.setVisibility(View.VISIBLE);
                submit.setVisibility(View.GONE);
                //time = 0;
                time = timerForQuiz;
                timercountdown.cancel();
                timercountdown.start();
                txt_time.setText("" + ":" + checkdigit(time));
            }
        }
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

    public void submitQuiz() {

        Boolean valid = false;
        countpage = pager.getCurrentItem();

        //submitflag = true;
        //Boolean valid = true;
        final int[] check = {0};
        int sum = 0;
        final String[] question_id = {""};
        final String[] question_ans = {""};
        final String[] value = {""};
        final RadioGroup[] radioGroup = new RadioGroup[1];
        final EditText[] ans_edit = new EditText[1];
        final RadioButton[] radioButton = new RadioButton[1];
//            Log.e("size", adapter.getItemCount() + "");

        String[] data = pagerAdapter.getselectedData();
        String[] question = pagerAdapter.getselectedquestion();

        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                if (i != 0) {
                    question_id[0] = question_id[0] + "$#";
                    question_ans[0] = question_ans[0] + "$#";
                }

                String id = questionList.get(i).getId();
                question_id[0] = question_id[0] + id;

                flag = true;
                flag1 = true;
                flag2 = true;
                if (data[i] != null) {
                    if (questionList.get(i).getReplied() != null) {
                        if (questionList.get(i).getReplied().equalsIgnoreCase("0")) {
                            if (!data[i].equalsIgnoreCase("")) {
                                question_ans[0] = question_ans[0] + data[i];
                            } else {
                                valid = false;
                            }
                        } else {

                            if (!data[i].equalsIgnoreCase("")) {

                                String idno = questionList.get(i).getId();

                                for (int j = 0; j < questionList.get(i).getQuiz_option().size(); j++) {
                                    if (questionList.get(i).getQuiz_option().get(j).getOption().equals(data[i]) && questionList.get(i).getQuiz_option().get(j).getQuiz_id().equals(idno)) {
                                        question_ans[0] = question_ans[0] + questionList.get(i).getQuiz_option().get(j).getOption_id();
                                    }
                                }
                            } else {
                                String idno = questionList.get(i).getId();

                                for (int j = 0; j < questionList.get(i).getQuiz_option().size(); j++) {
                                    if (questionList.get(i).getQuiz_option().get(j).getQuiz_id().equals(idno)) {
                                        if (flag1 == true) {
                                            question_ans[0] = question_ans[0] + "0";
                                            //question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                            flag1 = false;
                                        }
                                    }
                                }
                            }
                        }
                    } else {

                        if (!data[i].equalsIgnoreCase("")) {

                            String idno = questionList.get(i).getId();

                            for (int j = 0; j < questionList.get(i).getQuiz_option().size(); j++) {
                                if (questionList.get(i).getQuiz_option().get(j).getOption().equals(data[i]) && questionList.get(i).getQuiz_option().get(j).getQuiz_id().equals(idno)) {
                                    question_ans[0] = question_ans[0] + questionList.get(i).getQuiz_option().get(j).getOption_id();
                                }
                            }
                        } else {
                            String idno = questionList.get(i).getId();

                            for (int j = 0; j < questionList.get(i).getQuiz_option().size(); j++) {
                                if (questionList.get(i).getQuiz_option().get(j).getQuiz_id().equals(idno)) {
                                    if (flag2 == true) {
                                        question_ans[0] = question_ans[0] + "0";
                                        // question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                        flag2 = false;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    String idno = questionList.get(i).getId();


                    for (int j = 0; j < questionList.get(i).getQuiz_option().size(); j++) {
                        if (questionList.get(i).getQuiz_option().get(j).getQuiz_id().equals(idno)) {
                            if (flag == true) {
                                // question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                question_ans[0] = question_ans[0] + "0";
                                flag = false;
                            }
                        }
                    }
                }
            }

            Log.e("valid_ans", question_ans.toString());
            Log.e("valid_id", question_id.toString());
            Log.e("valid_string", valid.toString());

            //if (valid == true && submitflag == true) {
            if (submitflag == true) {
                quiz_question_id = question_id[0];
                quiz_options_id = question_ans[0];
                int answers = pagerAdapter.getCorrectOption();

                Log.d("Selected Options==>", quiz_options_id);
                quizDetailViewModel.quizSubmit(api_token, event_id, quiz_question_id, quiz_options_id);

                quizDetailViewModel.quizSubmit().observe(QuizDetailActivity.this, new Observer<QuizSubmit>() {
                    @Override
                    public void onChanged(QuizSubmit response) {
                        timercountdown.cancel();
                        pagerAdapter.checkArray = null;
                        pagerAdapter.correctAnswer = "";
                        pagerAdapter.selectedOption = "";
                        pagerAdapter.dataArray = null;
                        pagerAdapter.dataIDArray = null;
                        pagerAdapter.ansArray = null;

                        try {
                            List<Header> header = response.getHeader();

                            if (header.get(0).getType().equalsIgnoreCase("success")) {
                                int answers = pagerAdapter.getCorrectOption();
                                Intent intent = new Intent(QuizDetailActivity.this, YourScoreActivity.class);
                                intent.putExtra("folderName", foldername);
                                intent.putExtra("Answers", response.getTotal_correct_answer());
                                intent.putExtra("TotalQue", response.getTotal_questions());
                                intent.putExtra("Page", "Question");
                                startActivity(intent);
                                count1 = 1;
                                pagerAdapter.selectopt = 0;
                                submitflag = true;

                                finish();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                //Toast.makeText(appDelegate, quiz_options_id, Toast.LENGTH_SHORT).show();
//                new postQuizQuestion().execute();
            } /*else {
                Toast.makeText(getApplicationContext(), "Please answer all questions", Toast.LENGTH_SHORT).show();
            }*/
        }

        pager.setCurrentItem(pager.getCurrentItem() + 1, true);
        if (questionList.size() == pager.getCurrentItem() + 1) {
            btnNext.setVisibility(View.GONE);
            submit.setVisibility(View.VISIBLE);
            submitflag = true;
            valid = true;
        } else if (questionList.size() >= pager.getCurrentItem() + 1) {
            btnNext.setVisibility(View.VISIBLE);
            submit.setVisibility(View.GONE);
            //time = 0;
            if (timercountdown != null) {
                time = timerForQuiz;
                timercountdown.cancel();
                Log.e("SubmitQuizFun==>", "timer start");
                timercountdown.start();
                txt_time.setText("" + ":" + checkdigit(time));
            }
        }


    }

}


