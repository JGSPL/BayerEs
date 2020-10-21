package com.procialize.eventapp.ui.AgendaDetails.viewmodel;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.ui.AgendaDetails.networking.AgendaDetailsRepository;
import com.procialize.eventapp.ui.AgendaDetails.view.AgendaDetailsActivity;
import com.procialize.eventapp.ui.agenda.model.Agenda;
import com.procialize.eventapp.ui.liveStreaming.view.LiveStreamingActivity;
import com.procialize.eventapp.ui.newsFeedComment.networking.CommentRepository;
import com.procialize.eventapp.ui.newsFeedDetails.view.NewsFeedDetailsActivity;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.eventapp.ui.AgendaDetails.view.AgendaDetailsActivity.ll_main;

public class AgendaViewModel extends ViewModel {
    Date d1, d2;
    private MutableLiveData<String> mText;
    private AgendaDetailsRepository agendaDetailsRepository = AgendaDetailsRepository.getInstance();
    MutableLiveData<LoginOrganizer> agendaRateLiveData = new MutableLiveData<>();

    public LiveData<String> getText() {
        return mText;
    }

    public void openAgendaDetails(Activity activity,String liveStreamLink,Agenda agenda) {
        activity.startActivity(new Intent(activity, LiveStreamingActivity.class).
                putExtra("agendaDetails", (Serializable) agenda).
                putExtra("livestream_link",liveStreamLink));
    }


    public void saveToCalenderEx(final Activity activity, String sessionStartTime, String sessionEndTime, String sessionName,
                                 String sessionDescription, String sessionId, Agenda agendaDetails) {

        String api_token = SharedPreference.getPref(activity, AUTHERISATION_KEY);
        String event_id = SharedPreference.getPref(activity, EVENT_ID);

        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

        Random rand = new Random();
        final int reminder_id = rand.nextInt(1000);

        Intent intent = new Intent(Intent.ACTION_EDIT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, millisecondsStart)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, millisecondsEnd)
                .putExtra(CalendarContract.Events.TITLE, sessionName)
                .putExtra(CalendarContract.Events.DESCRIPTION, sessionDescription)
                .putExtra(CalendarContract.Events.ORIGINAL_ID, reminder_id);
        activity.startActivityForResult(intent, 111);

        agendaDetails.setReminder_id(String.valueOf(reminder_id));

        reminderAgenda(api_token, event_id, sessionId, "1",String.valueOf(reminder_id));
        reminderAgenda().observe((LifecycleOwner) activity, new Observer<LoginOrganizer>() {
            @Override
            public void onChanged(LoginOrganizer loginOrganizer) {
                if (loginOrganizer != null) {
                    if (loginOrganizer.getHeader().get(0).getType().equalsIgnoreCase("success")) {
                        Utility.createShortSnackBar(ll_main,loginOrganizer.getHeader().get(0).getMsg());
                    } else {
                        Utility.createShortSnackBar(ll_main,loginOrganizer.getHeader().get(0).getMsg());
                    }
                }

                if (reminderAgenda().hasObservers()) {
                    reminderAgenda().removeObservers((LifecycleOwner) activity);
                }
            }
        });
    }

    public void saveToCalender(final Activity activity, String sessionStartTime, String sessionEndTime, String sessionName,
                               String sessionDescription, String sessionId, Agenda agendaDetails) {

        String api_token = SharedPreference.getPref(activity, AUTHERISATION_KEY);
        String event_id = SharedPreference.getPref(activity, EVENT_ID);

        long eventID = -1;
        try {
            String eventUriString = "content://com.android.calendar/events";
            ContentValues eventValues = new ContentValues();
            eventValues.put("calendar_id", 3); // id, We need to choose from
            // our mobile for primary its 1
            eventValues.put("title", sessionName);
            eventValues.put("description", sessionDescription);
            //  eventValues.put("eventLocation", place);

            SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


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
            Uri eventUri = activity.getApplicationContext()
                    .getContentResolver()
                    .insert(Uri.parse(eventUriString), eventValues);
            eventID = Long.parseLong(eventUri.getLastPathSegment());
            agendaDetails.setReminder_id(String.valueOf(eventID));
            //Toast.makeText(activity, "Added to Calendar Successfully", Toast.LENGTH_SHORT).show();

            reminderAgenda(api_token, event_id, sessionId, "1",String.valueOf(eventID));
            reminderAgenda().observe((LifecycleOwner) activity, new Observer<LoginOrganizer>() {
                @Override
                public void onChanged(LoginOrganizer loginOrganizer) {
                    if (loginOrganizer != null) {
                        if (loginOrganizer.getHeader().get(0).getType().equalsIgnoreCase("success")) {
                            Utility.createShortSnackBar(ll_main,loginOrganizer.getHeader().get(0).getMsg());
                        } else {
                            Utility.createShortSnackBar(ll_main,loginOrganizer.getHeader().get(0).getMsg());
                        }
                    }

                    if (reminderAgenda().hasObservers()) {
                        reminderAgenda().removeObservers((LifecycleOwner) activity);
                    }
                }
            });
        } catch (Exception ex) {
            Toast.makeText(activity, "Attempt failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void rateTheAgenda(String token, String event_id,
                              String target_id,
                              float rating) {
        agendaDetailsRepository = AgendaDetailsRepository.getInstance();
        agendaRateLiveData = agendaDetailsRepository.rateAgenda(token, event_id,
                target_id,
                rating);
    }

    public LiveData<LoginOrganizer> rateAgenda() {
        return agendaRateLiveData;
    }

    public void reminderAgenda(String token, String event_id,
                               String session_id,
                               String reminder_flag,String reminder_id) {
        agendaDetailsRepository = AgendaDetailsRepository.getInstance();
        agendaRateLiveData = agendaDetailsRepository.reminderAgenda(token, event_id, session_id, reminder_flag,reminder_id);
    }

    public LiveData<LoginOrganizer> reminderAgenda() {
        return agendaRateLiveData;
    }
}
