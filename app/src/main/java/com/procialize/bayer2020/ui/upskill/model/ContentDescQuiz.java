package com.procialize.bayer2020.ui.upskill.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ContentDescQuiz implements Serializable {

    @SerializedName("folder_id")
    @Expose
    String folder_id;

    @SerializedName("folder_name")
    @Expose
    String folder_name;

    @SerializedName("show_result")
    @Expose
    String show_result;

    @SerializedName("replied")
    @Expose
    String replied;

    @SerializedName("quiz_list")
    @Expose
    List<QuizList> quiz_list;

    public String getFolder_id() {
        return folder_id;
    }

    public String getFolder_name() {
        return folder_name;
    }

    public String getShow_result() {
        return show_result;
    }

    public String getReplied() {
        return replied;
    }

    public List<QuizList> getQuiz_list() {
        return quiz_list;
    }
}
