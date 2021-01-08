package com.procialize.bayer2020.ui.loyalityleap.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FetchRedeemStatusBasicData {

    @SerializedName("basicdata")
    @Expose
    List<redeem_history_item> redeemHistoryList;

    @SerializedName("status")
    @Expose
    List<redeem_history_status_item> redeemHistoryStatusList;

    public List<redeem_history_item> getRedeemHistoryList() {
        return redeemHistoryList;
    }

    public void setRedeemHistoryList(List<redeem_history_item> redeemHistoryList) {
        this.redeemHistoryList = redeemHistoryList;
    }

    public List<redeem_history_status_item> getRedeemHistoryStatusList() {
        return redeemHistoryStatusList;
    }

    public void setRedeemHistoryStatusList(List<redeem_history_status_item> redeemHistoryStatusList) {
        this.redeemHistoryStatusList = redeemHistoryStatusList;
    }
}
