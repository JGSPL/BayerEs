package com.procialize.bayer2020.ui.newsfeed.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class News_feed_media implements Serializable {

    @SerializedName("media_id")
    @Expose
    private String media_id;

    @SerializedName("news_feed_id")
    @Expose
    private String news_feed_id;

    @SerializedName("media_type")
    @Expose
    private String media_type;
    @SerializedName("media_file")
    @Expose
    private String media_file;

    @SerializedName("thumb_image")
    @Expose
    private String thumb_image;
    @SerializedName("width")
    @Expose
    private String width;
    @SerializedName("height")
    @Expose
    private String height;


    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public String getNews_feed_id() {
        return news_feed_id;
    }

    public void setNews_feed_id(String news_feed_id) {
        this.news_feed_id = news_feed_id;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getMedia_file() {
        return media_file;
    }

    public void setMedia_file(String media_file) {
        this.media_file = media_file;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

}
