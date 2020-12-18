package com.procialize.bayer2020.ui.agenda.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.bayer2020.ui.agenda.model.FetchAgenda;
import com.procialize.bayer2020.ui.agenda.networking.AgendaRepository;

public class AgendaViewModel extends ViewModel {

    private MutableLiveData<String> mText;



    private AgendaRepository agendaRepository = AgendaRepository.getInstance();
    MutableLiveData<FetchAgenda> fetchAttendeeData = new MutableLiveData<>();

    public void getAgenda(String token, String event_id) {
        agendaRepository = AgendaRepository.getInstance();
        fetchAttendeeData = agendaRepository.getAgendaList(token,event_id);
    }


    public LiveData<FetchAgenda> getAgendaList() {
        return fetchAttendeeData;
    }


}
