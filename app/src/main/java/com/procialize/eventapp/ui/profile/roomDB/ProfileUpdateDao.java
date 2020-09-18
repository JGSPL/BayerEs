package com.procialize.eventapp.ui.profile.roomDB;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.procialize.eventapp.ui.newsFeedPost.roomDB.UploadMultimedia;
import com.procialize.eventapp.ui.newsfeed.roomDB.TableNewsFeed;
import com.procialize.eventapp.ui.newsfeed.roomDB.TableNewsFeedMedia;

import java.util.List;

@Dao
public interface ProfileUpdateDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertProfileWithEventId(ProfileEventId profileEventId);

    @Query("SELECT * from tbl_profile_event_id  where fld_event_id=:event_id")
    List<ProfileEventId> getProfileWithEventId(String event_id);

}
