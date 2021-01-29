package com.procialize.bayer2020.ui.upskill.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.bayer2020.ConnectionDetector;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.CommonFirebase;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.session.SessionManager;
import com.procialize.bayer2020.ui.agenda.model.FetchAgenda;
import com.procialize.bayer2020.ui.upskill.adapter.UpskillQuizSubmitedAdapter;
import com.procialize.bayer2020.ui.upskill.model.QuizList;
import com.procialize.bayer2020.ui.upskill.model.UpskillContentSubArray;
import com.procialize.bayer2020.ui.upskill.model.UpskillList;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;
import static com.procialize.bayer2020.ui.upskill.view.UpskillDetailsFirstActivity.click_count;

public class UpskillQuizSubmittedActivity extends AppCompatActivity implements View.OnClickListener {

    String event_id, api_token;
    ConnectionDetector cd;
    SessionManager session;
    LinearLayout ll_main;
    TextView questionTv, tv_header, txt_count;
    RecyclerView quiz_list;
    Button btnNext, submit;
    List<QuizList> quizList;
    List<QuizList> questionList;
    public static String folder_id, foldername = "null";
    UpskillQuizSubmitedAdapter adapter;
    LinearLayoutManager llm;
    int count = 1;
    RelativeLayout relative, relative_main;
    ImageView iv_back, headerlogoIv;
    UpskillContentSubArray upskillContentSubArray;
    String last_submit = "";

