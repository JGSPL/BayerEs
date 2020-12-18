package com.procialize.bayer2020.ui.newsfeed.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Live_stream_info {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("youtube_stream_url")
    @Expose
    private String youtube_stream_url;

    @SerializedName("stream_status")
    @Expose
    private String stream_status;
    @SerializedName("created")
    @Expose
    private String created;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYoutube_stream_url() {
        return youtube_stream_url;
    }

    public void setYoutube_stream_url(String youtube_stream_url) {
        this.youtube_stream_url = youtube_stream_url;
    }

    public String getStream_status() {
        return stream_status;
    }

    public void setStream_status(String stream_status) {
        this.stream_status = stream_status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
