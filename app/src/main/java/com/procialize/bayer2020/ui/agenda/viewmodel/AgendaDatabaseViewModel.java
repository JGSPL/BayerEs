package com.procialize.bayer2020.ui.agenda.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.bayer2020.Database.EventAppDB;
import com.procialize.bayer2020.ui.agenda.model.Agenda;
import com.procialize.bayer2020.ui.agenda.roomDB.TableAgenda;

import java.util.List;

public class AgendaDatabaseViewModel extends ViewModel {

    EventAppDB eventAppDB ;
    private LiveData<List<TableAgenda>> agendaList;// = new MutableLiveData<>();
    public void insertIntoDb(Context context, List<Agenda> agenda) {
        eventAppDB = EventAppDB.getDatabase(context);

        for (int i = 0; i < agenda.size(); i++) {
            TableAgenda tableAgenda = new TableAgenda();
            tableAgenda.setSession_id(agenda.get(i).getSession_id());
            tableAgenda.setSession_name(agenda.get(i).getSession_name());
            tableAgenda.setSession_short_description(agenda.get(i).getSession_short_description());
            tableAgenda.setSession_description(agenda.get(i).getSession_description());
            tableAgenda.setSession_start_time(agenda.get(i).getSession_start_time());
            tableAgenda.setSession_end_time(agenda.get(i).getSession_end_time());
            tableAgenda.setSession_date(agenda.get(i).getSession_date());
            tableAgenda.setEvent_id(agenda.get(i).getEvent_id());
            tableAgenda.setLivestream_link(agenda.get(i).getLivestream_link());
            tableAgenda.setStar(agenda.get(i).getStar());
            tableAgenda.setTotal_feedback(agenda.get(i).getTotal_feedback());
            tableAgenda.setFeedback_comment(agenda.get(i).getFeedback_comment());
            tableAgenda.setRated(agenda.get(i).getRated());
            eventAppDB.agendaDao().insertAgenda(tableAgenda);
        }
    }

    public void deleteAllAgenda(Context context)
    {
        eventAppDB = EventAppDB.getDatabase(context);
        eventAppDB.agendaDao().deleteAllAgenda();
    }

    public void getAgendaList(Context context)
    {
        eventAppDB = EventAppDB.getDatabase(context);
        agendaList = eventAppDB.agendaDao().getAllAgenda();
    }

    public LiveData<List<TableAgenda>> getAgenda()
    {
        return agendaList;
    }
}
