package com.procialize.bayer2020.ui.newsfeed.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Newsfeed_detail implements Serializable {

    @SerializedName("news_feed_id")
    @Expose
    private String news_feed_id;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("post_status")
    @Expose
    private String post_status;
    @SerializedName("event_id")
    @Expose
    private String event_id;

    @SerializedName("post_date")
    @Expose
    private String post_date;

    @SerializedName("first_name")
    @Expose
    private String first_name;

    @SerializedName("last_name")
    @Expose
    private String last_name;

    @SerializedName("company_name")
    @Expose
    private String company_name;

    @SerializedName("designation")
    @Expose
    private String designation;

    @SerializedName("city_id")
    @Expose
    private String city_id;

    @SerializedName("profile_pic")
    @Expose
    private String profile_pic;

    @SerializedName("attendee_id")
    @Expose
    private String attendee_id;
    @SerializedName("attendee_type")
    @Expose
    private String attendee_type;
    @SerializedName("like_flag")
    @Expose
    private String like_flag;
    @SerializedName("like_type")
    @Expose
    private String like_type;
    @SerializedName("total_likes")
    @Expose
    private String total_likes;
    @SerializedName("total_comments")
    @Expose
    private String total_comments;

    @SerializedName("news_feed_media")
    @Expose
    List<News_feed_media> news_feed_media;


    public List<News_feed_media> getNews_feed_media() {
        return news_feed_media;
    }

    public void setNews_feed_media(List<News_feed_media> news_feed_media) {
        this.news_feed_media = news_feed_media;
    }

    public String getNews_feed_id() {
        return news_feed_id;
    }

    public void setNews_feed_id(String news_feed_id) {
        this.news_feed_id = news_feed_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPost_status() {
        return post_status;
    }

    public void setPost_status(String post_status) {
        this.post_status = post_status;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
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

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getAttendee_id() {
        return attendee_id;
    }

    public void setAttendee_id(String attendee_id) {
        this.attendee_id = attendee_id;
    }

    public String getAttendee_type() {
        return attendee_type;
    }

    public void setAttendee_type(String attendee_type) {
        this.attendee_type = attendee_type;
    }

    public String getLike_flag() {
        return like_flag;
    }

    public void setLike_flag(String like_flag) {
        this.like_flag = like_flag;
    }

    public String getLike_type() {
        return like_type;
    }

    public void setLike_type(String like_type) {
        this.like_type = like_type;
    }

    public String getTotal_likes() {
        return total_likes;
    }

    public void setTotal_likes(String total_likes) {
        this.total_likes = total_likes;
    }

    public String getTotal_comments() {
        return total_comments;
    }

    public void setTotal_comments(String total_comments) {
        this.total_comments = total_comments;
    }

}
