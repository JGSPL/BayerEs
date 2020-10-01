package com.procialize.eventapp.ui.newsFeedLike.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.eventapp.GetterSetter.Header;

import java.util.List;

public class Like {

    @SerializedName("header")
    @Expose
    List<Header> header;

/*    @SerializedName("detailpreencrypt")
    @Expose
    List<LikeDetail> likeDetails;*/

    @SerializedName("total_records")
    @Expose
    String total_records;

    @SerializedName("detail")
    @Expose
    String detail;

    public List<Header> getHeader() {
        return header;
    }

    public void setHeader(List<Header> header) {
        this.header = header;
    }

/*    public List<LikeDetail> getLikeDetails() {
        return likeDetails;
    }

    public void setLikeDetails(List<LikeDetail> likeDetails) {
        this.likeDetails = likeDetails;
    }*/

    public String getTotal_records() {
        return total_records;
    }

    public void setTotal_records(String total_records) {
        this.total_records = total_records;
    }

    public String getDetail() {
        return detail;
    }
}
