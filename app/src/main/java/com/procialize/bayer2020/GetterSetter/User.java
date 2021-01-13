package com.procialize.bayer2020.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    @Expose
    private String id;
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
    @SerializedName("refresh_token")
    @Expose
    private String refresh_token;
    @SerializedName("user_type")
    @Expose
    private String user_type;
    @SerializedName("verify_otp")
    @Expose
    private String verify_otp;

    @SerializedName("profile_status")
    @Expose
    private String profile_status;
    @SerializedName("enrollleapflag")
    @Expose
    private String enrollleapflag;
    @SerializedName("total_event")
    @Expose
    private String total_event;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("expiry_time")
    @Expose
    private String expiry_time;
    @SerializedName("is_god")
    @Expose
    private String is_god;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getVerify_otp() {
        return verify_otp;
    }

    public void setVerify_otp(String verify_otp) {
        this.verify_otp = verify_otp;
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

    public String getTotal_event() {
        return total_event;
    }

    public void setTotal_event(String total_event) {
        this.total_event = total_event;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getExpiry_time() {
        return expiry_time;
    }

    public void setExpiry_time(String expiry_time) {
        this.expiry_time = expiry_time;
    }

    public String getIs_god() {
        return is_god;
    }

    public void setIs_god(String is_god) {
        this.is_god = is_god;
    }
}
