package com.procialize.eventapp.ui.newsfeed.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Zoom_info {

    @SerializedName("zoom_meeting_id")
    @Expose
    private String zoom_meeting_id;

    @SerializedName("zoom_password")
    @Expose
    private String zoom_password;

    @SerializedName("zoom_status")
    @Expose
    private String zoom_status;
    @SerializedName("zoom_datetime")
    @Expose
    private String zoom_datetime;

    public String getZoom_meeting_id() {
        return zoom_meeting_id;
    }

    public void setZoom_meeting_id(String zoom_meeting_id) {
        this.zoom_meeting_id = zoom_meeting_id;
    }

    public String getZoom_password() {
        return zoom_password;
    }

    public void setZoom_password(String zoom_password) {
        this.zoom_password = zoom_password;
    }

    public String getZoom_status() {
        return zoom_status;
    }

    public void setZoom_status(String zoom_status) {
        this.zoom_status = zoom_status;
    }

    public String getZoom_datetime() {
        return zoom_datetime;
    }

    public void setZoom_datetime(String zoom_datetime) {
        this.zoom_datetime = zoom_datetime;
    }
}
