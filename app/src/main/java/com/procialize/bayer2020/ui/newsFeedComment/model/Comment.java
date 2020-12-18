package com.procialize.bayer2020.ui.newsFeedComment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.bayer2020.GetterSetter.Header;

import java.util.List;

public class Comment {

    @SerializedName("header")
    @Expose
    List<Header> header;

/*    @SerializedName("detailpreencrypt")
    @Expose
    List<CommentDetail> commentDetails;*/

    @SerializedName("total_records")
    @Expose
    String total_records;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @SerializedName("detail")
    @Expose
    String detail;

    public List<Header> getHeader() {
        return header;
    }

    public void setHeader(List<Header> header) {
        this.header = header;
    }

   /* public List<CommentDetail> getCommentDetails() {
        return commentDetails;
    }

    public void setCommentDetails(List<CommentDetail> commentDetails) {
        this.commentDetails = commentDetails;
    }
*/
    public String getTotal_records() {
        return total_records;
    }

    public void setTotal_records(String total_records) {
        this.total_records = total_records;
    }
}
