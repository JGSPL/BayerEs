package com.procialize.eventapp.ui.eventList.viewModel;

import android.app.Activity;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.eventapp.session.SessionManager;
import com.procialize.eventapp.ui.eventList.model.Event;
import com.procialize.eventapp.ui.eventList.model.EventList;
import com.procialize.eventapp.ui.eventList.model.LoginUserInfo;
import com.procialize.eventapp.ui.eventList.model.UpdateDeviceInfo;
import com.procialize.eventapp.ui.eventList.networking.EventRepository;
import com.procialize.eventapp.ui.newsFeedComment.model.Comment;
import com.procialize.eventapp.ui.newsFeedComment.networking.CommentRepository;
import com.procialize.eventapp.ui.newsFeedComment.view.CommentActivity;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;
import com.procialize.eventapp.ui.profile.view.ProfileActivity;

import java.io.Serializable;
import java.util.List;

public class EventListViewModel extends ViewModel {

    private EventRepository eventRepository = EventRepository.getInstance();
    MutableLiveData<Event> eventData = new MutableLiveData<>();
    MutableLiveData<UpdateDeviceInfo> updateData = new MutableLiveData<>();

    public void getEvent(String organizer_id, String search_text) {
        eventRepository = EventRepository.getInstance();
        eventData = eventRepository.getEventList(organizer_id, search_text);
    }


    public LiveData<Event> getEventList() {
        return eventData;
    }

    //-------------Update User data ---------------------

    public void updateUserData(String eventid, String device_token, String platform, String device,
                               String osVersion, String appVersion, SessionManager sessionManager) {
        eventRepository = EventRepository.getInstance();
        updateData = eventRepository.UpdateUserInfo(eventid, device_token, platform, device, osVersion, appVersion, sessionManager);
    }


    public LiveData<UpdateDeviceInfo> getupdateUserdatq() {
        return updateData;
    }


    //---------------View Profile Page--------------------------------
    /*public void openProfilePage(Activity activity, EventList event, int position) {
        activity.startActivity(new Intent(activity, ProfileActivity.class)
                .putExtra("event_details", (Serializable) event)
                .putExtra("position", "" + position));
    }*/
    public void openProfilePage(Activity activity, List<LoginUserInfo> event, int position) {
        activity.startActivity(new Intent(activity, ProfileActivity.class)
                .putExtra("event_details", (Serializable) event)
                .putExtra("position", "" + position));
    }
}
