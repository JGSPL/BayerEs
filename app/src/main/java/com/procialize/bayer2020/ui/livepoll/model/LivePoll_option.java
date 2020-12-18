package com.procialize.bayer2020.ui.livepoll.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LivePoll_option implements Serializable {
    @SerializedName("live_poll_id")
    @Expose
    private String live_poll_id;

    @SerializedName("option_id")
    @Expose
    private String option_id;


    @SerializedName("option")
    @Expose
    private String option;

    @SerializedName("total_user")
    @Expose
    private String total_user;

    public String getLive_poll_id() {
        return live_poll_id;
    }

    public void setLive_poll_id(String live_poll_id) {
        this.live_poll_id = live_poll_id;
    }

    public String getOption_id() {
        return option_id;
    }

    public void setOption_id(String option_id) {
        this.option_id = option_id;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getTotal_user() {
        return total_user;
    }

    public void setTotal_user(String total_user) {
        this.total_user = total_user;
    }
}
