package com.procialize.bayer2020.ui.catalogue.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CataloguePestRecommendedProducts implements Serializable {

    @SerializedName("pest_id")
    @Expose
    private String pest_id;
    @SerializedName("product_id")
    @Expose
    private String product_id;
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

    public String getPest_id() {
        return pest_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getId() {
        return id;
    }

    public String getEvent_id() {
        return event_id;
    }

    public String getProduct_type_id() {
        return product_type_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_thumb_image() {
        return product_thumb_image;
    }

    public String getProduct_image() {
        return product_image;
    }

    public String getProduct_short_description() {
        return product_short_description;
    }

    public String getProduct_long_description() {
        return product_long_description;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public String getCreated() {
        return created;
    }

    public String getModified() {
        return modified;
    }
}
