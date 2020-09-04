package com.procialize.eventapp.ui.newsFeedPost.roomDB;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface UploadMultimediaDao {

    @Query("SELECT * from tbl_upload_multimedia WHERE fld_is_uploaded='0' and fld_folderUniqueId=:folderUniqueId")
    LiveData<List<UploadMultimedia>> getMultimediaToUpload(String folderUniqueId);

    @Query("SELECT * from tbl_upload_multimedia WHERE fld_compressedPath='' and fld_post_status=='' and fld_mime_type!='image/gif'")
    LiveData<List<UploadMultimedia>> getNonCompressesMultimedia();

    @Query("SELECT * from tbl_upload_multimedia WHERE fld_compressedPath='' and fld_post_status=='' and fld_mime_type!='image/gif'")
    List<UploadMultimedia> getNonCompressesMultimediaBg();

    @Query("SELECT fld_folderUniqueId from tbl_upload_multimedia GROUP BY fld_folderUniqueId")
    LiveData<List<String>> selectFolderUniqueId();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMultimediaToUpload(UploadMultimedia uploadMultimedia);

    @Query("DELETE FROM tbl_upload_multimedia")
    void deleteMultimedia();

    @Query("SELECT COUNT(fld_multimedia_id) FROM tbl_upload_multimedia")
    Integer getRowCount();

    @Query("UPDATE tbl_upload_multimedia SET fld_compressedPath=:compressedPath WHERE fld_media_file = :OldPath")
    void updateCompressedPath(String compressedPath, String OldPath);

    @Query("UPDATE tbl_upload_multimedia SET fld_is_uploaded='1' WHERE fld_folderUniqueId = :folderUniqueId")
    void updateIsUploded(String folderUniqueId);

/*    @Query("DELETE FROM tbl_upload_multimedia WHERE fld_folderUniqueId = :folderUniqueId")
    void updateIsUploded(String folderUniqueId);*/
}