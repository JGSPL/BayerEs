package com.procialize.bayer2020.ui.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuizList {

    public List<QuizQuestion> getQuiz_question() {
        return quiz_question;
    }

    public void setQuiz_question(List<QuizQuestion> quiz_question) {
        this.quiz_question = quiz_question;
    }

    @SerializedName("quiz_question")
    @Expose
    List<QuizQuestion> quiz_question;

    @SerializedName("answered")
    @Expose
    String answered;

    @SerializedName("folder_name")
    @Expose
    String folder_name;

    @SerializedName("folder_id")
    @Expose
    String folder_id;

    @SerializedName("quiz_id")
    @Expose
    String quiz_id;

    @SerializedName("total_quiz")
    @Expose
    String total_quiz;

    public String getAnswered() {
        return answered;
    }

    public void setAnswered(String answered) {
        this.answered = answered;
    }

    public String getTotal_quiz() {
        return total_quiz;
    }

    public void setTotal_quiz(String total_quiz) {
        this.total_quiz = total_quiz;
    }

    public String getTotal_correct() {
        return total_correct;
    }

    public void setTotal_correct(String total_correct) {
        this.total_correct = total_correct;
    }

    @SerializedName("total_correct")
    @Expose
    String total_correct;

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    @SerializedName("timer")
    @Expose
    String timer;


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
}
