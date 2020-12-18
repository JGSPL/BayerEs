package com.procialize.bayer2020.ui.eventinfo.roomDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EventInfoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertEventInfo(TableEventInfo eventInfoTable);

    @Query("SELECT * from tbl_event_info")
    LiveData<List<TableEventInfo>> getEventInfo();

    @Query("DELETE FROM tbl_event_info")
    void deleteEventInfo();
}
