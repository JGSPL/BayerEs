package com.procialize.bayer2020.ui.notification.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.bayer2020.ConnectionDetector;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.GetterSetter.LoginOrganizer;
import com.procialize.bayer2020.MainActivity;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.livepoll.view.LivePollActivity;
import com.procialize.bayer2020.ui.newsFeedComment.view.CommentActivity;
import com.procialize.bayer2020.ui.newsFeedComment.viewModel.CommentViewModel;
import com.procialize.bayer2020.ui.newsfeed.PaginationUtils.PaginationAdapterCallback;
import com.procialize.bayer2020.ui.newsfeed.PaginationUtils.PaginationScrollListener;
import com.procialize.bayer2020.ui.newsfeed.model.FetchNewsfeedMultiple;
import com.procialize.bayer2020.ui.newsfeed.model.Newsfeed_detail;
import com.procialize.bayer2020.ui.notification.adapter.NotificationAdapter;
import com.procialize.bayer2020.ui.notification.model.Notification;
import com.procialize.bayer2020.ui.notification.model.NotificationList;
import com.procialize.bayer2020.ui.notification.viewmodel.NotificationsViewModel;
import com.procialize.bayer2020.ui.quiz.view.QuizListingActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.ATTENDEE_STATUS;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.IS_GOD;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.IS_LOGIN;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_ATTENDEE_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_CITY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_COMPANY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_DESIGNATION;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_EMAIL;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_FNAME;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_GCM_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_LNAME;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_MOBILE;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_PASSWORD;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_PROFILE_PIC;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.notification_count;
import static com.procialize.bayer2020.ui.newsfeed.adapter.PaginationListener.PAGE_START;