    UpskillList upskillList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upskill_quiz_submitted);

        new RefreashToken(UpskillQuizSubmittedActivity.this).callGetRefreashToken(UpskillQuizSubmittedActivity.this);
        upskillContentSubArray = (UpskillContentSubArray) getIntent().getSerializableExtra("upskillContent");
        upskillList = (UpskillList) getIntent().getSerializableExtra("upskill_info");
        quizList = upskillContentSubArray.getContentInfo().get(0).getContent_desc_quiz().get(0).getQuiz_list();
        folder_id = upskillContentSubArray.getContentInfo().get(0).getContent_desc_quiz().get(0).getFolder_id();
        foldername = upskillContentSubArray.getContentInfo().get(0).getContent_desc_quiz().get(0).getFolder_name();
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        headerlogoIv = findViewById(R.id.headerlogoIv);

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

        relative = findViewById(R.id.relative);
        txt_count = findViewById(R.id.txt_count);
        questionTv = findViewById(R.id.questionTv);
        quiz_list = findViewById(R.id.quiz_list);
        submit = findViewById(R.id.submit);
        btnNext = findViewById(R.id.btnNext);
        tv_header = findViewById(R.id.tv_header);
        relative_main = findViewById(R.id.relative_main);

        quiz_list.setLayoutManager(new LinearLayoutManager(UpskillQuizSubmittedActivity.this, LinearLayoutManager.HORIZONTAL, false));
        llm = (LinearLayoutManager) quiz_list.getLayoutManager();

        session = new SessionManager(getApplicationContext());

        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(this, EVENT_ID);

        CommonFirebase.crashlytics("QuizSubmitted", api_token);
        CommonFirebase.firbaseAnalytics(this, "QuizSubmitted", api_token);
        cd = ConnectionDetector.getInstance(this);
        foldername = getIntent().getExtras().getString("folder_name");
        folder_id = getIntent().getExtras().getString("folder_id");
        submit.setOnClickListener(this);
        questionTv.setText(foldername);
        questionList = quizList;
        /*for (int i = 0; i < quizList.size(); i++) {
                questionList = quizList.get(i).getQuestion();
        }*/
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = adapter.getItemCount();
                if (i != count) {
                    quiz_list.getLayoutManager().scrollToPosition(llm.findLastVisibleItemPosition() + 1);
                    txt_count.setText("Questions " + (count + 1) + "/" + i);
                    count = count + 1;
                    if (questionList.size() == llm.findLastVisibleItemPosition() + 2) {
                        btnNext.setVisibility(View.GONE);
                        submit.setVisibility(View.VISIBLE);
                        if (upskillContentSubArray.getContentInfo().size() == click_count+1) {
                            btnNext.setText("Submit");
                            submit.setText("Submit");
                        } else {
                            btnNext.setText("Next");
                            submit.setText("Next");
                        }
                    } else {
                        btnNext.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.GONE);
                    }
                }
            }
        });

        adapter = new UpskillQuizSubmitedAdapter(UpskillQuizSubmittedActivity.this, questionList);
        quiz_list.setAdapter(adapter);
        int itemcount = adapter.getItemCount();
        txt_count.setText("Questions " + 1 + "/" + itemcount);
        if (questionList.size() > 1) {
            btnNext.setVisibility(View.VISIBLE);
            submit.setVisibility(View.GONE);
        } else {
            btnNext.setVisibility(View.GONE);
            submit.setVisibility(View.VISIBLE);
        }
        /*if (cd.isConnectingToInternet()) {
            quizDetailViewModel.getQuizList(api_token, event_id);

            quizDetailViewModel.getQuizList().observe(this, new Observer<QuizListing>() {
                @Override
                public void onChanged(QuizListing event) {
                    RefreashToken refreashToken = new RefreashToken(UpskillQuizSubmittedActivity.this);
                    String decrypteventdetail = refreashToken.decryptedData(event.getDetail());
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
                    SharedPreference.putPref(UpskillQuizSubmittedActivity.this, map);

                    *//*for (int i = 0; i < quizList.size(); i++) {
                        if (folder_id.equalsIgnoreCase(quizList.get(i).getFolder_id())) {
                            questionList = quizList.get(i).getQuiz_question();
                        }
                    }*//*
                    adapter = new QuizSubmitedAdapter(UpskillQuizSubmittedActivity.this, questionList);
                    quiz_list.setAdapter(adapter);
                    int itemcount = adapter.getItemCount();
                    txt_count.setText("Questions " + 1 + "/" + itemcount);
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
        }*/
    }

    @Override
    public void onClick(View v) {
        if (v == submit) {
            if (upskillContentSubArray.getContentInfo().size() == 1) {
                last_submit = "1";
            } else {
                last_submit = "0";
            }
            submitAnalytics();
            //upskillContentSubArray.getContentInfo().remove(0);
            click_count = click_count + 1;
            onNavigation();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        click_count = click_count - 1;
        onNavigation();
    }

    private void onNavigation() {
        if(click_count>0) {
            if (upskillContentSubArray.getContentInfo().size() > click_count) {
                if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Text")) {
                    startActivity(new Intent(UpskillQuizSubmittedActivity.this, UpskillDetailsTextActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                    finish();
                } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Survey")) {
                    startActivity(new Intent(UpskillQuizSubmittedActivity.this, UpskillSurveyActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                    finish();
                } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Poll")) {
                    startActivity(new Intent(UpskillQuizSubmittedActivity.this, UpskillDetailsPollActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                    finish();
                } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Pdf")) {
                    startActivity(new Intent(UpskillQuizSubmittedActivity.this, UpskillDetailsPdfActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                    finish();
                } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Image")) {
                    startActivity(new Intent(UpskillQuizSubmittedActivity.this, UpskillDetailsImageActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                    finish();
                } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Video")) {
                    startActivity(new Intent(UpskillQuizSubmittedActivity.this, UpskillDetailsVideoActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                    finish();
                } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Quiz")) {
                    if (upskillContentSubArray.getContentInfo().get(click_count).getContent_desc_quiz().get(0).getReplied().equalsIgnoreCase("0")) {
                        startActivity(new Intent(UpskillQuizSubmittedActivity.this, UpskillDetailsQuizActivity.class)
                                .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                        finish();
                    } else {
                        Intent intent = new Intent(UpskillQuizSubmittedActivity.this, UpskillQuizSubmittedActivity.class);
                        intent.putExtra("folderName", upskillContentSubArray.getContentInfo().get(click_count).getContent_desc_quiz().get(0).getFolder_name());
                        intent.putExtra("folderid", upskillContentSubArray.getContentInfo().get(click_count).getContent_desc_quiz().get(0).getFolder_id());
                        intent.putExtra("upskillContent", (Serializable) upskillContentSubArray);
                        intent.putExtra("Page", "Question");
                        startActivity(intent);
                        finish();
                   /* startActivity(new Intent(UpskillQuizSubmittedActivity.this, UpskillQuizSubmittedActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray));*/
                    }
                } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Audio")) {
                    startActivity(new Intent(UpskillQuizSubmittedActivity.this, UpskillDetailsAudioActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                    finish();
                }
            }
        }else
        {
            startActivity(new Intent(UpskillQuizSubmittedActivity.this,UpskillDetailsFirstActivity.class)
                    .putExtra("upskill_info", (Serializable) upskillList));
        }
    }


    private void submitAnalytics() {
        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(this, EVENT_ID);
        ApiUtils.getAPIService().AddTrainingAnalytics(api_token, event_id, upskillContentSubArray.getContentInfo().get(0).getTraining_id(),
                "Quiz", upskillContentSubArray.getMainInfo().getId(), last_submit)
                .enqueue(new Callback<FetchAgenda>() {
                    @Override
                    public void onResponse(Call<FetchAgenda> call, Response<FetchAgenda> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strType = response.body().getHeader().get(0).getType();
                                if (strType.equalsIgnoreCase("success")) {

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FetchAgenda> call, Throwable t) {
                        Log.e("Message", t.getMessage());
                    }
                });
    }
}