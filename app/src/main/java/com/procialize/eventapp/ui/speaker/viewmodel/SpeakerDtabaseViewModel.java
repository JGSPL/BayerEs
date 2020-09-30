package com.procialize.eventapp.ui.speaker.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.eventapp.Database.EventAppDB;
import com.procialize.eventapp.ui.speaker.model.Speaker;
import com.procialize.eventapp.ui.speaker.roomDB.TableSpeaker;

import java.util.List;

public class SpeakerDtabaseViewModel extends ViewModel {
    EventAppDB eventAppDB ;
    private LiveData<List<TableSpeaker>> speakerList;// = new MutableLiveData<>();
    public void insertIntoDb(Context context, List<Speaker> speakers) {
        eventAppDB = EventAppDB.getDatabase(context);

        for (int i = 0; i < speakers.size(); i++) {
            TableSpeaker TableSpeaker = new TableSpeaker();
            TableSpeaker.setMobile(speakers.get(i).getMobile());
            TableSpeaker.setEmail(speakers.get(i).getEmail());
            TableSpeaker.setAttendee_id(speakers.get(i).getAttendee_id());
            TableSpeaker.setFirst_name(speakers.get(i).getFirst_name());
            TableSpeaker.setLast_name(speakers.get(i).getLast_name());
            TableSpeaker.setProfile_picture(speakers.get(i).getProfile_picture());
            TableSpeaker.setCity(speakers.get(i).getCity());
            TableSpeaker.setDesignation(speakers.get(i).getDesignation());
            TableSpeaker.setCompany_name(speakers.get(i).getCompany_name());
            TableSpeaker.setAttendee_type(speakers.get(i).getAttendee_type());
            TableSpeaker.setTotal_sms(speakers.get(i).getTotal_sms());

            TableSpeaker.setFirebase_id(speakers.get(i).getFirebase_id());
            TableSpeaker.setFirebase_name(speakers.get(i).getFirebase_name());
            TableSpeaker.setFirebase_username(speakers.get(i).getFirebase_username());
            eventAppDB.speakerDao().insertSpeaker(TableSpeaker);
        }
    }

    public void deleteAllSpeaker(Context context)
    {
        eventAppDB = EventAppDB.getDatabase(context);
        eventAppDB.attendeeDao().deleteAllAttendee();
    }

    public void getSpeakerDetails(Context context)
    {
        eventAppDB = EventAppDB.getDatabase(context);
        speakerList = eventAppDB.speakerDao().getAllSpeaker();
    }

    public LiveData<List<TableSpeaker>> getSpeakerList()
    {
        return speakerList;
    }
}
