package com.procialize.bayer2020.ui.profile.model;

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

    public String getSap_code() {
        return sap_code;
    }

    public void setSap_code(String sap_code) {
        this.sap_code = sap_code;
    }

    public String getAssociated_since() {
        return associated_since;
    }

    public void setAssociated_since(String associated_since) {
        this.associated_since = associated_since;
    }

    public String getNo_of_pco_served() {
        return no_of_pco_served;
    }

    public void setNo_of_pco_served(String no_of_pco_served) {
        this.no_of_pco_served = no_of_pco_served;
    }

    public String getNo_of_technician() {
        return no_of_technician;
    }

    public void setNo_of_technician(String no_of_technician) {
        this.no_of_technician = no_of_technician;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getTurnover() {
        return turnover;
    }

    public void setTurnover(String turnover) {
        this.turnover = turnover;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @SerializedName("sap_code")
    @Expose
    private String sap_code;
    @SerializedName("associated_since")
    @Expose
    private String associated_since;
    @SerializedName("no_of_pco_served")
    @Expose
    private String no_of_pco_served;
    @SerializedName("no_of_technician")
    @Expose
    private String no_of_technician;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("license")
    @Expose
    private String license;
    @SerializedName("specialization")
    @Expose
    private String specialization;
    @SerializedName("turnover")
    @Expose
    private String turnover;

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
