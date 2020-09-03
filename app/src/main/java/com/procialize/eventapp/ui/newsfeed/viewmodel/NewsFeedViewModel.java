package com.procialize.eventapp.ui.newsfeed.viewmodel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.procialize.eventapp.Database.EventAppDB;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.R;
import com.procialize.eventapp.ui.newsFeedComment.view.CommentActivity;
import com.procialize.eventapp.ui.newsFeedDetails.view.NewsFeedDetailsActivity;
import com.procialize.eventapp.ui.newsFeedLike.view.LikeActivity;
import com.procialize.eventapp.ui.newsFeedPost.roomDB.UploadMultimedia;
import com.procialize.eventapp.ui.newsFeedPost.service.BackgroundServiceToCompressMedia;
import com.procialize.eventapp.ui.newsfeed.model.FetchNewsfeedMultiple;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;
import com.procialize.eventapp.ui.newsfeed.networking.NewsfeedRepository;

import java.io.Serializable;
import java.util.List;


public class NewsFeedViewModel extends ViewModel {
    Dialog dialog;
    private String ATTENDEE_STATUS = "0";

    private MutableLiveData<FetchNewsfeedMultiple> mutableLiveData = new MutableLiveData<>();
    private NewsfeedRepository newsRepository;
    private LiveData<List<UploadMultimedia>> nonCompressedMultimediaMutableLiveData = new MutableLiveData<>();
    private LiveData<List<UploadMultimedia>> newsFeedDataToUpload = new MutableLiveData<>();
    private LiveData<List<String>> folderUniqueIdMutableLiveData = new MutableLiveData<>();
    MutableLiveData<LoginOrganizer> multimediaUploadLiveData = new MutableLiveData<>();
    MutableLiveData<Boolean> isUpdatedIntoDB = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsUpdating = new MutableLiveData<>();
    MutableLiveData<LoginOrganizer> newsfeedReport = new MutableLiveData<>();
    MutableLiveData<LoginOrganizer> newsfeedHide = new MutableLiveData<>();


    public void init() {
       /* if (mutableLiveData != null) {
            return;
        }*/
        newsRepository = NewsfeedRepository.getInstance();
        mutableLiveData = newsRepository.getNewsFeed("1", "30", "1");
    }

    public LiveData<FetchNewsfeedMultiple> getNewsRepository() {
        return mutableLiveData;
    }

    //---------------View News feed details--------------------------------
    public void openNewsFeedDetails(Activity activity, Newsfeed_detail feed, int position) {
        activity.startActivity(new Intent(activity, NewsFeedDetailsActivity.class)
                .putExtra("Newsfeed_detail", (Serializable) feed)
                .putExtra("position", "" + position));
    }

    //---------------View News feed details--------------------------------
    public void openFeedDetails(Activity activity, Newsfeed_detail feed, int position) {
        activity.startActivity(new Intent(activity, NewsFeedDetailsActivity.class)
                .putExtra("Newsfeed_detail", (Serializable) feed)
                .putExtra("position", "" + position));
    }

    //---------------View Comment details--------------------------------
    public void openCommentPage(Activity activity, Newsfeed_detail feed, int position) {
        activity.startActivity(new Intent(activity, CommentActivity.class)
                .putExtra("Newsfeed_detail", (Serializable) feed)
                .putExtra("position", "" + position));
    }

    //---------------View Like details--------------------------------
    public void openLikePage(Activity activity, Newsfeed_detail feed, int position) {
        activity.startActivity(new Intent(activity, LikeActivity.class)
                .putExtra("Newsfeed_detail", (Serializable) feed)
                .putExtra("position", "" + position));
    }

