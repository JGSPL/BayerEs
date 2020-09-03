package com.procialize.eventapp.ui.newsFeedComment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentDetail {

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
}
