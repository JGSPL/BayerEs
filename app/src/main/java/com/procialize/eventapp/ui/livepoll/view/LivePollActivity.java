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
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.procialize.eventapp.ui.livepoll.adapter.LivePollAdapter;
import com.procialize.eventapp.ui.livepoll.model.FetchLivePoll;
import com.procialize.eventapp.ui.livepoll.model.LivePoll;
import com.procialize.eventapp.ui.livepoll.model.LivePoll_option;


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
    ListView pollRv;
    ProgressBar progressBar;
    List<LivePoll_option> optionLists;
    String eventid, colorActive;
    ImageView headerlogoIv;
    private APIService mAPIService;
    private ConnectionDetector cd;
    TextView empty, pullrefresh;
    LinearLayout linear;
    APIService updateApi;
    MutableLiveData<FetchLivePoll> livepollDetail = new MutableLiveData<>();
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
            getLivepoll(token, eventid);
        } else {

            Toast.makeText(LivePollActivity.this, "No internet connection",
                    Toast.LENGTH_SHORT).show();

        }


        pollrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (cd.isConnectingToInternet()) {
                    getLivepoll(token, eventid);
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

    //Update Api
    public MutableLiveData<FetchLivePoll> getLivepoll(final String token, final String event_id) {
        updateApi = ApiUtils.getAPIService();

        updateApi.LivePollFetch(token, event_id).enqueue(new Callback<FetchLivePoll>() {
            @Override
            public void onResponse(Call<FetchLivePoll> call,
                                   Response<FetchLivePoll> response) {
                if (response.isSuccessful()) {
                    livepollDetail.setValue(response.body());
                    String strCommentList =response.body().getDetail();
                    RefreashToken refreashToken = new RefreashToken(LivePollActivity.this);
                    String data = refreashToken.decryptedData(strCommentList);
                    Gson gson = new Gson();
                    List<LivePoll> eventLists = gson.fromJson(data, new TypeToken<ArrayList<LivePoll>>() {}.getType());
                    if(eventLists!=null) {
                        optionLists = eventLists.get(0).getLive_poll_option_list();
                    }

                   // if(optionLists!=null) {

                       // String pdfurl = eventLists.get(0).getSpeaker_document_path();
                        empty.setVisibility(View.GONE);

                        empty.setVisibility(View.GONE);
                        LivePollAdapter pollAdapter = new LivePollAdapter(LivePollActivity.this, eventLists, LivePollActivity.this);
                        pollAdapter.notifyDataSetChanged();
                        pollRv.setAdapter(pollAdapter);
                   /* }else{
                        empty.setVisibility(View.VISIBLE);
                    }*/

                }
            }

            @Override
            public void onFailure(Call<FetchLivePoll> call, Throwable t) {
                livepollDetail.setValue(null);
            }
        });
        return livepollDetail;
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
