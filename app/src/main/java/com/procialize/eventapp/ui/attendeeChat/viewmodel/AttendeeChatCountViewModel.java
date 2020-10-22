package com.procialize.eventapp.ui.attendeeChat.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.eventapp.Database.EventAppDB;
import com.procialize.eventapp.ui.attendeeChat.model.ChatCount;
import com.procialize.eventapp.ui.attendeeChat.roomDb.Table_Attendee_Chatcount;

import java.util.List;

public class AttendeeChatCountViewModel extends ViewModel {
    EventAppDB eventAppDB;
    private LiveData<List<Table_Attendee_Chatcount>> attendeeList;// = new MutableLiveData<>();
    private LiveData<List<Table_Attendee_Chatcount>> attendeeSinList;// = new MutableLiveData<>();

    public void insertIntoDb(Context context, List<Table_Attendee_Chatcount> attendees) {
        eventAppDB = EventAppDB.getDatabase(context);

        for (int i = 0; i < attendees.size(); i++) {
            Table_Attendee_Chatcount Table_Attendee_Chatcount = new Table_Attendee_Chatcount();
            Table_Attendee_Chatcount.setChatCount_receId(attendees.get(i).getChatCount_receId());
            Table_Attendee_Chatcount.setChat_mess(attendees.get(i).getChat_mess());
            Table_Attendee_Chatcount.setChat_count_read(attendees.get(i).getChat_count_read());
            Table_Attendee_Chatcount.setChat_count(attendees.get(i).getChat_count());

            eventAppDB.attendeeChatDao().insertAttendee(Table_Attendee_Chatcount);
        }
    }

    public void deleteAllAttendee(Context context) {
        eventAppDB = EventAppDB.getDatabase(context);
        eventAppDB.attendeeChatDao().deleteAllAttendee();
    }

    public void getAttendeeDetails(Context context) {
        eventAppDB = EventAppDB.getDatabase(context);
        attendeeList = eventAppDB.attendeeChatDao().getAllAttendee();
    }

    public LiveData<List<Table_Attendee_Chatcount>> getAttendeeList() {
        return attendeeList;
    }

    public void insertIntoDbSingle(Context context, Table_Attendee_Chatcount attendees) {
        eventAppDB = EventAppDB.getDatabase(context);

        Table_Attendee_Chatcount Table_Attendee_Chatcount = new Table_Attendee_Chatcount();
        Table_Attendee_Chatcount.setChatCount_receId(attendees.getChatCount_receId());
        Table_Attendee_Chatcount.setChat_mess(attendees.getChat_mess());
        Table_Attendee_Chatcount.setChat_count_read(attendees.getChat_count_read());
        Table_Attendee_Chatcount.setChat_count(attendees.getChat_count());

        eventAppDB.attendeeChatDao().insertSingleAttendee(Table_Attendee_Chatcount);

    }


    public void deleteAttendee(Context context, String id) {
        eventAppDB = EventAppDB.getDatabase(context);
        eventAppDB.attendeeChatDao().deleteAttendee(id);
    }
}


