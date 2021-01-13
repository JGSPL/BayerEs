package com.procialize.bayer2020.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class validateOTP {


    @SerializedName("header")
    @Expose
    List<Header> header;
    @SerializedName("tokenorgdata")
    @Expose
    User UserData;


    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("tokenpreenrypt")
    @Expose
    private String tokenpreenrypt;

    public String getTokenpreenrypt() {
        return tokenpreenrypt;
    }

    public void setTokenpreenrypt(String tokenpreenrypt) {
        this.tokenpreenrypt = tokenpreenrypt;
    }

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

    public User getUserData() {
        return UserData;
    }

    public void setUserData(User userData) {
        UserData = userData;
    }
}
