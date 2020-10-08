package com.procialize.eventapp.ui.attendee.roomDB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tbl_attendee")
public class TableAttendee implements Serializable {

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
    @ColumnInfo(name = "fld_mention_name")
    String fld_mention_name;
    @ColumnInfo(name = "firebase_id")
    String firebase_id;
    @ColumnInfo(name = "firebase_name")
    String firebase_name;
    @ColumnInfo(name = "firebase_username")
    String firebase_username;
    @ColumnInfo(name = "firebase_status")
    String firebase_status;

    public String getFirebase_status() {
        return firebase_status;
    }

    public void setFirebase_status(String firebase_status) {
        this.firebase_status = firebase_status;
    }

    public int getId() {
        return id;
    }


    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getAttendee_id() {
        return attendee_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public String getCity() {
        return city;
    }

    public String getDesignation() {
        return designation;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getAttendee_type() {
        return attendee_type;
    }

    public String getTotal_sms() {
        return total_sms;
    }

    public String getFld_mention_name() {
        return fld_mention_name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAttendee_id(String attendee_id) {
        this.attendee_id = attendee_id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public void setAttendee_type(String attendee_type) {
        this.attendee_type = attendee_type;
    }

    public void setTotal_sms(String total_sms) {
        this.total_sms = total_sms;
    }

    public void setFld_mention_name(String fld_mention_name) {
        this.fld_mention_name = fld_mention_name;
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
