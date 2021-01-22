package com.procialize.bayer2020.ui.upskill.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LivePollOption implements Serializable {

    @SerializedName("option_id")
    @Expose
    String option_id;
    @SerializedName("live_poll_id")
    @Expose
    String live_poll_id;
    @SerializedName("option")
    @Expose
    String option;
    @SerializedName("total_user")
    @Expose
    String total_user;

    public String getOption_id() {
        return option_id;
    }

    public String getLive_poll_id() {
        return live_poll_id;
    }

    public String getOption() {
        return option;
    }

    public String getTotal_user() {
        return total_user;
    }
}
