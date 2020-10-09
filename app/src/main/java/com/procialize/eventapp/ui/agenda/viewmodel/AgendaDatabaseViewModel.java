package com.procialize.eventapp.ui.agenda.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.procialize.eventapp.Database.EventAppDB;
import com.procialize.eventapp.ui.agenda.model.Agenda;
import com.procialize.eventapp.ui.agenda.roomDB.TableAgenda;
import com.procialize.eventapp.ui.attendee.model.Attendee;
import com.procialize.eventapp.ui.attendee.roomDB.TableAttendee;

import java.util.List;

public class AgendaDatabaseViewModel {

    EventAppDB eventAppDB ;
    private LiveData<List<TableAgenda>> attendeeList;// = new MutableLiveData<>();
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
}
