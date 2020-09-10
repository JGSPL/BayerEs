package com.procialize.eventapp.ui.profile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileDetails {

    @SerializedName("attendee_id")
    @Expose
    private String attendee_id;
    @SerializedName("first_name")
    @Expose
    private String first_name;
    @SerializedName("middle_name")
    @Expose
    private String middle_name;
    @SerializedName("last_name")
    @Expose
    private String last_name;
    @SerializedName("profile_picture")
    @Expose
    private String profile_picture;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("company_name")
    @Expose
    private String company_name;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("is_god")
    @Expose
    private String is_god;

    public String getAttendee_id() {
        return attendee_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getDesignation() {
        return designation;
    }

    public String getCity() {
        return city;
    }

    public String getIs_god() {
        return is_god;
    }
}
