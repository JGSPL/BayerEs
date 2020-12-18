package com.procialize.bayer2020.ui.agenda.roomDB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_agenda")
public class TableAgenda {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "fld_id")
    int id;
    @ColumnInfo(name = "fld_session_id")
    String session_id;
    @ColumnInfo(name = "fld_session_name")
    String session_name;
    @ColumnInfo(name = "fld_session_short_description")
    String session_short_description;
    @ColumnInfo(name = "fld_session_description")
    String session_description;
    @ColumnInfo(name = "fld_session_start_time")
    String session_start_time;
    @ColumnInfo(name = "fld_session_end_time")
    String session_end_time;
    @ColumnInfo(name = "fld_session_date")
    String session_date;
    @ColumnInfo(name = "fld_event_id")
    String event_id;
    @ColumnInfo(name = "fld_livestream_link")
    String livestream_link;
    @ColumnInfo(name = "fld_star")
    String star;
    @ColumnInfo(name = "fld_total_feedback")
    String total_feedback;
    @ColumnInfo(name = "fld_feedback_comment")
    String feedback_comment;
    @ColumnInfo(name = "fld_rated")
    String rated;
    @ColumnInfo(name = "fld_reminder_flag")
    String reminder_flag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getSession_name() {
        return session_name;
    }

    public void setSession_name(String session_name) {
        this.session_name = session_name;
    }

    public String getSession_short_description() {
        return session_short_description;
    }

    public void setSession_short_description(String session_short_description) {
        this.session_short_description = session_short_description;
    }

    public String getSession_description() {
        return session_description;
    }

    public void setSession_description(String session_description) {
        this.session_description = session_description;
    }

    public String getSession_start_time() {
        return session_start_time;
    }

    public void setSession_start_time(String session_start_time) {
        this.session_start_time = session_start_time;
    }

    public String getSession_end_time() {
        return session_end_time;
    }

    public void setSession_end_time(String session_end_time) {
        this.session_end_time = session_end_time;
    }

    public String getSession_date() {
        return session_date;
    }

    public void setSession_date(String session_date) {
        this.session_date = session_date;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getLivestream_link() {
        return livestream_link;
    }

    public void setLivestream_link(String livestream_link) {
        this.livestream_link = livestream_link;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getTotal_feedback() {
        return total_feedback;
    }

    public void setTotal_feedback(String total_feedback) {
        this.total_feedback = total_feedback;
    }

    public String getFeedback_comment() {
        return feedback_comment;
    }

    public void setFeedback_comment(String feedback_comment) {
        this.feedback_comment = feedback_comment;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getReminder_flag() {
        return reminder_flag;
    }
}
