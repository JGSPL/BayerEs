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
    List<LivePoll> livePollList;


    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("totalRecords")
    @Expose
    private String totalRecords;

    public List<Header> getHeader() {
        return header;
    }

    public void setHeader(List<Header> header) {
        this.header = header;
    }

    public List<LivePoll> getLivePollList() {
        return livePollList;
    }

    public void setLivePollList(List<LivePoll> livePollList) {
        this.livePollList = livePollList;
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
