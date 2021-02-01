package com.procialize.bayer2020.ui.storelocator.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class distributerProfile implements Serializable {
    @SerializedName("attendee_id")
    @Expose
    private String attendee_id;

    @SerializedName("first_name")
    @Expose
    private String first_name;


    @SerializedName("last_name")
    @Expose
    private String last_name;

    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("alternate_no")
    @Expose
    private String alternate_no;

    @SerializedName("alternate_no_2")
    @Expose
    private String alternate_no_2;

    @SerializedName("alternate_no_3")
    @Expose
    private String alternate_no_3;


    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("profile_picture")
    @Expose
    private String profile_picture;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("company_name")
    @Expose
    private String company_name;


    @SerializedName("latitude")
    @Expose
    private String latitude;

    @SerializedName("longitude")
    @Expose
    private String longitude;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("pincode")
    @Expose
    private String pincode;

    @SerializedName("attendee_type")
    @Expose
    private String attendee_type;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAlternate_no() {
        return alternate_no;
    }

    public void setAlternate_no(String alternate_no) {
        this.alternate_no = alternate_no;
    }

    public String getAlternate_no_2() {
        return alternate_no_2;
    }

    public void setAlternate_no_2(String alternate_no_2) {
        this.alternate_no_2 = alternate_no_2;
    }

    public String getAlternate_no_3() {
        return alternate_no_3;
    }

    public void setAlternate_no_3(String alternate_no_3) {
        this.alternate_no_3 = alternate_no_3;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAttendee_type() {
        return attendee_type;
    }

    public void setAttendee_type(String attendee_type) {
        this.attendee_type = attendee_type;
    }
}
