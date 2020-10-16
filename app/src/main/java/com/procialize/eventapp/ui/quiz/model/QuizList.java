package com.procialize.eventapp.ui.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuizList {

    @SerializedName("quiz_question")
    @Expose
    QuizQuestion quiz_question;

    @SerializedName("folder_name")
    @Expose
    String folder_name;

    @SerializedName("folder_id")
    @Expose
    String folder_id;

    @SerializedName("quiz_id")
    @Expose
    String quiz_id;

    public QuizQuestion getQuiz_question() {
        return quiz_question;
    }

    public void setQuiz_question(QuizQuestion quiz_question) {
        this.quiz_question = quiz_question;
    }

    public String getFolder_name() {
        return folder_name;
    }

    public void setFolder_name(String folder_name) {
        this.folder_name = folder_name;
    }

    public String getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(String folder_id) {
        this.folder_id = folder_id;
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(String quiz_id) {
        this.quiz_id = quiz_id;
    }

    public List<QuizOption> getQuiz_option() {
        return quiz_option;
    }

    public void setQuiz_option(List<QuizOption> quiz_option) {
        this.quiz_option = quiz_option;
    }

    @SerializedName("quiz_option")
    @Expose
    List<QuizOption> quiz_option;

}
