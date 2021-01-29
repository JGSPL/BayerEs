package com.procialize.bayer2020.ui.catalogue.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class product_dosage_detail implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("product_id")
    @Expose
    private String product_id;


    @SerializedName("content_of_product")
    @Expose
    private String content_of_product;

    @SerializedName("content_unit")
    @Expose
    private String content_unit;

    @SerializedName("infestation_level")
    @Expose
    private String infestation_level;

    @SerializedName("any_deposition_rate")
    @Expose
    private String any_deposition_rate;


    @SerializedName("low_deposition_rate")
    @Expose
    private String low_deposition_rate;

    @SerializedName("high_deposition_rate")
    @Expose
    private String high_deposition_rate;

    @SerializedName("deposition_unit")
    @Expose
    private String deposition_unit;

    @SerializedName("any_amount")
    @Expose
    private String any_amount;

    @SerializedName("low_amount")
    @Expose
    private String low_amount;


    @SerializedName("high_amount")
    @Expose
    private String high_amount;

    @SerializedName("amount_unit")
    @Expose
    private String amount_unit;

    @SerializedName("area_to_be_treated")
    @Expose
    private String area_to_be_treated;

    @SerializedName("area_to_be_treated_unit")
    @Expose
    private String area_to_be_treated_unit;

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

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getContent_of_product() {
        return content_of_product;
    }

    public void setContent_of_product(String content_of_product) {
        this.content_of_product = content_of_product;
    }

    public String getContent_unit() {
        return content_unit;
    }

    public void setContent_unit(String content_unit) {
        this.content_unit = content_unit;
    }

    public String getInfestation_level() {
        return infestation_level;
    }

    public void setInfestation_level(String infestation_level) {
        this.infestation_level = infestation_level;
    }

    public String getAny_deposition_rate() {
        return any_deposition_rate;
    }

    public void setAny_deposition_rate(String any_deposition_rate) {
        this.any_deposition_rate = any_deposition_rate;
    }

    public String getLow_deposition_rate() {
        return low_deposition_rate;
    }

    public void setLow_deposition_rate(String low_deposition_rate) {
        this.low_deposition_rate = low_deposition_rate;
    }

    public String getHigh_deposition_rate() {
        return high_deposition_rate;
    }

    public void setHigh_deposition_rate(String high_deposition_rate) {
        this.high_deposition_rate = high_deposition_rate;
    }

    public String getDeposition_unit() {
        return deposition_unit;
    }

    public void setDeposition_unit(String deposition_unit) {
        this.deposition_unit = deposition_unit;
    }

    public String getAny_amount() {
        return any_amount;
    }

    public void setAny_amount(String any_amount) {
        this.any_amount = any_amount;
    }

    public String getLow_amount() {
        return low_amount;
    }

    public void setLow_amount(String low_amount) {
        this.low_amount = low_amount;
    }

    public String getHigh_amount() {
        return high_amount;
    }

    public void setHigh_amount(String high_amount) {
        this.high_amount = high_amount;
    }

    public String getAmount_unit() {
        return amount_unit;
    }

    public void setAmount_unit(String amount_unit) {
        this.amount_unit = amount_unit;
    }

    public String getArea_to_be_treated() {
        return area_to_be_treated;
    }

    public void setArea_to_be_treated(String area_to_be_treated) {
        this.area_to_be_treated = area_to_be_treated;
    }

    public String getArea_to_be_treated_unit() {
        return area_to_be_treated_unit;
    }

    public void setArea_to_be_treated_unit(String area_to_be_treated_unit) {
        this.area_to_be_treated_unit = area_to_be_treated_unit;
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
