package com.procialize.eventapp.ui.AgendaDetails.networking;

import androidx.lifecycle.MutableLiveData;

import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.ui.newsFeedComment.model.Comment;
import com.procialize.eventapp.ui.newsFeedComment.model.LikePost;
import com.procialize.eventapp.ui.newsfeed.model.FetchNewsfeedMultiple;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgendaDetailsRepository {
    MutableLiveData<LoginOrganizer> agendaRateData = new MutableLiveData<>();
    MutableLiveData<LoginOrganizer> agendaReminderData = new MutableLiveData<>();
    private static AgendaDetailsRepository agendaDetailsRepository;
    MutableLiveData<LoginOrganizer> reportUser = new MutableLiveData<>();

    public static AgendaDetailsRepository getInstance() {
        if (agendaDetailsRepository == null) {
            agendaDetailsRepository = new AgendaDetailsRepository();
        }
        return agendaDetailsRepository;
    }

    private APIService agendaApi;

    public AgendaDetailsRepository() {
        agendaApi = ApiUtils.getAPIService();
    }

    public MutableLiveData<LoginOrganizer> rateAgenda(String token,String event_id,
                                                       String target_id,
                                                       float rating) {
        agendaApi.rateAgendaApi(token,event_id,
                target_id,
                String.valueOf(rating))
                .enqueue(new Callback<LoginOrganizer>() {
                    @Override
                    public void onResponse(Call<LoginOrganizer> call, Response<LoginOrganizer> response) {
                        if (response.isSuccessful()) {
                            agendaRateData.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                        agendaRateData.setValue(null);
                    }
                });

        return agendaRateData;
    }

    public MutableLiveData<LoginOrganizer> reminderAgenda(String token,String event_id,String session_id,
                                                          String reminder_flag,String reminder_id) {
        agendaApi.setReminderAgenda(token,event_id,
                session_id,
                reminder_flag,reminder_id)
                .enqueue(new Callback<LoginOrganizer>() {
                    @Override
                    public void onResponse(Call<LoginOrganizer> call, Response<LoginOrganizer> response) {
                        if (response.isSuccessful()) {
                            agendaReminderData.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                        agendaReminderData.setValue(null);
                    }
                });
        return agendaReminderData;
    }
}