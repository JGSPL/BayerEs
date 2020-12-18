package com.procialize.bayer2020.ui.eventList.networking;

import androidx.lifecycle.MutableLiveData;

import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.session.SessionManager;
import com.procialize.bayer2020.ui.eventList.model.Event;
import com.procialize.bayer2020.ui.eventList.model.UpdateDeviceInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventRepository {
    private static EventRepository eventRepository;
    MutableLiveData<Event> eventList = new MutableLiveData<>();

    MutableLiveData<UpdateDeviceInfo> updateLoginUserList = new MutableLiveData<>();


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

    public MutableLiveData<Event> getEventList(String token,String organizer_id, String search_text) {
        eventApi.getEventList(token, organizer_id,search_text)
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


    public MutableLiveData<UpdateDeviceInfo> UpdateUserInfo(String token, String eventid, String device_token, String platform, String device,
                                                            String osVersion, String appVersion, SessionManager sessionManager) {
        eventApi.updateDeviceInfo(token, eventid, device_token, platform, device, osVersion, appVersion)
                .enqueue(new Callback<UpdateDeviceInfo>() {
                    @Override
                    public void onResponse(Call<UpdateDeviceInfo> call, Response<UpdateDeviceInfo> response) {
                        if (response.isSuccessful()) {
                            try {
                                updateLoginUserList.setValue(response.body());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

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
