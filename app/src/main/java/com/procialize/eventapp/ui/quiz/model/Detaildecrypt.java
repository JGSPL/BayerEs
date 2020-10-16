package com.procialize.eventapp.ui.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Detaildecrypt {

    @SerializedName("logo_url_path")
    @Expose
    String logo_url_path;

    @SerializedName("quiz_logo")
    @Expose
    QuizLogo quiz_logo;

    @SerializedName("quiz_list")
    @Expose
    List<QuizList> quiz_list;

    public String getLogo_url_path() {
        return logo_url_path;
    }

    public void setLogo_url_path(String logo_url_path) {
        this.logo_url_path = logo_url_path;
    }

    public QuizLogo getQuiz_logo() {
        return quiz_logo;
    }

    public void setQuiz_logo(QuizLogo quiz_logo) {
        this.quiz_logo = quiz_logo;
    }

    public List<QuizList> getQuiz_list() {
        return quiz_list;
    }

    public void setQuiz_list(List<QuizList> quiz_list) {
        this.quiz_list = quiz_list;
    }
}
