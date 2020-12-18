package com.procialize.bayer2020.ui.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuizOption {

    @SerializedName("quiz_id")
    @Expose
    String quiz_id;
    @SerializedName("option_id")
    @Expose
    String option_id;

    public String getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(String quiz_id) {
        this.quiz_id = quiz_id;
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

    @SerializedName("option")
    @Expose
    String option;
}


