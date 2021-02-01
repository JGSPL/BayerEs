package com.procialize.bayer2020.ui.storelocator.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class distributer_item implements Serializable {
    @SerializedName("distributor_count")
    @Expose
    private String distributor_count;

    @SerializedName("city")
    @Expose
    private String city;


    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("latitude")
    @Expose
    private String latitude;

    @SerializedName("longitude")
    @Expose
    private String longitude;

    public String getDistributor_count() {
        return distributor_count;
    }

    public void setDistributor_count(String distributor_count) {
        this.distributor_count = distributor_count;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
