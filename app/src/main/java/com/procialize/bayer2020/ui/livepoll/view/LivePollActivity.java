package com.procialize.bayer2020.ui.livepoll.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.bayer2020.ConnectionDetector;
import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.MainActivity;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.Utility.KeyboardUtility;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.livepoll.adapter.LivePollAdapter;
import com.procialize.bayer2020.ui.livepoll.model.FetchLivePoll;
import com.procialize.bayer2020.ui.livepoll.model.LivePoll;
import com.procialize.bayer2020.ui.livepoll.model.LivePoll_option;
import com.procialize.bayer2020.ui.livepoll.model.Logo;
import com.procialize.bayer2020.ui.livepoll.viewmodel.LivePollViewModel;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.CommonFirebase.crashlytics;
import static com.procialize.bayer2020.Utility.CommonFirebase.firbaseAnalytics;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;

public class LivePollActivity extends AppCompatActivity implements LivePollAdapter.PollAdapterListner {
    SwipeRefreshLayout pollrefresh;
    RecyclerView pollRv;
    ProgressBar progressBar,progressView;
    List<LivePoll_option> optionLists;
    String eventid;
    ImageView headerlogoIv;
    private ConnectionDetector cd;
    TextView empty, pullrefresh,title;
    LinearLayout linear;
    LivePollViewModel livePollViewModel;
    ImageView iv_profile, iv_back;
     String token;
     View bgView;
    MutableLiveData<FetchLivePoll> FetchLivePollList = new MutableLiveData<>();
    private APIService eventApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_polllist);
        KeyboardUtility.hideSoftKeyboard(this);

        eventid = SharedPreference.getPref(this, EVENT_ID);

        /*Toolbar toolbar = findViewById(R.id.toolbar);
        headerlogoIv = findViewById(R.id.headerlogoIv);

        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);*/

       /* toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.getNavigationIcon().setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)), PorterDuff.Mode.SRC_ATOP);
*/
        livePollViewModel = ViewModelProviders.of(this).get(LivePollViewModel.class);

        cd = ConnectionDetector.getInstance(this);
        headerlogoIv = findViewById(R.id.headerlogoIv);

        String eventLogo = SharedPreference.getPref(LivePollActivity.this, EVENT_LOGO);
        String eventListMediaPath = SharedPreference.getPref(LivePollActivity.this, EVENT_LIST_MEDIA_PATH);
        Glide.with(LivePollActivity.this)
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


        pollRv = findViewById(R.id.pollRv);
        pollrefresh = findViewById(R.id.pollrefresh);
        progressBar = findViewById(R.id.progressBar);
        linear = findViewById(R.id.linear);
        pullrefresh = findViewById(R.id.pullrefresh);
        iv_profile = findViewById(R.id.iv_profile);
        progressView = findViewById(R.id.progressView);
        title = findViewById(R.id.title);
        bgView = findViewById(R.id.bgView);
        empty = findViewById(R.id.empty);
        iv_back = findViewById(R.id.iv_back);

        optionLists = new ArrayList<>();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

       // iv_back.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)), PorterDuff.Mode.SRC_ATOP);


        //CommonFunction.showBackgroundImage(LivePollActivity.this, linear);
      //  title.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)));
       // bgView.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this,EVENT_COLOR_2)));


         token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        crashlytics("Live Poll",token);
        firbaseAnalytics(this, "Live Poll", token);


        if (cd.isConnectingToInternet()) {
            if (livePollViewModel != null && livePollViewModel.getLivePollList().hasObservers()) {
                livePollViewModel.getLivePollList().removeObservers(LivePollActivity.this);
            }

            getLivepoll(token,eventid);
        } else {

            Utility.createShortSnackBar(linear, "No internet connection");


        }



        pollrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (cd.isConnectingToInternet()) {
                    if (livePollViewModel != null && livePollViewModel.getLivePollList().hasObservers()) {
                        livePollViewModel.getLivePollList().removeObservers(LivePollActivity.this);
                    }

                    getLivepoll(token,eventid);

                } else {
                    if (pollrefresh.isRefreshing()) {
                        pollrefresh.setRefreshing(false);
                    }
                    Utility.createShortSnackBar(linear, "No internet connection");


                }
            }
        });
    }

    public MutableLiveData<FetchLivePoll> getLivepoll(String token, String eventid) {
        if (pollrefresh.isRefreshing()) {
            pollrefresh.setRefreshing(false);
        }
        eventApi = ApiUtils.getAPIService();

        eventApi.livePollFetch(token,eventid
        )
                .enqueue(new Callback<FetchLivePoll>() {
                    @Override
                    public void onResponse(Call<FetchLivePoll> call, Response<FetchLivePoll> response) {
                        if (response.isSuccessful()) {
                            FetchLivePollList.setValue(response.body());
                            String strCommentList =response.body().getDetail();
                            RefreashToken refreashToken = new RefreashToken(LivePollActivity.this);
                            String data = refreashToken.decryptedData(strCommentList);
                            Gson gson = new Gson();
                            List<Logo> eventLists = gson.fromJson(data, new TypeToken<ArrayList<Logo>>() {}.getType());

                            //Fetch Livepoll list
                            if(eventLists!=null) {

                                progressBar.setVisibility(View.GONE);
                                empty.setVisibility(View.GONE);

                                List<LivePoll> PollLists = eventLists.get(0).getLivePoll_list();

                                if(PollLists.size()>0) {

/*
                                    Glide.with(LivePollActivity.this)
                                            .load(eventLists.get(0).getLogo_url_path() + eventLists.get(0).getLive_poll_logo().getApp_livepoll_logo())
                                            .listener(new RequestListener<Drawable>() {
                                                @Override
                                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                    progressView.setVisibility(View.GONE);
                                                    return false;
                                                }

                                                @Override
                                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                    progressView.setVisibility(View.GONE);

                                                    return false;
                                                }
                                            }).into(iv_profile);
*/


                                    setupEventAdapter(PollLists);
                                }else{
                                    progressBar.setVisibility(View.GONE);
                                    empty.setVisibility(View.VISIBLE);
                                    progressView.setVisibility(View.GONE);
                                    empty.setTextColor(Color.parseColor(SharedPreference.getPref(LivePollActivity.this, EVENT_COLOR_4)));

                                }


                            }else{

                                progressBar.setVisibility(View.GONE);
                                empty.setVisibility(View.VISIBLE);

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FetchLivePoll> call, Throwable t) {
                        FetchLivePollList.setValue(null);
                    }
                });

        return FetchLivePollList;
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
        if (cd.isConnectingToInternet()) {
            if (livePollViewModel != null && livePollViewModel.getLivePollList().hasObservers()) {
                livePollViewModel.getLivePollList().removeObservers(LivePollActivity.this);
            }

            getLivepoll(token,eventid);
        } else {
            if (pollrefresh.isRefreshing()) {
                pollrefresh.setRefreshing(false);
            }
            Utility.createShortSnackBar(linear, "No internet connection");


        }
    }


    @Override
    public void onContactSelected(LivePoll pollList) {
        optionLists = pollList.getLive_poll_option_list();
        if(pollList.getHide_result().equalsIgnoreCase("0")|| pollList.getReplied().equalsIgnoreCase("0")) {
            Intent polldetail = new Intent(getApplicationContext(), PollDetailActivity.class);
            polldetail.putExtra("id", pollList.getId());
            polldetail.putExtra("question", pollList.getQuestion());
            polldetail.putExtra("replied", pollList.getReplied());
            polldetail.putExtra("optionlist", (Serializable) optionLists);
            polldetail.putExtra("show_result", pollList.getHide_result());
            startActivity(polldetail);
        }else{
            Utility.createShortSnackBar(linear, "Answer hide from admin");

        }

    }

    @Override
    public void onPause() {
        super.onPause();

        JzvdStd.releaseAllVideos();

    }

   
}
