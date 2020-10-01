package com.procialize.eventapp.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.eventapp.ui.newsfeed.model.Live_stream_info;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;
import com.procialize.eventapp.ui.newsfeed.model.Zoom_info;

import java.util.List;

public class validateOTP {


    @SerializedName("header")
    @Expose
    List<Header> header;

    @SerializedName("token")
    @Expose
    private String token;

/*    @SerializedName("tokenpreenrypt")
    @Expose
    private String tokenpreenrypt;*/
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

    /*public String getTokenpreenrypt() {
        return tokenpreenrypt;
    }

    public void setTokenpreenrypt(String tokenpreenrypt) {
        this.tokenpreenrypt = tokenpreenrypt;
    }*/
}
