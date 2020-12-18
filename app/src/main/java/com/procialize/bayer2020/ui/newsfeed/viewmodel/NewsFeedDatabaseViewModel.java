package com.procialize.bayer2020.ui.newsfeed.viewmodel;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.procialize.bayer2020.Database.EventAppDB;
import com.procialize.bayer2020.ui.attendee.roomDB.TableAttendee;
import com.procialize.bayer2020.ui.newsfeed.model.News_feed_media;
import com.procialize.bayer2020.ui.newsfeed.model.Newsfeed_detail;
import com.procialize.bayer2020.ui.newsfeed.roomDB.TableNewsFeed;
import com.procialize.bayer2020.ui.newsfeed.roomDB.TableNewsFeedMedia;

import java.util.List;

public class NewsFeedDatabaseViewModel extends ViewModel {
    EventAppDB eventAppDB;
    private LiveData<List<TableNewsFeed>> newsFeedData = new MutableLiveData<>();
    private LiveData<List<TableNewsFeedMedia>> newsFeedMediaData;// = new MutableLiveData<>();
    private LiveData<List<TableAttendee>> attendeeDetail;
    public void insertIntoDb(Context context, List<Newsfeed_detail> newsfeedDetails) {
        eventAppDB = EventAppDB.getDatabase(context);

        for (int i = 0; i < newsfeedDetails.size(); i++) {
            TableNewsFeed tableNewsFeed = new TableNewsFeed();
            tableNewsFeed.setNews_feed_id(newsfeedDetails.get(i).getNews_feed_id());
            tableNewsFeed.setType(newsfeedDetails.get(i).getType());
            tableNewsFeed.setPost_status(newsfeedDetails.get(i).getPost_status());
            tableNewsFeed.setEvent_id(newsfeedDetails.get(i).getEvent_id());
            tableNewsFeed.setPost_date(newsfeedDetails.get(i).getPost_date());
            tableNewsFeed.setFirst_name(newsfeedDetails.get(i).getFirst_name());
            tableNewsFeed.setLast_name(newsfeedDetails.get(i).getLast_name());
            tableNewsFeed.setCompany_name(newsfeedDetails.get(i).getCompany_name());
            tableNewsFeed.setDesignation(newsfeedDetails.get(i).getDesignation());
            tableNewsFeed.setCity_id(newsfeedDetails.get(i).getCity_id());
            tableNewsFeed.setProfile_pic(newsfeedDetails.get(i).getProfile_pic());
            tableNewsFeed.setAttendee_id(newsfeedDetails.get(i).getAttendee_id());
            tableNewsFeed.setAttendee_type(newsfeedDetails.get(i).getAttendee_type());
            tableNewsFeed.setLike_flag(newsfeedDetails.get(i).getLike_flag());
            tableNewsFeed.setLike_type(newsfeedDetails.get(i).getLike_type());
            tableNewsFeed.setTotal_likes(newsfeedDetails.get(i).getTotal_likes());
            tableNewsFeed.setTotal_comments(newsfeedDetails.get(i).getTotal_comments());

            List<News_feed_media> news_feed_media = newsfeedDetails.get(i).getNews_feed_media();
            for (int k = 0; k < news_feed_media.size(); k++) {
                TableNewsFeedMedia tableNewsFeedMedia = new TableNewsFeedMedia();
                tableNewsFeedMedia.setMedia_id(news_feed_media.get(k).getMedia_id());
                tableNewsFeedMedia.setNews_feed_id(news_feed_media.get(k).getNews_feed_id());
                tableNewsFeedMedia.setMedia_type(news_feed_media.get(k).getMedia_type());
                tableNewsFeedMedia.setMedia_file(news_feed_media.get(k).getMedia_file());
                tableNewsFeedMedia.setThumb_image(news_feed_media.get(k).getThumb_image());
                tableNewsFeedMedia.setWidth(news_feed_media.get(k).getWidth());
                tableNewsFeedMedia.setHeight(news_feed_media.get(k).getHeight());

                insertMediaIntoDb(tableNewsFeedMedia);
            }
            eventAppDB.newsFeedDao().insertNewsFeed(tableNewsFeed);
        }
    }

    public void insertMediaIntoDb(TableNewsFeedMedia tableNewsFeedMedia)
    {
        eventAppDB.newsFeedDao().insertNewsFeedMedia(tableNewsFeedMedia);
    }

    public void getNewsFeed(Activity activity) {
        EventAppDB eventAppDB = EventAppDB.getDatabase(activity);
        if(eventAppDB.newsFeedDao().getNewsFeed()!=null)
        newsFeedData = eventAppDB.newsFeedDao().getNewsFeed();
    }

    public void getNewsFeedMedia(Activity activity,String newsFeedId) {
        // mIsUploading.setValue(false);
        EventAppDB eventAppDB = EventAppDB.getDatabase(activity);
        newsFeedMediaData = eventAppDB.newsFeedDao().getNewsFeedMedia(newsFeedId);
    }

    public LiveData<List<TableNewsFeed>> getNewsFeedList() {
        return newsFeedData;
    }

    public LiveData<List<TableNewsFeedMedia>> getNewsFeedMediaDataList(Activity activity,String newsFeedId) {
        if(newsFeedMediaData.getValue()==null) {
            EventAppDB eventAppDB = EventAppDB.getDatabase(activity);
            newsFeedMediaData = eventAppDB.newsFeedDao().getNewsFeedMedia(newsFeedId);
        }
        return newsFeedMediaData;
    }

    public void deleteNewsFeedMediaDataList(Activity activity) {
        EventAppDB eventAppDB = EventAppDB.getDatabase(activity);
        eventAppDB.newsFeedDao().deleteNewsFeedMedia();
        eventAppDB.newsFeedDao().deleteNewsFeed();
        return ;
    }

    public void getAttendeeDetailsFromId(Context context,String attendeeid)
    {
        eventAppDB = EventAppDB.getDatabase(context);
        attendeeDetail = eventAppDB.attendeeDao().getAttendeeDetailsFromId(attendeeid);
    }

    public LiveData<List<TableAttendee>> getAttendeeDetails() {
        return attendeeDetail;
    }
}
