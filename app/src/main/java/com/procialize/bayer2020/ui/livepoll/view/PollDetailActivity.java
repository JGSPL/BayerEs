package com.procialize.bayer2020.ui.livepoll.view;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.ui.livepoll.adapter.PollGraphAdapter;
import com.procialize.bayer2020.ui.livepoll.model.FetchLivePoll;
import com.procialize.bayer2020.ui.livepoll.model.LivePoll;
import com.procialize.bayer2020.ui.livepoll.model.LivePoll_option;
import com.procialize.bayer2020.ui.livepoll.model.Logo;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;


public class PollDetailActivity extends AppCompatActivity implements View.OnClickListener {

    String questionId, question, replyFlag, quiz_options_id, show_result;
    ArrayList<LivePoll_option> optionLists = new ArrayList<>();
    List<LivePoll_option> AlloptionLists;
    List<LivePoll> LivePollList;
    List<LivePoll> LivePollListResult = new ArrayList<>();

    TextView questionTv, test;
    RadioGroup ll;
    Button subBtn, PollBtn;
    ProgressBar progressBar;
    String selected;
    int Count;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    ViewGroup viewGroup;
    List<RadioButton> radios;
    String token, colorActive;
    LinearLayout linView;
    RecyclerView pollGraph;
    ImageView headerlogoIv, iv_back;

    private APIService mAPIService;
    RelativeLayout relative,relMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_detail);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = SharedPreference.getPref(this, EVENT_ID);
        colorActive = "#e4004b";

       /* Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.getNavigationIcon().setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)), PorterDuff.Mode.SRC_ATOP);*/

        headerlogoIv = findViewById(R.id.headerlogoIv);


        pollGraph = findViewById(R.id.pollGraph);
        relative = findViewById(R.id.relative);
        test = findViewById(R.id.test);
        iv_back = findViewById(R.id.iv_back);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_back.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)), PorterDuff.Mode.SRC_ATOP);


       // CommonFunction.showBackgroundImage(PollDetailActivity.this, relative);


        TextView title = findViewById(R.id.title);
       // title.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)));

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        pollGraph.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        pollGraph.setLayoutAnimation(animation);

        optionLists = new ArrayList<>();
        mAPIService = ApiUtils.getAPIService();

        questionTv = findViewById(R.id.questionTv);
        relMain = findViewById(R.id.relMain);
        subBtn = findViewById(R.id.subBtn);
        subBtn.setOnClickListener(this);
        /*questionTv.setTextColor(Color.parseColor(SharedPreference.getPref(this,EVENT_COLOR_1)));

        relMain.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this,EVENT_COLOR_2)));
        subBtn.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this,EVENT_COLOR_1)));

        subBtn.setTextColor(Color.parseColor(SharedPreference.getPref(this,EVENT_COLOR_2)));*/


        PollBtn = findViewById(R.id.PollBtn);
        /*PollBtn.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this,EVENT_COLOR_1)));
        PollBtn.setTextColor(Color.parseColor(SharedPreference.getPref(this,EVENT_COLOR_2)));*/

