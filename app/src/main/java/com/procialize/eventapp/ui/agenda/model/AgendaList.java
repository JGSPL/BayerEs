package com.procialize.eventapp.ui.agenda.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.eventapp.GetterSetter.Header;

import java.util.List;

public class AgendaList {

    @SerializedName("agenda_media_url_path")
    @Expose
    private String agenda_media_url_path;

    @SerializedName("agenda_list")
    @Expose
    List<Agenda> agenda_list;

    public String getAgenda_media_url_path() {
        return agenda_media_url_path;
    }

    public List<Agenda> getAgenda_list() {
        return agenda_list;
    }
}
