package com.procialize.bayer2020.ui.eventList.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.bayer2020.GetterSetter.Header;

import java.util.List;

public class Event {

    @SerializedName("header")
    @Expose
    List<Header> header;

//    @SerializedName("detailpreencrypt")
//    @Expose
//    List<EventList> eventLists;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @SerializedName("detail")
    @Expose
    String detail;

    @SerializedName("file_path")
    @Expose
    String file_path;

    public List<Header> getHeader() {
        return header;
    }


//    public List<EventList> getEventLists() {
//        return eventLists;
//    }

    public String getFile_path() {
        return file_path;
    }
}
