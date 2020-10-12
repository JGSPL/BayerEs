package com.procialize.eventapp.ui.livepoll.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Logo {
    @SerializedName("logo_url_path")
    @Expose
    private String logo_url_path;

    @SerializedName("live_poll_logo")
    @Expose
    private LivePoll_logo live_poll_logo;

    @SerializedName("LivePoll")
    @Expose
    List<LivePoll> LivePoll_list;

    public List<LivePoll> getLivePoll_list() {
        return LivePoll_list;
    }

    public void setLivePoll_list(List<LivePoll> livePoll_list) {
        LivePoll_list = livePoll_list;
    }

    public String getLogo_url_path() {
        return logo_url_path;
    }

    public void setLogo_url_path(String logo_url_path) {
        this.logo_url_path = logo_url_path;
    }

    public LivePoll_logo getLive_poll_logo() {
        return live_poll_logo;
    }

    public void setLive_poll_logo(LivePoll_logo live_poll_logo) {
        this.live_poll_logo = live_poll_logo;
    }
}
