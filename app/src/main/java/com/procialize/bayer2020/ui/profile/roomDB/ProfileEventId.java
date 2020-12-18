package com.procialize.bayer2020.ui.profile.roomDB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tbl_profile_event_id")
public class ProfileEventId implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "fld_id")
    int id;
    @ColumnInfo(name = "fld_event_id")
    String fld_event_id;
    @ColumnInfo(name = "fld_is_profile_update")
    String fld_is_profile_update;
    @ColumnInfo(name = "fld_attendee_id")
    String fld_attendee_id;

    public int getId() {
        return id;
    }

    public String getFld_event_id() {
        return fld_event_id;
    }

    public String getFld_is_profile_update() {
        return fld_is_profile_update;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFld_event_id(String fld_event_id) {
        this.fld_event_id = fld_event_id;
    }

    public void setFld_is_profile_update(String fld_is_profile_update) {
        this.fld_is_profile_update = fld_is_profile_update;
    }

    public String getFld_attendee_id() {
        return fld_attendee_id;
    }

    public void setFld_attendee_id(String fld_attendee_id) {
        this.fld_attendee_id = fld_attendee_id;
    }
}
