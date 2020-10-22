package com.procialize.eventapp.ui.spotQnA.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.eventapp.ui.agenda.model.Agenda;

import java.util.List;

public class SpotQnAList {

    @SerializedName("session_question_list")
    @Expose
    List<SpotQnA> session_question_list;

    public List<SpotQnA> getSession_question_list() {
        return session_question_list;
    }
}
