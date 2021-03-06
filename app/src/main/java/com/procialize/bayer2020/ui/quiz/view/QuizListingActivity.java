package com.procialize.bayer2020.ui.quiz.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.procialize.bayer2020.ConnectionDetector;
import com.procialize.bayer2020.Constants.Constant;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.MainActivity;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.CommonFirebase;
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.session.SessionManager;
import com.procialize.bayer2020.ui.livepoll.view.LivePollActivity;
import com.procialize.bayer2020.ui.notification.view.NotificationActivity;
import com.procialize.bayer2020.ui.quiz.adapter.QuizListAdapter;
import com.procialize.bayer2020.ui.quiz.model.QuizList;
import com.procialize.bayer2020.ui.quiz.model.QuizListing;
import com.procialize.bayer2020.ui.quiz.viewmodel.QuizListingViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.procialize.bayer2020.Utility.CommonFunction.setNotification;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.QUIZLOGO_MEDIA_PATH;

public class QuizListingActivity extends AppCompatActivity implements QuizListAdapter.QuizListAdapterListner {

    QuizListingViewModel quizlistingviewmodel;
    ConnectionDetector cd;
    public static LinearLayout ll_main;
    ImageView iv_back;
    RecyclerView quizrecycler;
    SessionManager session;
    String api_token = "", event_id;
    SwipeRefreshLayout quizRefreash;
    TextView tv_header;
    QuizListAdapter quizAdapter;
    RelativeLayout layoutBottom;
    public static List<QuizList> gsonevent = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_listing);

        new RefreashToken(QuizListingActivity.this).callGetRefreashToken(QuizListingActivity.this);


        quizrecycler = findViewById(R.id.quizrecycler);
        quizRefreash = findViewById(R.id.quizrefresher);
        ll_main = findViewById(R.id.ll_main);
        ImageView iv_back = findViewById(R.id.iv_back);
        //iv_back.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)), PorterDuff.Mode.SRC_ATOP);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_header = findViewById(R.id.tv_header);
      //  layoutBottom = findViewById(R.id.layoutBottom);
        //CommonFunction.showBackgroundImage(QuizListingActivity.this, ll_main);
       // tv_header.setTextColor(Color.parseColor(SharedPreference.getPref(QuizListingActivity.this, EVENT_COLOR_3)));
        session = new SessionManager(getApplicationContext());
        //layoutBottom.setBackgroundColor(Color.parseColor(SharedPreference.getPref(QuizListingActivity.this, EVENT_COLOR_2)));
        api_token = SharedPreference.getPref(QuizListingActivity.this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(QuizListingActivity.this, EVENT_ID);

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
/*

        //--------------------------------------------------------------------------------------
        GetUserActivityReport getUserActivityReport = new GetUserActivityReport(this,api_token,
                event_id,
                Constant.pageVisited,
                "22",
                "0");
        getUserActivityReport.userActivityReport();
        //--------------------------------------------------------------------------------------

*/

        //-----------------------------For Notification count-----------------------------
        try {
            LinearLayout ll_notification_count = findViewById(R.id.ll_notification_count);
            TextView tv_notification = findViewById(R.id.tv_notification);
            setNotification(this, tv_notification, ll_notification_count);
            RelativeLayout rl_notification = findViewById(R.id.rl_notification);
            rl_notification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(QuizListingActivity.this, NotificationActivity.class));
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        //----------------------------------------------------------------------------------


        /* // final ProgressBar progressBar = findViewById(R.id.progressBar);
        final ImageView profileIV = findViewById(R.id.profileIV);
       // final String logoUrl = SharedPreference.getPref(QuizListingActivity.this, QUIZLOGO_MEDIA_PATH);
       if (logoUrl != null) {
            progressBar.setVisibility(View.GONE);

            Glide.with(getApplicationContext()).load(logoUrl)
                    .apply(RequestOptions.skipMemoryCacheOf(false))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            profileIV.setImageResource(R.drawable.quizlogo);

                            progressBar.setVisibility(View.GONE);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(profileIV);

        } else {
            progressBar.setVisibility(View.GONE);

        }*/

        //-----------------------------For Notification count-----------------------------
        try {
            LinearLayout ll_notification_count = findViewById(R.id.ll_notification_count);
            TextView tv_notification = findViewById(R.id.tv_notification);
            setNotification(this, tv_notification, ll_notification_count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //----------------------------------------------------------------------------------

        CommonFirebase.crashlytics("QuizListing", api_token);
        CommonFirebase.firbaseAnalytics(this, "QuizListing", api_token);
        cd = ConnectionDetector.getInstance(this);
        quizlistingviewmodel = ViewModelProviders.of(this).get(QuizListingViewModel.class);

        if (cd.isConnectingToInternet()) {
            quizlistingviewmodel.getQuizList(api_token, event_id);

            quizlistingviewmodel.getQuizList().observe(this, new Observer<QuizListing>() {
                @Override
                public void onChanged(QuizListing event) {
                    gsonevent.clear();
                    RefreashToken refreashToken = new RefreashToken(QuizListingActivity.this);
                    String decrypteventdetail = refreashToken.decryptedData(event.getDetail());

                    JsonParser jp = new JsonParser();
                    JsonElement je = jp.parse(decrypteventdetail);
                    JsonElement je2 = je.getAsJsonObject().get("logo_url_path");
                    JsonElement je3 = je.getAsJsonObject().get("quiz_logo");
                    JsonElement je4 = je.getAsJsonObject().get("quiz_list");
                    JsonElement je5 = je3.getAsJsonObject().get("app_quiz_logo");

                    String path = CommonFunction.stripquotes(String.valueOf(je2));
                    String logo = CommonFunction.stripquotes(String.valueOf(je5));
                    String strFilePath = path + logo;
                    Gson gson = new Gson();
                    gsonevent = gson.fromJson(je4, new TypeToken<ArrayList<QuizList>>() {
                    }.getType());
                    HashMap<String, String> map = new HashMap<>();
                    map.put(QUIZLOGO_MEDIA_PATH, strFilePath.replace("\\/", "/"));
                    SharedPreference.putPref(QuizListingActivity.this, map);
                    setupQuizAdapter(gsonevent);
                }
            });
        } else {
            Utility.createShortSnackBar(ll_main, "No Internet Connection..!");
        }

        quizRefreash.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                quizRefreash.setRefreshing(false);
                if (cd.isConnectingToInternet()) {
                    quizlistingviewmodel.getQuizList(api_token, event_id);

                    quizlistingviewmodel.getQuizList().observe(QuizListingActivity.this, new Observer<QuizListing>() {
                        @Override
                        public void onChanged(QuizListing event) {
                            gsonevent.clear();
                            RefreashToken refreashToken = new RefreashToken(QuizListingActivity.this);
                            String decrypteventdetail = refreashToken.decryptedData(event.getDetail());
//                    String strFilePath = CommonFunction.stripquotes(refreashToken.decryptedData(event.getDetailpreencrypt().getLogo_url_path()));
//                    String strFilePath = CommonFunction.stripquotes(event.getDetailpreencrypt().getLogo_url_path());
                            JsonParser jp = new JsonParser();
                            JsonElement je = jp.parse(decrypteventdetail);
                            JsonElement je2 = je.getAsJsonObject().get("logo_url_path");
                            JsonElement je3 = je.getAsJsonObject().get("quiz_logo");
                            JsonElement je4 = je.getAsJsonObject().get("quiz_list");
                            JsonElement je5 = je3.getAsJsonObject().get("app_quiz_logo");
                            String path = CommonFunction.stripquotes(String.valueOf(je2));
                            String logo = CommonFunction.stripquotes(String.valueOf(je5));
                            String strFilePath = path + logo;
                            Gson gson = new Gson();
                            gsonevent = gson.fromJson(je4, new TypeToken<ArrayList<QuizList>>() {
                            }.getType());
                            HashMap<String, String> map = new HashMap<>();
                            map.put(QUIZLOGO_MEDIA_PATH, strFilePath.replace("\\/", "/"));
                            SharedPreference.putPref(QuizListingActivity.this, map);
                            setupQuizAdapter(gsonevent);
                        }
                    });
                } else {
                    Utility.createShortSnackBar(ll_main, "No Internet Connection..!");
                }
            }
        });
    }

    public void setupQuizAdapter(List<QuizList> commentList) {
        quizAdapter = new QuizListAdapter(QuizListingActivity.this, commentList, QuizListingActivity.this);
        quizrecycler.setLayoutManager(new LinearLayoutManager(this));
        quizrecycler.setAdapter(quizAdapter);
        quizAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMoreSelected(QuizList quiz, final int position) {
        if (quiz.getQuiz_question().get(0).getReplied().equalsIgnoreCase("0")) {
            QuizDetailActivity.count1 = 1;
            QuizDetailActivity.submitflag = false;
            Intent intent = new Intent(QuizListingActivity.this, QuizDetailActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("timer", quiz.getTimer());
            intent.putExtra("folder_id", quiz.getFolder_id());
            intent.putExtra("folder_name", quiz.getFolder_name());
            startActivity(intent);
        } else {
            Intent intent = new Intent(QuizListingActivity.this, QuizSubmittedActivity.class);
            intent.putExtra("timer", quiz.getTimer());
            intent.putExtra("folder_id", quiz.getFolder_id());
            intent.putExtra("folder_name", quiz.getFolder_name());
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cd.isConnectingToInternet()) {
            quizlistingviewmodel.getQuizList(api_token, event_id);
            quizlistingviewmodel.getQuizList().observe(this, new Observer<QuizListing>() {
                @Override
                public void onChanged(QuizListing event) {
                    gsonevent.clear();
                    RefreashToken refreashToken = new RefreashToken(QuizListingActivity.this);
                    String decrypteventdetail = refreashToken.decryptedData(event.getDetail());
//                    String strFilePath = CommonFunction.stripquotes(refreashToken.decryptedData(event.getDetailpreencrypt().getLogo_url_path()));
//                    String strFilePath = CommonFunction.stripquotes(event.getDetailpreencrypt().getLogo_url_path());
                    JsonParser jp = new JsonParser();
                    JsonElement je = jp.parse(decrypteventdetail);
                    JsonElement je2 = je.getAsJsonObject().get("logo_url_path");
                    JsonElement je3 = je.getAsJsonObject().get("quiz_logo");
                    JsonElement je4 = je.getAsJsonObject().get("quiz_list");
                    JsonElement je5 = je3.getAsJsonObject().get("app_quiz_logo");
                    String path = CommonFunction.stripquotes(String.valueOf(je2));
                    String logo = CommonFunction.stripquotes(String.valueOf(je5));
                    String strFilePath = path + logo;
                    Gson gson = new Gson();
                    gsonevent = gson.fromJson(je4, new TypeToken<ArrayList<QuizList>>() {
                    }.getType());
                    HashMap<String, String> map = new HashMap<>();
                    map.put(QUIZLOGO_MEDIA_PATH, strFilePath.replace("\\/", "/"));
                    SharedPreference.putPref(QuizListingActivity.this, map);
                    setupQuizAdapter(gsonevent);
                }
            });
        } else {
            Utility.createShortSnackBar(ll_main, "No Internet Connection..!");
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(QuizListingActivity.this, MainActivity.class));
        finish();
    }
}