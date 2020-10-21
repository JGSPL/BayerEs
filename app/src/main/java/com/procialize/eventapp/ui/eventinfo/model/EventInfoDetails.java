package com.procialize.eventapp.ui.eventinfo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventInfoDetails {

    @SerializedName("event_id")
    @Expose
    private String event_id;
    @SerializedName("event_name")
    @Expose
    private String event_name;
    @SerializedName("event_start_date")
    @Expose
    private String event_start_date;
    @SerializedName("event_end_date")
    @Expose
    private String event_end_date;
    @SerializedName("event_description")
    @Expose
    private String event_description;
    @SerializedName("event_location")
    @Expose
    private String event_location;
    @SerializedName("event_city")
    @Expose
    private String event_city;
    @SerializedName("event_latitude")
    @Expose
    private String event_latitude;
    @SerializedName("event_longitude")
    @Expose
    private String event_longitude;
    @SerializedName("event_image")
    @Expose
    private String event_image;
    @SerializedName("header_image")
    @Expose
    private String header_image;
    @SerializedName("background_image")
    @Expose
    private String background_image;
    @SerializedName("event_cover_image")
    @Expose
    private String event_cover_image;

    public String getEvent_cover_image() {
        return event_cover_image;
    }


    public String getEvent_id() {
        return event_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getEvent_start_date() {
        return event_start_date;
    }

    public String getEvent_end_date() {
        return event_end_date;
    }

    public String getEvent_description() {
        return event_description;
    }

    public String getEvent_location() {
        return event_location;
    }

    public String getEvent_city() {
        return event_city;
    }

    public String getEvent_latitude() {
        return event_latitude;
    }

    public String getEvent_longitude() {
        return event_longitude;
    }

    public String getEvent_image() {
        return event_image;
    }

    public String getHeader_image() {
        return header_image;
    }

    public String getBackground_image() {
        return background_image;
    }
}
