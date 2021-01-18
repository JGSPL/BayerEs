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
    @SerializedName("company_name")
    @Expose
    private String company_name;
    @SerializedName("designation")
    @Expose
    private String designation;
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
    @SerializedName("specialization")
    @Expose
    private String specialization;
    @SerializedName("turnover")
    @Expose
    private String turnover;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("is_god")
    @Expose
    private String is_god;


    @SerializedName("user_type")
    @Expose
    private String user_type;

    @SerializedName("profile_status")
    @Expose
    private String profile_status;
    @SerializedName("enrollleapflag")
    @Expose
    private String enrollleapflag;
    @SerializedName("access_token")
    @Expose
    private String access_token;

    public void setAttendee_id(String attendee_id) {
        this.attendee_id = attendee_id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setIs_god(String is_god) {
        this.is_god = is_god;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getProfile_status() {
        return profile_status;
    }

    public void setProfile_status(String profile_status) {
        this.profile_status = profile_status;
    }

    public String getEnrollleapflag() {
        return enrollleapflag;
    }

    public void setEnrollleapflag(String enrollleapflag) {
        this.enrollleapflag = enrollleapflag;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

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
