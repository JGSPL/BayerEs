package com.procialize.bayer2020.ui.catalogue.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductType implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("event_id")
    @Expose
    private String event_id;

    @SerializedName("product_type_name")
    @Expose
    private String product_type_name;

    @SerializedName("product_type_image")
    @Expose
    private String product_type_image;

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

    public String getProduct_type_name() {
        return product_type_name;
    }

    public void setProduct_type_name(String product_type_name) {
        this.product_type_name = product_type_name;
    }

    public String getProduct_type_image() {
        return product_type_image;
    }

    public void setProduct_type_image(String product_type_image) {
        this.product_type_image = product_type_image;
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
