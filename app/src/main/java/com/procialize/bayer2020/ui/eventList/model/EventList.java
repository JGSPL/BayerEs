package com.procialize.bayer2020.ui.eventList.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EventList implements Serializable {

    @SerializedName("event_id")
    @Expose
    private String event_id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("event_start_date")
    @Expose
    private String event_start_date;
    @SerializedName("event_end_date")
    @Expose
    private String event_end_date;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("event_image")
    @Expose
    private String event_image;
    @SerializedName("header_image")
    @Expose
    private String header_image;
    @SerializedName("background_image")
    @Expose
    private String background_image;
    @SerializedName("color_one")
    @Expose
    private String color_one;
    @SerializedName("color_two")
    @Expose
    private String color_two;
    @SerializedName("color_three")
    @Expose
    private String color_three;
    @SerializedName("color_four")
    @Expose
    private String color_four;
    @SerializedName("color_five")
    @Expose
    private String color_five;

    public EventList(String event_id, String name, String logo, String backgroundImage, String color_one, String color_two, String color_three, String color_four, String color_five) {
        this.event_id = event_id;
        this.name = name;
        this.event_image = logo;
        this.background_image = backgroundImage;
        this.color_one = color_one;
        this.color_two = color_two;
        this.color_three = color_three;
        this.color_four = color_four;
        this.color_five = color_five;

    }


    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getColor_one() {
        return color_one;
    }

    public void setColor_one(String color_one) {
        this.color_one = color_one;
    }

    public String getColor_two() {
        return color_two;
    }

    public void setColor_two(String color_two) {
        this.color_two = color_two;
    }

    public String getColor_three() {
        return color_three;
    }

    public void setColor_three(String color_three) {
        this.color_three = color_three;
    }

    public String getColor_four() {
        return color_four;
    }

    public void setColor_four(String color_four) {
        this.color_four = color_four;
    }

    public String getColor_five() {
        return color_five;
    }

    public void setColor_five(String color_five) {
        this.color_five = color_five;
    }
}
