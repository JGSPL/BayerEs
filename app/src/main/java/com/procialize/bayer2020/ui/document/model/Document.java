package com.procialize.bayer2020.ui.document.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.bayer2020.GetterSetter.Header;

import java.util.List;

public class Document {

    @SerializedName("header")
    @Expose
    public List<com.procialize.bayer2020.GetterSetter.Header> header;

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

    @SerializedName("detail")
    @Expose
    private String detail;

    @SerializedName("document_path")
    @Expose
    private String document_path;

    public String getDocument_path() {
        return document_path;
    }
}
