package com.procialize.bayer2020.ui.eventinfo.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.bayer2020.Database.EventAppDB;
import com.procialize.bayer2020.ui.eventinfo.model.EventInfoDetails;
import com.procialize.bayer2020.ui.eventinfo.roomDB.TableEventInfo;

import java.util.List;

public class EventInfoDatabaseViewModel extends ViewModel {

    EventAppDB eventAppDB ;
    private LiveData<List<TableEventInfo>> eventInfoList;// = new MutableLiveData<>();

    public void insertIntoDb(Context context, List<EventInfoDetails> eventInfoDetails) {
        eventAppDB = EventAppDB.getDatabase(context);

        for (int i = 0; i < eventInfoDetails.size(); i++) {
            TableEventInfo tableEventInfo = new TableEventInfo();
            tableEventInfo.setEvent_id(eventInfoDetails.get(i).getEvent_id());
            tableEventInfo.setEvent_name(eventInfoDetails.get(i).getEvent_name());
            tableEventInfo.setEvent_start_date(eventInfoDetails.get(i).getEvent_start_date());
            tableEventInfo.setEvent_end_date(eventInfoDetails.get(i).getEvent_end_date());
            tableEventInfo.setEvent_description(eventInfoDetails.get(i).getEvent_description());
            tableEventInfo.setEvent_location(eventInfoDetails.get(i).getEvent_location());
            tableEventInfo.setEvent_city(eventInfoDetails.get(i).getEvent_city());
            tableEventInfo.setEvent_latitude(eventInfoDetails.get(i).getEvent_latitude());
            tableEventInfo.setEvent_longitude(eventInfoDetails.get(i).getEvent_longitude());
            tableEventInfo.setEvent_image(eventInfoDetails.get(i).getEvent_image());
            tableEventInfo.setHeader_image(eventInfoDetails.get(i).getHeader_image());
            tableEventInfo.setBackground_image(eventInfoDetails.get(i).getBackground_image());
            tableEventInfo.setEvent_cover_image(eventInfoDetails.get(i).getEvent_cover_image());
            eventAppDB.eventInfoDao().insertEventInfo(tableEventInfo);
        }
    }

    public void deleteEventInfo(Context context)
    {
        eventAppDB = EventAppDB.getDatabase(context);
        eventAppDB.eventInfoDao().deleteEventInfo();
    }

    public void getEventInfoDetails(Context context)
    {
        eventAppDB = EventAppDB.getDatabase(context);
        eventInfoList = eventAppDB.eventInfoDao().getEventInfo();
    }


    public LiveData<List<TableEventInfo>> getEventInfoList()
    {
        return eventInfoList;
    }
}
