package com.procialize.bayer2020.ui.attendeeChat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChatCount implements Serializable {
    @SerializedName("ChatCount_receId")
    @Expose
    private String ChatCount_receId;

    @SerializedName("Chat_mess")
    @Expose
    private String Chat_mess;


    @SerializedName("chat_count_read")
    @Expose
    private String chat_count_read;

    @SerializedName("chat_count")
    @Expose
    private int chat_count;

    public int getChat_count() {
        return chat_count;
    }

    public void setChat_count(int chat_count) {
        this.chat_count = chat_count;
    }

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
}
