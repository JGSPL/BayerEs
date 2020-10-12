package com.procialize.eventapp.ui.agenda.roomDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.procialize.eventapp.ui.attendee.roomDB.TableAttendee;

import java.util.List;

@Dao
public interface AgendaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertAgenda(TableAgenda tableAgenda);

    @Query("DELETE FROM tbl_agenda")
    void deleteAllAgenda();

    @Query("SELECT * from tbl_agenda")
    LiveData<List<TableAgenda>> getAllAgenda();
}
