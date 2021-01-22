package com.procialize.bayer2020.ui.upskill.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QuizOption implements Serializable {

    @SerializedName("option_id")
    @Expose
    String option_id;
    @SerializedName("quiz_id")
    @Expose
    String quiz_id;
    @SerializedName("option")
    @Expose
    String option;

    public String getOption_id() {
        return option_id;
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public String getOption() {
        return option;
    }
}
