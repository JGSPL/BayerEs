package com.procialize.bayer2020.ui.loyalityleap.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class redeem_history_item implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("redemption_date")
    @Expose
    private String redemption_date;


    @SerializedName("user_id")
    @Expose
    private String user_id;

    @SerializedName("product_code")
    @Expose
    private String product_code;

    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("points")
    @Expose
    private String points;
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("is_delete")
    @Expose
    private String is_delete;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;

    @SerializedName("product_name")
    @Expose
    private String product_name;

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRedemption_date() {
        return redemption_date;
    }

    public void setRedemption_date(String redemption_date) {
        this.redemption_date = redemption_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
