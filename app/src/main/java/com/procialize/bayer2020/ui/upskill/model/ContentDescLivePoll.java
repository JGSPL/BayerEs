package com.procialize.bayer2020.ui.upskill.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ContentDescLivePoll implements Serializable {

    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("question")
    @Expose
    String question;
    @SerializedName("show_result")
    @Expose
    String show_result;
    @SerializedName("replied")
    @Expose
    String replied;
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("show_progress_bar")
    @Expose
    String show_progress_bar;
    @SerializedName("live_poll_id")
    @Expose
    String live_poll_id;
    @SerializedName("option")
    @Expose
    List<LivePollOption> option;

    public String getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getShow_result() {
        return show_result;
    }

    public String getReplied() {
        return replied;
    }

    public String getStatus() {
        return status;
    }

    public String getShow_progress_bar() {
        return show_progress_bar;
    }

    public String getLive_poll_id() {
        return live_poll_id;
    }

    public List<LivePollOption> getOption() {
        return option;
    }
}
