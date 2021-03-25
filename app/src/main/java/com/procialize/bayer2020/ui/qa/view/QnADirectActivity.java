package com.procialize.bayer2020.ui.qa.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.procialize.bayer2020.Constants.Constant;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.Database.EventAppDB;
import com.procialize.bayer2020.GetterSetter.LoginOrganizer;
import com.procialize.bayer2020.MainActivity;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.CommonFunction;
//import com.procialize.bayer2020.Utility.GetUserActivityReport;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.faq.view.FAQActivity;
import com.procialize.bayer2020.ui.livepoll.view.LivePollActivity;
import com.procialize.bayer2020.ui.loyalityleap.view.RequestToRedeemActivity;
import com.procialize.bayer2020.ui.newsFeedComment.model.LikePost;
import com.procialize.bayer2020.ui.newsFeedDetails.view.NewsFeedDetailsActivity;
import com.procialize.bayer2020.ui.newsFeedPost.roomDB.UploadMultimedia;
import com.procialize.bayer2020.ui.newsfeed.PaginationUtils.PaginationScrollListener;
import com.procialize.bayer2020.ui.newsfeed.model.Newsfeed_detail;
import com.procialize.bayer2020.ui.newsfeed.view.NewsFeedFragment;
import com.procialize.bayer2020.ui.newsfeed.viewmodel.NewsFeedViewModel;
import com.procialize.bayer2020.ui.notification.view.NotificationActivity;
import com.procialize.bayer2020.ui.qa.adapter.QnAAdapter;
import com.procialize.bayer2020.ui.qa.model.QnA;
import com.procialize.bayer2020.ui.qa.viewmodel.QADirectViewModel;
import com.procialize.bayer2020.ui.qapost.view.PostNewActivity;
import com.procialize.bayer2020.ui.spotQnA.model.FetchSpotQnA;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.CommonFunction.setNotification;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;
import static com.procialize.bayer2020.ui.newsfeed.adapter.PaginationListener.PAGE_START;

public class QnADirectActivity extends AppCompatActivity implements View.OnClickListener, QnAAdapter.FeedAdapterListner {

    QADirectViewModel qADirectViewModel;
    TextView tv_ask_question, tv_header,tv_asked_question;
    QnAAdapter qnAAdapter;
    RecyclerView rv_qna;
    LinearLayoutManager linearLayoutManager;
    String api_token, eventid;
    LinearLayout ll_main, ll_ask_question;
    SwipeRefreshLayout question_refresh;
    ImageView iv_back;
    private APIService newsfeedApi;
    IntentFilter mFilter;
    int pageNumber = 1;
    int pageSize = 1000;
    int totalPages;
    private int currentPage = PAGE_START;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    NewsFeedViewModel newsfeedViewModel;

    String noOfLikes = "0";
    String likeStatus = "";
    Dialog askQuestionDialog;

