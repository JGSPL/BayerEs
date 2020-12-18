package com.procialize.bayer2020.ui.newsFeedComment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.bayer2020.GetterSetter.Header;

import java.util.List;

public class LikePost {

    @SerializedName("header")
    @Expose
    List<Header> header;

    @SerializedName("like_status")
    @Expose
    String like_status;

    public List<Header> getHeader() {
        return header;
    }

    public String getLike_status() {
        return like_status;
    }
}
