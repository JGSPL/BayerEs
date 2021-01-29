package com.procialize.bayer2020.ui.qapost.roomDB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_qanewsfeed_uniqueid")
public class NewsFeedUniqueIdUploadedStarted {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "fld_id")
    int multimedia_id;

    @ColumnInfo(name = "fld_folder_uniqueid")
    String folder_uniqueid;


    public String getFolder_uniqueid() {
        return folder_uniqueid;
    }

    public void setFolder_uniqueid(String folder_uniqueid) {
        this.folder_uniqueid = folder_uniqueid;
    }
}
