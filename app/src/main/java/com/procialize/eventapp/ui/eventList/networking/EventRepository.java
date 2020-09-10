package com.procialize.eventapp.ui.eventList.networking;

import androidx.lifecycle.MutableLiveData;

import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.session.SessionManager;
import com.procialize.eventapp.ui.eventList.model.Event;
import com.procialize.eventapp.ui.eventList.model.UpdateDeviceInfo;
import com.procialize.eventapp.ui.newsFeedComment.model.Comment;
import com.procialize.eventapp.ui.newsFeedComment.networking.CommentRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventRepository {
    private static EventRepository eventRepository;
    MutableLiveData<Event> eventList = new MutableLiveData<>();

    MutableLiveData<UpdateDeviceInfo> updateLoginUserList = new MutableLiveData<>();

    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjYiLCJmaXJzdF9uYW1lIjoiQXBhcm5hIiwibWlkZGxlX25hbWUiOiIiLCJsYXN0X25hbWUiOiJCYWRoYW4iLCJtb2JpbGUiOiI4ODMwNDE2NzkwIiwiZW1haWwiOiJhcGFybmFAcHJvY2lhbGl6ZS5pbiIsInJlZnJlc2hfdG9rZW4iOiIwNTE0M2JmOTI0NzcwYTk5MTdlZjNhMWU5MjY4MGE3NTU5M2M1NDZiIiwidXNlcl90eXBlIjoiQSIsInZlcmlmeV9vdHAiOiIxIiwicHJvZmlsZV9waWMiOiJodHRwczpcL1wvc3RhZ2UtYWRtaW4ucHJvY2lhbGl6ZS5saXZlXC9iYXNlYXBwXC91cGxvYWRzXC91c2VyXC8xNTk5NTczNjM0ODMzNC5qcGciLCJpc19nb2QiOiIwIiwidGltZSI6MTU5OTcyNzQ0MiwiZXhwaXJ5X3RpbWUiOjE1OTk3MzEwNDJ9.HcJDPuJMtS_o8Q6FrzUmHWNulrPzNcAzAhodkCa9E0M";
    public static EventRepository getInstance() {
        if (eventRepository == null) {
            eventRepository = new EventRepository();
        }
        return eventRepository;
    }

    private APIService eventApi;

    public EventRepository() {
        eventApi = ApiUtils.getAPIService();
    }

    public MutableLiveData<Event> getEventList(String organizer_id, String search_text) {//, String pageSize, String pageNumber) {
        eventApi.getEventList(token,organizer_id,
                search_text)
                .enqueue(new Callback<Event>() {
                    @Override
                    public void onResponse(Call<Event> call, Response<Event> response) {
                        if (response.isSuccessful()) {
                            eventList.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Event> call, Throwable t) {
                        eventList.setValue(null);
                    }
                });

        return eventList;
    }



    public MutableLiveData<UpdateDeviceInfo> UpdateUserInfo(String eventid, String device_token, String platform, String device,
                                                            String osVersion, String appVersion, SessionManager sessionManager) {
        eventApi.updateDeviceInfo(eventid, device_token, platform, device, osVersion, appVersion)
                .enqueue(new Callback<UpdateDeviceInfo>() {
                    @Override
                    public void onResponse(Call<UpdateDeviceInfo> call, Response<UpdateDeviceInfo> response) {
                        if (response.isSuccessful()) {
                            updateLoginUserList.setValue(response.body());

                            /*List<LoginUserInfo> userData = response.body().getLoginUserInfoList();
                            String fname = userData.get(0).getFirst_name();
                            String lName = userData.get(0).getLast_name();
                            String designation = userData.get(0).getDesignation();
                            String company = userData.get(0).getCompany_name();
                            String attnId = userData.get(0).getAttendee_id();
                            String profilePic = userData.get(0).getProfile_picture();
                            String city = userData.get(0).getCity();
                            String is_god = userData.get(0).getIs_god();
                            String emailId = userData.get(0).getEmail();
                           sessionManager.createLoginSession(fname,lName,emailId,"",company,designation, "",city,profilePic,attnId,"",is_god);*/
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateDeviceInfo> call, Throwable t) {
                        updateLoginUserList.setValue(null);
                    }
                });

        return updateLoginUserList;
    }

}
