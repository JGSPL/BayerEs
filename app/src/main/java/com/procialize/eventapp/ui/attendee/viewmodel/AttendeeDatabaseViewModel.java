package com.procialize.eventapp.ui.attendee.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Database.EventAppDB;
import com.procialize.eventapp.ui.attendee.model.Attendee;
import com.procialize.eventapp.ui.attendee.roomDB.TableAttendee;
import com.procialize.eventapp.ui.newsfeed.model.News_feed_media;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;
import com.procialize.eventapp.ui.newsfeed.roomDB.TableNewsFeed;
import com.procialize.eventapp.ui.newsfeed.roomDB.TableNewsFeedMedia;

import java.util.List;

public class AttendeeDatabaseViewModel extends ViewModel {
    EventAppDB eventAppDB ;
    private LiveData<List<TableAttendee>> attendeeList;// = new MutableLiveData<>();
    public void insertIntoDb(Context context, List<Attendee> attendees) {
        eventAppDB = EventAppDB.getDatabase(context);

        for (int i = 0; i < attendees.size(); i++) {
            TableAttendee tableAttendee = new TableAttendee();
            tableAttendee.setMobile(attendees.get(i).getMobile());
            tableAttendee.setEmail(attendees.get(i).getEmail());
            tableAttendee.setAttendee_id(attendees.get(i).getAttendee_id());
            tableAttendee.setFirst_name(attendees.get(i).getFirst_name());
            tableAttendee.setLast_name(attendees.get(i).getLast_name());
            tableAttendee.setProfile_picture(attendees.get(i).getProfile_picture());
            tableAttendee.setCity(attendees.get(i).getCity());
            tableAttendee.setDesignation(attendees.get(i).getDesignation());
            tableAttendee.setCompany_name(attendees.get(i).getCompany_name());
            tableAttendee.setAttendee_type(attendees.get(i).getAttendee_type());
            tableAttendee.setTotal_sms(attendees.get(i).getTotal_sms());

            String mention_name;
            if (attendees.get(i).getLast_name() != null) {
                mention_name = "<"+attendees.get(i).getAttendee_id()+"^"+attendees.get(i).getFirst_name() + " " + attendees.get(i).getLast_name()+">";
            } else {
                mention_name = "<"+attendees.get(i).getAttendee_id()+"^"+attendees.get(i).getFirst_name()+">";
            }
            tableAttendee.setFld_mention_name(mention_name);
            tableAttendee.setFirebase_id(attendees.get(i).getFirebase_id());
            tableAttendee.setFirebase_name(attendees.get(i).getFirebase_name());
            tableAttendee.setFirebase_username(attendees.get(i).getFirebase_username());
            eventAppDB.attendeeDao().insertAttendee(tableAttendee);
        }
    }

    public void deleteAllAttendee(Context context)
    {
        eventAppDB = EventAppDB.getDatabase(context);
        eventAppDB.attendeeDao().deleteAllAttendee();
    }

    public void getAttendeeDetails(Context context)
    {
        eventAppDB = EventAppDB.getDatabase(context);
        attendeeList = eventAppDB.attendeeDao().getAllAttendee();
    }

    public LiveData<List<TableAttendee>> getAttendeeList()
    {
        return attendeeList;
    }
}
