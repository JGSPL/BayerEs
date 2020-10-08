package com.procialize.eventapp.ui.agenda.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Agenda {

    @SerializedName("session_id")
    @Expose
    private String session_id;
    @SerializedName("session_name")
    @Expose
    private String session_name;
    @SerializedName("session_short_description")
    @Expose
    private String session_short_description;
    @SerializedName("session_description")
    @Expose
    private String session_description;
    @SerializedName("session_start_time")
    @Expose
    private String session_start_time;
    @SerializedName("session_end_time")
    @Expose
    private String session_end_time;
    @SerializedName("session_date")
    @Expose
    private String session_date;
    @SerializedName("event_id")
    @Expose
    private String event_id;
    @SerializedName("livestream_link")
    @Expose
    private String livestream_link;
    @SerializedName("star")
    @Expose
    private String star;
    @SerializedName("total_feedback")
    @Expose
    private String total_feedback;
    @SerializedName("feedback_comment")
    @Expose
    private String feedback_comment;
    @SerializedName("rated")
    @Expose
    private String rated;

    public String getSession_id() {
        return session_id;
    }

    public String getSession_name() {
        return session_name;
    }

    public String getSession_short_description() {
        return session_short_description;
    }

    public String getSession_description() {
        return session_description;
    }

    public String getSession_start_time() {
        return session_start_time;
    }

    public String getSession_end_time() {
        return session_end_time;
    }

    public String getSession_date() {
        return session_date;
    }

    public String getEvent_id() {
        return event_id;
    }

    public String getLivestream_link() {
        return livestream_link;
    }

    public String getStar() {
        return star;
    }

    public String getTotal_feedback() {
        return total_feedback;
    }

    public String getFeedback_comment() {
        return feedback_comment;
    }

    public String getRated() {
        return rated;
    }
}
