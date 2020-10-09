package com.procialize.eventapp.ui.agenda.roomDB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.procialize.eventapp.ui.attendee.roomDB.TableAttendee;

@Dao
public interface AgendaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertAgenda(TableAgenda tableAgenda);
}
