package com.procialize.bayer2020.ui.notification.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationList {

    @SerializedName("notification_id")
    @Expose
    private String notification_id;
    @SerializedName("parent_id")
    @Expose
    private String parent_id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("media_type")
    @Expose
    private String media_type;
    @SerializedName("media_url")
    @Expose
    private String media_url;
    @SerializedName("first_name")
    @Expose
    private String first_name;
    @SerializedName("last_name")
    @Expose
    private String last_name;
    @SerializedName("profile_pic")
    @Expose
    private String profile_pic;
    @SerializedName("company_name")
    @Expose
    private String company_name;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("datetime")
    @Expose
    private String datetime;

    public String getDatetime() {
        return datetime;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getMedia_type() {
        return media_type;
    }

    public String getMedia_url() {
        return media_url;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getDesignation() {
        return designation;
    }
}
