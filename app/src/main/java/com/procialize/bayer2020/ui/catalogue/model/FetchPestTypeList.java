package com.procialize.bayer2020.ui.catalogue.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.bayer2020.GetterSetter.Header;

import java.util.List;

public class FetchPestTypeList {
    @SerializedName("header")
    @Expose
    List<Header> header;

    @SerializedName("detailpreencrypt")
    @Expose
    List<PestTypeItem> productList;

    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("totalRecords")
    @Expose
    private String totalRecords;
    @SerializedName("pest_imagepath")
    @Expose
    private String pest_imagepath;

    public List<Header> getHeader() {
        return header;
    }

    public void setHeader(List<Header> header) {
        this.header = header;
    }

    public List<PestTypeItem> getProductList() {
        return productList;
    }

    public void setProductList(List<PestTypeItem> productList) {
        this.productList = productList;
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

    public String getPest_imagepath() {
        return pest_imagepath;
    }

    public void setPest_imagepath(String pest_imagepath) {
        this.pest_imagepath = pest_imagepath;
    }
}
