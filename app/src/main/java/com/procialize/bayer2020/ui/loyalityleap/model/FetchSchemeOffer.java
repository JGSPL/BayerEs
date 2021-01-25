package com.procialize.bayer2020.ui.loyalityleap.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.bayer2020.GetterSetter.Header;

import java.util.List;

public class FetchSchemeOffer {
    @SerializedName("header")
    @Expose
    List<Header> header;

    @SerializedName("detailpreencrypt")
    @Expose
    List<Scheme_offer_item> scheameOfferList;

    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("available_redeemable_points")
    @Expose
    private String available_redeemable_points;
    @SerializedName("totalRecords")
    @Expose
    private String totalRecords;

    @SerializedName("imagepath")
    @Expose
    private String imagepath;

    public List<Header> getHeader() {
        return header;
    }

    public void setHeader(List<Header> header) {
        this.header = header;
    }

    public List<Scheme_offer_item> getScheameOfferList() {
        return scheameOfferList;
    }

    public void setScheameOfferList(List<Scheme_offer_item> scheameOfferList) {
        this.scheameOfferList = scheameOfferList;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAvailable_redeemable_points() {
        return available_redeemable_points;
    }

    public void setAvailable_redeemable_points(String available_redeemable_points) {
        this.available_redeemable_points = available_redeemable_points;
    }

    public String getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(String totalRecords) {
        this.totalRecords = totalRecords;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }
}
