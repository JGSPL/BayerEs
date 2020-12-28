package com.procialize.bayer2020.ui.catalogue.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Pest_item implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("event_id")
    @Expose
    private String event_id;


    @SerializedName("pest_name")
    @Expose
    private String pest_name;

    @SerializedName("pest_image")
    @Expose
    private String pest_image;

    @SerializedName("pest_short_description")
    @Expose
    private String pest_short_description;
    @SerializedName("pest_long_description")
    @Expose
    private String pest_long_description;

    @SerializedName("is_delete")
    @Expose
    private String is_delete;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getPest_name() {
        return pest_name;
    }

    public void setPest_name(String pest_name) {
        this.pest_name = pest_name;
    }

    public String getPest_image() {
        return pest_image;
    }

    public void setPest_image(String pest_image) {
        this.pest_image = pest_image;
    }

    public String getPest_short_description() {
        return pest_short_description;
    }

    public void setPest_short_description(String pest_short_description) {
        this.pest_short_description = pest_short_description;
    }

    public String getPest_long_description() {
        return pest_long_description;
    }

    public void setPest_long_description(String pest_long_description) {
        this.pest_long_description = pest_long_description;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }
}
