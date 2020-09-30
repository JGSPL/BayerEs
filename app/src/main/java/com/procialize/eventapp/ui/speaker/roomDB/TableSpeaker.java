package com.procialize.eventapp.ui.speaker.roomDB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tbl_speaker")

public class TableSpeaker implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "fld_id")
    int id;
    @ColumnInfo(name = "fld_mobile")
    String mobile;
    @ColumnInfo(name = "fld_email")
    String email;
    @ColumnInfo(name = "fld_attendee_id")
    String attendee_id;
    @ColumnInfo(name = "fld_first_name")
    String first_name;
    @ColumnInfo(name = "fld_last_name")
    String last_name;
    @ColumnInfo(name = "fld_profile_picture")
    String profile_picture;
    @ColumnInfo(name = "fld_city")
    String city;
    @ColumnInfo(name = "fld_designation")
    String designation;
    @ColumnInfo(name = "fld_company_name")
    String company_name;
    @ColumnInfo(name = "fld_attendee_type")
    String attendee_type;
    @ColumnInfo(name = "fld_total_sms")
    String total_sms;
    @ColumnInfo(name = "firebase_id")
    String firebase_id;
    @ColumnInfo(name = "firebase_name")
    String firebase_name;
    @ColumnInfo(name = "firebase_username")
    String firebase_username;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
