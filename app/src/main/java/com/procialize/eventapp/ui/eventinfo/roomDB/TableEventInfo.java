package com.procialize.eventapp.ui.eventinfo.roomDB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "tbl_event_info")
public class TableEventInfo implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "fld_id")
    int id;
    @ColumnInfo(name = "fld_event_id")
    String event_id;
    @ColumnInfo(name = "fld_event_name")
    String event_name;
    @ColumnInfo(name = "fld_event_start_date")
    String event_start_date;
    @ColumnInfo(name = "fld_event_end_date")
    String event_end_date;
    @ColumnInfo(name = "fld_event_description")
    String event_description;
    @ColumnInfo(name = "fld_event_location")
    String event_location;
    @ColumnInfo(name = "fld_event_city")
    String event_city;
    @ColumnInfo(name = "fld_event_latitude")
    String event_latitude;
    @ColumnInfo(name = "fld_event_longitude")
    String event_longitude;
    @ColumnInfo(name = "fld_event_image")
    String event_image;
    @ColumnInfo(name = "fld_header_image")
    String header_image;
    @ColumnInfo(name = "fld_background_image")
    String background_image;
    @ColumnInfo(name = "fld_event_cover_image")
    String event_cover_image;

    public void setEvent_cover_image(String event_cover_image) {
        this.event_cover_image = event_cover_image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_start_date() {
        return event_start_date;
    }

    public void setEvent_start_date(String event_start_date) {
        this.event_start_date = event_start_date;
    }

    public String getEvent_end_date() {
        return event_end_date;
    }

    public void setEvent_end_date(String event_end_date) {
        this.event_end_date = event_end_date;
    }

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    public String getEvent_location() {
        return event_location;
    }

    public void setEvent_location(String event_location) {
        this.event_location = event_location;
    }

    public String getEvent_city() {
        return event_city;
    }

    public void setEvent_city(String event_city) {
        this.event_city = event_city;
    }

    public String getEvent_latitude() {
        return event_latitude;
    }

    public void setEvent_latitude(String event_latitude) {
        this.event_latitude = event_latitude;
    }

    public String getEvent_longitude() {
        return event_longitude;
    }

    public void setEvent_longitude(String event_longitude) {
        this.event_longitude = event_longitude;
    }

    public String getEvent_image() {
        return event_image;
    }

    public void setEvent_image(String event_image) {
        this.event_image = event_image;
    }

    public String getHeader_image() {
        return header_image;
    }

    public void setHeader_image(String header_image) {
        this.header_image = header_image;
    }

    public String getBackground_image() {
        return background_image;
    }

    public void setBackground_image(String background_image) {
        this.background_image = background_image;
    }

    public String getEvent_cover_image() {
        return event_cover_image;
    }
}
