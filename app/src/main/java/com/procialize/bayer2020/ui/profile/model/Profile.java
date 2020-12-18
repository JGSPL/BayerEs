package com.procialize.bayer2020.ui.profile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.bayer2020.GetterSetter.Header;

import java.util.List;

public class Profile {

    @SerializedName("header")
    @Expose
    List<Header> header;

    @SerializedName("detail")
    @Expose
    String profileDetailsEncrypted;

/*    @SerializedName("detailpreencrypt")
    @Expose
    List<ProfileDetails> profileDetails;*/

    public List<Header> getHeader() {
        return header;
    }

    public String getProfileDetailsEncrypted() {
        return profileDetailsEncrypted;
    }
    /*public List<ProfileDetails> getProfileDetails() {
        return profileDetails;
    }*/
}