package com.procialize.bayer2020.ui.upskill.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MainInfo implements Serializable {

    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("cover_img")
    @Expose
    String cover_img;
    @SerializedName("description")
    @Expose
    String description;
    @SerializedName("multiple_attempts")
    @Expose
    String multiple_attempts;
    @SerializedName("all_employee")
    @Expose
    String all_employee;
    @SerializedName("external_link")
    @Expose
    String external_link;
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("active_once")
    @Expose
    String active_once;
    @SerializedName("created_at")
    @Expose
    String created_at;
    @SerializedName("updated_at")
    @Expose
    String updated_at;

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

    public String getAll_employee() {
        return all_employee;
    }

    public String getExternal_link() {
        return external_link;
    }

    public String getStatus() {
        return status;
    }

    public String getActive_once() {
        return active_once;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
