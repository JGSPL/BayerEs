package com.procialize.bayer2020.ui.speaker.roomDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import java.util.List;

@Dao
public interface SpeakerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertSpeaker(TableSpeaker speakerTable);

    @Query("SELECT * from tbl_speaker")
    LiveData<List<TableSpeaker>> getAllSpeaker();

    @Query("DELETE FROM tbl_speaker")
    void deleteAllSpeaker();



    @Query("SELECT * from tbl_speaker where fld_attendee_id=:attendeeId")
    public LiveData<List<TableSpeaker>> getSpeakerDetailsFromId(String attendeeId);
}
