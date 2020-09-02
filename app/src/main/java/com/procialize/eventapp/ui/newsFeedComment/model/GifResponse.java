package com.procialize.eventapp.ui.newsFeedComment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GifResponse {
    @SerializedName("results")
    @Expose
    private List<GifResult> results = null;

    public List<GifResult> getResults() {
        return results;
    }

    public void setResults(List<GifResult> results) {
        this.results = results;
    }
}
