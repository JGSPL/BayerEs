package com.procialize.eventapp.ui.AgendaDetails.view;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.ui.agenda.model.Agenda;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AgendaDetailsActivity extends Activity {
    TextView tv_date, tv_start_time, tv_start_time_am, tv_end_time, tv_end_time_am, tv_session_name, tv_short_session_description, tv_session_description;
    LinearLayout ll_live_stream;
    Switch switch_reminder;
    RatingBar ratingbar;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_agenda_details);

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

        Intent intent = getIntent();
        Agenda agendaDetails = (Agenda) intent.getSerializableExtra("agendaDetails");
        String sessionName = agendaDetails.getSession_name();
        String sessionShortDescription = agendaDetails.getSession_short_description();
        String sessionDescription = agendaDetails.getSession_description();
        String sessionStartTime = agendaDetails.getSession_start_time();
        String sessionEndTime = agendaDetails.getSession_end_time();
        String sessionDate = agendaDetails.getSession_date();
        String eventId = agendaDetails.getEvent_id();
        String livestreamLink = agendaDetails.getLivestream_link();
        String star = agendaDetails.getStar();
        String totalFeedback = agendaDetails.getTotal_feedback();
        String feedbackComment = agendaDetails.getFeedback_comment();
        String rated = agendaDetails.getRated();


        /*try {
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

        tv_date.setText(CommonFunction.convertEventDate(sessionDate));*/
        tv_session_name.setText(sessionName);
        tv_short_session_description.setText(sessionShortDescription);
        tv_session_description.setText(sessionDescription);
    }
}