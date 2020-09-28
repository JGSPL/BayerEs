package com.procialize.eventapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.procialize.eventapp.ui.attendee.roomDB.AttendeeDao;
import com.procialize.eventapp.ui.attendee.roomDB.TableAttendee;
import com.procialize.eventapp.ui.newsFeedPost.roomDB.UploadMultimedia;
import com.procialize.eventapp.ui.newsFeedPost.roomDB.UploadMultimediaDao;
import com.procialize.eventapp.ui.newsfeed.roomDB.NewsFeedDao;
import com.procialize.eventapp.ui.newsfeed.roomDB.TableNewsFeed;
import com.procialize.eventapp.ui.newsfeed.roomDB.TableNewsFeedMedia;
import com.procialize.eventapp.ui.profile.roomDB.ProfileEventId;
import com.procialize.eventapp.ui.profile.roomDB.ProfileUpdateDao;
import com.procialize.eventapp.ui.speaker.roomDB.SpeakerDao;
import com.procialize.eventapp.ui.speaker.roomDB.TableSpeaker;

@Database(entities = {UploadMultimedia.class, TableNewsFeed.class, TableNewsFeedMedia.class,
        ProfileEventId.class, TableAttendee.class, TableSpeaker.class},
        version = 1, exportSchema = false)
public abstract class EventAppDB extends RoomDatabase {

    private static volatile EventAppDB eventAppDB;
    public abstract UploadMultimediaDao uploadMultimediaDao();
    public abstract NewsFeedDao newsFeedDao();
    public abstract ProfileUpdateDao profileUpdateDao();
    public abstract AttendeeDao attendeeDao();
    public abstract SpeakerDao speakerDao();

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
