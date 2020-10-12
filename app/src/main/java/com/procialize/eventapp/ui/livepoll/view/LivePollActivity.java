package com.procialize.eventapp.ui.livepoll.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.session.SessionManager;
import com.procialize.eventapp.ui.attendee.view.AttendeeDetailActivity;
import com.procialize.eventapp.ui.eventinfo.viewmodel.EventInfoViewModel;
import com.procialize.eventapp.ui.livepoll.adapter.LivePollAdapter;
import com.procialize.eventapp.ui.livepoll.model.FetchLivePoll;
import com.procialize.eventapp.ui.livepoll.model.LivePoll;
import com.procialize.eventapp.ui.livepoll.model.LivePoll_option;
import com.procialize.eventapp.ui.livepoll.model.Logo;
import com.procialize.eventapp.ui.livepoll.viewmodel.LivePollViewModel;
import com.procialize.eventapp.ui.speaker.adapter.SpeakerAdapter;
import com.procialize.eventapp.ui.speaker.model.Speaker;
import com.procialize.eventapp.ui.speaker.view.SpeakerFragment;


import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.eventapp.Utility.CommonFirebase.crashlytics;
import static com.procialize.eventapp.Utility.CommonFirebase.firbaseAnalytics;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;

public class LivePollActivity extends AppCompatActivity implements LivePollAdapter.PollAdapterListner {
    SwipeRefreshLayout pollrefresh;
    RecyclerView pollRv;
    ProgressBar progressBar;
    List<LivePoll_option> optionLists;
    String eventid, colorActive;
    ImageView headerlogoIv;
    private APIService mAPIService;
    private ConnectionDetector cd;
    TextView empty, pullrefresh;
    LinearLayout linear;
    APIService updateApi;
    LivePollViewModel livePollViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_polllist);
        eventid = SharedPreference.getPref(this, EVENT_ID);
        colorActive = "#ffffff";


        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        livePollViewModel = ViewModelProviders.of(this).get(LivePollViewModel.class);

        cd = ConnectionDetector.getInstance(this);

        headerlogoIv = findViewById(R.id.headerlogoIv);


        pollRv = findViewById(R.id.pollRv);
        pollrefresh = findViewById(R.id.pollrefresh);
        progressBar = findViewById(R.id.progressBar);
        linear = findViewById(R.id.linear);
        pullrefresh = findViewById(R.id.pullrefresh);

        empty = findViewById(R.id.empty);

        TextView header = findViewById(R.id.title);
        header.setTextColor(Color.parseColor(colorActive));
        pullrefresh.setTextColor(Color.parseColor(colorActive));

       /* RelativeLayout layoutTop = findViewById(R.id.layoutTop);
        layoutTop.setBackgroundColor(Color.parseColor(colorActive));*/


        optionLists = new ArrayList<>();

        mAPIService = ApiUtils.getAPIService();


        final String token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        crashlytics("Live Poll",token);
        firbaseAnalytics(this, "Live Poll", token);


        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        CommonFunction.showBackgroundImage(LivePollActivity.this, linear);

        if (cd.isConnectingToInternet()) {
            livePollViewModel.getLivepoll(token, eventid);
            livePollViewModel.getSpeakerList().observe(this, new Observer<FetchLivePoll>() {
                @Override
                public void onChanged(FetchLivePoll event) {
                    //List<Speaker> eventLists = event.getSpeakerList();
                    String strCommentList =event.getDetail();
                    RefreashToken refreashToken = new RefreashToken(LivePollActivity.this);
                    String data = refreashToken.decryptedData(strCommentList);
                    Gson gson = new Gson();
                    List<Logo> eventLists = gson.fromJson(data, new TypeToken<ArrayList<Logo>>() {}.getType());

                    //Delete All Speaker from local db and insert Speaker
                    if(eventLists!=null) {

                        progressBar.setVisibility(View.GONE);
                        List<LivePoll> PollLists = eventLists.get(0).getLivePoll_list();

                        setupEventAdapter(PollLists);
                    }else{
                        progressBar.setVisibility(View.GONE);

                    }

                    if (livePollViewModel != null && livePollViewModel.getSpeakerList().hasObservers()) {
                        livePollViewModel.getSpeakerList().removeObservers(LivePollActivity.this);
                    }
                }
            });
        } else {

            Toast.makeText(LivePollActivity.this, "No internet connection",
                    Toast.LENGTH_SHORT).show();

        }



        pollrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (cd.isConnectingToInternet()) {
                    
                } else {
                    if (pollrefresh.isRefreshing()) {
                        pollrefresh.setRefreshing(false);
                    }
                    Toast.makeText(LivePollActivity.this, "No internet connection",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void setupEventAdapter(List<LivePoll> commentList) {
        LivePollAdapter pollAdapter = new LivePollAdapter(this, commentList, LivePollActivity.this);
        pollRv.setLayoutManager(new LinearLayoutManager(this));
        pollRv.setAdapter(pollAdapter);
        pollAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

    }


    @Override
    public void onContactSelected(LivePoll pollList) {

        if (pollList.getReplied().equalsIgnoreCase("1")) {
            if (pollList.getStatus().equalsIgnoreCase("1")) {
                Toast.makeText(LivePollActivity.this, "You are already submit quiz.", Toast.LENGTH_SHORT).show();
            } else {
               /* Intent polldetail = new Intent(getApplicationContext(), PollDetailActivity.class);
                polldetail.putExtra("id", pollList.getId());
                polldetail.putExtra("question", pollList.getQuestion());
                polldetail.putExtra("replied", pollList.getReplied());
                polldetail.putExtra("show_result", pollList.getStatus());
                polldetail.putExtra("optionlist", (Serializable) optionLists);
                startActivity(polldetail);
                finish();*/
            }

        } else {
            /*Intent polldetail = new Intent(getApplicationContext(), PollDetailActivity.class);
            polldetail.putExtra("id", pollList.getId());
            polldetail.putExtra("question", pollList.getQuestion());
            polldetail.putExtra("replied", pollList.getReplied());
            polldetail.putExtra("optionlist", (Serializable) optionLists);
            polldetail.putExtra("show_result", pollList.getStatus());
            startActivity(polldetail);
            finish();*/
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        JzvdStd.releaseAllVideos();

    }

   
}
