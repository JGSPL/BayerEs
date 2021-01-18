package com.procialize.bayer2020.ui.survey.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Survey {

    @SerializedName("survey_id")
    @Expose
    private String survey_id;
    @SerializedName("survey_name")
    @Expose
    private String survey_name;
    @SerializedName("survey_url")
    @Expose
    private String survey_url;

    public String getSurvey_id() {
        return survey_id;
    }

    public String getSurvey_name() {
        return survey_name;
    }

    public String getSurvey_url() {
        return survey_url;
    }
}
