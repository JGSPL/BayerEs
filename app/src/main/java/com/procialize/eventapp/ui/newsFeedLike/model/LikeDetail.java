package com.procialize.eventapp.ui.newsFeedLike.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LikeDetail {

       String like_id;
       String first_name;
       String last_name;
       String profile_picture;
       String dateTime;

    public String getLike_id() {
        return like_id;
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

    public String getDateTime() {
        return dateTime;
    }
}
