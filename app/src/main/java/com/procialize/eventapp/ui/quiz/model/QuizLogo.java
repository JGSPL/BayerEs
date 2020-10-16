package com.procialize.eventapp.ui.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuizLogo {

    @SerializedName("app_quiz_logo")
    @Expose
    String app_quiz_logo;

    public String getApp_quiz_logo() {
        return app_quiz_logo;
    }

    public void setApp_quiz_logo(String app_quiz_logo) {
        this.app_quiz_logo = app_quiz_logo;
    }

    public String getWeb_quiz_logo() {
        return web_quiz_logo;
    }

    public void setWeb_quiz_logo(String web_quiz_logo) {
        this.web_quiz_logo = web_quiz_logo;
    }

    @SerializedName("web_quiz_logo")
    @Expose
    String web_quiz_logo;

}
