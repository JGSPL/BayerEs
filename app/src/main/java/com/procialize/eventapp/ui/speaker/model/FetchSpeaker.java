package com.procialize.eventapp.ui.speaker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.eventapp.GetterSetter.Header;

import java.util.List;

public class FetchSpeaker {
    @SerializedName("header")
    @Expose
    List<Header> header;

    @SerializedName("detailpreencrypt")
    @Expose
    List<Speaker> speakerList;

    @SerializedName("detail")
    @Expose
    private String detail;

    public List<Header> getHeader() {
        return header;
    }

    public void setHeader(List<Header> header) {
        this.header = header;
    }

    public List<Speaker> getSpeakerList() {
        return speakerList;
    }

    public void setSpeakerList(List<Speaker> speakerList) {
        this.speakerList = speakerList;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
