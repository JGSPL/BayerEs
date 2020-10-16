package com.procialize.eventapp.ui.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.eventapp.GetterSetter.Header;

import java.util.List;

public class QuizListing {

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

    @SerializedName("header")
    @Expose
    List<Header> header;

    @SerializedName("detail")
    @Expose
    String detail;

    public Detaildecrypt getDetailpreencrypt() {
        return detailpreencrypt;
    }

    public void setDetailpreencrypt(Detaildecrypt detailpreencrypt) {
        this.detailpreencrypt = detailpreencrypt;
    }

    @SerializedName("detailpreencrypt")
    @Expose
    Detaildecrypt detailpreencrypt;

    @SerializedName("totalRecords")
    @Expose
    String totalRecords;

}
