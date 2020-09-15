package com.procialize.eventapp.ui.newsFeedLike.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.ui.newsFeedComment.adapter.CommentAdapter;
import com.procialize.eventapp.ui.newsFeedComment.model.Comment;
import com.procialize.eventapp.ui.newsFeedComment.model.CommentDetail;
import com.procialize.eventapp.ui.newsFeedComment.view.CommentActivity;
import com.procialize.eventapp.ui.newsFeedComment.viewModel.CommentViewModel;
import com.procialize.eventapp.ui.newsFeedLike.adapter.LikeAdapter;
import com.procialize.eventapp.ui.newsFeedLike.model.Like;
import com.procialize.eventapp.ui.newsFeedLike.model.LikeDetail;
import com.procialize.eventapp.ui.newsFeedLike.viewModel.LikeViewModel;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;

import java.util.List;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);

        api_token = SharedPreference.getPref(this,AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(this,EVENT_ID);
        likeViewModel = ViewModelProviders.of(this).get(LikeViewModel.class);
        connectionDetector = ConnectionDetector.getInstance(this);
        ll_main = findViewById(R.id.ll_main);
        rv_like = findViewById(R.id.rv_like);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        Intent intent = getIntent();
        try {
            newsfeed_detail = (Newsfeed_detail) getIntent().getSerializableExtra("Newsfeed_detail");
            position = intent.getStringExtra("position");
        } catch (Exception e) {
            e.printStackTrace();
        }

        CommonFunction.showBackgroundImage(this,ll_main);

        geLikes();
    }

    public void geLikes() {
        if (connectionDetector.isConnectingToInternet()) {
            likeViewModel.getLike(api_token,event_id, newsfeed_detail.getNews_feed_id(), "20", "1");
            likeViewModel.getLikeList().observe(this, new Observer<Like>() {
                @Override
                public void onChanged(Like like) {
                    if (like != null) {
                        likeList = like.getLikeDetails();
                        setupLikeAdapter(likeList);
                        //showLikeCount(likeList);
                    }
                }
            });
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
}