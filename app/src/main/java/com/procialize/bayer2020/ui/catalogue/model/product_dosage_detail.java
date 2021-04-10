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

    @SerializedName("diluted_solution_quantity")
    @Expose
    private String diluted_solution_quantity;

    @SerializedName("diluted_solution_quantity_unit")
    @Expose
    private String diluted_solution_quantity_unit;

    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;

    @SerializedName("app_type1_deposition_rate")
    @Expose
    private String app_type1_deposition_rate;
    @SerializedName("app_type2_deposition_rate")
    @Expose
    private String app_type2_deposition_rate;
    @SerializedName("app_type3_deposition_rate")
    @Expose
    private String app_type3_deposition_rate;
    @SerializedName("app_type4_deposition_rate")
    @Expose
    private String app_type4_deposition_rate;
    @SerializedName("app_type5_deposition_rate")
    @Expose
    private String app_type5_deposition_rate;
    @SerializedName("app_type1_label")
    @Expose
    private String app_type1_label;

    @SerializedName("app_type2_label")
    @Expose
    private String app_type2_label;
    @SerializedName("app_type3_label")
    @Expose
    private String app_type3_label;
    @SerializedName("app_type4_label")
    @Expose
    private String app_type4_label;
    @SerializedName("app_type5_label")
    @Expose
    private String app_type5_label;

    @SerializedName("diluted_quantity_1")
    @Expose
    private String diluted_quantity_1;

    @SerializedName("diluted_quantity_2")
    @Expose
    private String diluted_quantity_2;
    @SerializedName("diluted_quantity_3")
    @Expose
    private String diluted_quantity_3;
    @SerializedName("diluted_quantity_4")
    @Expose
    private String diluted_quantity_4;
    @SerializedName("diluted_quantity_5")
    @Expose
    private String diluted_quantity_5;


    public String getApp_type1_deposition_rate() {
        return app_type1_deposition_rate;
    }

    public void setApp_type1_deposition_rate(String app_type1_deposition_rate) {
        this.app_type1_deposition_rate = app_type1_deposition_rate;
    }

    public String getApp_type2_deposition_rate() {
        return app_type2_deposition_rate;
    }

    public void setApp_type2_deposition_rate(String app_type2_deposition_rate) {
        this.app_type2_deposition_rate = app_type2_deposition_rate;
    }

    public String getApp_type3_deposition_rate() {
        return app_type3_deposition_rate;
    }

    public void setApp_type3_deposition_rate(String app_type3_deposition_rate) {
        this.app_type3_deposition_rate = app_type3_deposition_rate;
    }

    public String getApp_type4_deposition_rate() {
        return app_type4_deposition_rate;
    }

    public void setApp_type4_deposition_rate(String app_type4_deposition_rate) {
        this.app_type4_deposition_rate = app_type4_deposition_rate;
    }

    public String getApp_type5_deposition_rate() {
        return app_type5_deposition_rate;
    }

    public void setApp_type5_deposition_rate(String app_type5_deposition_rate) {
        this.app_type5_deposition_rate = app_type5_deposition_rate;
    }

    public String getApp_type1_label() {
        return app_type1_label;
    }

    public void setApp_type1_label(String app_type1_label) {
        this.app_type1_label = app_type1_label;
    }

    public String getApp_type2_label() {
        return app_type2_label;
    }

    public void setApp_type2_label(String app_type2_label) {
        this.app_type2_label = app_type2_label;
    }

    public String getApp_type3_label() {
        return app_type3_label;
    }

    public void setApp_type3_label(String app_type3_label) {
        this.app_type3_label = app_type3_label;
    }

    public String getApp_type4_label() {
        return app_type4_label;
    }

    public void setApp_type4_label(String app_type4_label) {
        this.app_type4_label = app_type4_label;
    }

    public String getApp_type5_label() {
        return app_type5_label;
    }

    public void setApp_type5_label(String app_type5_label) {
        this.app_type5_label = app_type5_label;
    }

    public String getDiluted_quantity_1() {
        return diluted_quantity_1;
    }

    public void setDiluted_quantity_1(String diluted_quantity_1) {
        this.diluted_quantity_1 = diluted_quantity_1;
    }

    public String getDiluted_quantity_2() {
        return diluted_quantity_2;
    }

    public void setDiluted_quantity_2(String diluted_quantity_2) {
        this.diluted_quantity_2 = diluted_quantity_2;
    }

    public String getDiluted_quantity_3() {
        return diluted_quantity_3;
    }

    public void setDiluted_quantity_3(String diluted_quantity_3) {
        this.diluted_quantity_3 = diluted_quantity_3;
    }

    public String getDiluted_quantity_4() {
        return diluted_quantity_4;
    }

    public void setDiluted_quantity_4(String diluted_quantity_4) {
        this.diluted_quantity_4 = diluted_quantity_4;
    }

    public String getDiluted_quantity_5() {
        return diluted_quantity_5;
    }

    public void setDiluted_quantity_5(String diluted_quantity_5) {
        this.diluted_quantity_5 = diluted_quantity_5;
    }

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

    public String getDiluted_solution_quantity() {
        return diluted_solution_quantity;
    }

    public void setDiluted_solution_quantity(String diluted_solution_quantity) {
        this.diluted_solution_quantity = diluted_solution_quantity;
    }

    public String getDiluted_solution_quantity_unit() {
        return diluted_solution_quantity_unit;
    }

    public void setDiluted_solution_quantity_unit(String diluted_solution_quantity_unit) {
        this.diluted_solution_quantity_unit = diluted_solution_quantity_unit;
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