public class NotificationActivity extends AppCompatActivity implements NotificationAdapter.NotificationAdapterListner
        , View.OnClickListener, PaginationAdapterCallback {

    NotificationsViewModel notificationViewModel;
    String api_token, event_id;
    TextView tv_header;
    ConnectionDetector connectionDetector;
    private List<NotificationList> notificationList;
    RecyclerView recycler_notification;
    LinearLayoutManager linearLayoutManager;
    NotificationAdapter notificationAdapter;
    LinearLayout ll_main;
    ImageView iv_add;

    String scheduledDatenTime = "";
    int totalPages = 0;
    int pageNumber = 1;
    int pageSize = 1000;

    private int currentPage = PAGE_START;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    SwipeRefreshLayout notirefresh;
    private ArrayList<Newsfeed_detail> newsfeed_detail = new ArrayList<>();
    CommentViewModel commentViewModel;
    Dialog askQuestionDialog;
    ImageView iv_back;
    String ATTENDEE_STATUS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        HashMap<String, String> map = new HashMap<>();
        map.put(notification_count, "0");
        SharedPreference.putPref(this, map);

        ATTENDEE_STATUS = SharedPreference.getPref(this, IS_GOD);
        recycler_notification = findViewById(R.id.recycler_notification);
        ll_main = findViewById(R.id.ll_main);
        iv_back = findViewById(R.id.iv_back);
        tv_header = findViewById(R.id.tv_header);
        notirefresh = findViewById(R.id.notirefresh);
        iv_add = findViewById(R.id.iv_add);
        iv_add.setOnClickListener(this);
        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(this, EVENT_ID);
        connectionDetector = ConnectionDetector.getInstance(this);
        notificationViewModel = ViewModelProviders.of(this).get(NotificationsViewModel.class);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (ATTENDEE_STATUS.equalsIgnoreCase("1")) {
            iv_add.setVisibility(View.VISIBLE);
        } else {
            iv_add.setVisibility(View.GONE);
        }

        //--------------------------------------------------------------------------------------
        /*GetUserActivityReport getUserActivityReport = new GetUserActivityReport(this,api_token,
                event_id,
                Constant.pageVisited,
                "36",
                "0");
        getUserActivityReport.userActivityReport();*/
        //--------------------------------------------------------------------------------------


        commentViewModel = ViewModelProviders.of(this).get(CommentViewModel.class);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(NotificationActivity.this);
        recycler_notification.setLayoutManager(mLayoutManager);
        notificationAdapter = new NotificationAdapter(this, NotificationActivity.this);
        linearLayoutManager = new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.VERTICAL, false);
        recycler_notification.setLayoutManager(linearLayoutManager);
        recycler_notification.setAdapter(notificationAdapter);
        notificationAdapter.notifyDataSetChanged();
        setDynamicColor();
        CommonFunction.showBackgroundImage(this, ll_main);
        getNotificationFromApi();

        notirefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                notirefresh.setRefreshing(false);
                if (ConnectionDetector.getInstance(NotificationActivity.this).isConnectingToInternet()) {
                    currentPage = PAGE_START;
                    getNotificationFromApi();
                } else {
                    try {
                        Utility.createShortSnackBar(ll_main, "No Internet Connection");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        recycler_notification.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {


                if (connectionDetector.isConnectingToInternet()) {
                    isLoading = true;
                    currentPage += 1;

                    loadNextPage();
                } else {
                    notirefresh.setRefreshing(false);
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


    }

    private void getNotificationFromApi() {

        currentPage = PAGE_START;
        ApiUtils.getAPIService().FetchNotification(api_token, event_id, pageSize + "",
                currentPage + "")
                .enqueue(new Callback<Notification>() {
                    @Override
                    public void onResponse(Call<Notification> call, Response<Notification> response) {
                        if (response.isSuccessful()) {
                            String strCommentList = response.body().getDetail();
                            RefreashToken refreashToken = new RefreashToken(NotificationActivity.this);
                            String data = refreashToken.decryptedData(strCommentList);
                            Gson gson = new Gson();
                            notificationList = gson.fromJson(data, new TypeToken<ArrayList<NotificationList>>() {
                            }.getType());

                            try {
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

                                notificationAdapter.getNotifications().clear();
                                notificationAdapter.notifyDataSetChanged();
                                notificationAdapter.addAll(notificationList);
                                // likeList = like.getLikeDetails();
                                if (notificationList != null) {
                                    setupLikeAdapter();
                                }
                                try {
                                    if (currentPage <= totalPages)
                                        notificationAdapter.addLoadingFooter();
                                    else
                                        isLastPage = true;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<Notification> call, Throwable t) {
                        Utility.createShortSnackBar(ll_main, "Please try after some time");
                    }
                });
    }

    private void loadNextPage() {
        Log.d("loadNextPage", "loadNextPage: " + currentPage);
        ApiUtils.getAPIService().FetchNotification(api_token, event_id, pageSize + "",
                currentPage + "")
                .enqueue(new Callback<Notification>() {
                    @Override
                    public void onResponse(Call<Notification> call, Response<Notification> response) {
                        if (response.isSuccessful()) {

                            notificationAdapter.removeLoadingFooter();
                            isLoading = false;
                            String strCommentList = response.body().getDetail();
                            RefreashToken refreashToken = new RefreashToken(NotificationActivity.this);
                            String data = refreashToken.decryptedData(strCommentList);
                            Gson gson = new Gson();
                            notificationList = gson.fromJson(data, new TypeToken<ArrayList<NotificationList>>() {
                            }.getType());

                            notificationAdapter.addAll(notificationList);

                            if (currentPage != totalPages)
                                notificationAdapter.addLoadingFooter();
                            else
                                isLastPage = true;
                        }
                    }

                    @Override
                    public void onFailure(Call<Notification> call, Throwable t) {
                        Utility.createShortSnackBar(ll_main, "Please try after some time");
                    }
                });
    }

    public void setupLikeAdapter() {

        if (notificationAdapter == null) {
            recycler_notification.setAdapter(notificationAdapter);
            recycler_notification.setItemAnimator(new DefaultItemAnimator());
            recycler_notification.setNestedScrollingEnabled(true);
        } else {
            notificationAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onMoreSelected(NotificationList comment, int position) {
        if (comment.getType().equalsIgnoreCase("Quiz")) {
            startActivity(new Intent(this, QuizListingActivity.class));
        } else if (comment.getType().equalsIgnoreCase("AdminPost") ||
                comment.getType().equalsIgnoreCase("UserPost")) {
            startActivity(new Intent(this, MainActivity.class).putExtra("from","postNewsFeed"));
        } else if (comment.getType().equalsIgnoreCase("Comment")) {
            getNewsfeedDetails(comment.getParent_id());
        } else if (comment.getType().equalsIgnoreCase("Like")) {
            getNewsfeedDetails(comment.getParent_id());
        } else if (comment.getType().equalsIgnoreCase("LivePoll")) {
            startActivity(new Intent(this, LivePollActivity.class));
        }else if (comment.getType().equalsIgnoreCase("Reply") ) {
            startActivity(new Intent(this, MainActivity.class).putExtra("from","postNewsFeed"));
        }
    }

    private void getNewsfeedDetails(final String newsfeedId) {
        if (ConnectionDetector.getInstance(this).isConnectingToInternet()) {
            ApiUtils.getAPIService().NewsFeedDetailFetch(api_token, event_id, newsfeedId).enqueue(
                    new Callback<FetchNewsfeedMultiple>() {
                        @Override
                        public void onResponse(Call<FetchNewsfeedMultiple> call,
                                               Response<FetchNewsfeedMultiple> response) {
                            if (response.isSuccessful()) {
                                String strEventList = response.body().getDetail();
                                RefreashToken refreashToken = new RefreashToken(NotificationActivity.this);
                                String data = refreashToken.decryptedData(strEventList);
                                Gson gson = new Gson();
                                newsfeed_detail = gson.fromJson(data, new TypeToken<ArrayList<Newsfeed_detail>>() {
                                }.getType());

                                startActivity(new Intent(NotificationActivity.this, CommentActivity.class)
                                        .putExtra("Newsfeed_detail", (Serializable) newsfeed_detail.get(0))
                                        .putExtra("newsfeedId", newsfeedId)
                                        .putExtra("positionOfList", 0)
                                        .putExtra("position", "" + 0));
                            }
                        }

                        @Override
                        public void onFailure(Call<FetchNewsfeedMultiple> call, Throwable t) {

                        }
                    });
        } else {
            Utility.createShortSnackBar(ll_main, "No Internet Connection");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add:
                openAskQuestionDialog();
                break;
        }
    }

    public void openAskQuestionDialog() {
        askQuestionDialog = new Dialog(NotificationActivity.this, R.style.MyAlertDialogTheme);
        askQuestionDialog.setContentView(R.layout.dialog_send_notification);
        askQuestionDialog.setCancelable(false);
        askQuestionDialog.show();

        ImageView iv_close = askQuestionDialog.findViewById(R.id.iv_close);
        ImageView iv_calender = askQuestionDialog.findViewById(R.id.iv_calender);
        final EditText et_question = askQuestionDialog.findViewById(R.id.et_question);
        final TextView tv_scehduled = askQuestionDialog.findViewById(R.id.tv_scehduled);
        final TextView tv_count = askQuestionDialog.findViewById(R.id.tv_count);
        final TextView tv_send_question = askQuestionDialog.findViewById(R.id.tv_send_question);
        final TextView tv_scehduled_datentime = askQuestionDialog.findViewById(R.id.tv_scehduled_datentime);
        final TextView tv_total_count = askQuestionDialog.findViewById(R.id.tv_total_count);
        final TextView tv_header_ask = askQuestionDialog.findViewById(R.id.tv_header_ask);
        final LinearLayout ll_send_question = askQuestionDialog.findViewById(R.id.ll_send_question);
        final LinearLayout ll_schedule_datentime = askQuestionDialog.findViewById(R.id.ll_schedule_datentime);
        final LinearLayout ll_main = askQuestionDialog.findViewById(R.id.ll_main);

        ll_main.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)));

        ll_send_question.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)));
        tv_send_question.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)));
        //ll_speaker_dropdown.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_1)));
        iv_close.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_1)), PorterDuff.Mode.SRC_ATOP);
        iv_calender.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_1)), PorterDuff.Mode.SRC_ATOP);

        tv_scehduled.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)));
        tv_scehduled_datentime.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)));
        tv_count.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)));
        et_question.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)));
        et_question.setHintTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)));
        tv_header_ask.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_1)));
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
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        int mSecond = c.get(Calendar.SECOND);
        tv_scehduled_datentime.setText(mDay + "-" + (mMonth + 1) + "-" + mYear + " " + mHour + ":" + mMinute + ":" + mSecond);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askQuestionDialog.dismiss();
            }
        });
        ll_schedule_datentime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(NotificationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                scheduledDatenTime = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                                final Calendar c = Calendar.getInstance();
                                int mHour = c.get(Calendar.HOUR_OF_DAY);
                                int mMinute = c.get(Calendar.MINUTE);
                                final int mSecond = c.get(Calendar.SECOND);

                                // Launch Time Picker Dialog
                                TimePickerDialog timePickerDialog = new TimePickerDialog(NotificationActivity.this,
                                        new TimePickerDialog.OnTimeSetListener() {

                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                                  int minute) {

                                                scheduledDatenTime = scheduledDatenTime + " " + hourOfDay + ":" + minute + ":" + mSecond;
                                                tv_scehduled_datentime.setText(scheduledDatenTime);
                                            }
                                        }, mHour, mMinute, false);
                                timePickerDialog.show();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        ll_send_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectionDetector.getInstance(NotificationActivity.this).isConnectingToInternet()) {
                    ll_send_question.setEnabled(true);
                    final String question = et_question.getText().toString().trim();

                    if (!question.isEmpty()) {
                        api_token = SharedPreference.getPref(NotificationActivity.this, AUTHERISATION_KEY);
                        String eventid = SharedPreference.getPref(NotificationActivity.this, EVENT_ID);
                        String scheduledTime = tv_scehduled_datentime.getText().toString();
                        notificationViewModel.submitNotificationData(api_token, eventid, scheduledTime, question);
                        notificationViewModel.submitNotification().observe(NotificationActivity.this, new Observer<LoginOrganizer>() {
                            @Override
                            public void onChanged(final LoginOrganizer loginOrganizer) {
                                if (loginOrganizer.getHeader().get(0).getType().equalsIgnoreCase("success")) {

                                    getNotificationFromApi();
                                    Utility.createShortSnackBar(ll_main, loginOrganizer.getHeader().get(0).getMsg());
                                    et_question.setText("");
                                    askQuestionDialog.dismiss();
                                } else {
                                    getNotificationFromApi();
                                    ll_send_question.setEnabled(true);
                                    Utility.createShortSnackBar(ll_main, loginOrganizer.getHeader().get(0).getMsg());
                                    askQuestionDialog.dismiss();
                                }
                                if (notificationViewModel != null && notificationViewModel.submitNotification().hasObservers()) {
                                    notificationViewModel.submitNotification().removeObservers(NotificationActivity.this);
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
    public void retryPageLoad() {
        loadNextPage();
    }

    private void setDynamicColor() {
       // tv_header.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)));
       // iv_back.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)), PorterDuff.Mode.SRC_ATOP);
       // iv_add.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //startActivity(new Intent(NotificationActivity.this,MainActivity.class));
        finish();
    }

}