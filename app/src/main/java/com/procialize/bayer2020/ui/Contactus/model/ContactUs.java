package com.procialize.bayer2020.ui.Contactus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactUs {

    @SerializedName("contact_id")
    @Expose
    private String contact_id;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("created")
    @Expose
    private String created;

    public String getContact_id() {
        return contact_id;
    }

    public String getContent() {
        return content;
    }

    public String getCreated() {
        return created;
    }
}
