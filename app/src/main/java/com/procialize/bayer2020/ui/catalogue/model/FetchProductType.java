package com.procialize.bayer2020.ui.catalogue.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.bayer2020.GetterSetter.Header;

import java.util.List;

public class FetchProductType {
    @SerializedName("header")
    @Expose
    List<Header> header;

    @SerializedName("detail")
    @Expose
    private String detail;

   /* @SerializedName("detailpreencrypt")
    @Expose
    List<ProductType> producttypeList;*/

    @SerializedName("totalRecords")
    @Expose
    private String totalRecords;

    @SerializedName("product_type_imagepath")
    @Expose
    private String product_type_imagepath;

    public List<Header> getHeader() {
        return header;
    }

    public void setHeader(List<Header> header) {
        this.header = header;
    }

   /* public List<ProductType> getProducttypeList() {
        return producttypeList;
    }

    public void setProducttypeList(List<ProductType> producttypeList) {
        this.producttypeList = producttypeList;
    }*/

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

    public String getProduct_type_imagepath() {
        return product_type_imagepath;
    }

    public void setProduct_type_imagepath(String product_type_imagepath) {
        this.product_type_imagepath = product_type_imagepath;
    }
}
