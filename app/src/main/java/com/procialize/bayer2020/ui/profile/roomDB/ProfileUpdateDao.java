package com.procialize.bayer2020.ui.profile.roomDB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProfileUpdateDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertProfileWithEventId(ProfileEventId profileEventId);

    @Query("SELECT * from tbl_profile_event_id  where fld_event_id=:event_id and fld_attendee_id=:attendee_id")
    List<ProfileEventId> getProfileWithEventId(String event_id,String attendee_id);

    @Query("DELETE from tbl_profile_event_id")
   public void deleteData();

}
