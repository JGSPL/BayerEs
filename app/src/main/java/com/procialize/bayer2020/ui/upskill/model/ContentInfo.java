package com.procialize.bayer2020.ui.upskill.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ContentInfo implements Serializable {

    @SerializedName("training_id")
    @Expose
    String training_id;
    @SerializedName("content_id")
    @Expose
    String content_id;
    @SerializedName("content_type")
    @Expose
    String content_type;
    @SerializedName("content_name")
    @Expose
    String content_name;
    @SerializedName("link")
    @Expose
    String link;
    @SerializedName("content_desc")
    @Expose
    String content_desc;
    @SerializedName("content_url")
    @Expose
    String content_url;
    @SerializedName("content_desc_poll")
    @Expose
    List<ContentDescLivePoll> content_desc_poll;
    @SerializedName("content_desc_quiz")
    @Expose
    List<ContentDescQuiz> content_desc_quiz;

    public String getTraining_id() {
        return training_id;
    }

    public String getContent_id() {
        return content_id;
    }

    public String getContent_type() {
        return content_type;
    }

    public String getContent_name() {
        return content_name;
    }

    public String getLink() {
        return link;
    }

    public List<ContentDescLivePoll> getContent_desc_poll() {
        return content_desc_poll;
    }

    public String getContent_desc() {
        return content_desc;
    }

    public List<ContentDescQuiz> getContent_desc_quiz() {
        return content_desc_quiz;
    }

    public String getContent_url() {
        return content_url;
    }
}
