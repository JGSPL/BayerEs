package com.procialize.eventapp.ui.newsfeed.roomDB;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.procialize.eventapp.ui.newsFeedPost.roomDB.UploadMultimedia;

import java.util.List;

@Dao
public interface NewsFeedDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertNewsFeed(TableNewsFeed tableNewsFeed);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertNewsFeedMedia(TableNewsFeedMedia tableNewsFeedMedia);

    @Query("SELECT * from tbl_news_feed")
    LiveData<List<TableNewsFeed>> getNewsFeed();

    @Query("SELECT * from tbl_news_feed_media where fld_news_feed_id=:newsFeedId")
    LiveData<List<TableNewsFeedMedia>> getNewsFeedMedia(String newsFeedId);

    @Query("DELETE FROM tbl_news_feed_media")
    void deleteNewsFeedMedia();

    @Query("DELETE FROM tbl_news_feed")
    void deleteNewsFeed();
}
