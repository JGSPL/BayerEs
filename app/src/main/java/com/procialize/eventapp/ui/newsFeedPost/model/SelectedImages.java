package com.procialize.eventapp.ui.newsFeedPost.model;

public class SelectedImages {
    /**
     * File path.
     */
    private String mPath;
    /**
     * Thumb path.
     */
    private String mThumbPath;
    /**
     * Video compressed or not
     */
    private Boolean mIsCompressed;
    /**
     * Original Path
     */
    private String mOriginalFilePath;
    /**
     * Media Type
     */
    private String mMediaType;
    /**
     * Mime Type
     */
    private String mMimeType;


    public SelectedImages(String originalFilePath, String path, String thumbPath, Boolean isCompressed, String mediaType, String mimeType) {
        this.mOriginalFilePath = originalFilePath;
        this.mPath = path;
        this.mThumbPath = thumbPath;
        this.mIsCompressed = isCompressed;
        this.mMediaType = mediaType;
        this.mMimeType = mimeType;
    }

    public String getmOriginalFilePath() {
        return mOriginalFilePath;
    }

    public Boolean getmIsCompressed() {
        return mIsCompressed;
    }

    public String getmPath() {
        return mPath;
    }

    public String getmThumbPath() {
        return mThumbPath;
    }

    public String getmMimeType() {
        return mMimeType;
    }


    public String getmMediaType() {
        return mMediaType;
    }

    public void setmMediaType(String mMediaType) {
        this.mMediaType = mMediaType;
    }
}
