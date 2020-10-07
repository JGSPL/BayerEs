package com.procialize.eventapp.ui.AgendaDetails.model;

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

}
