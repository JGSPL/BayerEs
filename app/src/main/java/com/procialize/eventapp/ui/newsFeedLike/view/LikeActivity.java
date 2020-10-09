package com.procialize.eventapp.ui.newsFeedLike.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.MainActivity;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFirebase;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.ui.newsFeedComment.model.CommentDetail;
import com.procialize.eventapp.ui.newsFeedComment.view.CommentActivity;
import com.procialize.eventapp.ui.newsFeedLike.adapter.LikeAdapter;
import com.procialize.eventapp.ui.newsFeedLike.model.Like;
import com.procialize.eventapp.ui.newsFeedLike.model.LikeDetail;
import com.procialize.eventapp.ui.newsFeedLike.viewModel.LikeViewModel;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;

public class LikeActivity extends AppCompatActivity implements View.OnClickListener {

    ConnectionDetector connectionDetector;
    private String position;
    private Newsfeed_detail newsfeed_detail;
    LikeViewModel likeViewModel;
    String event_id;
    private List<LikeDetail> likeList;
    RecyclerView rv_like;
    LinearLayout ll_main;
    ImageView iv_back;
    String api_token = "";
    TextView tv_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);

        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(this, EVENT_ID);
        likeViewModel = ViewModelProviders.of(this).get(LikeViewModel.class);
        connectionDetector = ConnectionDetector.getInstance(this);
        ll_main = findViewById(R.id.ll_main);
        rv_like = findViewById(R.id.rv_like);
        iv_back = findViewById(R.id.iv_back);
        tv_header = findViewById(R.id.tv_header);
        iv_back.setOnClickListener(this);
        Intent intent = getIntent();
        try {
            newsfeed_detail = (Newsfeed_detail) getIntent().getSerializableExtra("Newsfeed_detail");
            position = intent.getStringExtra("position");
        } catch (Exception e) {
            e.printStackTrace();
        }
        CommonFirebase.crashlytics("Like", api_token);
        CommonFirebase.firbaseAnalytics(this, "Like", api_token);
        setDynamicColor();
        CommonFunction.showBackgroundImage(this, ll_main);
        geLikes();
    }

    public void geLikes() {
        if (connectionDetector.isConnectingToInternet()) {
            ApiUtils.getAPIService().getLikes(api_token, event_id, newsfeed_detail.getNews_feed_id(), "500", "1")
                    .enqueue(new Callback<Like>() {
                        @Override
                        public void onResponse(Call<Like> call, Response<Like> response) {
                            if (response.isSuccessful()) {
                                String strCommentList =response.body().getDetail();
                                RefreashToken refreashToken = new RefreashToken(LikeActivity.this);
                                String data = refreashToken.decryptedData(strCommentList);
                                Gson gson = new Gson();
                                likeList = gson.fromJson(data, new TypeToken<ArrayList<LikeDetail>>() {}.getType());
                                // likeList = like.getLikeDetails();
                                if (likeList != null) {
                                    setupLikeAdapter(likeList);
                                    //showLikeCount(likeList);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Like> call, Throwable t) {
                            Utility.createShortSnackBar(ll_main, "Please try after some time");
                        }
                    });


            /*likeViewModel.getLike(api_token, event_id, newsfeed_detail.getNews_feed_id(), "500", "1");
            likeViewModel.getLikeList().observe(this, new Observer<Like>() {
                @Override
                public void onChanged(Like like) {
                    if (like != null) {
                        String strCommentList =like.getDetail();
                        RefreashToken refreashToken = new RefreashToken(LikeActivity.this);
                        String data = refreashToken.decryptedData(strCommentList);
                        Gson gson = new Gson();
                        likeList = gson.fromJson(data, new TypeToken<ArrayList<LikeDetail>>() {}.getType());
                       // likeList = like.getLikeDetails();
                        if (likeList != null) {
                            setupLikeAdapter(likeList);
                            //showLikeCount(likeList);
                        }
                    }

                    if (likeViewModel != null && likeViewModel.getLikeList().hasObservers()) {
                        likeViewModel.getLikeList().removeObservers(LikeActivity.this);
                    }
                }
            });*/
        } else {
            Utility.createShortSnackBar(ll_main, "No Internet Connection");
        }
    }


    public void setupLikeAdapter(List<LikeDetail> commentList) {
        LikeAdapter commentAdapter = new LikeAdapter(LikeActivity.this, commentList);
        rv_like.setLayoutManager(new LinearLayoutManager(this));
        rv_like.setAdapter(commentAdapter);
        commentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:

                onBackPressed();
                break;
        }
    }

    public void setDynamicColor() {

        int color4 = Color.parseColor(SharedPreference.getPref(LikeActivity.this, EVENT_COLOR_4));
        iv_back.setColorFilter(color4, PorterDuff.Mode.SRC_ATOP);
        tv_header.setTextColor(Color.parseColor(SharedPreference.getPref(LikeActivity.this, EVENT_COLOR_4)));
    }

}