    ///------------------Open More dot features------------------
    public void openMoreDetails(Activity activity, Newsfeed_detail feed, int position) {
        /*activity.startActivity(new Intent(activity, LikeActivity.class)
                .putExtra("Newsfeed_detail", (Serializable) feed)
                .putExtra("position", "" + position));*/
        dialog = new BottomSheetDialog(activity);
        dialog.setContentView(R.layout.botomfeeddialouge);


        TextView reportTv = dialog.findViewById(R.id.reportTv);
        TextView hideTv = dialog.findViewById(R.id.hideTv);
        TextView deleteTv = dialog.findViewById(R.id.deleteTv);
        TextView reportuserTv = dialog.findViewById(R.id.reportuserTv);
        TextView blockuserTv = dialog.findViewById(R.id.blockuserTv);
        TextView cancelTv = dialog.findViewById(R.id.cancelTv);
        TextView editIV = dialog.findViewById(R.id.editIV);


        if (/*user.get(SessionManager.ATTENDEE_STATUS)*/ATTENDEE_STATUS.equalsIgnoreCase("1")) {
            if (/*user.get(SessionManager.KEY_ID).equalsIgnoreCase(feed.getAttendeeId())*/ATTENDEE_STATUS.equalsIgnoreCase("1")) {
                deleteTv.setVisibility(View.VISIBLE);
                //editIV.setVisibility(View.VISIBLE);
                hideTv.setVisibility(View.GONE);
                reportTv.setVisibility(View.GONE);
                reportuserTv.setVisibility(View.GONE);
                blockuserTv.setVisibility(View.GONE);

                if (feed.getType().equalsIgnoreCase("Video")) {
                    editIV.setVisibility(View.GONE);

                } else {
                    editIV.setVisibility(View.GONE);

                }
            } else {
                deleteTv.setVisibility(View.VISIBLE);
                //editIV.setVisibility(View.VISIBLE);
                hideTv.setVisibility(View.GONE);
                reportTv.setVisibility(View.GONE);
                reportuserTv.setVisibility(View.GONE);
                blockuserTv.setVisibility(View.GONE);
                editIV.setVisibility(View.GONE);
            }

        } else {
            if (/*user.get(SessionManager.KEY_ID).equalsIgnoreCase(feed.getAttendeeId())*/ATTENDEE_STATUS.equalsIgnoreCase("1")) {
                deleteTv.setVisibility(View.VISIBLE);
                //editIV.setVisibility(View.VISIBLE);
                hideTv.setVisibility(View.GONE);
                reportTv.setVisibility(View.GONE);
                reportuserTv.setVisibility(View.GONE);
                blockuserTv.setVisibility(View.GONE);

                if (feed.getType().equalsIgnoreCase("Video")) {
                    editIV.setVisibility(View.GONE);

                } else {
                    editIV.setVisibility(View.GONE);

                }

            } else {
                deleteTv.setVisibility(View.GONE);
                editIV.setVisibility(View.GONE);
                hideTv.setVisibility(View.VISIBLE);
                reportTv.setVisibility(View.VISIBLE);
                reportuserTv.setVisibility(View.VISIBLE);
                blockuserTv.setVisibility(View.VISIBLE);
            }

        }
        deleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PostDelete(eventid, feed.getNewsFeedId(), token, position);
            }
        });


        hideTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // ReportPostHide(eventid, feed.getNewsFeedId(), token, position);
                newsRepository = NewsfeedRepository.getInstance();
                newsfeedHide = newsRepository.PostHide("1", feed.getNews_feed_id());
                dialog.cancel();
            }
        });

        reportTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showratedialouge("reportPost", feed.getNewsFeedId());
            }
        });


        reportuserTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showratedialouge("reportUser", feed.getAttendeeId());
            }
        });

        blockuserTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // ReportUserHide(eventid, feed.getAttendeeId(), token);

            }
        });


        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    //--------------Start Background service to compress media------------------------
    public void startBackgroundService(Activity activity, List<UploadMultimedia> uploadMultimedia) {
        mIsUpdating.setValue(true);
        Intent intent = new Intent(activity, BackgroundServiceToCompressMedia.class);
        intent.putExtra("MediaList", (Serializable) uploadMultimedia);
        activity.startService(intent);
    }


    //----------------Multimedia to compress------------------------
    public void getNonCompressesMultimedia(Activity activity) {
        EventAppDB eventAppDB = EventAppDB.getDatabase(activity);
        nonCompressedMultimediaMutableLiveData = eventAppDB.uploadMultimediaDao().getNonCompressesMultimedia();
    }

    public LiveData<List<UploadMultimedia>> getNonCompressedMedia() {
        return nonCompressedMultimediaMutableLiveData;
    }

    //----------------get Folder Unique Ids------------------------
    public void getFolderUniqueId(Activity activity) {
        EventAppDB eventAppDB = EventAppDB.getDatabase(activity);
        folderUniqueIdMutableLiveData = eventAppDB.uploadMultimediaDao().selectFolderUniqueId();
    }

    public LiveData<List<String>> getFolderIdList() {
        return folderUniqueIdMutableLiveData;
    }

    //----------------Multimedia to compress------------------------
    public void getNewsFeedDataAccrodingToFolderUniqueId(Activity activity,String folderUniqueId) {
        EventAppDB eventAppDB = EventAppDB.getDatabase(activity);
        newsFeedDataToUpload = eventAppDB.uploadMultimediaDao().getMultimediaToUpload(folderUniqueId);
    }

    public LiveData<List<UploadMultimedia>> getNewsFeedToUpload() {

        return newsFeedDataToUpload;
    }

    //-------------call to upload newsfeed---------------------
    public void sendPost(String event_id, String status, List<UploadMultimedia> resultList) {//, String[] mediaFile, String[] mediaFileThumb) {
        NewsfeedRepository postNewsFeedRepository = NewsfeedRepository.getInstance();
        multimediaUploadLiveData = postNewsFeedRepository.postNewsFeed(event_id, status, resultList);//,mediaFile,mediaFileThumb);
    }
    public MutableLiveData<LoginOrganizer> getPostStatus() {
        mIsUpdating.setValue(false);
        return multimediaUploadLiveData;
    }

    //---------------Upldate uploaded flag to 1------------
    public void updateisUplodedIntoDB(Context context,String folderUniqueId) {
        NewsfeedRepository newsfeedRepository = NewsfeedRepository.getInstance();
        isUpdatedIntoDB = newsfeedRepository.updateIsUplodedIntoDb(context,folderUniqueId);
    }

    public MutableLiveData<Boolean> getIsUplodedStatus() {
        return isUpdatedIntoDB;
    }

    //----------------------------------------------------------------
    public LiveData<Boolean> getIsUpdating(){
        return mIsUpdating;
    }

}