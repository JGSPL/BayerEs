package com.procialize.bayer2020.ui.loyalityleap.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.bayer2020.GetterSetter.Header;

import java.util.List;

public class FetchRequestToRedeem {

    @SerializedName("header")
    @Expose
    List<Header> header;

    @SerializedName("detail")
    @Expose
    private String detail;

    @SerializedName("totalRecords")
    @Expose
    private String totalRecords;

    @SerializedName("product_imagepath")
    @Expose
    private String product_imagepath;

    public List<Header> getHeader() {
        return header;
    }

    public void setHeader(List<Header> header) {
        this.header = header;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(String totalRecords) {
        this.totalRecords = totalRecords;
    }

    public String getProduct_imagepath() {
        return product_imagepath;
    }

    public void setProduct_imagepath(String product_imagepath) {
        this.product_imagepath = product_imagepath;
    }
}
