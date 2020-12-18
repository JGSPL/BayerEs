package com.procialize.bayer2020.ui.attendee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Attendee implements Serializable {
    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("email")
    @Expose
    private String email;


    @SerializedName("attendee_id")
    @Expose
    private String attendee_id;

    @SerializedName("first_name")
    @Expose
    private String first_name;

    @SerializedName("last_name")
    @Expose
    private String last_name;
    @SerializedName("profile_picture")
    @Expose
    private String profile_picture;

    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("company_name")
    @Expose
    private String company_name;
    @SerializedName("attendee_type")
    @Expose
    private String attendee_type;
    @SerializedName("total_sms")
    @Expose
    private String total_sms;

    @SerializedName("firebase_id")
    @Expose
    private String firebase_id;
    @SerializedName("firebase_name")
    @Expose
    private String firebase_name;
    @SerializedName("firebase_username")
    @Expose
    private String firebase_username;

    @SerializedName("chat_status")
    @Expose
    private String firebase_status;

    public String getFirebase_status() {
        return firebase_status;
    }

    public void setFirebase_status(String firebase_status) {
        this.firebase_status = firebase_status;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirebase_id() {
        return firebase_id;
    }

    public void setFirebase_id(String firebase_id) {
        this.firebase_id = firebase_id;
    }

    public String getFirebase_name() {
        return firebase_name;
    }

    public void setFirebase_name(String firebase_name) {
        this.firebase_name = firebase_name;
    }

    public String getFirebase_username() {
        return firebase_username;
    }

    public void setFirebase_username(String firebase_username) {
        this.firebase_username = firebase_username;
    }

    public String getAttendee_id() {
        return attendee_id;
    }

    public void setAttendee_id(String attendee_id) {
        this.attendee_id = attendee_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getAttendee_type() {
        return attendee_type;
    }

    public void setAttendee_type(String attendee_type) {
        this.attendee_type = attendee_type;
    }

    public String getTotal_sms() {
        return total_sms;
    }

    public void setTotal_sms(String total_sms) {
        this.total_sms = total_sms;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }
}
