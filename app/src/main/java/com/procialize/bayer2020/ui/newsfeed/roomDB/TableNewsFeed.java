package com.procialize.bayer2020.ui.newsfeed.roomDB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tbl_news_feed")
public class TableNewsFeed implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "fld_id")
    int id;
    @ColumnInfo(name = "fld_news_feed_id")
    String news_feed_id;
    @ColumnInfo(name = "fld_type")
    String type;
    @ColumnInfo(name = "fld_post_status")
    String post_status;
    @ColumnInfo(name = "fld_event_id")
    String event_id;
    @ColumnInfo(name = "fld_post_date")
    String post_date;
    @ColumnInfo(name = "fld_first_name")
    String first_name;
    @ColumnInfo(name = "fld_last_name")
    String last_name;
    @ColumnInfo(name = "fld_company_name")
    String company_name;
    @ColumnInfo(name = "fld_designation")
    String designation;
    @ColumnInfo(name = "fld_city_id")
    String city_id;
    @ColumnInfo(name = "fld_profile_pic")
    String profile_pic;
    @ColumnInfo(name = "fld_attendee_id")
    String attendee_id;
    @ColumnInfo(name = "fld_attendee_type")
    String attendee_type;
    @ColumnInfo(name = "fld_like_flag")
    String like_flag;
    @ColumnInfo(name = "fld_like_type")
    String like_type;
    @ColumnInfo(name = "fld_total_likes")
    String total_likes;
    @ColumnInfo(name = "fld_total_comments")
    String total_comments;

    //@ColumnInfo(name = "fld_news_feed_media")
    /*TableNewsFeedMedia news_feed_media;

    public TableNewsFeedMedia getNews_feed_media() {
        return news_feed_media;
    }

    public void setNews_feed_media(TableNewsFeedMedia news_feed_media) {
        this.news_feed_media = news_feed_media;
    }*/

    public void setId(int id) {
        this.id = id;
    }

    public void setNews_feed_id(String news_feed_id) {
        this.news_feed_id = news_feed_id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPost_status(String post_status) {
        this.post_status = post_status;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public void setAttendee_id(String attendee_id) {
        this.attendee_id = attendee_id;
    }

    public void setAttendee_type(String attendee_type) {
        this.attendee_type = attendee_type;
    }

    public void setLike_flag(String like_flag) {
        this.like_flag = like_flag;
    }

    public void setLike_type(String like_type) {
        this.like_type = like_type;
    }

    public void setTotal_likes(String total_likes) {
        this.total_likes = total_likes;
    }

    public void setTotal_comments(String total_comments) {
        this.total_comments = total_comments;
    }

    public int getId() {
        return id;
    }

    public String getNews_feed_id() {
        return news_feed_id;
    }

    public String getType() {
        return type;
    }

    public String getPost_status() {
        return post_status;
    }

    public String getEvent_id() {
        return event_id;
    }

    public String getPost_date() {
        return post_date;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getDesignation() {
        return designation;
    }

    public String getCity_id() {
        return city_id;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public String getAttendee_id() {
        return attendee_id;
    }

    public String getAttendee_type() {
        return attendee_type;
    }

    public String getLike_flag() {
        return like_flag;
    }

    public String getLike_type() {
        return like_type;
    }

    public String getTotal_likes() {
        return total_likes;
    }

    public String getTotal_comments() {
        return total_comments;
    }
}
