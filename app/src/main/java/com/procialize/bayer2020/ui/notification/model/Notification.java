package com.procialize.bayer2020.ui.notification.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.bayer2020.GetterSetter.Header;

import java.util.List;

public class Notification {
    @SerializedName("header")
    @Expose
    List<Header> header;
    @SerializedName("totalRecords")
    @Expose
    String total_records;

    @SerializedName("detail")
    @Expose
    String detail;

    public List<Header> getHeader() {
        return header;
    }

    public void setHeader(List<Header> header) {
        this.header = header;
    }

    public String getTotal_records() {
        return total_records;
    }

    public void setTotal_records(String total_records) {
        this.total_records = total_records;
    }

    public String getDetail() {
        return detail;
    }
}
