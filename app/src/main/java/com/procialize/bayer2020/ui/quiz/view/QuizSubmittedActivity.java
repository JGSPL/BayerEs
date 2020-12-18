package com.procialize.bayer2020.ui.quiz.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.procialize.bayer2020.ConnectionDetector;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.CommonFirebase;
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.session.SessionManager;
import com.procialize.bayer2020.ui.quiz.adapter.QuizSubmitedAdapter;
import com.procialize.bayer2020.ui.quiz.model.QuizList;
import com.procialize.bayer2020.ui.quiz.model.QuizListing;
import com.procialize.bayer2020.ui.quiz.model.QuizQuestion;
import com.procialize.bayer2020.ui.quiz.viewmodel.QuizSubmittedViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.QUIZLOGO_MEDIA_PATH;

public class QuizSubmittedActivity extends AppCompatActivity implements View.OnClickListener {

    String event_id, api_token;
    ConnectionDetector cd;
    SessionManager session;
    QuizSubmittedViewModel quizDetailViewModel;
    LinearLayout ll_main;
    TextView questionTv, tv_header, txt_count;
    RecyclerView quiz_list;
    Button btnNext, submit;
    List<QuizList> quizList;
    List<QuizQuestion> questionList;
    public static String folder_id, foldername = "null", strTimer = "0";
    QuizSubmitedAdapter adapter;
    LinearLayoutManager llm;
    int count = 1;
    RelativeLayout relative,relative_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_submitted);

        new RefreashToken(QuizSubmittedActivity.this).callGetRefreashToken(QuizSubmittedActivity.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        String eventColor3 = SharedPreference.getPref(QuizSubmittedActivity.this, EVENT_COLOR_3);

        String eventColor3Opacity40 = eventColor3.replace("#", "");

        relative = findViewById(R.id.relative);
        txt_count = findViewById(R.id.txt_count);
        questionTv = findViewById(R.id.questionTv);
        quiz_list = findViewById(R.id.quiz_list);
        submit = findViewById(R.id.submit);
        btnNext = findViewById(R.id.btnNext);
        tv_header = findViewById(R.id.tv_header);
        relative_main = findViewById(R.id.relative_main);

        CommonFunction.showBackgroundImage(QuizSubmittedActivity.this, relative);
        tv_header.setTextColor(Color.parseColor(SharedPreference.getPref(QuizSubmittedActivity.this, EVENT_COLOR_1)));
        questionTv.setTextColor(Color.parseColor(SharedPreference.getPref(QuizSubmittedActivity.this, EVENT_COLOR_1)));
        txt_count.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        btnNext.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)));
        submit.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)));
        relative_main.setBackgroundColor(Color.parseColor(SharedPreference.getPref(QuizSubmittedActivity.this, EVENT_COLOR_2)));
        btnNext.setBackgroundColor(Color.parseColor(SharedPreference.getPref(QuizSubmittedActivity.this, EVENT_COLOR_1)));
        submit.setBackgroundColor(Color.parseColor(SharedPreference.getPref(QuizSubmittedActivity.this, EVENT_COLOR_1)));

        quiz_list.setLayoutManager(new LinearLayoutManager(QuizSubmittedActivity.this, LinearLayoutManager.HORIZONTAL, false));
        llm = (LinearLayoutManager) quiz_list.getLayoutManager();

        session = new SessionManager(getApplicationContext());

        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(this, EVENT_ID);

        CommonFirebase.crashlytics("QuizSubmitted", api_token);
        CommonFirebase.firbaseAnalytics(this, "QuizSubmitted", api_token);
        cd = ConnectionDetector.getInstance(this);
        foldername = getIntent().getExtras().getString("folder_name");
        strTimer = getIntent().getExtras().getString("timer");
        folder_id = getIntent().getExtras().getString("folder_id");
        submit.setOnClickListener(this);
        questionTv.setText(foldername);

        quizDetailViewModel = ViewModelProviders.of(this).get(QuizSubmittedViewModel.class);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int option = adapter.getSelectedOption();
                String correctOption = quizList.get(llm.findLastVisibleItemPosition()).getTotal_correct();
                int i = adapter.getItemCount();
