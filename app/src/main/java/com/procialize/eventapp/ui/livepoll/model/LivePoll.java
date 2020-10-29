package com.procialize.eventapp.ui.livepoll.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.eventapp.ui.speaker.model.Speaker_Doc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LivePoll implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("question")
    @Expose
    private String question;


    @SerializedName("hide_result")
    @Expose
    private String hide_result;

    @SerializedName("replied")
    @Expose
    private String replied;
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("show_progress_bar")
    @Expose
    private String show_progress_bar;


    @SerializedName("live_poll_id")
    @Expose
    private String live_poll_id;

    @SerializedName("live_poll_option_list")
    @Expose
    ArrayList<LivePoll_option> live_poll_option_list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getHide_result() {
        return hide_result;
    }

    public void setHide_result(String hide_result) {
        this.hide_result = hide_result;
    }

    public String getReplied() {
        return replied;
    }

    public void setReplied(String replied) {
        this.replied = replied;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShow_progress_bar() {
        return show_progress_bar;
    }

    public void setShow_progress_bar(String show_progress_bar) {
        this.show_progress_bar = show_progress_bar;
    }

    public String getLive_poll_id() {
        return live_poll_id;
    }

    public void setLive_poll_id(String live_poll_id) {
        this.live_poll_id = live_poll_id;
    }

    public ArrayList<LivePoll_option> getLive_poll_option_list() {
        return live_poll_option_list;
    }

    public void setLive_poll_option_list(ArrayList<LivePoll_option> live_poll_option_list) {
        this.live_poll_option_list = live_poll_option_list;
    }
}

