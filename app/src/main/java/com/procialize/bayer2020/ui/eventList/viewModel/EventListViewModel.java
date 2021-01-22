package com.procialize.bayer2020.ui.eventList.viewModel;

import android.app.Activity;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.bayer2020.MainActivity;
import com.procialize.bayer2020.session.SessionManager;
import com.procialize.bayer2020.ui.eventList.model.Event;
import com.procialize.bayer2020.ui.eventList.model.EventList;
import com.procialize.bayer2020.ui.eventList.model.LoginUserInfo;
import com.procialize.bayer2020.ui.eventList.model.UpdateDeviceInfo;
import com.procialize.bayer2020.ui.eventList.networking.EventRepository;
import com.procialize.bayer2020.ui.profile.roomDB.ProfileEventId;
import com.procialize.bayer2020.ui.profile.view.ProfileActivity;
import com.procialize.bayer2020.ui.profile.view.ProfilePCOActivity;

import java.io.Serializable;
import java.util.List;

public class EventListViewModel extends ViewModel {

    private EventRepository eventRepository = EventRepository.getInstance();
    MutableLiveData<Event> eventData = new MutableLiveData<>();
    MutableLiveData<UpdateDeviceInfo> updateData = new MutableLiveData<>();

    private LiveData<List<ProfileEventId>> profileDataUpdated;


    public void getEvent(String token, String organizer_id, String search_text) {

        eventRepository = EventRepository.getInstance();
        eventData = eventRepository.getEventList(token, organizer_id, search_text);
    }


    public LiveData<Event> getEventList() {
        return eventData;
    }

    //-------------Update User data ---------------------

    public void updateUserData(String token, String eventid, String device_token, String platform, String device,
                               String osVersion, String appVersion, SessionManager sessionManager) {
        eventRepository = EventRepository.getInstance();
        updateData = eventRepository.UpdateUserInfo(token, eventid, device_token, platform, device, osVersion, appVersion, sessionManager);
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
    /*public void openProfilePage(Activity activity, List<LoginUserInfo> event, int position,String eventBg) {
        activity.startActivity(new Intent(activity, ProfileActivity.class)
                .putExtra("event_details", (Serializable) event)
                .putExtra("position", "" + position)
                .putExtra("eventBg", "" + eventBg)
        );
        activity.finish();
    }

    public void openMainPage(Activity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class));
        activity.finish();
    }
*/
    public void openProfilePage(Activity activity, List<LoginUserInfo> userInfo, int position, String eventBg,EventList event) {
        //if (event.getEvent_type().equalsIgnoreCase("0")) {
        activity.startActivity(new Intent(activity, ProfileActivity.class)
                .putExtra("event_details", (Serializable) userInfo)
                .putExtra("position", "" + position)
                .putExtra("eventBg", "" + eventBg)
        );
        activity.finish();
        /*} else {
            activity.startActivity(new Intent(activity, LiveStreamingActivityForWebinar.class).putExtra("eventType", event.getEvent_type()));
            activity.finish();
        }*/
    }

    public void openProfilePCOPage(Activity activity, List<LoginUserInfo> userInfo, int position, String eventBg,EventList event) {
        activity.startActivity(new Intent(activity, ProfilePCOActivity .class)
                .putExtra("event_details", (Serializable) userInfo)
                .putExtra("position", "" + position)
                .putExtra("eventBg", "" + eventBg)
        );
        activity.finish();
    }


    public void openMainPage(Activity activity, EventList event) {
        //if (event.getEvent_type().equalsIgnoreCase("0")) {
        activity.startActivity(new Intent(activity, MainActivity.class));
        activity.finish();
        /*} else {
            activity.startActivity(new Intent(activity, LiveStreamingActivityForWebinar.class));
            activity.finish();
        }*/
    }
}
