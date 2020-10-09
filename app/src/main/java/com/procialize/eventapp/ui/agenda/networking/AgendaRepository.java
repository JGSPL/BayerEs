package com.procialize.eventapp.ui.agenda.networking;

import androidx.lifecycle.MutableLiveData;

import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.ApiUtils;
import com.procialize.eventapp.ui.agenda.model.FetchAgenda;
import com.procialize.eventapp.ui.attendee.model.FetchAttendee;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgendaRepository {
    private static AgendaRepository agendaRepository;
    MutableLiveData<FetchAgenda> fetchAgendaList = new MutableLiveData<>();
    
    public static AgendaRepository getInstance() {
        if (agendaRepository == null) {
            agendaRepository = new AgendaRepository();
        }
        return agendaRepository;
    }

    private APIService eventApi;

    public AgendaRepository() {
        eventApi = ApiUtils.getAPIService();
    }

    public MutableLiveData<FetchAgenda> getAgendaList(String token , String event_id) {//, String pageSize, String pageNumber) {
        eventApi.agendaFetch(token,event_id)
                .enqueue(new Callback<FetchAgenda>() {
                    @Override
                    public void onResponse(Call<FetchAgenda> call, Response<FetchAgenda> response) {
                        if (response.isSuccessful()) {
                            fetchAgendaList.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<FetchAgenda> call, Throwable t) {
                        fetchAgendaList.setValue(null);
                    }
                });

        return fetchAgendaList;
    }
}