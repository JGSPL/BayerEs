package com.procialize.eventapp.ui.newsFeedLike.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFirebase;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.ui.newsFeedComment.model.Comment;
import com.procialize.eventapp.ui.newsFeedComment.model.CommentDetail;
import com.procialize.eventapp.ui.newsFeedComment.view.CommentActivity;
import com.procialize.eventapp.ui.newsFeedLike.adapter.LikeAdapter;
import com.procialize.eventapp.ui.newsFeedLike.model.Like;
import com.procialize.eventapp.ui.newsFeedLike.model.LikeDetail;
import com.procialize.eventapp.ui.newsFeedLike.viewModel.LikeViewModel;
import com.procialize.eventapp.ui.newsfeed.PaginationUtils.PaginationScrollListener;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.eventapp.ui.newsfeed.adapter.PaginationListener.PAGE_START;
import static com.procialize.eventapp.ui.newsfeed.view.NewsFeedFragment.newsfeedAdapter;

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

    LikeAdapter likeAdapter;
    LinearLayoutManager linearLayoutManager;

    int pageSize = 30;
    int totalPages = 0;
    private int currentPage = PAGE_START;
    private boolean isLoading = false;
    private boolean isLastPage = false;

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

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(LikeActivity.this);
        rv_like.setLayoutManager(mLayoutManager);
        likeAdapter = new LikeAdapter(LikeActivity.this);
        linearLayoutManager = new LinearLayoutManager(LikeActivity.this, LinearLayoutManager.VERTICAL, false);
        rv_like.setLayoutManager(linearLayoutManager);
        rv_like.setAdapter(likeAdapter);
        likeAdapter.notifyDataSetChanged();

        rv_like.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                if(totalPages>1) {
                    isLoading = true;
                    currentPage += 1;
                    Log.e("currentPage", currentPage + "");
                    loadNextPage();
                }
            }

            @Override
            public int getTotalPageCount() {
                return totalPages;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        geLikes();
    }

    public void geLikes() {
        if (connectionDetector.isConnectingToInternet()) {
            ApiUtils.getAPIService().getLikes(api_token, event_id, newsfeed_detail.getNews_feed_id(), pageSize + "",
                    currentPage + "")
                    .enqueue(new Callback<Like>() {
                        @Override
                        public void onResponse(Call<Like> call, Response<Like> response) {
                            if (response.isSuccessful()) {
                                String strCommentList = response.body().getDetail();
                                RefreashToken refreashToken = new RefreashToken(LikeActivity.this);
                                String data = refreashToken.decryptedData(strCommentList);
                                Gson gson = new Gson();
                                likeList = gson.fromJson(data, new TypeToken<ArrayList<LikeDetail>>() {
                                }.getType());

                                String totRecords = response.body().getTotal_records();
                                Long longTotalRecords = Long.parseLong(totRecords);
                                if (longTotalRecords < pageSize) {
                                    totalPages = 1;
                                } else {
                                    if ((int) (longTotalRecords % pageSize) == 0) {
                                        totalPages = (int) (longTotalRecords / pageSize);
                                    } else {
                                        totalPages = (int) (longTotalRecords / pageSize) + 1;
                                    }
                                }
                                Log.e("totalPages_like", "" + totalPages);

                                likeAdapter.getLikeDetails().clear();
                                likeAdapter.notifyDataSetChanged();
                                likeAdapter.addAll(likeList);
                                // likeList = like.getLikeDetails();
                                if (likeList != null) {
                                    setupLikeAdapter(likeList);
                                    //showLikeCount(likeList);
                                }

                                if (currentPage <= totalPages)
                                    likeAdapter.addLoadingFooter();
                                else isLastPage = true;
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
       /* LikeAdapter commentAdapter = new LikeAdapter(LikeActivity.this, commentList);
        rv_like.setLayoutManager(new LinearLayoutManager(this));
        rv_like.setAdapter(commentAdapter);
        commentAdapter.notifyDataSetChanged();*/

        if (likeAdapter == null) {
           /* newsfeedAdapter = new NewsFeedAdapter(getContext(), newsfeedArrayList, NewsFeedFragment.this);
            recycler_feed.setLayoutManager(new LinearLayoutManager(getContext()));*/
            rv_like.setAdapter(likeAdapter);
            rv_like.setItemAnimator(new DefaultItemAnimator());
            rv_like.setNestedScrollingEnabled(true);
        } else {
            likeAdapter.notifyDataSetChanged();
        }
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

    public void loadNextPage() {
        Log.e("pageNumber", "loadNextPage: " + currentPage);
        if (connectionDetector.isConnectingToInternet()) {
            String newsFeedId = newsfeed_detail.getNews_feed_id();
            ApiUtils.getAPIService().getLikes(api_token, event_id, newsfeed_detail.getNews_feed_id(), pageSize + "",
                    currentPage + "")
                    .enqueue(new Callback<Like>() {
                        @Override
                        public void onResponse(Call<Like> call, Response<Like> response) {
                            if (response.isSuccessful()) {
                                likeAdapter.removeLoadingFooter();
                                isLoading = false;
                                String strCommentList = response.body().getDetail();
                                if (strCommentList != null) {
                                    RefreashToken refreashToken = new RefreashToken(LikeActivity.this);
                                    String data = refreashToken.decryptedData(strCommentList);
                                    Gson gson = new Gson();
                                    likeList = gson.fromJson(data, new TypeToken<ArrayList<LikeDetail>>() {
                                    }.getType());

                                    if (likeList != null) {
                                        likeAdapter.addAll(likeList);
                                        setupLikeAdapter(likeList);

                                        if (currentPage != totalPages)
                                            likeAdapter.addLoadingFooter();
                                        else
                                            isLastPage = true;
                                    } /*else {
                                    likeList = new ArrayList<>();
                                    //setupCommentAdapter(commentList);
                                    likeAdapter.addAll(likeList);
                                    setupLikeAdapter(likeList);




                                }*/

                                    if (currentPage < totalPages)
                                        likeAdapter.addLoadingFooter();
                                    else {
                                        isLastPage = true;
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Like> call, Throwable t) {
                            //commentList.setValue(null);
                        }
                    });
        } else {
            Utility.createShortSnackBar(ll_main, "No Internet Connection");
        }
    }


}