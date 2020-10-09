package com.procialize.eventapp.ui.agenda.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Agenda implements Serializable {

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

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public void setSession_name(String session_name) {
        this.session_name = session_name;
    }

    public void setSession_short_description(String session_short_description) {
        this.session_short_description = session_short_description;
    }

    public void setSession_description(String session_description) {
        this.session_description = session_description;
    }

    public void setSession_start_time(String session_start_time) {
        this.session_start_time = session_start_time;
    }

    public void setSession_end_time(String session_end_time) {
        this.session_end_time = session_end_time;
    }

    public void setSession_date(String session_date) {
        this.session_date = session_date;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public void setLivestream_link(String livestream_link) {
        this.livestream_link = livestream_link;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public void setTotal_feedback(String total_feedback) {
        this.total_feedback = total_feedback;
    }

    public void setFeedback_comment(String feedback_comment) {
        this.feedback_comment = feedback_comment;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }
}
