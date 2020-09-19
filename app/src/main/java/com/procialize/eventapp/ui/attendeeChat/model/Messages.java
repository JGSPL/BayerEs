package com.procialize.eventapp.ui.attendeeChat.model;

/**
 * Created by KSHITIZ on 3/27/2018.
 * ----CREATED TO WORK WITH "messages" CHILD IN DATABASE----
 */

public class Messages {

    private String message,type;
    private long time;
    private boolean seen;
    private String from;
    private String thumbImg;



    public Messages(){

    }

    public String getThumbImg() {
        return thumbImg;
    }

    public void setThumbImg(String thumbImg) {
        this.thumbImg = thumbImg;
    }

    public Messages(String message, String type, long time, boolean seen, String from, String thumbImg) {
        this.message = message;
        this.type = type;
        this.time = time;
        this.seen = seen;
        this.from = from;
        this.thumbImg = thumbImg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
