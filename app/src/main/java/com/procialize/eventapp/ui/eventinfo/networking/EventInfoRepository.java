package com.procialize.eventapp.ui.eventinfo.networking;


import androidx.lifecycle.MutableLiveData;

import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.ui.eventinfo.model.EventInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventInfoRepository {

    MutableLiveData<EventInfo> likeList = new MutableLiveData<>();
    private static EventInfoRepository eventInfoRepository;

    public static EventInfoRepository getInstance() {
        if (eventInfoRepository == null) {
            eventInfoRepository = new EventInfoRepository();
        }
        return eventInfoRepository;
    }

    private APIService likeApi;

    public EventInfoRepository() {
        likeApi = ApiUtils.getAPIService();
    }


    public MutableLiveData<EventInfo> getEventInfo(String token, String event_id) {
        likeApi.eventInfoFetch(token, event_id)
                .enqueue(new Callback<EventInfo>() {
                    @Override
                    public void onResponse(Call<EventInfo> call, Response<EventInfo> response) {
                        if (response.isSuccessful()) {
                            try {
                                likeList.setValue(response.body());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<EventInfo> call, Throwable t) {
                        likeList.setValue(null);
                    }
                });

        return likeList;
    }

}
