package com.procialize.eventapp.ui.newsfeed.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.procialize.eventapp.GetterSetter.Header;

import java.util.List;

public class FetchNewsfeedMultiple {
    @SerializedName("header")
    @Expose
    List<Header> header;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @SerializedName("detail")
    @Expose
    private String detail;

/*    @SerializedName("detailpreencrypt")
    @Expose
    List<Newsfeed_detail> newsfeed_detail;*/

    @SerializedName("live_stream_info")
    @Expose
    List<Live_stream_info> live_stream_info;

    @SerializedName("zoom_info")
    @Expose
    List<Zoom_info> zoom_info;

    @SerializedName("media_path")
    @Expose
    private String media_path;

    @SerializedName("totalRecords")
    @Expose
    private String totalRecords;

    public List<Live_stream_info> getLive_stream_info() {
        return live_stream_info;
    }

    public void setLive_stream_info(List<Live_stream_info> live_stream_info) {
        this.live_stream_info = live_stream_info;
    }

    public List<Zoom_info> getZoom_info() {
        return zoom_info;
    }

    public void setZoom_info(List<Zoom_info> zoom_info) {
        this.zoom_info = zoom_info;
    }

    public List<Header> getHeader() {
        return header;
    }

    public void setHeader(List<Header> header) {
        this.header = header;
    }
/*

    public List<Newsfeed_detail> getNewsfeed_detail() {
        return newsfeed_detail;
    }

    public void setNewsfeed_detail(List<Newsfeed_detail> newsfeed_detail) {
        this.newsfeed_detail = newsfeed_detail;
    }
*/


    public String getMedia_path() {
        return media_path;
    }

    public void setMedia_path(String media_path) {
        this.media_path = media_path;
    }

    public String getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(String totalRecords) {
        this.totalRecords = totalRecords;
    }
}