    public boolean isUploadingStarted = false;
    ConnectionDetector connectionDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_n_a_direct);

        qADirectViewModel = ViewModelProviders.of(this).get(QADirectViewModel.class);
        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(this, EVENT_ID);

        iv_back = findViewById(R.id.iv_back);
        tv_header = findViewById(R.id.tv_header);
        ll_ask_question = findViewById(R.id.ll_ask_question);
        ll_main = findViewById(R.id.ll_main);
        tv_ask_question = findViewById(R.id.tv_ask_question);
        tv_asked_question = findViewById(R.id.tv_asked_question);
        rv_qna = findViewById(R.id.rv_qna);
        question_refresh = findViewById(R.id.question_refresh);
        tv_ask_question.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        setUpToolbar() ;
        //--------------------------------------------------------------------------------------
       /* GetUserActivityReport getUserActivityReport = new GetUserActivityReport(this,api_token,
                eventid,
                Constant.pageVisited,
                "19",
                "0");
        getUserActivityReport.userActivityReport();*/
        //--------------------------------------------------------------------------------------


       // CommonFunction.showBackgroundImage(QnADirectActivity.this, ll_main);
        newsfeedViewModel = ViewModelProviders.of(this).get(NewsFeedViewModel.class);

        //-----------------------------For Notification count-----------------------------
        try {
            LinearLayout ll_notification_count = findViewById(R.id.ll_notification_count);
            TextView tv_notification = findViewById(R.id.tv_notification);
            setNotification(this, tv_notification, ll_notification_count);

            RelativeLayout rl_notification = findViewById(R.id.rl_notification);
            rl_notification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(QnADirectActivity.this, NotificationActivity.class));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        //----------------------------------------------------------------------------------

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rv_qna.setLayoutManager(mLayoutManager);
        qnAAdapter = new QnAAdapter(this, QnADirectActivity.this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_qna.setLayoutManager(linearLayoutManager);
        rv_qna.setItemAnimator(new DefaultItemAnimator());
        rv_qna.setAdapter(qnAAdapter);
        qnAAdapter.notifyDataSetChanged();
        getDataFromApi();

        question_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                question_refresh.setRefreshing(false);
                if (ConnectionDetector.getInstance(QnADirectActivity.this).isConnectingToInternet()) {

                    currentPage = PAGE_START;
                    getDataFromApi();
                } else {
                    try {
                        Utility.createShortSnackBar(ll_main, "No Internet Connection");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //-----------------------------For Notification count-----------------------------
        try {
            LinearLayout ll_notification_count = findViewById(R.id.ll_notification_count);
            TextView tv_notification = findViewById(R.id.tv_notification);
            setNotification(this, tv_notification, ll_notification_count);

            RelativeLayout rl_notification = findViewById(R.id.rl_notification);
            rl_notification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(QnADirectActivity.this, NotificationActivity.class));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        //----------------------------------------------------------------------------------


        /*rv_qna.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                    currentPage += 1;
                    Log.e("currentPage", currentPage + "");
                    loadNextPage();
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
        });*/
      //  setDynamicColor();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ask_question:
                //openAskQuestionDialog();
                startActivity(new Intent(QnADirectActivity.this, PostNewActivity.class));
                finish();
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    public void getDataFromApi() {
        if (ConnectionDetector.getInstance(this).isConnectingToInternet()) {
            currentPage = PAGE_START;

            ApiUtils.getAPIService().directQnAFetch(api_token, eventid,
                    pageSize + "",
                    currentPage + "")
                    .enqueue(new Callback<FetchSpotQnA>() {
                        @Override
                        public void onResponse(Call<FetchSpotQnA> call, Response<FetchSpotQnA> response) {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    if (response.body().getHeader().get(0).getType().equalsIgnoreCase("success")) {
                                        String strCommentList = response.body().getDetail();
                                        RefreashToken refreashToken = new RefreashToken(QnADirectActivity.this);
                                        String data = refreashToken.decryptedData(strCommentList);
                                        try {
                                            qnAAdapter.removeLoadingFooter();
                                            isLoading = false;
                                            Gson gson = new Gson();
                                            List<Newsfeed_detail> agendaLists = gson.fromJson(data, new TypeToken<ArrayList<Newsfeed_detail>>() {
                                            }.getType());
                                            if (agendaLists != null) {
                                                if (agendaLists.size() > 0) {
                                                    String totRecords = response.body().getTotalRecords();
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
                                                    Log.e("totalPages", "" + totalPages);

                                                    qnAAdapter.getNewsFeedList().clear();
                                                    qnAAdapter.notifyDataSetChanged();
                                                    qnAAdapter.addAll(agendaLists);
                                                    setupAgendaAdapter();
                                                    try {
                                                        if (currentPage < totalPages)
                                                            qnAAdapter.addLoadingFooter();
                                                        else
                                                            isLastPage = true;
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }else{
                                        Utility.createShortSnackBar(ll_main, response.body().getHeader().get(0).getMsg());
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<FetchSpotQnA> call, Throwable t) {
                            //fetchSpotQnAList.setValue(null);
                        }
                    });
           /* qADirectViewModel.getDirectQnA(api_token, eventid,
                    pageSize + "",
                    currentPage + "");
            qADirectViewModel.getDirectQnAList().observe(this, new Observer<FetchSpotQnA>() {
                @Override
                public void onChanged(FetchSpotQnA fetchSpotQnA) {
                    if (fetchSpotQnA != null) {
                        if (fetchSpotQnA.getHeader().get(0).getType().equalsIgnoreCase("success")) {
                            String strCommentList = fetchSpotQnA.getDetail();
                            RefreashToken refreashToken = new RefreashToken(QnADirectActivity.this);
                            String data = refreashToken.decryptedData(strCommentList);
                            try {
                                qnAAdapter.removeLoadingFooter();
                                isLoading = false;
                                Gson gson = new Gson();
                                List<QnA> agendaLists = gson.fromJson(data, new TypeToken<ArrayList<QnA>>() {
                                }.getType());
                                if (agendaLists != null) {
                                    if (agendaLists.size() > 0) {
                                        String totRecords = fetchSpotQnA.getTotalRecords();
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
                                        Log.e("totalPages", "" + totalPages);

                                        qnAAdapter.getQnAList().clear();
                                        qnAAdapter.notifyDataSetChanged();
                                        qnAAdapter.addAll(agendaLists);
                                        setupAgendaAdapter();
                                        try {
                                            if (currentPage < totalPages)
                                                qnAAdapter.addLoadingFooter();
                                            else
                                                isLastPage = true;
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else{
                            Utility.createShortSnackBar(ll_main, fetchSpotQnA.getHeader().get(0).getMsg());
                        }
                    }
                    else {
                        Utility.createShortSnackBar(ll_main, "Failure..!");
                    }

                    if (qADirectViewModel != null && qADirectViewModel.getDirectQnAList().hasObservers()) {
                        qADirectViewModel.getDirectQnAList().removeObservers(QnADirectActivity.this);
                    }
                }
        });*/
        /*ApiUtils.getAPIService().directQnAFetch(api_token, eventid,
                pageSize + "",
                currentPage + "")
                .enqueue(new Callback<FetchSpotQnA>() {
                    @Override
                    public void onResponse(Call<FetchSpotQnA> call, Response<FetchSpotQnA> response) {
                        if (response.isSuccessful()) {
                            String strCommentList = response.body().getDetail();
                            RefreashToken refreashToken = new RefreashToken(QnADirectActivity.this);
                            String data = refreashToken.decryptedData(strCommentList);
                            try {
                                qnAAdapter.removeLoadingFooter();
                                isLoading = false;
                                Gson gson = new Gson();
                                List<QnA> agendaLists = gson.fromJson(data, new TypeToken<ArrayList<QnA>>() {
                                }.getType());
                                if (agendaLists != null) {
                                    if (agendaLists.size() > 0) {
                                        String totRecords = response.body().getTotalRecords();
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
                                        Log.e("totalPages", "" + totalPages);

                                        qnAAdapter.getQnAList().clear();
                                        qnAAdapter.notifyDataSetChanged();
                                        qnAAdapter.addAll(agendaLists);
                                        setupAgendaAdapter();
                                        try {
                                            if (currentPage < totalPages)
                                                qnAAdapter.addLoadingFooter();
                                            else
                                                isLastPage = true;
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FetchSpotQnA> call, Throwable t) {
                        Utility.createShortSnackBar(ll_main, "Failure..!");
                    }
                });*/
    }

}

    public void loadNextPage() {
        Log.e("pageNumber", "loadNextPage: " + currentPage);
        if (ConnectionDetector.getInstance(this).isConnectingToInternet()) {
            ApiUtils.getAPIService().directQnAFetch(api_token, eventid,
                    pageSize + "",
                    currentPage + "")
                    .enqueue(new Callback<FetchSpotQnA>() {
                        @Override
                        public void onResponse(Call<FetchSpotQnA> call, Response<FetchSpotQnA> response) {
                            if (response.isSuccessful()) {
                                qnAAdapter.removeLoadingFooter();
                                isLoading = false;
                                String strCommentList = response.body().getDetail();
                                RefreashToken refreashToken = new RefreashToken(QnADirectActivity.this);
                                String data = refreashToken.decryptedData(strCommentList);
                                try {
                                    Gson gson = new Gson();
                                    List<Newsfeed_detail> agendaLists = gson.fromJson(data, new TypeToken<ArrayList<Newsfeed_detail>>() {
                                    }.getType());
                                    if (agendaLists != null) {
                                        if (agendaLists.size() > 0) {
                                            qnAAdapter.addAll(agendaLists);

                                            setupAgendaAdapter();

                                            if (currentPage != totalPages)
                                                qnAAdapter.addLoadingFooter();
                                            else
                                                isLastPage = true;
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<FetchSpotQnA> call, Throwable t) {
                            //Utility.createShortSnackBar(ll_main, "Failure..!");
                        }
                    });
        }
    }

    public void setupAgendaAdapter() {

        if (qnAAdapter == null) {
            rv_qna.setAdapter(qnAAdapter);
            rv_qna.setItemAnimator(new DefaultItemAnimator());
        } else {
            qnAAdapter.notifyDataSetChanged();
        }
    }

/*
    @Override
    public void onLikeClicked(QnA spotQnA, final int position, final TextView liketext, final ImageView likeImage) {
        ApiUtils.getAPIService().PostQnALikeDirect(api_token, eventid, spotQnA.getId()).enqueue(new Callback<LikePost>() {
            @Override
            public void onResponse(Call<LikePost> call,
                                   Response<LikePost> response) {
                if (response.isSuccessful()) {
                    likeStatus = response.body().getLike_status();
                    noOfLikes = liketext.getText().toString().split(" ")[0];
                    if (likeStatus.equalsIgnoreCase("1")) {
                        int LikeCount = Integer.parseInt(noOfLikes) + 1;
                        if (LikeCount == 1) {
                            liketext.setText(LikeCount + " Like");
                        } else {
                            liketext.setText(LikeCount + " Likes");
                        }

                        likeImage.setImageDrawable(getDrawable(R.drawable.ic_active_like));
                        List<QnA> spotQnA = qnAAdapter.getQnAList();
                        spotQnA.get(position).setLike_flag("1");
                        spotQnA.get(position).setTotal_likes(LikeCount + "");

                    } else {
                        if (Integer.parseInt(noOfLikes) > 0) {
                            int LikeCount = Integer.parseInt(noOfLikes) - 1;
                            if (LikeCount == 1) {
                                liketext.setText(LikeCount + " Like");
                            } else {
                                liketext.setText(LikeCount + " Likes");
                            }
                            likeImage.setImageDrawable(getDrawable(R.drawable.ic_like));
                            noOfLikes = "0";

                            List<QnA> spotQnA = qnAAdapter.getQnAList();
                            spotQnA.get(position).setLike_flag("0");
                            spotQnA.get(position).setTotal_likes(LikeCount + "");
                        }
                    }
                    Utility.createShortSnackBar(ll_main, response.body().getHeader().get(0).getMsg());
                }
            }

            @Override
            public void onFailure(Call<LikePost> call, Throwable t) {
                Utility.createShortSnackBar(ll_main, "Failure..");
            }
        });
    }
*/


   /* private void setDynamicColor() {
        tv_header.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)));
        tv_asked_question.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)));
        iv_back.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)), PorterDuff.Mode.SRC_ATOP);
        ll_ask_question.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)));
        tv_ask_question.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_1)));
        tv_ask_question.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)));
    }*/

    public void openAskQuestionDialog() {
        //askQuestionDialog = new Dialog(QnADirectActivity.this);
        askQuestionDialog = new Dialog(QnADirectActivity.this, R.style.MyAlertDialogTheme);
        askQuestionDialog.setContentView(R.layout.dialog_ask_question);

        askQuestionDialog.setCancelable(false);
        askQuestionDialog.show();

        ImageView iv_close = askQuestionDialog.findViewById(R.id.iv_close);
        final EditText et_question = askQuestionDialog.findViewById(R.id.et_question);
        final TextView tv_count = askQuestionDialog.findViewById(R.id.tv_count);
        final TextView tv_send_question = askQuestionDialog.findViewById(R.id.tv_send_question);
        final TextView tv_total_count = askQuestionDialog.findViewById(R.id.tv_total_count);
        final TextView tv_header_ask = askQuestionDialog.findViewById(R.id.tv_header_ask);
        final LinearLayout ll_send_question = askQuestionDialog.findViewById(R.id.ll_send_question);
        final LinearLayout ll_main = askQuestionDialog.findViewById(R.id.ll_main);
       /* iv_close.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)), PorterDuff.Mode.SRC_ATOP);
        ll_send_question.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)));
        tv_send_question.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_1)));
        tv_header_ask.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)));*/

        ll_main.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)));
        //ll_post_status.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)));
        tv_header_ask.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_1)));
        ll_send_question.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)));
        tv_send_question.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)));
        //ll_speaker_dropdown.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_1)));
        iv_close.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_1)), PorterDuff.Mode.SRC_ATOP);

        tv_count.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)));
        et_question.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)));
        et_question.setHintTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)));
       // tv_header.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_1)));

        et_question.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                count = 500 - s.length();
                tv_count.setText(count + "/500");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askQuestionDialog.dismiss();
            }
        });

        ll_send_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectionDetector.getInstance(QnADirectActivity.this).isConnectingToInternet()) {
                    ll_send_question.setEnabled(true);
                    final String question = et_question.getText().toString().trim();

                    if (!question.isEmpty()) {
                        api_token = SharedPreference.getPref(QnADirectActivity.this, AUTHERISATION_KEY);
                        eventid = SharedPreference.getPref(QnADirectActivity.this, EVENT_ID);

                        qADirectViewModel.submitDirectQnA(api_token, eventid, question);
                        qADirectViewModel.submitDirectQnAList().observe(QnADirectActivity.this, new Observer<LoginOrganizer>() {
                            @Override
                            public void onChanged(final LoginOrganizer loginOrganizer) {
                                if (loginOrganizer.getHeader().get(0).getType().equalsIgnoreCase("success")) {

                                    getDataFromApi();
                                    Utility.createShortSnackBar(ll_main, loginOrganizer.getHeader().get(0).getMsg());
                                    et_question.setText("");
                                    askQuestionDialog.dismiss();
                                } else {
                                    getDataFromApi();
                                    ll_send_question.setEnabled(true);
                                    Utility.createShortSnackBar(ll_main, loginOrganizer.getHeader().get(0).getMsg());
                                    askQuestionDialog.dismiss();
                                }
                                if (qADirectViewModel != null && qADirectViewModel.submitDirectQnAList().hasObservers()) {
                                    qADirectViewModel.submitDirectQnAList().removeObservers(QnADirectActivity.this);
                                }
                            }
                        });
                    } else {
                        ll_send_question.setEnabled(true);
                        Utility.createShortSnackBar(ll_main, "Please enter some question..");
                    }

                } else {
                    Utility.createShortSnackBar(ll_main, "No Internet Connection..!");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onContactSelected(Newsfeed_detail feed, int position) {
        newsfeedViewModel.openNewsFeedDetails(this, feed, position);
    }

    @Override
    public void onCommentClick(Newsfeed_detail feed, int position, int swipeablePosition) {
        newsfeedViewModel.openCommentPage(this, feed, position, swipeablePosition);
    }

    @Override
    public void onLikeClick(Newsfeed_detail feed, int position) {
        newsfeedViewModel.openLikePage(this, feed, position);
    }

    @Override
    public void onShareClick(Newsfeed_detail feed, int position, int swipeablePosition) {
        newsfeedViewModel.openShareTask(this, feed, swipeablePosition);
    }

    @Override
    public void onSliderClick(Newsfeed_detail feed, int position) {
       // newsfeedViewModel.openFeedDetails(this, feed, position);
        startActivity(new Intent(QnADirectActivity.this, NewsFeedDetailsActivity.class)
                .putExtra("Newsfeed_detail", (Serializable) feed)
                .putExtra("page", "QnA")
                .putExtra("position", "" + position));
    }

    @Override
    public void moreTvFollowOnClick(View v, Newsfeed_detail feed, int position) {
        //newsfeedViewModel.openMoreDetails(this, feed, position, api_token, eventid, QnAAdapter);
    }

    @Override
    public void likeTvViewOnClick(View v, Newsfeed_detail feed, final int position, final ImageView likeimage, final TextView liketext) {

        //newsfeedViewModel.openLikeimg(this), api_token, eventid, feed.getNews_feed_id(), v, feed, position, likeimage, liketext);
        noOfLikes = "0";
        // newsfeedViewModel.PostLike(api_token, eventid, feed.getNews_feed_id());
        newsfeedApi = ApiUtils.getAPIService();
        newsfeedApi.PostLike(api_token, eventid, feed.getNews_feed_id()).enqueue(new Callback<LikePost>() {
            @Override
            public void onResponse(Call<LikePost> call,
                                   Response<LikePost> response) {
                if (response.isSuccessful()) {
                    likeStatus = response.body().getLike_status();
                    noOfLikes = liketext.getText().toString().split(" ")[0];
                    if (likeStatus.equalsIgnoreCase("1")) {
                        //showLikeCount(Integer.parseInt(noOfLikes) + 1);
                        int LikeCount = Integer.parseInt(noOfLikes) + 1;
                        if (LikeCount == 1) {
                            liketext.setText(LikeCount + " Like");
                        } else {
                            liketext.setText(LikeCount + " Likes");
                        }
                        List<Newsfeed_detail> newsfeed_details = qnAAdapter.getNewsFeedList();
                        newsfeed_details.get(position).setLike_flag("1");
                        newsfeed_details.get(position).setTotal_likes(LikeCount + "");

                        likeimage.setImageDrawable(QnADirectActivity.this.getDrawable(R.drawable.ic_active_like));
                        noOfLikes = "0";
                        likeStatus = "";
                    } else {
                        if (Integer.parseInt(noOfLikes) > 0) {
                            // showLikeCount(Integer.parseInt(noOfLikes) - 1);
                            int LikeCount = Integer.parseInt(noOfLikes) - 1;
                            if (LikeCount == 1) {
                                liketext.setText(LikeCount + " Like");
                            } else {
                                liketext.setText(LikeCount + " Likes");
                            }
                            likeimage.setImageDrawable(QnADirectActivity.this.getDrawable(R.drawable.ic_like));
                            noOfLikes = "0";
                            List<Newsfeed_detail> newsfeed_details = qnAAdapter.getNewsFeedList();
                            newsfeed_details.get(position).setLike_flag("0");
                            newsfeed_details.get(position).setTotal_likes(LikeCount + "");
                        }
                        noOfLikes = "0";
                        likeStatus = "";
                    }
                    Utility.createShortSnackBar(ll_main, response.body().getHeader().get(0).getMsg());
                }
            }

            @Override
            public void onFailure(Call<LikePost> call, Throwable t) {
                // liketPostUpdate.setValue(null);
                Utility.createShortSnackBar(ll_main, "Failure..");

            }
        });

    }

    private void setUpToolbar() {
        /*Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
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
            });*/
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
        //}
    }


}