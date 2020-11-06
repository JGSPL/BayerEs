package com.procialize.eventapp.ui.spotPoll.view;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.ui.agenda.model.Agenda;
import com.procialize.eventapp.ui.livepoll.adapter.PollGraphAdapter;
import com.procialize.eventapp.ui.livepoll.model.FetchLivePoll;
import com.procialize.eventapp.ui.livepoll.model.LivePoll;
import com.procialize.eventapp.ui.livepoll.model.LivePoll_option;
import com.procialize.eventapp.ui.livepoll.model.Logo;
import com.procialize.eventapp.ui.livepoll.view.PollDetailActivity;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;

public class SpotPolldetailFragment extends Fragment implements View.OnClickListener{
    String questionId, question, replyFlag, quiz_options_id, show_result, session_id;
    ArrayList<LivePoll_option> optionLists = new ArrayList<>();
    List<LivePoll> LivePollList;
    List<LivePoll> LivePollListResult = new ArrayList<>();

    TextView questionTv, test;
    Button subBtn, PollBtn;
    ProgressBar progressBar;
    String eventid;
    ViewGroup viewGroup;
    List<RadioButton> radios;
    String token, colorActive;
    LinearLayout linView;
    RecyclerView pollGraph;

    private APIService mAPIService;
    LinearLayout relMain;
    RelativeLayout relative;
    View root;
    MutableLiveData<FetchLivePoll> FetchLivePollList = new MutableLiveData<>();
    private APIService eventApi;
    LinearLayout linThankyou;
    Button ratebtn;
    TextView tvPollTitle, tvPollTitle2;
    NestedScrollView ScrollMain;

    public static SpotPollFragment newInstance() {

        return new SpotPollFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.spotpoll_detailfrag, container, false);
        init();
        
        if (ConnectionDetector.getInstance(getActivity()).isConnectingToInternet()) {

        } else {
        }

