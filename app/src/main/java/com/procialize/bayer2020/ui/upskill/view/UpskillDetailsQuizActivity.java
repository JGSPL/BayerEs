package com.procialize.bayer2020.ui.upskill.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.GetterSetter.Header;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.costumTools.CustomViewPager;
import com.procialize.bayer2020.ui.agenda.model.FetchAgenda;
import com.procialize.bayer2020.ui.quiz.model.QuizSubmit;
import com.procialize.bayer2020.ui.upskill.adapter.UpskillQuizPagerAdapter;
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

public class UpskillDetailsQuizActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txt_count;
    ImageView iv_banner;
    String api_token, event_id,trainingId;
    Button btn_next, btn_quiz_next;
    UpskillContentSubArray upskillContentSubArray;
    CustomViewPager pager;
    UpskillQuizPagerAdapter upskillpagerAdapter;
    String last_submit = "0",folderName,folderId;
    List<QuizList> quizList;
    public static int countpage = 1;
    public static boolean submitflag = false;
    boolean flag = true;
    boolean flag1 = true;
    boolean flag2 = true;
    public static String quiz_question_id;
    private String quiz_options_id, folder_id;
    public static int count1 = 1;

    UpskillList upskillList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upskill_details_quiz);

        upskillContentSubArray = (UpskillContentSubArray) getIntent().getSerializableExtra("upskillContent");
        upskillList = (UpskillList) getIntent().getSerializableExtra("upskill_info");
        setUpToolbar();
        folder_id = upskillContentSubArray.getContentInfo().get(click_count).getContent_desc_quiz().get(0).getFolder_id();
        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(this, EVENT_ID);
        trainingId = upskillContentSubArray.getContentInfo().get(click_count).getTraining_id();
        txt_count = findViewById(R.id.txt_count);
        btn_next = findViewById(R.id.btn_next);
        btn_quiz_next = findViewById(R.id.btn_quiz_next);
        pager = findViewById(R.id.pager);
        btn_next.setOnClickListener(this);
        btn_quiz_next.setOnClickListener(this);
        folderName = upskillContentSubArray.getContentInfo().get(click_count).getContent_desc_quiz().get(0).getFolder_name();
        folderId = upskillContentSubArray.getContentInfo().get(click_count).getContent_desc_quiz().get(0).getFolder_id();
        quizList = upskillContentSubArray.getContentInfo().get(click_count).getContent_desc_quiz().get(0).getQuiz_list();
        txt_count.setText("Questions 1" + "/" + quizList.size());
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                txt_count.setText("Questions " + (position + 1) + "/" + quizList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        upskillpagerAdapter = new UpskillQuizPagerAdapter(this, quizList);
        pager.setAdapter(upskillpagerAdapter);
        pager.setPagingEnabled(false);

        if (quizList.size() == 1 && upskillContentSubArray.getContentInfo().size() == 1) {
            btn_next.setVisibility(View.VISIBLE);
            btn_quiz_next.setVisibility(View.GONE);
            btn_next.setText("Submit");
        } else if (quizList.size() > 1 && upskillContentSubArray.getContentInfo().size() == 1) {
            btn_quiz_next.setVisibility(View.VISIBLE);
            btn_next.setVisibility(View.GONE);
            btn_next.setText("Next");
        } else if (upskillContentSubArray.getContentInfo().size() == click_count + 1) {
            btn_next.setVisibility(View.VISIBLE);
            btn_quiz_next.setVisibility(View.GONE);
            btn_next.setText("Submit");
        } else if (quizList.size() > 1) {
            btn_quiz_next.setVisibility(View.VISIBLE);
            btn_next.setVisibility(View.GONE);
            btn_next.setText("Next");
        } else {
            btn_quiz_next.setVisibility(View.GONE);
            btn_next.setVisibility(View.VISIBLE);
            btn_next.setText("Next");
        }

        btn_quiz_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int option = upskillpagerAdapter.getSelectedOption();
                int i = upskillpagerAdapter.getItemCount();
                api_token = SharedPreference.getPref(UpskillDetailsQuizActivity.this, AUTHERISATION_KEY);
                event_id = SharedPreference.getPref(UpskillDetailsQuizActivity.this, EVENT_ID);

                if (quizList.size() == pager.getCurrentItem() + 1) {
                    btn_quiz_next.setVisibility(View.GONE);
                    // submit.setVisibility(View.VISIBLE);
                    submitflag = true;
                    if (upskillContentSubArray.getContentInfo().size() == 1) {
                        btn_next.setVisibility(View.VISIBLE);
                        btn_quiz_next.setVisibility(View.GONE);
                        btn_next.setText("Submit");

                        submitAnswer();

                        if (upskillContentSubArray.getContentInfo().size() == 1) {
                            last_submit = "1";
                        } else {
                            last_submit = "0";
                        }
                        submitAnalytics();

                    } else {
                        btn_quiz_next.setVisibility(View.VISIBLE);
                        btn_next.setVisibility(View.GONE);
                        btn_next.setText("Next");

                        submitAnswer();

                        if (upskillContentSubArray.getContentInfo().size() == 1) {
                            last_submit = "1";
                        } else {
                            last_submit = "0";
                        }
                        submitAnalytics();

                    }
                } else if (quizList.size() >= pager.getCurrentItem() + 1) {
                    submitflag = false;
                    btn_quiz_next.setVisibility(View.VISIBLE);
                    //submit.setVisibility(View.GONE);
                }
                pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                countpage = pager.getCurrentItem();
            }
        });
    }

    private void setUpToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            mToolbar.showOverflowMenu();
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
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

    @Override
    public void onClick(View v) {
        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(this, EVENT_ID);
        switch (v.getId()) {
            case R.id.btn_next:
                if (upskillContentSubArray.getContentInfo().size() == 1) {
                    last_submit = "1";
                } else {
                    last_submit = "0";
                }
                submitAnalytics();
                //click_count = click_count + 1;
                submitAnswer();

                //onNavigation();
                break;
        }
    }

    private void submitAnalytics() {
        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(this, EVENT_ID);
        ApiUtils.getAPIService().AddTrainingAnalytics(api_token, event_id, trainingId,
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

    private void submitAnswer() {
        submitflag = true;
        Boolean valid = true;
        final String[] question_id = {""};
        final String[] question_ans = {""};

        String[] data = upskillpagerAdapter.getselectedOption();

        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                if (i != 0) {
                    question_id[0] = question_id[0] + "$#";
                    question_ans[0] = question_ans[0] + "$#";
                }

                String id = quizList.get(i).getQuiz_id();
                question_id[0] = question_id[0] + id;

                flag = true;
                flag1 = true;
                flag2 = true;
                if (data[i] != null) {
                    if (quizList.get(i).getReplied() != null) {
                        if (quizList.get(i).getReplied().equalsIgnoreCase("0")) {
                            if (!data[i].equalsIgnoreCase("")) {
                                question_ans[0] = question_ans[0] + data[i];
                            } else {
                                valid = false;
                            }
                        } else {

                            if (!data[i].equalsIgnoreCase("")) {
                                String idno = quizList.get(i).getQuiz_id();
                                for (int j = 0; j < quizList.get(i).getOption().size(); j++) {
                                    if (quizList.get(i).getOption().get(j).getOption().equals(data[i]) && quizList.get(i).getOption().get(j).getQuiz_id().equals(idno)) {
                                        question_ans[0] = question_ans[0] + quizList.get(i).getOption().get(j).getOption_id();
                                    }
                                }
                            } else {
                                String idno = quizList.get(i).getQuiz_id();
                                for (int j = 0; j < quizList.get(i).getOption().size(); j++) {
                                    if (quizList.get(i).getOption().get(j).getQuiz_id().equals(idno)) {
                                        if (flag1 == true) {
                                            question_ans[0] = question_ans[0] + "0";
                                            flag1 = false;
                                        }
                                    }
                                }
                            }
                        }
                    } else {

                        if (!data[i].equalsIgnoreCase("")) {
                            String idno = quizList.get(i).getQuiz_id();
                            for (int j = 0; j < quizList.get(i).getOption().size(); j++) {
                                if (quizList.get(i).getOption().get(j).getOption().equals(data[i]) && quizList.get(i).getOption().get(j).getQuiz_id().equals(idno)) {
                                    question_ans[0] = question_ans[0] + quizList.get(i).getOption().get(j).getOption_id();
                                }
                            }
                        } else {
                            String idno = quizList.get(i).getQuiz_id();
                            for (int j = 0; j < quizList.get(i).getOption().size(); j++) {
                                if (quizList.get(i).getOption().get(j).getQuiz_id().equals(idno)) {
                                    if (flag2 == true) {
                                        question_ans[0] = question_ans[0] + "0";
                                        flag2 = false;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    String idno = quizList.get(i).getQuiz_id();
                    for (int j = 0; j < quizList.get(i).getOption().size(); j++) {
                        if (quizList.get(i).getOption().get(j).getQuiz_id().equals(idno)) {
                            if (flag == true) {
                                question_ans[0] = question_ans[0] + "0";
                                flag = false;
                            }
                        }
                    }
                }
            }


            if (submitflag == true) {
                quiz_question_id = question_id[0];
                quiz_options_id = question_ans[0];

                ApiUtils.getAPIService().TrainingQuizReply(api_token, event_id, quiz_question_id, quiz_options_id, folder_id)
                        .enqueue(new Callback<QuizSubmit>() {
                            @Override
                            public void onResponse(Call<QuizSubmit> call, Response<QuizSubmit> response) {
                                if (response.isSuccessful()) {
                                    upskillpagerAdapter.checkArray = null;
                                    upskillpagerAdapter.correctAnswer = "";
                                    upskillpagerAdapter.selectedOption = "";
                                    upskillpagerAdapter.dataArray = null;
                                    upskillpagerAdapter.dataIDArray = null;
                                    upskillpagerAdapter.ansArray = null;

                                    try {
                                        List<Header> header = response.body().getHeader();

                                        if (header.get(0).getType().equalsIgnoreCase("success")) {
                                            count1 = 1;
                                            upskillpagerAdapter.selectopt = 0;
                                            submitflag = true;
                                            int answers = upskillpagerAdapter.getCorrectOption();
                                            Intent intent = new Intent(UpskillDetailsQuizActivity.this, UpskillQuizSubmittedActivity.class);
                                            intent.putExtra("upskillContent", (Serializable) upskillContentSubArray);
                                            intent.putExtra("folderName", folderName);
                                            intent.putExtra("folderid", folderId);
                                            intent.putExtra("Answers", response.body().getTotal_correct_answer());
                                            intent.putExtra("TotalQue", response.body().getTotal_questions());
                                            intent.putExtra("TotalQue", response.body().getTotal_questions());
                                            intent.putExtra("Page", "Question");
                                            startActivity(intent);
                                            finish();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<QuizSubmit> call, Throwable t) {
                                //quizsubmit.setValue(null);
                            }
                        });
            }
        }
    }

    private void onNavigation() {

        try {
            btn_next.setEnabled(false);
            //if (last_submit.equalsIgnoreCase("0")) {
            //upskillContentSubArray.getContentInfo().remove(0);
            if (click_count > 0) {
                if (upskillContentSubArray.getContentInfo().size() > click_count) {


                    if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Text")) {
                        startActivity(new Intent(this, UpskillDetailsTextActivity.class)
                                .putExtra("upskillContent", (Serializable) upskillContentSubArray)
                                .putExtra("click_count", click_count)
                                .putExtra("upskill_info", (Serializable) upskillList));
                        finish();
                    } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Survey")) {
                        startActivity(new Intent(this, UpskillSurveyActivity.class)
                                .putExtra("upskillContent", (Serializable) upskillContentSubArray)
                                .putExtra("click_count", click_count)
                                .putExtra("upskill_info", (Serializable) upskillList));
                        finish();
                    } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Poll")) {
                        startActivity(new Intent(this, UpskillDetailsPollActivity.class)
                                .putExtra("upskillContent", (Serializable) upskillContentSubArray)
                                .putExtra("click_count", click_count)
                                .putExtra("upskill_info", (Serializable) upskillList));
                        finish();
                    } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Pdf")) {
                        startActivity(new Intent(this, UpskillDetailsPdfActivity.class)
                                .putExtra("upskillContent", (Serializable) upskillContentSubArray)
                                .putExtra("click_count", click_count)
                                .putExtra("upskill_info", (Serializable) upskillList));
                        finish();
                    } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Image")) {
                        startActivity(new Intent(this, UpskillDetailsImageActivity.class)
                                .putExtra("upskillContent", (Serializable) upskillContentSubArray)
                                .putExtra("click_count", click_count)
                                .putExtra("upskill_info", (Serializable) upskillList));
                        finish();
                    } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Video")) {
                        startActivity(new Intent(this, UpskillDetailsVideoActivity.class)
                                .putExtra("upskillContent", (Serializable) upskillContentSubArray)
                                .putExtra("click_count", click_count)
                                .putExtra("upskill_info", (Serializable) upskillList));
                        finish();
                    } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Quiz")) {
                        if (upskillContentSubArray.getContentInfo().get(click_count).getContent_desc_quiz().get(0).getReplied().equalsIgnoreCase("0")) {
                            startActivity(new Intent(this, UpskillDetailsQuizActivity.class)
                                    .putExtra("upskillContent", (Serializable) upskillContentSubArray)
                                    .putExtra("click_count", click_count)
                                    .putExtra("upskill_info", (Serializable) upskillList));
                            finish();
                        } else {
                            Intent intent = new Intent(this, UpskillQuizSubmittedActivity.class);
                            intent.putExtra("folderName", upskillContentSubArray.getContentInfo().get(click_count).getContent_desc_quiz().get(0).getFolder_name());
                            intent.putExtra("folderid", upskillContentSubArray.getContentInfo().get(click_count).getContent_desc_quiz().get(0).getFolder_id());
                            intent.putExtra("upskillContent", (Serializable) upskillContentSubArray);
                            intent.putExtra("Page", "Question");
                            intent.putExtra("click_count", click_count);
                            intent.putExtra("upskill_info", (Serializable) upskillList);
                            startActivity(intent);
                            finish();
                       /* startActivity(new Intent(UpskillDetailsFirstActivity.this, UpskillQuizSubmittedActivity.class)
                                .putExtra("upskillContent", (Serializable) upskillContentSubArray));*/
                        }
                    } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Audio")) {
                        startActivity(new Intent(this, UpskillDetailsAudioActivity.class)
                                .putExtra("upskillContent", (Serializable) upskillContentSubArray)
                                .putExtra("click_count", click_count)
                                .putExtra("upskill_info", (Serializable) upskillList));
                        finish();
                    }
                }
                //}
               /* else {
                    submitAnswer();
                }*/
            } else {
                startActivity(new Intent(this, UpskillDetailsFirstActivity.class)
                        .putExtra("upskill_info", (Serializable) upskillList));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        click_count = click_count - 1;
        onNavigation();
    }
}