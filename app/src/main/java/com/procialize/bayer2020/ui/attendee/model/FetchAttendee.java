package com.procialize.bayer2020.ui.attendee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.bayer2020.GetterSetter.Header;

import java.util.List;

public class FetchAttendee {
    @SerializedName("header")
    @Expose
    List<Header> header;

    @SerializedName("detailpreencrypt")
    @Expose
    List<Attendee> attandeeList;

    @SerializedName("detail")
    @Expose
    private String detail;

    public List<Header> getHeader() {
        return header;
    }

    public void setHeader(List<Header> header) {
        this.header = header;
    }

    public List<Attendee> getAttandeeList() {
        return attandeeList;
    }

    public void setAttandeeList(List<Attendee> attandeeList) {
        this.attandeeList = attandeeList;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
