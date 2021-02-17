package com.procialize.bayer2020.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class loginTokendetail {
    @SerializedName("callId")
    @Expose
    private String callId;
    @SerializedName("errorCode")
    @Expose
    private String errorCode;
    @SerializedName("apiVersion")
    @Expose
    private String apiVersion;
    @SerializedName("statusCode")
    @Expose
    private String statusCode;
    @SerializedName("statusReason")
    @Expose
    private String statusReason;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("vToken")
    @Expose
    private String vToken;
    @SerializedName("errorDetails")
    @Expose
    private String errorDetails;

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getvToken() {
        return vToken;
    }

    public void setvToken(String vToken) {
        this.vToken = vToken;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }
}
