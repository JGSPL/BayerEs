package com.procialize.bayer2020.ui.loyalityleap.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RequestToRedeem implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("event_id")
    @Expose
    private String event_id;

    @SerializedName("product_image")
    @Expose
    private String product_image;

    @SerializedName("product_name")
    @Expose
    private String product_name;

    @SerializedName("product_code")
    @Expose
    private String product_code;

    @SerializedName("product_value")
    @Expose
    private String product_value;

    @SerializedName("is_delete")
    @Expose
    private String is_delete;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;

    @SerializedName("redeem_flag")
    @Expose
    private String redeem_flag;

    @SerializedName("redeem_status_line")
    @Expose
    private String redeem_status_line;

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

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_value() {
        return product_value;
    }

    public void setProduct_value(String product_value) {
        this.product_value = product_value;
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

    public String getRedeem_flag() {
        return redeem_flag;
    }

    public void setRedeem_flag(String redeem_flag) {
        this.redeem_flag = redeem_flag;
    }

    public String getRedeem_status_line() {
        return redeem_status_line;
    }

    public void setRedeem_status_line(String redeem_status_line) {
        this.redeem_status_line = redeem_status_line;
    }
}
