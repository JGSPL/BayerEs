package com.procialize.eventapp.ui.attendeeChat.roomDb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import java.util.List;
@Dao
public interface AttendeeChatDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertAttendee(Table_Attendee_Chatcount attendeeTable);

    @Query("SELECT * from Table_Attendee_Chatcount")
    LiveData<List<Table_Attendee_Chatcount>> getAllAttendee();

    @Query("SELECT * from Table_Attendee_Chatcount")
    List<Table_Attendee_Chatcount>  getAllDaoAttendee();

    @Query("DELETE FROM Table_Attendee_Chatcount")
    void deleteAllAttendee();

    @Query("SELECT * from Table_Attendee_Chatcount where ChatCount_receId= :ChatCount_receId")
    List<Table_Attendee_Chatcount>  getSingleAttendee(String ChatCount_receId);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertSingleAttendee(Table_Attendee_Chatcount attendeeTable);

    @Query("DELETE FROM Table_Attendee_Chatcount where ChatCount_receId=:attendeeId")
    void deleteAttendee(String attendeeId);

    @Query("UPDATE Table_Attendee_Chatcount SET chat_count= :count WHERE ChatCount_receId = :ChatCount_receId")
    void updateChatCount(int count, String ChatCount_receId);

    @Query("UPDATE Table_Attendee_Chatcount SET chat_count_read='1' WHERE ChatCount_receId = :ChatCount_receId")
    void updateIsRead(String ChatCount_receId);

    @Query("SELECT chat_count as chat_count from Table_Attendee_Chatcount where ChatCount_receId= :ChatCount_receId")
    public int getChatCountId(String ChatCount_receId);


}
