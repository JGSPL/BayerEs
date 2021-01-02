package com.procialize.bayer2020.ui.loyalityleap.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class redeem_history_item implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("event_id")
    @Expose
    private String event_id;


    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("tile_images")
    @Expose
    private String tile_images;

    @SerializedName("short_description")
    @Expose
    private String short_description;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("is_delete")
    @Expose
    private String status;

    @SerializedName("status")
    @Expose
    private String is_delete;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;
}
