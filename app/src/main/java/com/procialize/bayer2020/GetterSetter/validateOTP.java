package com.procialize.bayer2020.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class validateOTP {


    @SerializedName("header")
    @Expose
    List<Header> header;

    @SerializedName("token")
    @Expose
    private String token;

    public List<Header> getHeader() {
        return header;
    }

    public void setHeader(List<Header> header) {
        this.header = header;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
