package com.procialize.eventapp.ui.agenda.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.eventapp.GetterSetter.Header;

import java.util.List;

public class AgendaList {


    @SerializedName("agenda_list")
    @Expose
    List<Agenda> agenda_list;

    public List<Agenda> getAgenda_list() {
        return agenda_list;
    }
}
