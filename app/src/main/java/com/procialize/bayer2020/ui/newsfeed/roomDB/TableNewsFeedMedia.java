package com.procialize.bayer2020.ui.newsfeed.roomDB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_news_feed_media")
public class TableNewsFeedMedia {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "fld_id")
    int id;

    @ColumnInfo(name = "fld_media_id")
    private String media_id;

    @ColumnInfo(name = "fld_news_feed_id")
    private String news_feed_id;

    @ColumnInfo(name = "fld_media_type")
    private String media_type;

    @ColumnInfo(name = "fld_media_file")
    private String media_file;

    @ColumnInfo(name = "fld_thumb_image")
    private String thumb_image;

    @ColumnInfo(name = "fld_width")
    private String width;

    @ColumnInfo(name = "fld_height")
    private String height;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
