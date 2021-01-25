package com.procialize.bayer2020.ui.loyalityleap.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class My_point {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("user_id")
    @Expose
    private String user_id;


    @SerializedName("mypoint")
    @Expose
    private String mypoint;

    @SerializedName("rank")
    @Expose
    private String rank;

    @SerializedName("schemeUnreadCount")
    @Expose
    private String schemeUnreadCount;

    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMypoint() {
        return mypoint;
    }

    public void setMypoint(String mypoint) {
        this.mypoint = mypoint;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getSchemeUnreadCount() {
        return schemeUnreadCount;
    }

    public void setSchemeUnreadCount(String schemeUnreadCount) {
        this.schemeUnreadCount = schemeUnreadCount;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }
}
