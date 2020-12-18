package com.procialize.bayer2020.ui.newsFeedComment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentDetail {

    @SerializedName("commented_user_id")
    @Expose
    private String user_id;
    @SerializedName("comment_id")
    @Expose
    private String comment_id;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("first_name")
    @Expose
    private String first_name;
    @SerializedName("last_name")
    @Expose
    private String last_name;
    @SerializedName("profile_picture")
    @Expose
    private String profile_picture;

    public String getDateTime() {
        return dateTime;
    }

    @SerializedName("dateTime")
    @Expose
    private String dateTime;

    public String getComment_id() {
        return comment_id;
    }

    public String getComment() {
        return comment;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
