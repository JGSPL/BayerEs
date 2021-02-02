package com.procialize.bayer2020.ui.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class QuizQuestion implements Serializable {

    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("question")
    @Expose
    String question;
    @SerializedName("correct_answer_id")
    @Expose
    String correct_answer_id;
    @SerializedName("folder_id")
    @Expose
    String folder_id;
    @SerializedName("replied")
    @Expose
    String replied;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrect_answer_id() {
        return correct_answer_id;
    }

    public void setCorrect_answer_id(String correct_answer_id) {
        this.correct_answer_id = correct_answer_id;
    }

    public String getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(String folder_id) {
        this.folder_id = folder_id;
    }

    public String getReplied() {
        return replied;
    }

    public void setReplied(String replied) {
        this.replied = replied;
    }

    public String getSelected_option() {
        return selected_option;
    }

    public void setSelected_option(String selected_option) {
        this.selected_option = selected_option;
    }

    @SerializedName("selected_option")
    @Expose
    String selected_option;

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
