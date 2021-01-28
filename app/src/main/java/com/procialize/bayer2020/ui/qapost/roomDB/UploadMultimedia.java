package com.procialize.bayer2020.ui.qapost.roomDB;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity(tableName = "tbl_uploadqa_multimedia")
public class UploadMultimedia implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "fld_multimedia_id")
    int multimedia_id;


    @ColumnInfo(name = "fld_post_status")
    String post_status;

    @ColumnInfo(name = "fld_media_file")
    String media_file;

    @ColumnInfo(name = "fld_media_file_thumb")
    String media_file_thumb;

    @ColumnInfo(name = "fld_news_feed_id")
    String news_feed_id;

    @ColumnInfo(name = "fld_is_uploaded")
    String is_uploaded;

    @ColumnInfo(name = "fld_media_type")
    String media_type;

    @ColumnInfo(name = "fld_compressedPath")
    String compressedPath;

    @ColumnInfo(name = "fld_folderUniqueId")
    String folderUniqueId;

    @ColumnInfo(name = "fld_mime_type")
    String mimeType;
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setMultimedia_id(@NonNull int multimedia_id) {
        this.multimedia_id = multimedia_id;
    }

    public void setMedia_file(String media_file) {
        this.media_file = media_file;
    }

    public void setMedia_file_thumb(String media_file_thumb) {
        this.media_file_thumb = media_file_thumb;
    }

    public void setNews_feed_id(String news_feed_id) {
        this.news_feed_id = news_feed_id;
    }

    public void setIs_uploaded(String is_uploaded) {
        this.is_uploaded = is_uploaded;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public void setCompressedPath(String compressedPath) {
        this.compressedPath = compressedPath;
    }

    public void setFolderUniqueId(String folderUniqueId) {
        this.folderUniqueId = folderUniqueId;
    }

    public void setPost_status(String post_status) {
        this.post_status = post_status;
    }


    @NonNull
    public int getMultimedia_id() {
        return multimedia_id;
    }

    public String getPost_status() { return post_status;
    }

    public String getMedia_file() {
        return media_file;
    }

    public String getMedia_file_thumb() {
        return media_file_thumb;
    }

    public String getNews_feed_id() {
        return news_feed_id;
    }

    public String getIs_uploaded() {
        return is_uploaded;
    }

    public String getMedia_type() {
        return media_type;
    }

    public String getCompressedPath() {
        return compressedPath;
    }

    public String getFolderUniqueId() {
        return folderUniqueId;
    }
}