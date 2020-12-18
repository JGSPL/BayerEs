package com.procialize.bayer2020.ui.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.bayer2020.GetterSetter.Header;

import java.util.List;

public class QuizSubmit {

    public List<Header> getHeader() {
        return header;
    }

    public void setHeader(List<Header> header) {
        this.header = header;
    }

    public String getTotal_correct_answer() {
        return total_correct_answer;
    }

    public void setTotal_correct_answer(String total_correct_answer) {
        this.total_correct_answer = total_correct_answer;
    }

    @SerializedName("header")
    @Expose
    List<Header> header;

    @SerializedName("total_correct_answer")
    @Expose
    String total_correct_answer;

    public String getTotal_questions() {
        return total_questions;
    }

    public void setTotal_questions(String total_questions) {
        this.total_questions = total_questions;
    }

    @SerializedName("total_questions")
    @Expose
    String total_questions;
}
