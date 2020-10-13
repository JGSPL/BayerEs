package com.procialize.eventapp.ui.AgendaDetails.view;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.ui.AgendaDetails.viewmodel.AgendaViewModel;
import com.procialize.eventapp.ui.agenda.model.Agenda;
import com.procialize.eventapp.ui.newsfeed.viewmodel.NewsFeedViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AgendaDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_date, tv_start_time, tv_start_time_am, tv_end_time, tv_end_time_am, tv_session_name, tv_short_session_description, tv_session_description;
    LinearLayout ll_live_stream;
    Switch switch_reminder;
    RatingBar ratingbar;
    AgendaViewModel agendaViewModel;
    String statusSwitch1;
    String sessionName,sessionShortDescription,sessionDescription,sessionStartTime,sessionEndTime,sessionDate,eventId,livestreamLink,star,totalFeedback,feedbackComment,rated;

    Date d1, d2;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_agenda_details);

        agendaViewModel = ViewModelProviders.of(this).get(AgendaViewModel.class);

        tv_date = findViewById(R.id.tv_date);
        tv_start_time = findViewById(R.id.tv_start_time);
        tv_start_time_am = findViewById(R.id.tv_start_time_am);
        tv_end_time = findViewById(R.id.tv_end_time);
        tv_end_time_am = findViewById(R.id.tv_end_time_am);
        ll_live_stream = findViewById(R.id.ll_live_stream);
        switch_reminder = findViewById(R.id.switch_reminder);
        tv_session_name = findViewById(R.id.tv_session_name);
        tv_short_session_description = findViewById(R.id.tv_short_session_description);
        ratingbar = findViewById(R.id.ratingbar);
        tv_session_description = findViewById(R.id.tv_session_description);

        ll_live_stream.setOnClickListener(this);

        Intent intent = getIntent();
        Agenda agendaDetails = (Agenda) intent.getSerializableExtra("agendaDetails");
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


        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.UK);
            SimpleDateFormat targetFormat = new SimpleDateFormat("HH:mm aa");
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date sourceDate = null;
        try {
            sourceDate = dateFormat.parse(sessionDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd, YYYY");
        //SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy HH:mm aa");
        String targetdatevalue = targetFormat.format(sourceDate);
        tv_date.setText(targetdatevalue);
        tv_session_name.setText(sessionName);
        tv_short_session_description.setText(sessionShortDescription);
        tv_session_description.setText(sessionDescription);

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
                                saveToCalenderEx();
                            }
                        } else {
                            saveToCalender();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    statusSwitch1 = switch_reminder.getTextOff().toString();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveToCalenderEx();
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
                agendaViewModel.openAgendaDetails(this);
                break;
        }
    }

        public void saveToCalenderEx() {

        SimpleDateFormat f1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            d1 = f1.parse(sessionStartTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long millisecondsStart = d1.getTime();
        try {
            d2 = f1.parse(sessionEndTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millisecondsEnd = d2.getTime();

        Intent intent = new Intent(Intent.ACTION_EDIT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, millisecondsStart)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, millisecondsEnd)
                .putExtra(CalendarContract.Events.TITLE, sessionName)
                .putExtra(CalendarContract.Events.DESCRIPTION, sessionDescription);
        startActivityForResult(intent, 111);

        Toast.makeText(getBaseContext(), "Added to Calendar Successfully", Toast.LENGTH_SHORT).show();
    }

    public void saveToCalender() {
        long eventID = -1;
        try {
            String eventUriString = "content://com.android.calendar/events";
            ContentValues eventValues = new ContentValues();
            eventValues.put("calendar_id", 3); // id, We need to choose from
            // our mobile for primary its 1
            eventValues.put("title", sessionName);
            eventValues.put("description", sessionDescription);
            //  eventValues.put("eventLocation", place);

            SimpleDateFormat f1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");


            try {
                d1 = f1.parse(sessionStartTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long millisecondsStart = d1.getTime();

            try {
                d2 = f1.parse(sessionEndTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
//2017-11-24 23:45:00
            long millisecondsEnd = d2.getTime();


            long endDate = System.currentTimeMillis() + 1000 * 10 * 10; // For next 10min
            eventValues.put("dtstart", millisecondsStart);
            eventValues.put("dtend", millisecondsEnd);

            eventValues.put("eventStatus", 1);
            eventValues.put("eventTimezone", TimeZone.getDefault().getID());

            eventValues.put("hasAlarm", 1); // 0 for false, 1 for true

            Uri eventUri = AgendaDetailsActivity.this.getApplicationContext()
                    .getContentResolver()
                    .insert(Uri.parse(eventUriString), eventValues);
            eventID = Long.parseLong(eventUri.getLastPathSegment());

            Toast.makeText(getBaseContext(), "Added to Calendar Successfully", Toast.LENGTH_SHORT).show();

        } catch (Exception ex) {
            Toast.makeText(getBaseContext(), "Attempt failed", Toast.LENGTH_SHORT).show();

            //log.error("Error in adding event on calendar" + ex.getMessage());
        }

    }
}