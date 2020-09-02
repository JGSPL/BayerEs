package com.procialize.eventapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.procialize.eventapp.ui.newsFeedPost.roomDB.UploadMultimedia;
import com.procialize.eventapp.ui.newsFeedPost.roomDB.UploadMultimediaDao;

@Database(entities = {UploadMultimedia.class}, version = 1, exportSchema = false)
public abstract class EventAppDB extends RoomDatabase {

    private static volatile EventAppDB eventAppDB;
    public abstract UploadMultimediaDao uploadMultimediaDao();

    public static EventAppDB getDatabase(final Context context) {
        if (eventAppDB == null) {
            synchronized (EventAppDB.class) {
                if (eventAppDB == null) {
                    eventAppDB = Room.databaseBuilder(context.getApplicationContext(),
                            EventAppDB.class, "EventApp_database.db").allowMainThreadQueries()
                           .build();
                }
            }
        }
        return eventAppDB;
    }


}
