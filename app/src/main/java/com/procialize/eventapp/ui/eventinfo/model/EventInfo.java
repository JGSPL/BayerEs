package com.procialize.eventapp.ui.eventinfo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.eventapp.GetterSetter.Header;

import java.util.List;

public class EventInfo {

    @SerializedName("header")
    @Expose
    List<Header> header;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @SerializedName("detail")
    @Expose
    private String detail;

    @SerializedName("file_path")
    @Expose
    private String file_path;

    public String getFile_path() {
        return file_path;
    }
}
