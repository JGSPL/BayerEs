package com.procialize.eventapp.ui.attendeeChat.model;

/**
 * Created by KSHITIZ on 3/29/2018.
 * ----FOR RETRIEVING "chats" SECTION OF DATABASE-----
 */

public class Conv {
    public boolean seen;
    public long timeStamp;
    public String thumbImage;

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }

    public Conv(){

    }

    public Conv(boolean seen, long timeStamp) {
        this.seen = seen;
        this.timeStamp = timeStamp;
    }

    public Conv(boolean seen, long timeStamp, String thumbImg) {
        this.seen = seen;
        this.timeStamp = timeStamp;
        this.thumbImage = thumbImg;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
