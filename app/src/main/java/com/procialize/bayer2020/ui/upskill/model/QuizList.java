package com.procialize.bayer2020.ui.upskill.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class QuizList implements Serializable {

    @SerializedName("quiz_id")
    @Expose
    String quiz_id;
    @SerializedName("question")
    @Expose
    String question;
    @SerializedName("correct_answer")
    @Expose
    String correct_answer;
    @SerializedName("replied")
    @Expose
    String replied;
    @SerializedName("selected_option_id")
    @Expose
    String selected_option_id;
    @SerializedName("selected_option")
    @Expose
    String selected_option;
    @SerializedName("option")
    @Expose
    List<QuizOption> option;

    public String getQuiz_id() {
        return quiz_id;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public String getReplied() {
        return replied;
    }

    public String getSelected_option_id() {
        return selected_option_id;
    }

    public String getSelected_option() {
        return selected_option;
    }

    public List<QuizOption> getOption() {
        return option;
    }
}
