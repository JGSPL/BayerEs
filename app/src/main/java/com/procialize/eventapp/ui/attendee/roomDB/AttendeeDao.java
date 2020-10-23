package com.procialize.eventapp.ui.attendee.roomDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.procialize.eventapp.ui.attendee.model.Attendee;

import java.sql.SQLException;
import java.util.List;

@Dao
public interface AttendeeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertAttendee(TableAttendee attendeeTable);

    @Query("SELECT * from tbl_attendee")
    LiveData<List<TableAttendee>> getAllAttendee();

    @Query("DELETE FROM tbl_attendee")
    void deleteAllAttendee();

    @Query("SELECT fld_mention_name as name from tbl_attendee where fld_attendee_id=:attendeeId")
    public String getMentionNameFromAttendeeId(String attendeeId);

    @Query("SELECT * from tbl_attendee where fld_attendee_id=:attendeeId")
    public LiveData<List<TableAttendee>> getAttendeeDetailsFromId(String attendeeId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertSingleAttendee(TableAttendee attendeeTable);

    @Query("DELETE FROM tbl_attendee where fld_attendee_id=:attendeeId")
    void deleteAttendee(String attendeeId);

    @Query("SELECT * from tbl_attendee where firebase_id=:firebase_id")
    Attendee getAttendeeDetailsFromFireId(String firebase_id);
}