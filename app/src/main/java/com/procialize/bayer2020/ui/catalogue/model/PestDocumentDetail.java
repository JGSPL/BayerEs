package com.procialize.bayer2020.ui.catalogue.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PestDocumentDetail implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("event_id")
    @Expose
    private String event_id;


    @SerializedName("pest_id")
    @Expose
    private String pest_id;

    @SerializedName("pest_document_filetype")
    @Expose
    private String pest_document_filetype;

    @SerializedName("pest_document_filename")
    @Expose
    private String pest_document_filename;
    @SerializedName("pest_document_original_filename")
    @Expose
    private String pest_document_original_filename;

    @SerializedName("pest_document_thumb_filename")
    @Expose
    private String pest_document_thumb_filename;
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

    public String getPest_id() {
        return pest_id;
    }

    public void setPest_id(String pest_id) {
        this.pest_id = pest_id;
    }

    public String getPest_document_filetype() {
        return pest_document_filetype;
    }

    public void setPest_document_filetype(String pest_document_filetype) {
        this.pest_document_filetype = pest_document_filetype;
    }

    public String getPest_document_filename() {
        return pest_document_filename;
    }

    public void setPest_document_filename(String pest_document_filename) {
        this.pest_document_filename = pest_document_filename;
    }

    public String getPest_document_original_filename() {
        return pest_document_original_filename;
    }

    public void setPest_document_original_filename(String pest_document_original_filename) {
        this.pest_document_original_filename = pest_document_original_filename;
    }

    public String getPest_document_thumb_filename() {
        return pest_document_thumb_filename;
    }

    public void setPest_document_thumb_filename(String pest_document_thumb_filename) {
        this.pest_document_thumb_filename = pest_document_thumb_filename;
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
