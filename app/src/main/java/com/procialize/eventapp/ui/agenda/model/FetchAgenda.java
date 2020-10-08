package com.procialize.eventapp.ui.agenda.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.eventapp.GetterSetter.Header;
import com.procialize.eventapp.ui.attendee.model.Attendee;

import java.util.List;

public class FetchAgenda {

    @SerializedName("header")
    @Expose
    List<Header> header;

    @SerializedName("detail")
    @Expose
    private String detail;

    public List<Header> getHeader() {
        return header;
    }

    public String getDetail() {
        return detail;
    }
}
