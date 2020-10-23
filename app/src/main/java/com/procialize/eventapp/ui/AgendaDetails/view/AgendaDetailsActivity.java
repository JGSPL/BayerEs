package com.procialize.eventapp.ui.AgendaDetails.view;


import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.MainActivity;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.ui.AgendaDetails.viewmodel.AgendaViewModel;
import com.procialize.eventapp.ui.agenda.model.Agenda;
import com.procialize.eventapp.ui.newsFeedComment.view.CommentActivity;
import com.procialize.eventapp.ui.newsFeedPost.view.PostNewActivity;
import com.procialize.eventapp.ui.newsfeed.viewmodel.NewsFeedViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;

public class AgendaDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_date, tv_start_time, tv_start_time_am, tv_end_time, tv_end_time_am, tv_session_name,
            tv_short_session_description, tv_session_description, tv_horizontal, tv_rate, tv_header,
            tv_rate_this_session, tv_set_reminder;
    public static LinearLayout ll_live_stream, ll_rate, ll_main, ll_start_time, ll_end_time;
    SwitchCompat switch_reminder;
    RatingBar ratingbar;
    AgendaViewModel agendaViewModel;
    String statusSwitch1, api_token, event_id;
    String sessionId, sessionName, sessionShortDescription, sessionDescription, sessionStartTime, sessionEndTime, sessionDate,
            eventId, livestreamLink, star, totalFeedback, feedbackComment, rated, reminder_flag, reminder_id;
    float ratingValue = 0.0f;
    ImageView iv_back;
    ImageView iv_reminder;
    ScrollView scrollView;
    View view_short_desc;
    Agenda agendaDetails;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_agenda_details);

        agendaViewModel = ViewModelProviders.of(this).get(AgendaViewModel.class);

        tv_date = findViewById(R.id.tv_date);
        tv_start_time = findViewById(R.id.tv_start_time);
        tv_start_time_am = findViewById(R.id.tv_start_time_am);
        tv_end_time = findViewById(R.id.tv_end_time);
        tv_header = findViewById(R.id.tv_header);
        tv_set_reminder = findViewById(R.id.tv_set_reminder);
        tv_end_time_am = findViewById(R.id.tv_end_time_am);
        ll_live_stream = findViewById(R.id.ll_live_stream);
        ll_rate = findViewById(R.id.ll_rate);
        ll_main = findViewById(R.id.ll_main);
        iv_back = findViewById(R.id.iv_back);
        iv_reminder = findViewById(R.id.iv_reminder);
        switch_reminder = findViewById(R.id.switch_reminder);
        tv_session_name = findViewById(R.id.tv_session_name);
        tv_rate_this_session = findViewById(R.id.tv_rate_this_session);
        tv_short_session_description = findViewById(R.id.tv_short_session_description);
        ratingbar = findViewById(R.id.ratingbar);
        tv_session_description = findViewById(R.id.tv_session_description);
        tv_date = findViewById(R.id.tv_date);
        tv_rate = findViewById(R.id.tv_rate);
        tv_horizontal = findViewById(R.id.tv_horizontal);
        ll_start_time = findViewById(R.id.ll_start_time);
        ll_end_time = findViewById(R.id.ll_end_time);
        scrollView = findViewById(R.id.scrollView);
        view_short_desc = findViewById(R.id.view_short_desc);

        ll_live_stream.setOnClickListener(this);
        ll_rate.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(this, EVENT_ID);

        Intent intent = getIntent();
        agendaDetails = (Agenda) intent.getSerializableExtra("agendaDetails");
        sessionId = agendaDetails.getSession_id();
        sessionName = agendaDetails.getSession_name();
        sessionShortDescription = agendaDetails.getSession_short_description();
        sessionDescription = agendaDetails.getSession_description();
        sessionStartTime = agendaDetails.getSession_start_time();
        sessionEndTime = agendaDetails.getSession_end_time();
        sessionDate = agendaDetails.getSession_date();
        eventId = agendaDetails.getEvent_id();
        livestreamLink = agendaDetails.getLivestream_link();
        star = agendaDetails.getStar();
        totalFeedback = agendaDetails.getTotal_feedback();
        feedbackComment = agendaDetails.getFeedback_comment();
        rated = agendaDetails.getRated();
        reminder_flag = agendaDetails.getReminder_flag();
        reminder_id = agendaDetails.getReminder_id();

        if (sessionShortDescription == null || sessionShortDescription.isEmpty()) {
            tv_short_session_description.setVisibility(View.GONE);
            view_short_desc.setVisibility(View.GONE);
        } else {
            tv_short_session_description.setVisibility(View.VISIBLE);
            view_short_desc.setVisibility(View.VISIBLE);
        }


        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
            SimpleDateFormat targetFormat = new SimpleDateFormat("hh:mm aa");
            Date startTime = originalFormat.parse(sessionStartTime);
            Date endTime = originalFormat.parse(sessionEndTime);
            String strStartTime = targetFormat.format(startTime);
            String strEndTime = targetFormat.format(endTime);
            tv_start_time.setText(strStartTime.split(" ")[0].toUpperCase());
            tv_start_time_am.setText(strStartTime.split(" ")[1].toUpperCase());
            tv_end_time.setText(strEndTime.split(" ")[0].toUpperCase());
            tv_end_time_am.setText(strEndTime.split(" ")[1].toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(livestreamLink.isEmpty())
        {ll_live_stream.setVisibility(View.GONE);}
        else
        {ll_live_stream.setVisibility(View.VISIBLE);}

        /*try {
            Date currentTime = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strCurrnetDate = dateFormat.format(currentTime);
            if (CommonFunction.isTimeBetweenTwoTime(agendaDetails.getSession_start_time(), agendaDetails.getSession_end_time(), strCurrnetDate)) {
                ll_live_stream.setVisibility(View.VISIBLE);
            } else {
                ll_live_stream.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        setDynamicColor();
/*
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date sourceDate = null;
        try {
            sourceDate = dateFormat.parse(sessionDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd, YYYY");
        //SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy HH:mm aa");
        String targetdatevalue = targetFormat.format(sourceDate);*/
        String targetdatevalue = CommonFunction.convertAgendaDate(sessionDate);

        tv_date.setText(targetdatevalue);
        tv_session_name.setText(sessionName);
        tv_short_session_description.setText(sessionShortDescription);
        tv_session_description.setText(sessionDescription);

        if (reminder_flag != null) {
            if (reminder_flag.equalsIgnoreCase("1")) {
                switch_reminder.setChecked(true);
            } else {
                switch_reminder.setChecked(false);
            }
        }

        switch_reminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switch_reminder.isChecked()) {
                    statusSwitch1 = switch_reminder.getTextOn().toString();
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            // Android M Permission check
                            if (checkSelfPermission("Manifest.permission.WRITE_CALENDAR") != PackageManager.PERMISSION_GRANTED && checkSelfPermission("Manifest.permission.READ_CALENDAR") != PackageManager.PERMISSION_GRANTED) {
                                final String[] permissions = new String[]{"android.permission.WRITE_CALENDAR", "android.permission.READ_CALENDAR"};
                                ActivityCompat.requestPermissions(AgendaDetailsActivity.this,
                                        permissions, 0);
                            } else {
                                if (ConnectionDetector.getInstance(AgendaDetailsActivity.this).isConnectingToInternet()) {
                                    agendaViewModel.saveToCalenderEx(AgendaDetailsActivity.this, sessionStartTime, sessionEndTime, sessionName,
                                            sessionDescription, sessionId, agendaDetails);
                                } else {
                                    Utility.createShortSnackBar(ll_main, "No Internet Connection.!");
                                }
                            }
                        } else {
                            if (ConnectionDetector.getInstance(AgendaDetailsActivity.this).isConnectingToInternet()) {
                                agendaViewModel.saveToCalender(AgendaDetailsActivity.this, sessionStartTime, sessionEndTime, sessionName,
                                        sessionDescription, sessionId, agendaDetails);
                            } else {
                                Utility.createShortSnackBar(ll_main, "No Internet Connection.!");
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    if (ConnectionDetector.getInstance(AgendaDetailsActivity.this).isConnectingToInternet()) {
                        statusSwitch1 = switch_reminder.getTextOff().toString();
                        reminder_id = agendaDetails.getReminder_id();
                        try {
                            if (!reminder_id.equalsIgnoreCase("0")) {
                                Uri deleteUri = null;
                                deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, Long.parseLong(reminder_id));
                                int rows = getContentResolver().delete(deleteUri, null, null);
                                Toast.makeText(AgendaDetailsActivity.this, "Event deleted", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        agendaViewModel.reminderAgenda(api_token, event_id, sessionId, "0", "0");
                        agendaViewModel.reminderAgenda().observe(AgendaDetailsActivity.this, new Observer<LoginOrganizer>() {
                            @Override
                            public void onChanged(LoginOrganizer loginOrganizer) {
                                if (loginOrganizer != null) {
                                    if (loginOrganizer.getHeader().get(0).getType().equalsIgnoreCase("success")) {
                                        Utility.createShortSnackBar(ll_main, loginOrganizer.getHeader().get(0).getMsg());
                                    } else {
                                        Utility.createShortSnackBar(ll_main, loginOrganizer.getHeader().get(0).getMsg());
                                    }
                                }
                                if (agendaViewModel != null && agendaViewModel.reminderAgenda().hasObservers()) {
                                    agendaViewModel.reminderAgenda().removeObservers(AgendaDetailsActivity.this);
                                }
                            }
                        });
                    } else {
                        Utility.createShortSnackBar(ll_main, "No Internet Connection.!");
                    }
                }
            }
        });

        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingValue = rating;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ConnectionDetector.getInstance(AgendaDetailsActivity.this).isConnectingToInternet()) {
                    agendaViewModel.saveToCalenderEx(AgendaDetailsActivity.this, sessionStartTime, sessionEndTime, sessionName,
                            sessionDescription, sessionId, agendaDetails);
                } else {
                    Utility.createShortSnackBar(ll_main, "No Internet Connection.!");
                }
            } else {
                Toast.makeText(this, "Permission denied to write your Calender",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_live_stream:
                agendaViewModel.openAgendaDetails(this, agendaDetails.getLivestream_link(),agendaDetails);
                break;
            case R.id.ll_rate:
                if (ConnectionDetector.getInstance(AgendaDetailsActivity.this).isConnectingToInternet()) {
                    agendaViewModel.rateTheAgenda(api_token, event_id,
                            sessionId,
                            ratingValue);
                    agendaViewModel.rateAgenda().observe(this, new Observer<LoginOrganizer>() {
                        @Override
                        public void onChanged(LoginOrganizer loginOrganizer) {
                            if (loginOrganizer != null) {
                                if (loginOrganizer.getHeader().get(0).getType().equalsIgnoreCase("success")) {
                                    Utility.createShortSnackBar(ll_main, loginOrganizer.getHeader().get(0).getMsg());
                                    ratingbar.setRating(0);
                                } else {
                                    Utility.createShortSnackBar(ll_main, loginOrganizer.getHeader().get(0).getMsg());
                                }
                            }

                            if (agendaViewModel != null && agendaViewModel.rateAgenda().hasObservers()) {
                                agendaViewModel.rateAgenda().removeObservers(AgendaDetailsActivity.this);
                            }
                        }
                    });
                } else {
                    Utility.createShortSnackBar(ll_main, "No Internet Connection.!");
                }
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setDynamicColor() {
        CommonFunction.showBackgroundImage(this, ll_main);
        tv_date.setBackgroundColor(Color.parseColor(SharedPreference.getPref(AgendaDetailsActivity.this, EVENT_COLOR_1)));
        ll_start_time.setBackgroundColor(Color.parseColor(SharedPreference.getPref(AgendaDetailsActivity.this, EVENT_COLOR_1)));
        ll_end_time.setBackgroundColor(Color.parseColor(SharedPreference.getPref(AgendaDetailsActivity.this, EVENT_COLOR_1)));
        tv_horizontal.setBackgroundColor(Color.parseColor(SharedPreference.getPref(AgendaDetailsActivity.this, EVENT_COLOR_1)));
        ll_rate.setBackgroundColor(Color.parseColor(SharedPreference.getPref(AgendaDetailsActivity.this, EVENT_COLOR_1)));
        scrollView.setBackgroundColor(Color.parseColor(SharedPreference.getPref(AgendaDetailsActivity.this, EVENT_COLOR_2)));
        tv_rate.setBackgroundColor(Color.parseColor(SharedPreference.getPref(AgendaDetailsActivity.this, EVENT_COLOR_2)));
        tv_rate.setTextColor(Color.parseColor(SharedPreference.getPref(AgendaDetailsActivity.this, EVENT_COLOR_1)));
        tv_date.setTextColor(Color.parseColor(SharedPreference.getPref(AgendaDetailsActivity.this, EVENT_COLOR_2)));
        tv_session_name.setTextColor(Color.parseColor(SharedPreference.getPref(AgendaDetailsActivity.this, EVENT_COLOR_1)));
        tv_start_time.setTextColor(Color.parseColor(SharedPreference.getPref(AgendaDetailsActivity.this, EVENT_COLOR_2)));
        tv_start_time_am.setTextColor(Color.parseColor(SharedPreference.getPref(AgendaDetailsActivity.this, EVENT_COLOR_2)));
        tv_end_time.setTextColor(Color.parseColor(SharedPreference.getPref(AgendaDetailsActivity.this, EVENT_COLOR_2)));
        tv_end_time_am.setTextColor(Color.parseColor(SharedPreference.getPref(AgendaDetailsActivity.this, EVENT_COLOR_2)));
        tv_date.setTextColor(Color.parseColor(SharedPreference.getPref(AgendaDetailsActivity.this, EVENT_COLOR_2)));
        tv_header.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)));
        iv_back.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)), PorterDuff.Mode.SRC_ATOP);
        iv_reminder.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)), PorterDuff.Mode.SRC_ATOP);

        String eventColor3 = SharedPreference.getPref(this, EVENT_COLOR_3);

        String eventColor3Opacity40 = eventColor3.replace("#", "");
        switch_reminder.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        tv_short_session_description.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        tv_rate.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        tv_session_description.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        tv_rate_this_session.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        tv_set_reminder.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));

    }

}