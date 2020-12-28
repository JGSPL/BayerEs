package com.procialize.bayer2020.ui.catalogue.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Pest_detail {
    @SerializedName("pest_detail")
    @Expose
    List<Pest_item> pestList;

    @SerializedName("pest_document_detail")
    @Expose
    List<PestDocumentDetail> pestDocumentList;


    @SerializedName("total_document_count")
    @Expose
    private String total_document_count;
    @SerializedName("pest_imagepath")
    @Expose
    private String pest_imagepath;
    @SerializedName("pest_documentpath")
    @Expose
    private String pest_documentpath;

    public List<Pest_item> getPestList() {
        return pestList;
    }

    public void setPestList(List<Pest_item> pestList) {
        this.pestList = pestList;
    }

    public List<PestDocumentDetail> getPestDocumentList() {
        return pestDocumentList;
    }

    public void setPestDocumentList(List<PestDocumentDetail> pestDocumentList) {
        this.pestDocumentList = pestDocumentList;
    }

    public String getTotal_document_count() {
        return total_document_count;
    }

    public void setTotal_document_count(String total_document_count) {
        this.total_document_count = total_document_count;
    }

    public String getPest_imagepath() {
        return pest_imagepath;
    }

    public void setPest_imagepath(String pest_imagepath) {
        this.pest_imagepath = pest_imagepath;
    }

    public String getPest_documentpath() {
        return pest_documentpath;
    }

    public void setPest_documentpath(String pest_documentpath) {
        this.pest_documentpath = pest_documentpath;
    }
}
