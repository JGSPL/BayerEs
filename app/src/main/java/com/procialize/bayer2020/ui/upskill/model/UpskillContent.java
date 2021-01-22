package com.procialize.bayer2020.ui.upskill.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.bayer2020.GetterSetter.Header;

import java.util.List;

public class UpskillContent {

    @SerializedName("header")
    @Expose
    List<Header> header;
 @SerializedName("detail")
    @Expose
    String detail;

   /* @SerializedName("mainInfo")
    @Expose
    MainInfo mainInfo;

    @SerializedName("contentInfo")
    @Expose
    List<ContentInfo> contentInfo;*/

    public List<Header> getHeader() {
        return header;
    }

    public String getDetail() {
        return detail;
    }

    /* public MainInfo getMainInfo() {
        return mainInfo;
    }

    public List<ContentInfo> getContentInfo() {
        return contentInfo;
    }*/
}