//        questionTv.setTextColor(Color.parseColor(colorActive));
        progressBar = findViewById(R.id.progressBar);


        // token
        token = SharedPreference.getPref(this, AUTHERISATION_KEY);

        radios = new ArrayList<RadioButton>();
        try {
            questionId = getIntent().getExtras().getString("id");
            question = getIntent().getExtras().getString("question");
            replyFlag = getIntent().getExtras().getString("replied");
            show_result = getIntent().getExtras().getString("show_result");
            optionLists = (ArrayList<LivePoll_option>) getIntent().getSerializableExtra("optionlist");

        } catch (Exception e) {
            e.printStackTrace();
        }
        PollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if (replyFlag != null) {
            if (replyFlag.equalsIgnoreCase("1")) {
                subBtn.setVisibility(View.GONE);
            } else {
                subBtn.setVisibility(View.VISIBLE);
            }
        }

        questionTv.setText(StringEscapeUtils.unescapeJava(question));



        if (optionLists.size() != 0) {

            viewGroup = (RadioGroup) findViewById(R.id.radiogroup);
            // viewGroup.setOnCheckedChangeListener(this);

            addRadioButtons(optionLists.size() + 1);

            if (viewGroup.isSelected()) {
                for (int i = 0; i < optionLists.size(); i++) {
                    test.setText(StringEscapeUtils.unescapeJava(optionLists.get(i).getOption()));
                    if (optionLists.get(i).getOption().equalsIgnoreCase(test.getText().toString())) {

                        quiz_options_id = optionLists.get(i)
                                .getOption_id();

                    }

                }
            }
        } else {
            Toast.makeText(PollDetailActivity.this, "Select Option", Toast.LENGTH_SHORT).show();
        }


    }


    public void LivePollSubmitFetch(String token, String eventid, String pollid, String polloptionid) {
        showProgress();
        mAPIService.LivePollSubmit(token, eventid, pollid, polloptionid).enqueue(new Callback<FetchLivePoll>() {
            @Override
            public void onResponse(Call<FetchLivePoll> call, Response<FetchLivePoll> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());


                    dismissProgress();
                    showResponse(response);
                } else {

                    dismissProgress();
                    Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchLivePoll> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();

                dismissProgress();
            }
        });
    }

    public void showResponse(Response<FetchLivePoll> response) {

        // specify an adapter (see also next example)
        if (response.body().getHeader().get(0).getType().equalsIgnoreCase("Success")) {
            optionLists.clear();
            String strCommentList = response.body().getDetail();
            RefreashToken refreashToken = new RefreashToken(PollDetailActivity.this);
            String data = refreashToken.decryptedData(strCommentList);
            Gson gson = new Gson();
            List<Logo> eventLists = gson.fromJson(data, new TypeToken<ArrayList<Logo>>() {
            }.getType());

            //   fetchPoll(token, eventid);
            if (eventLists.get(0).getLivePoll_list() != null) {
                pollGraph.setVisibility(View.VISIBLE);
               /* AlloptionLists.clear();
                AlloptionLists = response.body().getLivePollOptionList();
                if (AlloptionLists.size() != 0) {
                    for (int i = 0; i < AlloptionLists.size(); i++) {

                        if (AlloptionLists.get(i).getLivePollId().equalsIgnoreCase(questionId)) {
                            Count = Count + Integer.parseInt(AlloptionLists.get(i).getTotalUser());
                            optionLists.add(AlloptionLists.get(i));
                        }
                    }*/
                for (int i = 0; i < eventLists.get(0).getLivePoll_list().size(); i++) {
                    if (eventLists.get(0).getLivePoll_list().get(i).getLive_poll_id().equalsIgnoreCase(questionId)) {
                        LivePoll poll = new LivePoll();
                        poll.setLive_poll_id(eventLists.get(0).getLivePoll_list().get(i).getLive_poll_id());
                        poll.setHide_result(eventLists.get(0).getLivePoll_list().get(i).getHide_result());
                        poll.setId(eventLists.get(0).getLivePoll_list().get(i).getId());
                        poll.setQuestion(eventLists.get(0).getLivePoll_list().get(i).getQuestion());
                        poll.setReplied(eventLists.get(0).getLivePoll_list().get(i).getReplied());
                        poll.setShow_progress_bar(eventLists.get(0).getLivePoll_list().get(i).getShow_progress_bar());
                        poll.setReplied(eventLists.get(0).getLivePoll_list().get(i).getReplied());
                        poll.setLive_poll_option_list( eventLists.get(0).getLivePoll_list().get(i).getLive_poll_option_list());
                        //optionLists.add((LivePoll_option) eventLists.get(0).getLivePoll_list().get(i).getLive_poll_option_list());
                        LivePollListResult.add(poll);

                    }
                }


                replyFlag = "1";
                subBtn.setVisibility(View.GONE);

                if (show_result.equalsIgnoreCase("0")) {
                    if (LivePollListResult.size() != 0) {


                        viewGroup.setVisibility(View.GONE);
                        PollGraphAdapter pollAdapter = new PollGraphAdapter(this, LivePollListResult.get(0).getLive_poll_option_list(), questionId);
                        pollAdapter.notifyDataSetChanged();
                        pollGraph.setAdapter(pollAdapter);
                        pollGraph.scheduleLayoutAnimation();
                        subBtn.setVisibility(View.GONE);
                        PollBtn.setVisibility(View.VISIBLE);

                    }


                    } else {


                    finish();


            }

        }

            Toast.makeText(PollDetailActivity.this,
                    response.body().getHeader().get(0).getMsg(), Toast.LENGTH_SHORT)
                    .show();


        } else {
            Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();

        }
    }


    public void showProgress() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void dismissProgress() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }


    public void addRadioButtons(int number) {

        viewGroup.removeAllViewsInLayout();


        String[] color = {"#112F7A", "#0E73BA", "#04696E", "#00A89C", "#000000", "#4D4D4D", "#949494", "#112F7A", "#0E73BA", "#04696E", "#00A89C", "#000000"};

        Float totalUser = 0.0f;



        for (int k = 0; k < optionLists.size(); k++) {

            if (optionLists.get(k).getLive_poll_id()
                    .equalsIgnoreCase(questionId)) {
                totalUser = (totalUser + Float.parseFloat(optionLists
                        .get(k).getTotal_user()));

            }

        }

        if (replyFlag.equalsIgnoreCase("1")) {
            if (show_result.equalsIgnoreCase("0")) {
                viewGroup.setVisibility(View.GONE);
                PollGraphAdapter pollAdapter = new PollGraphAdapter(this, optionLists, questionId);
                pollAdapter.notifyDataSetChanged();
                pollGraph.setAdapter(pollAdapter);
                pollGraph.scheduleLayoutAnimation();
                subBtn.setVisibility(View.GONE);
                PollBtn.setVisibility(View.VISIBLE);
            } else {
                finish();
            }

        } else {

            pollGraph.setVisibility(View.GONE);
            viewGroup.setVisibility(View.VISIBLE);

            for (int row = 0; row < 1; row++) {

                for (int i = 1; i < number; i++) {

                    LinearLayout ll = new LinearLayout(this);

                    LinearLayout l3 = new LinearLayout(this);
                    FrameLayout fl = new FrameLayout(this);

                    ll.setOrientation(LinearLayout.HORIZONTAL);
                    ll.setPadding(5, 10, 5, 10);

                    LinearLayout ll2 = new LinearLayout(this);
                    ll2.setOrientation(LinearLayout.HORIZONTAL);


                    LinearLayout.LayoutParams rprms, rprmsRdBtn, rpms2;

                    RadioButton rdbtn = new RadioButton(this);
                    rdbtn.setId((row * 2) + i);
                    rdbtn.setText(StringEscapeUtils.unescapeJava(optionLists.get(i - 1).getOption()));
                    rdbtn.setTextColor(Color.parseColor("#000000"));
                    rdbtn.setOnClickListener(this);

                    radios.add(rdbtn);


                    rprms = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    rprms.setMargins(5, 5, 5, 5);

                    Float weight = 1.0f;

                    if (replyFlag.equalsIgnoreCase("1")) {


                        ll2.setBackgroundColor(Color.parseColor(color[i]));

                        weight = ((Float.parseFloat(optionLists.get(i - 1)
                                .getTotal_user()) / totalUser) * 100);


                    } else {

                        weight = 100.0f;
                    }

                    rpms2 = new LinearLayout.LayoutParams(0,
                            ViewGroup.LayoutParams.MATCH_PARENT, weight);
                    rpms2.setMargins(5, 5, 5, 5);


                    ll.setWeightSum(weight);
                    ll.setLayoutParams(rprms);

                    l3.setLayoutParams(rprms);
                    ll.setPadding(5, 10, 5, 10);
                    l3.setWeightSum(weight);

                    ll2.setLayoutParams(rpms2);


                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                    params.gravity = Gravity.CENTER;
                    rprmsRdBtn = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    rprms.setMargins(5, 5, 5, 5);

                    rdbtn.setLayoutParams(rprmsRdBtn);
                    if (Build.VERSION.SDK_INT >= 21) {

                        ColorStateList colorStateList = new ColorStateList(
                                new int[][]{

                                        new int[]{-android.R.attr.state_checked}, //disabled
                                        new int[]{android.R.attr.state_checked} //enabled
                                },
                                new int[]{

                                        Color.parseColor("#4d4d4d")//disabled
                                        , Color.parseColor(colorActive)//enabled

                                }
                        );


                        rdbtn.setButtonTintList(colorStateList);//set the color tint list
                        rdbtn.invalidate(); //could not be necessary
                    }
//                    rdbtn.setButtonDrawable(R.drawable.radio_buttontoggle_first);
                    rdbtn.setBackgroundResource(R.drawable.livepollback);
                    l3.addView(ll2, rpms2);


                    fl.addView(l3, rprms);
                    fl.addView(rdbtn, rprmsRdBtn);

                    // ll2.addView(rdbtn, rprmsRdBtn);
                    ll.addView(fl, params);

                    viewGroup.addView(ll, rprms);
                    viewGroup.invalidate();
                }

            }
        }

    }

    @Override
    public void onClick(View v) {
        if (v == subBtn) {
            if (replyFlag.equalsIgnoreCase("1")) {
                Toast.makeText(getApplicationContext(), "You Already Submited This Poll", Toast.LENGTH_SHORT).show();

            } else {
                if (quiz_options_id != null) {
                    LivePollSubmitFetch(token, eventid, questionId, quiz_options_id);

                } else {
                    Toast.makeText(getApplicationContext(), "Please select an option", Toast.LENGTH_SHORT).show();

                }
            }

        } else {

            String option = ((RadioButton) v).getText().toString();

            for (RadioButton radio : radios) {
                if (!radio.getText().equals(option)) {

                    radio.setChecked(false);

                }

            }

            for (int i = 0; i < optionLists.size(); i++) {
                test.setText(StringEscapeUtils.unescapeJava(optionLists.get(i).getOption()));
                if (option.equalsIgnoreCase(test.getText().toString())) {

                    quiz_options_id = optionLists.get(i)
                            .getOption_id();
                }

            }

        }
    }

    public void fetchPoll(String token, String eventid) {
        showProgress();
        mAPIService.livePollFetch(token, eventid).enqueue(new Callback<FetchLivePoll>() {
            @Override
            public void onResponse(Call<FetchLivePoll> call, Response<FetchLivePoll> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());


                    dismissProgress();
                    showResponseLiveList(response);
                } else {

                    dismissProgress();
                    Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchLivePoll> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();

                dismissProgress();

            }
        });
    }

    public void showResponseLiveList(Response<FetchLivePoll> response) {

        // specify an adapter (see also next example)
        if (response.body().getLivePollList().size() != 0) {

            AlloptionLists.clear();

            LivePollList = response.body().getLivePollList().get(0).getLivePoll_list();
            /*if (AlloptionLists.size() != 0) {
                for (int i = 0; i < AlloptionLists.size(); i++) {

                    if (AlloptionLists.get(i).getLivePollId().equalsIgnoreCase(questionId)) {
                        Count = Count + Integer.parseInt(AlloptionLists.get(i).getTotalUser());
                        optionLists.add(AlloptionLists.get(i));
                    }
                }*/
            if (LivePollList.size() != 0) {
                for (int i = 0; i < LivePollList.size(); i++) {

                    if (LivePollList.get(i).getLive_poll_id().equalsIgnoreCase(questionId)) {
                        Count = Count + Integer.parseInt(LivePollList.get(i).getLive_poll_option_list().get(0).getTotal_user());
                        optionLists = (ArrayList<LivePoll_option>) LivePollList.get(i).getLive_poll_option_list();
                    }
                }

                replyFlag = "1";
                subBtn.setVisibility(View.GONE);
                if (optionLists.size() != 0) {

                    viewGroup = (RadioGroup) findViewById(R.id.radiogroup);

                    addRadioButtons(optionLists.size() + 1);



                }
                startActivity(getIntent());


            }

        } else {
            Toast.makeText(getApplicationContext(), "No Poll Available", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
