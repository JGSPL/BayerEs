package com.procialize.bayer2020.ui.catalogue.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Product_item implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("event_id")
    @Expose
    private String event_id;


    @SerializedName("product_type_id")
    @Expose
    private String product_type_id;

    @SerializedName("product_name")
    @Expose
    private String product_name;

    @SerializedName("product_thumb_image")
    @Expose
    private String product_thumb_image;
    @SerializedName("product_image")
    @Expose
    private String product_image;
    @SerializedName("product_short_description")
    @Expose
    private String product_short_description;

    @SerializedName("product_long_description")
    @Expose
    private String product_long_description;

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

    public String getProduct_type_id() {
        return product_type_id;
    }

    public void setProduct_type_id(String product_type_id) {
        this.product_type_id = product_type_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_thumb_image() {
        return product_thumb_image;
    }

    public void setProduct_thumb_image(String product_thumb_image) {
        this.product_thumb_image = product_thumb_image;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_short_description() {
        return product_short_description;
    }

    public void setProduct_short_description(String product_short_description) {
        this.product_short_description = product_short_description;
    }

    public String getProduct_long_description() {
        return product_long_description;
    }

    public void setProduct_long_description(String product_long_description) {
        this.product_long_description = product_long_description;
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
