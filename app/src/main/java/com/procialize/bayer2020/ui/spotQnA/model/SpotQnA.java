package com.procialize.bayer2020.ui.spotQnA.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpotQnA {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("session_id")
    @Expose
    private String session_id;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("reply")
    @Expose
    private String reply;
    @SerializedName("attendee_id")
    @Expose
    private String attendee_id;
    @SerializedName("org_first_name")
    @Expose
    private String org_first_name;
    @SerializedName("org_last_name")
    @Expose
    private String org_last_name;
    @SerializedName("profile_picture")
    @Expose
    private String profile_picture;
    @SerializedName("company_name")
    @Expose
    private String company_name;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("like_flag")
    @Expose
    private String like_flag;
    @SerializedName("total_likes")
    @Expose
    private String total_likes;

    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getSession_id() {
        return session_id;
    }

    public String getQuestion() {
        return question;
    }

    public String getCreated() {
        return created;
    }

    public String getReply() {
        return reply;
    }

    public String getAttendee_id() {
        return attendee_id;
    }

    public String getOrg_first_name() {
        return org_first_name;
    }

    public String getOrg_last_name() {
        return org_last_name;
    }

    public String getProfile_picture() {
        return profile_picture;
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

    public String getLike_flag() {
        return like_flag;
    }

    public String getTotal_likes() {
        return total_likes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public void setAttendee_id(String attendee_id) {
        this.attendee_id = attendee_id;
    }

    public void setOrg_first_name(String org_first_name) {
        this.org_first_name = org_first_name;
    }

    public void setOrg_last_name(String org_last_name) {
        this.org_last_name = org_last_name;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setLike_flag(String like_flag) {
        this.like_flag = like_flag;
    }

    public void setTotal_likes(String total_likes) {
        this.total_likes = total_likes;
    }
}
