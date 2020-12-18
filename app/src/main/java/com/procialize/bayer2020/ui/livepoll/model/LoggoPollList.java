package com.procialize.bayer2020.ui.livepoll.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoggoPollList {
    @SerializedName("live_poll_logo")
    @Expose
    List<Logo> logo_list;
    @SerializedName("LivePoll")
    @Expose
    List<LivePoll> LivePoll_list;

    public List<Logo> getLogo_list() {
        return logo_list;
    }

    public void setLogo_list(List<Logo> logo_list) {
        this.logo_list = logo_list;
    }

    public List<LivePoll> getLivePoll_list() {
        return LivePoll_list;
    }

    public void setLivePoll_list(List<LivePoll> livePoll_list) {
        LivePoll_list = livePoll_list;
    }
}
