package com.procialize.bayer2020.ui.upskill.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.bayer2020.GetterSetter.Header;

import java.util.List;

public class UpSkill {

    @SerializedName("app_upskill_logo")
    @Expose
    String app_upskill_logo;

    @SerializedName("app_upskill_description")
    @Expose
    String app_upskill_description;

    @SerializedName("trainingList")
    @Expose
    List<UpskillList> trainingList;

    public String getApp_upskill_logo() {
        return app_upskill_logo;
    }

    public String getApp_upskill_description() {
        return app_upskill_description;
    }

    public List<UpskillList> getTrainingList() {
        return trainingList;
    }
}