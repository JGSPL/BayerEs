package com.procialize.bayer2020.ui.document.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocumentDetail {

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public String getDocument_name() {
        return document_name;
    }

    public void setDocument_name(String document_name) {
        this.document_name = document_name;
    }

    public String getDocument_file_name() {
        return document_file_name;
    }

    public void setDocument_file_name(String document_file_name) {
        this.document_file_name = document_file_name;
    }

    @SerializedName("doc_id")
    @Expose
    String doc_id;

    @SerializedName("document_name")
    @Expose
    String document_name;

    @SerializedName("document_file_name")
    @Expose
    String document_file_name;
}
