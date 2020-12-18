package com.procialize.bayer2020.ui.spotQnA.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.bayer2020.GetterSetter.Header;

import java.util.List;

public class FetchSpotQnA {

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


    @SerializedName("totalRecords")
    @Expose
    String totalRecords;

    public String getTotalRecords() {
        return totalRecords;
    }

}
