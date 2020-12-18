package com.procialize.bayer2020.ui.eventinfo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.bayer2020.ui.eventinfo.model.EventInfo;
import com.procialize.bayer2020.ui.eventinfo.networking.EventInfoRepository;

public class EventInfoViewModel extends ViewModel {
    private EventInfoRepository eventInfoRepository = EventInfoRepository.getInstance();
    MutableLiveData<EventInfo> eventInfoData = new MutableLiveData<>();

    public void getEvent(String token, String event_id) {
        eventInfoRepository = EventInfoRepository.getInstance();
        eventInfoData = eventInfoRepository.getEventInfo(token,event_id);
    }

    public LiveData<EventInfo> getEventInfo() {
        return eventInfoData;
    }
}