        return root;
    }
    
    void init(){
        pollGraph = root.findViewById(R.id.pollGraph);
        relative = root.findViewById(R.id.relative);
        test = root.findViewById(R.id.test);
        questionTv = root.findViewById(R.id.questionTv);
        relMain = root.findViewById(R.id.relMain);
        subBtn = root.findViewById(R.id.subBtn);
        PollBtn = root.findViewById(R.id.PollBtn);
        progressBar = root.findViewById(R.id.progressBar);
        ratebtn = root.findViewById(R.id.ratebtn);
        linThankyou = root.findViewById(R.id.linThankyou);
        tvPollTitle = root.findViewById(R.id.tvPollTitle);
        tvPollTitle2 = root.findViewById(R.id.tvPollTitle2);
        ScrollMain = root.findViewById(R.id.ScrollMain);

        subBtn.setOnClickListener(this);
        CommonFunction.showBackgroundImage(getContext(), relative);

        eventid = SharedPreference.getPref(getContext(), EVENT_ID);
        colorActive = SharedPreference.getPref(getContext(), EVENT_COLOR_3);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        pollGraph.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);
        pollGraph.setLayoutAnimation(animation);

        optionLists = new ArrayList<>();
        mAPIService = ApiUtils.getAPIService();
        LinearLayout rate2 = root.findViewById(R.id.rate2);

        rate2.setBackgroundColor(Color.parseColor(SharedPreference.getPref(getContext(), EVENT_COLOR_4)));
        ratebtn.setBackgroundColor(Color.parseColor(SharedPreference.getPref(getContext(), EVENT_COLOR_2)));

        ratebtn.setTextColor(Color.parseColor(SharedPreference.getPref(getContext(), EVENT_COLOR_4)));
        tvPollTitle.setTextColor(Color.parseColor(SharedPreference.getPref(getContext(), EVENT_COLOR_4)));
        tvPollTitle2.setTextColor(Color.parseColor(SharedPreference.getPref(getContext(), EVENT_COLOR_4)));

        questionTv.setTextColor(Color.parseColor(SharedPreference.getPref(getContext(),EVENT_COLOR_1)));
        relMain.setBackgroundColor(Color.parseColor(SharedPreference.getPref(getContext(),EVENT_COLOR_2)));
        ScrollMain.setBackgroundColor(Color.parseColor(SharedPreference.getPref(getContext(),EVENT_COLOR_2)));
        relative.setBackgroundColor(Color.parseColor(SharedPreference.getPref(getContext(),EVENT_COLOR_2)));
        subBtn.setBackgroundColor(Color.parseColor(SharedPreference.getPref(getContext(),EVENT_COLOR_1)));
        subBtn.setTextColor(Color.parseColor(SharedPreference.getPref(getContext(),EVENT_COLOR_2)));
        PollBtn.setBackgroundColor(Color.parseColor(SharedPreference.getPref(getContext(),EVENT_COLOR_1)));
        PollBtn.setTextColor(Color.parseColor(SharedPreference.getPref(getContext(),EVENT_COLOR_2)));

        // token
        token = SharedPreference.getPref(getContext(), AUTHERISATION_KEY);

        radios = new ArrayList<RadioButton>();

        try {
            questionId = getArguments().getString("id");
            question = getArguments().getString("question");
            replyFlag = getArguments().getString("replied");
            show_result = getArguments().getString("show_result");
            session_id = getArguments().getString("session_id");
            optionLists = (ArrayList<LivePoll_option>) getArguments().getSerializable("optionlist");

        } catch (Exception e) {
            e.printStackTrace();
        }


        if (replyFlag != null) {
            if (replyFlag.equalsIgnoreCase("1")) {
                subBtn.setVisibility(View.GONE);

                linThankyou.setVisibility(View.VISIBLE);
                questionTv.setVisibility(View.GONE);
            } else {
                subBtn.setVisibility(View.VISIBLE);
                questionTv.setVisibility(View.VISIBLE);
                linThankyou.setVisibility(View.GONE);

            }
        }

        questionTv.setText(StringEscapeUtils.unescapeJava(question));

        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linThankyou.setVisibility(View.GONE);
                viewGroup.setVisibility(View.GONE);
                questionTv.setVisibility(View.VISIBLE);

                PollGraphAdapter pollAdapter = new PollGraphAdapter(getContext(), optionLists, questionId);
                pollAdapter.notifyDataSetChanged();
                pollGraph.setAdapter(pollAdapter);
                pollGraph.scheduleLayoutAnimation();
                subBtn.setVisibility(View.GONE);
                PollBtn.setVisibility(View.GONE);
            }
        });



        if (optionLists.size() != 0) {

            viewGroup = (RadioGroup) root.findViewById(R.id.radiogroup);
            // viewGroup.setOnCheckedChangeListener(getContext());

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
            Toast.makeText(getContext(), "Option not available for this poll", Toast.LENGTH_SHORT).show();
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
                linThankyou.setVisibility(View.VISIBLE);
                questionTv.setVisibility(View.GONE);

               /* PollGraphAdapter pollAdapter = new PollGraphAdapter(getContext(), optionLists, questionId);
                pollAdapter.notifyDataSetChanged();
                pollGraph.setAdapter(pollAdapter);
                pollGraph.scheduleLayoutAnimation();
                subBtn.setVisibility(View.GONE);
                PollBtn.setVisibility(View.VISIBLE);*/
            } else {
            }

        } else {

            pollGraph.setVisibility(View.GONE);
            viewGroup.setVisibility(View.VISIBLE);

            for (int row = 0; row < 1; row++) {

                for (int i = 1; i < number; i++) {

                    LinearLayout ll = new LinearLayout(getContext());

                    LinearLayout l3 = new LinearLayout(getContext());
                    FrameLayout fl = new FrameLayout(getContext());

                    ll.setOrientation(LinearLayout.HORIZONTAL);
                    ll.setPadding(5, 10, 5, 10);

                    LinearLayout ll2 = new LinearLayout(getContext());
                    ll2.setOrientation(LinearLayout.HORIZONTAL);


                    LinearLayout.LayoutParams rprms, rprmsRdBtn, rpms2;

                    RadioButton rdbtn = new RadioButton(getContext());
                    rdbtn.setId((row * 2) + i);
                    rdbtn.setText(StringEscapeUtils.unescapeJava(optionLists.get(i - 1).getOption()));
                    rdbtn.setTextColor(Color.parseColor("#898989"));
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
                Toast.makeText(getContext(), "You Already Submited This Poll", Toast.LENGTH_SHORT).show();

            } else {
                if (quiz_options_id != null) {
                    LivePollSubmitFetch(token, eventid, questionId, quiz_options_id);

                } else {
                    Toast.makeText(getContext(), "Please select an option", Toast.LENGTH_SHORT).show();

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

    public void LivePollSubmitFetch(String token, String eventid, String pollid, String polloptionid) {
        showProgress();
        mAPIService.SpotLivePollSubmit(token, eventid, pollid, polloptionid).enqueue(new Callback<LoginOrganizer>() {
            @Override
            public void onResponse(Call<LoginOrganizer> call, Response<LoginOrganizer> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());


                    dismissProgress();
                    showResponse(response);
                } else {

                    dismissProgress();
                    Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();

                dismissProgress();
            }
        });
    }

    public void showResponse(Response<LoginOrganizer> response) {

        // specify an adapter (see also next example)
        if (response.body().getHeader().get(0).getType().equalsIgnoreCase("Success")) {
            getLivepoll(token,eventid);


        } else {
            Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();

        }
    }

    public MutableLiveData<FetchLivePoll> getLivepoll(String token, String eventid) {
        eventApi = ApiUtils.getAPIService();

        eventApi.SpotLivePollFetch(token,eventid,session_id)
                .enqueue(new Callback<FetchLivePoll>() {
                    @Override
                    public void onResponse(Call<FetchLivePoll> call, Response<FetchLivePoll> response) {
                        if (response.isSuccessful()) {
                            FetchLivePollList.setValue(response.body());

                                    optionLists.clear();
                                    String strCommentList = response.body().getDetail();
                                    RefreashToken refreashToken = new RefreashToken(getContext());
                                    String data = refreashToken.decryptedData(strCommentList);
                                    Gson gson = new Gson();
                                    List<Logo> eventLists = gson.fromJson(data, new TypeToken<ArrayList<Logo>>() {
                                    }.getType());

                                    //   fetchPoll(token, eventid);
                                    if (eventLists.get(0).getLivePoll_list() != null) {
                                        pollGraph.setVisibility(View.VISIBLE);
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
                                                optionLists.addAll(poll.getLive_poll_option_list());
                                                LivePollListResult.add(poll);

                                            }
                                        }


                                        replyFlag = "1";
                                        subBtn.setVisibility(View.GONE);

                                        if (show_result.equalsIgnoreCase("0")) {
                                            if (LivePollListResult.size() != 0) {


                                                viewGroup.setVisibility(View.GONE);
                                                linThankyou.setVisibility(View.VISIBLE);
                                                questionTv.setVisibility(View.GONE);

                                                //optionLists.add( LivePollListResult.get(0).getLive_poll_option_list());
                                               /* PollGraphAdapter pollAdapter = new PollGraphAdapter(getContext(), LivePollListResult.get(0).getLive_poll_option_list(), questionId);
                                                pollAdapter.notifyDataSetChanged();
                                                pollGraph.setAdapter(pollAdapter);
                                                pollGraph.scheduleLayoutAnimation();
                                                subBtn.setVisibility(View.GONE);
                                                PollBtn.setVisibility(View.VISIBLE);*/

                                            }


                                        } else {



                                        }

                                    }

                                    Toast.makeText(getContext(),
                                            response.body().getHeader().get(0).getMsg(), Toast.LENGTH_SHORT)
                                            .show();


                                }else{
                                    progressBar.setVisibility(View.GONE);

                                }
                    }

                    @Override
                    public void onFailure(Call<FetchLivePoll> call, Throwable t) {
                        FetchLivePollList.setValue(null);
                    }
                });

        return FetchLivePollList;
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
}
