package com.procialize.bayer2020.ui.upskill.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UpskillContentSubArray implements Serializable {

    @SerializedName("mainInfo")
    @Expose
    MainInfo mainInfo;

    @SerializedName("contentInfo")
    @Expose
    List<ContentInfo> contentInfo;

    public MainInfo getMainInfo() {
        return mainInfo;
    }

    public List<ContentInfo> getContentInfo() {
        return contentInfo;
    }
}
