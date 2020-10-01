package com.procialize.eventapp.ui.eventList.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.eventapp.GetterSetter.Header;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;

import java.util.List;

public class Event {

    @SerializedName("header")
    @Expose
    List<Header> header;

/*    @SerializedName("detailpreencrypt")
    @Expose
    List<EventList> eventLists;*/

    @SerializedName("detail")
    @Expose
    String eventListEncrypted;

    @SerializedName("file_pathpreencrypt")
    @Expose
    String file_path;

    public List<Header> getHeader() {
        return header;
    }


   /* public List<EventList> getEventLists() {
        return eventLists;
    }*/

    public String getFile_path() {
        return file_path;
    }

    public String getEventListEncrypted() {
        return eventListEncrypted;
    }

    public void setEventListEncrypted(String eventListEncrypted) {
        this.eventListEncrypted = eventListEncrypted;
    }
}
