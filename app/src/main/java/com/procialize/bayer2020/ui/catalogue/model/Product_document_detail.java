package com.procialize.bayer2020.ui.catalogue.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Product_document_detail implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("event_id")
    @Expose
    private String event_id;


    @SerializedName("product_id")
    @Expose
    private String product_id;

    @SerializedName("product_document_filetype")
    @Expose
    private String product_document_filetype;

    @SerializedName("product_document_filename")
    @Expose
    private String product_document_filename;
    @SerializedName("product_document_original_filename")
    @Expose
    private String product_document_original_filename;

    @SerializedName("product_document_thumb_filename")
    @Expose
    private String product_document_thumb_filename;
    @SerializedName("is_delete")
    @Expose
    private String is_delete;
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

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_document_filetype() {
        return product_document_filetype;
    }

    public void setProduct_document_filetype(String product_document_filetype) {
        this.product_document_filetype = product_document_filetype;
    }

    public String getProduct_document_filename() {
        return product_document_filename;
    }

    public void setProduct_document_filename(String product_document_filename) {
        this.product_document_filename = product_document_filename;
    }

    public String getProduct_document_original_filename() {
        return product_document_original_filename;
    }

    public void setProduct_document_original_filename(String product_document_original_filename) {
        this.product_document_original_filename = product_document_original_filename;
    }

    public String getProduct_document_thumb_filename() {
        return product_document_thumb_filename;
    }

    public void setProduct_document_thumb_filename(String product_document_thumb_filename) {
        this.product_document_thumb_filename = product_document_thumb_filename;
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