//                adapter.getItemViewType(llm.findLastVisibleItemPosition());
                if (i != count) {
//                    if (option != llm.findLastVisibleItemPosition()) {
                    quiz_list.getLayoutManager().scrollToPosition(llm.findLastVisibleItemPosition() + 1);
                    txt_count.setText("Questions " + (count + 1) + "/" + i);
                    count = count + 1;
                    if (questionList.size() == llm.findLastVisibleItemPosition() + 2) {

                        btnNext.setVisibility(View.INVISIBLE);
                        submit.setVisibility(View.VISIBLE);
                    } else {
                        btnNext.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.INVISIBLE);
                    }
//                    } else {
//                        Toast.makeText(QuizNewActivity.this, "Please Select Option", Toast.LENGTH_SHORT).show();
//                    }
                }


            }
        });

        if (cd.isConnectingToInternet()) {
            quizDetailViewModel.getQuizList(api_token, event_id);

            quizDetailViewModel.getQuizList().observe(this, new Observer<QuizListing>() {
                @Override
                public void onChanged(QuizListing event) {
                    RefreashToken refreashToken = new RefreashToken(QuizSubmittedActivity.this);
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
                    SharedPreference.putPref(QuizSubmittedActivity.this, map);

                    for (int i = 0; i < quizList.size(); i++) {
                        if (folder_id.equalsIgnoreCase(quizList.get(i).getFolder_id())) {
                            questionList = quizList.get(i).getQuiz_question();
                        }
                    }

                    adapter = new QuizSubmitedAdapter(QuizSubmittedActivity.this, questionList);
                    quiz_list.setAdapter(adapter);
                    int itemcount = adapter.getItemCount();
                    txt_count.setText("Questions " + 1 + "/" + itemcount);
                    if (questionList.size() > 1) {
                        btnNext.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.INVISIBLE);
                    } else {
                        btnNext.setVisibility(View.INVISIBLE);
                        submit.setVisibility(View.VISIBLE);
                    }

                }
            });
        } else {
            Utility.createShortSnackBar(ll_main, "No Internet Connection");
        }
    }

    @Override
    public void onClick(View v) {
        if (v == submit) {
            Boolean valid = true;
            final int[] check = {0};
            int sum = 0;
            final String[] question_id = {""};
            final String[] question_ans = {""};
            final String[] value = {""};
            final RadioGroup[] radioGroup = new RadioGroup[1];
            final EditText[] ans_edit = new EditText[1];
            final RadioButton[] radioButton = new RadioButton[1];
            Log.e("size", adapter.getItemCount() + "");


            String[] data = adapter.getselectedData();
            String[] question = adapter.getselectedquestion();

            if (data != null) {
                for (int i = 0; i < data.length; i++) {
                    if (i != 0) {
                        question_id[0] = question_id[0] + "$#";
                        question_ans[0] = question_ans[0] + "$#";
                    }

                    String id = questionList.get(i).getId();
                    question_id[0] = question_id[0] + id;

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

                                    for (int j = 0; j < questionList.size(); j++) {
                                        if (questionList.get(i).getQuiz_option().get(j).getOption().equals(data[i]) && questionList.get(i).getQuiz_option().get(j).getQuiz_id().equals(idno)) {
                                            question_ans[0] = question_ans[0] + questionList.get(i).getQuiz_option().get(j).getOption_id();
                                        }
                                    }
                                } else {
                                    valid = false;
                                }
                            }
                        } else {

                            if (!data[i].equalsIgnoreCase("")) {

                                String idno = questionList.get(i).getId();

                                for (int j = 0; j < questionList.size(); j++) {
                                    if (questionList.get(i).getQuiz_option().get(j).getOption().equals(data[i]) && questionList.get(i).getQuiz_option().get(j).getOption_id().equals(idno)) {
                                        question_ans[0] = question_ans[0] + questionList.get(i).getQuiz_option().get(j).getOption_id();
                                    }
                                }
                            } else {
                                valid = false;
                            }
                        }
                    } else {
                        valid = false;
                    }

                }


                Log.e("valid", question_ans.toString());
                Log.e("valid", question_id.toString());
                Log.e("valid", valid.toString());

                Intent intent = new Intent(QuizSubmittedActivity.this, YourScoreActivity.class);
                intent.putExtra("folderName", foldername);
                intent.putExtra("folderid", folder_id);
                intent.putExtra("Answers", String.valueOf(adapter.getCorrectOption()));
                intent.putExtra("TotalQue", String.valueOf(adapter.getselectedData().length));
                intent.putExtra("Page", "Answer");

                startActivity(intent);
                finish();


            }

        }

    }
}
