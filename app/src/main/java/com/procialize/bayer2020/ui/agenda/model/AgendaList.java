package com.procialize.bayer2020.ui.agenda.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AgendaList {


    @SerializedName("agenda_list")
    @Expose
    List<Agenda> agenda_list;

    public List<Agenda> getAgenda_list() {
        return agenda_list;
    }
}
