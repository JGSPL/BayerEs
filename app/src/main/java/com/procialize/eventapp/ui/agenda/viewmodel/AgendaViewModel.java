package com.procialize.eventapp.ui.agenda.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.eventapp.ui.agenda.model.FetchAgenda;
import com.procialize.eventapp.ui.agenda.networking.AgendaRepository;
import com.procialize.eventapp.ui.attendee.model.FetchAttendee;

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
