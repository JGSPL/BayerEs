package com.procialize.bayer2020.ui.catalogue.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product_detail {

    @SerializedName("product_detail")
    @Expose
    List<Product_item> productList;

    @SerializedName("product_document_detail")
    @Expose
    List<Product_document_detail> productDocumentList;


    @SerializedName("total_document_count")
    @Expose
    private String total_document_count;
    @SerializedName("product_imagepath")
    @Expose
    private String product_imagepath;
    @SerializedName("product_documentpath")
    @Expose
    private String product_documentpath;

    public List<Product_item> getProductList() {
        return productList;
    }

    public void setProductList(List<Product_item> productList) {
        this.productList = productList;
    }

    public List<Product_document_detail> getProductDocumentList() {
        return productDocumentList;
    }

    public void setProductDocumentList(List<Product_document_detail> productDocumentList) {
        this.productDocumentList = productDocumentList;
    }

    public String getTotal_document_count() {
        return total_document_count;
    }

    public void setTotal_document_count(String total_document_count) {
        this.total_document_count = total_document_count;
    }

    public String getProduct_imagepath() {
        return product_imagepath;
    }

    public void setProduct_imagepath(String product_imagepath) {
        this.product_imagepath = product_imagepath;
    }

    public String getProduct_documentpath() {
        return product_documentpath;
    }

    public void setProduct_documentpath(String product_documentpath) {
        this.product_documentpath = product_documentpath;
    }
}
