package com.procialize.eventapp.ui.eventList.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.eventapp.GetterSetter.Header;

import java.util.List;

public class UpdateDeviceInfo {
    @SerializedName("header")
    @Expose
    List<Header> header;

/*    @SerializedName("detailpreencrypt")
    @Expose
    List<LoginUserInfo> LoginUserInfoList;*/

    @SerializedName("detail")
    @Expose
    String detail;

    public List<Header> getHeader() {
        return header;
    }

    public void setHeader(List<Header> header) {
        this.header = header;
    }

   /* public List<LoginUserInfo> getLoginUserInfoList() {
        return LoginUserInfoList;
    }

    public void setLoginUserInfoList(List<LoginUserInfo> loginUserInfoList) {
        LoginUserInfoList = loginUserInfoList;
    }*/

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
