package com.procialize.bayer2020.ui.newsFeedPost.roomDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface NewsFeedUniqueIdUploadedStartedDao {

    @Query("Select count(fld_folder_uniqueid) as fld_folder_uniqueid from tbl_newsfeed_uniqueid WHERE fld_folder_uniqueid = :folderUniqueId")
    LiveData<Integer> isUniqueIdPresent(String folderUniqueId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertFolderUniqueId(NewsFeedUniqueIdUploadedStarted newsFeedUniqueIdUploadedStarted);

}
