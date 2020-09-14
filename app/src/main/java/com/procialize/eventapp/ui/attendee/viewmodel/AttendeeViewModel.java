package com.procialize.eventapp.ui.attendee.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.eventapp.ui.attendee.model.FetchAttendee;
import com.procialize.eventapp.ui.attendee.networking.AttendeeRepository;
import com.procialize.eventapp.ui.eventList.model.UpdateDeviceInfo;

public class AttendeeViewModel extends ViewModel {


    
    private AttendeeRepository attendeeRepository = AttendeeRepository.getInstance();
    MutableLiveData<FetchAttendee> fetchAttendeeData = new MutableLiveData<>();

    public void getAttendee(String token, String organizer_id, String search_text, String pageNumber, String pageSize) {
        attendeeRepository = AttendeeRepository.getInstance();
        fetchAttendeeData = attendeeRepository.getAttendeeList(token,organizer_id, search_text,pageNumber, pageSize);
    }


    public LiveData<FetchAttendee> getEventList() {
        return fetchAttendeeData;
    }
}