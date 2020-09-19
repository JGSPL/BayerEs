package com.procialize.eventapp.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class resendOTP {
    public List<Header> getHeader() {
        return header;
    }

    public void setHeader(List<Header> header) {
        this.header = header;
    }

    @SerializedName("header")
    @Expose
    List<Header> header;
}
