package com.procialize.eventapp.ui.newsFeedComment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.eventapp.GetterSetter.Header;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;

import java.util.List;

public class Comment {

    @SerializedName("header")
    @Expose
    List<Header> header;

    @SerializedName("detailpreencrypt")
    @Expose
    List<CommentDetail> commentDetails; @

    SerializedName("detailpreencrypt")
    @Expose
    String total_records;

    public List<Header> getHeader() {
        return header;
    }

    public void setHeader(List<Header> header) {
        this.header = header;
    }

    public List<CommentDetail> getCommentDetails() {
        return commentDetails;
    }

    public void setCommentDetails(List<CommentDetail> commentDetails) {
        this.commentDetails = commentDetails;
    }

    public String getTotal_records() {
        return total_records;
    }

    public void setTotal_records(String total_records) {
        this.total_records = total_records;
    }
}
