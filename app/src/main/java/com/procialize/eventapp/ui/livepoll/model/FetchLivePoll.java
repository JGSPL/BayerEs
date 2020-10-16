package com.procialize.eventapp.ui.livepoll.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.eventapp.GetterSetter.Header;
import com.procialize.eventapp.ui.speaker.model.Speaker;

import java.util.List;

public class FetchLivePoll {
    @SerializedName("header")
    @Expose
    List<Header> header;

    @SerializedName("detailpreencrypt")
    @Expose
    List<Logo> LivePollList;



    @SerializedName("detail")
    @Expose
    private String detail;

    public List<Logo> getLivePollList() {
        return LivePollList;
    }

    public void setLivePollList(List<Logo> livePollList) {
        LivePollList = livePollList;
    }

    @SerializedName("totalRecords")
    @Expose
    private String totalRecords;



    public List<Header> getHeader() {
        return header;
    }

    public void setHeader(List<Header> header) {
        this.header = header;
    }



    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(String totalRecords) {
        this.totalRecords = totalRecords;
    }
}
