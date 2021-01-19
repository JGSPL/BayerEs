package com.procialize.bayer2020.ui.upskill.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpskillList {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cover_img")
    @Expose
    private String cover_img;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("multiple_attempts")
    @Expose
    private String multiple_attempts;
    @SerializedName("external_link")
    @Expose
    private String external_link;
    @SerializedName("attended")
    @Expose
    private String attended;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCover_img() {
        return cover_img;
    }

    public String getDescription() {
        return description;
    }

    public String getMultiple_attempts() {
        return multiple_attempts;
    }

    public String getExternal_link() {
        return external_link;
    }

    public String getAttended() {
        return attended;
    }
}
