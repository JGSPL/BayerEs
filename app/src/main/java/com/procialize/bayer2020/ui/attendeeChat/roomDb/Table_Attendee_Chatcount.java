package com.procialize.bayer2020.ui.attendeeChat.roomDb;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Table_Attendee_Chatcount")

public class Table_Attendee_Chatcount implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "Cld_id")
    int id;
    @ColumnInfo(name = "ChatCount_receId")
    String ChatCount_receId;
    @ColumnInfo(name = "Chat_mess")
    String Chat_mess;
    @ColumnInfo(name = "chat_count_read")
    String chat_count_read;
    @ColumnInfo(name = "chat_count")
    int chat_count;

    public String getChatCount_receId() {
        return ChatCount_receId;
    }

    public void setChatCount_receId(String chatCount_receId) {
        ChatCount_receId = chatCount_receId;
    }

    public String getChat_mess() {
        return Chat_mess;
    }

    public void setChat_mess(String chat_mess) {
        Chat_mess = chat_mess;
    }

    public String getChat_count_read() {
        return chat_count_read;
    }

    public void setChat_count_read(String chat_count_read) {
        this.chat_count_read = chat_count_read;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChat_count() {
        return chat_count;
    }

    public void setChat_count(int chat_count) {
        this.chat_count = chat_count;
    }
}
