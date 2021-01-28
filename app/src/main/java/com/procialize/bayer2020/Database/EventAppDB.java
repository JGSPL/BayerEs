package com.procialize.bayer2020.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.procialize.bayer2020.ui.agenda.roomDB.AgendaDao;
import com.procialize.bayer2020.ui.agenda.roomDB.TableAgenda;
import com.procialize.bayer2020.ui.attendee.roomDB.AttendeeDao;
import com.procialize.bayer2020.ui.attendee.roomDB.TableAttendee;
import com.procialize.bayer2020.ui.attendeeChat.roomDb.AttendeeChatDao;
import com.procialize.bayer2020.ui.attendeeChat.roomDb.Table_Attendee_Chatcount;
import com.procialize.bayer2020.ui.eventinfo.roomDB.EventInfoDao;
import com.procialize.bayer2020.ui.eventinfo.roomDB.TableEventInfo;
import com.procialize.bayer2020.ui.newsFeedPost.roomDB.NewsFeedUniqueIdUploadedStarted;
import com.procialize.bayer2020.ui.newsFeedPost.roomDB.NewsFeedUniqueIdUploadedStartedDao;
import com.procialize.bayer2020.ui.newsFeedPost.roomDB.UploadMultimedia;
import com.procialize.bayer2020.ui.newsFeedPost.roomDB.UploadMultimediaDao;
import com.procialize.bayer2020.ui.newsfeed.roomDB.NewsFeedDao;
import com.procialize.bayer2020.ui.newsfeed.roomDB.TableNewsFeed;
import com.procialize.bayer2020.ui.newsfeed.roomDB.TableNewsFeedMedia;
import com.procialize.bayer2020.ui.profile.roomDB.ProfileEventId;
import com.procialize.bayer2020.ui.profile.roomDB.ProfileUpdateDao;
import com.procialize.bayer2020.ui.speaker.roomDB.SpeakerDao;
import com.procialize.bayer2020.ui.speaker.roomDB.TableSpeaker;

@Database(entities = {UploadMultimedia.class, TableNewsFeed.class, TableNewsFeedMedia.class,
        ProfileEventId.class, TableAttendee.class, TableSpeaker.class, TableAgenda.class,
        TableEventInfo.class, Table_Attendee_Chatcount.class, NewsFeedUniqueIdUploadedStarted.class, com.procialize.bayer2020.ui.qapost.roomDB.UploadMultimedia.class, com.procialize.bayer2020.ui.qapost.roomDB.NewsFeedUniqueIdUploadedStarted.class},
        version = 1, exportSchema = false)
public abstract class EventAppDB extends RoomDatabase {

    private static volatile EventAppDB eventAppDB;
    public abstract UploadMultimediaDao uploadMultimediaDao();
    public abstract com.procialize.bayer2020.ui.qapost.roomDB.UploadMultimediaDao uploadMultimediaQaDao();

    public abstract NewsFeedDao newsFeedDao();
    public abstract ProfileUpdateDao profileUpdateDao();
    public abstract AttendeeDao attendeeDao();
    public abstract SpeakerDao speakerDao();
    public abstract AgendaDao agendaDao();
    public abstract EventInfoDao eventInfoDao();
    public abstract AttendeeChatDao attendeeChatDao();
    public abstract NewsFeedUniqueIdUploadedStartedDao newsFeedUniqueIdUploadedStartedDao();
    public abstract com.procialize.bayer2020.ui.qapost.roomDB.NewsFeedUniqueIdUploadedStartedDao qaIdUploadedStartedDao();

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
