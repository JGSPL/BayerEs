package com.procialize.eventapp.ui.newsfeed.viewmodel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Database.EventAppDB;
import com.procialize.eventapp.GetterSetter.Header;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.R;
import com.procialize.eventapp.ui.newsFeedComment.model.Comment;
import com.procialize.eventapp.ui.newsFeedComment.model.CommentDetail;
import com.procialize.eventapp.ui.newsFeedComment.model.LikePost;
import com.procialize.eventapp.ui.newsFeedComment.view.CommentActivity;
import com.procialize.eventapp.ui.newsFeedDetails.view.NewsFeedDetailsActivity;
import com.procialize.eventapp.ui.newsFeedLike.view.LikeActivity;
import com.procialize.eventapp.ui.newsFeedPost.roomDB.UploadMultimedia;
import com.procialize.eventapp.ui.newsFeedPost.service.BackgroundServiceToCompressMedia;
import com.procialize.eventapp.ui.newsfeed.model.FetchNewsfeedMultiple;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;
import com.procialize.eventapp.ui.newsfeed.networking.NewsfeedRepository;
import com.procialize.eventapp.ui.newsfeed.view.NewsFeedFragment;



import org.apache.commons.lang3.StringEscapeUtils;

import java.io.Serializable;
import java.util.List;


public class NewsFeedViewModel extends ViewModel {
    Dialog dialog, myDialog;
    private String ATTENDEE_STATUS = "0";
    private Activity activityVar;
    ConnectionDetector connectionDetector;
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
    MutableLiveData<LikePost> newsfeedLike= new MutableLiveData<>();

    //private MutableLiveData<Boolean> mIsUploading = new MutableLiveData<>();
    MutableLiveData<Boolean> isValid = new MutableLiveData<>();
    String noOfLikes = "0";
    String likeStatus="";
     public void init(String pagesize, String pagenumber) {
       /* if (mutableLiveData != null) {
            return;
        }*/


        newsRepository = NewsfeedRepository.getInstance();
        mutableLiveData = newsRepository.getNewsFeed("1", pagesize, pagenumber);

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

    //---------------View Like Action mechanism--------------------------------
    public void openLikeimg(Activity activity, String event_id, String newsfeedid, View v, Newsfeed_detail feed, int position, ImageView likeimage, TextView liketext) {
         activityVar = activity;
        newsRepository = NewsfeedRepository.getInstance();
        if (ConnectionDetector.getInstance(activityVar).isConnectingToInternet()) {
            newsfeedLike = newsRepository.PostLike(event_id, newsfeedid);

         newsRepository.getLikeActivity().observe((LifecycleOwner) activityVar, new Observer<LikePost>() {
              @Override
              public void onChanged(LikePost loginOrganizer) {
                if (loginOrganizer != null) {

                    List<Header> heaserList = loginOrganizer.getHeader();
                        String status = heaserList.get(0).getType();
                        if (status.equalsIgnoreCase("success")) {
                            likeStatus = loginOrganizer.getLike_status();
                            noOfLikes = liketext.getText().toString().split(" ")[0];
                            if (likeStatus.equalsIgnoreCase("1")) {
                                //newsFeedFragment.showLikeCount(Integer.parseInt(noOfLikes) + 1);
                                int LikeCount = Integer.parseInt(noOfLikes) + 1;
                                if (LikeCount == 1) {
                                    liketext.setText(LikeCount + " Like");
                                } else {
                                    liketext.setText(LikeCount + " Likes");
                                }
                                likeimage.setImageDrawable(activityVar.getDrawable(R.drawable.ic_active_like));
                                noOfLikes = "0";
                                likeStatus = "";
                            } else {
                                if (Integer.parseInt(noOfLikes) > 0) {
                                    int LikeCount = Integer.parseInt(noOfLikes) + 1;
                                    if (LikeCount == 1) {
                                        liketext.setText(LikeCount + " Like");
                                    } else {
                                        liketext.setText(LikeCount + " Likes");
                                    }
                                    likeimage.setImageDrawable(activityVar.getDrawable(R.drawable.ic_like));
                                    noOfLikes = "0";
                                }
                                noOfLikes = "0";
                                likeStatus = "";
                            }
                            Snackbar.make(NewsFeedFragment.cl_main, loginOrganizer.getHeader().get(0).getMsg(), Snackbar.LENGTH_SHORT).show();
                        }
                        /*if (commentViewModel != null && commentViewModel.likePostData().hasObservers()) {
                            commentViewModel.likePostData().removeObservers(CommentActivity.this);
                        }*/
                    }

                    Snackbar.make(NewsFeedFragment.cl_main, loginOrganizer.getHeader().get(0).getMsg(), Snackbar.LENGTH_SHORT).show();

                  }

          });
        } else {
        Snackbar.make(NewsFeedFragment.cl_main, "No Internet Connection", Snackbar.LENGTH_SHORT).show();
        }

    }

    ///------------------Open More dot features------------------
    public void openMoreDetails(Activity activity, Newsfeed_detail feed, int position) {
        activityVar = activity;
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
                deleteTv.setVisibility(View.VISIBLE);
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
                if (ConnectionDetector.getInstance(activityVar).isConnectingToInternet()) {
                    newsfeedHide = newsRepository.DeletePost("1", feed.getNews_feed_id());

                    newsRepository.getPostActivity().observe((LifecycleOwner) activity, new Observer<LoginOrganizer>() {
                        @Override
                        public void onChanged(LoginOrganizer loginOrganizer) {
                            if (loginOrganizer != null) {
                                List<Header> heaserList = loginOrganizer.getHeader();
                                Snackbar.make(NewsFeedFragment.cl_main, heaserList.get(0).getMsg(), Snackbar.LENGTH_SHORT).show();
                                dialog.cancel();

                            }
                        }
                    });
                } else {
                    dialog.cancel();
                    Snackbar.make(NewsFeedFragment.cl_main, "No Internet Connection", Snackbar.LENGTH_SHORT).show();
                }
            }

        });


        hideTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsRepository = NewsfeedRepository.getInstance();

                if (ConnectionDetector.getInstance(activityVar).isConnectingToInternet()) {
                    newsfeedHide = newsRepository.PostHide("1", feed.getNews_feed_id());

                    newsRepository.getPostActivity().observe((LifecycleOwner) activity, new Observer<LoginOrganizer>() {
                        @Override
                        public void onChanged(LoginOrganizer loginOrganizer) {
                            if (loginOrganizer != null) {
                                List<Header> heaserList = loginOrganizer.getHeader();
                                Snackbar.make(NewsFeedFragment.cl_main, heaserList.get(0).getMsg(), Snackbar.LENGTH_SHORT).show();
                                dialog.cancel();

                            }
                        }
                    });
                } else {
                    dialog.cancel();
                    Snackbar.make(NewsFeedFragment.cl_main, "No Internet Connection", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        reportTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showratedialouge("reportPost", feed.getNews_feed_id(),feed.getAttendee_id());
            }
        });


        reportuserTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showratedialouge("reportUser",feed.getNews_feed_id(), feed.getAttendee_id());
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
    public void startBackgroundService(Activity activity){//, List<UploadMultimedia> uploadMultimedia) {
        Log.d("Bg Service","Start Service");
        mIsUpdating.setValue(true);
        Intent intent = new Intent(activity, BackgroundServiceToCompressMedia.class);
        //intent.putExtra("MediaList", (Serializable) uploadMultimedia);
        activity.startService(intent);
    }

    public void stopBackgroundService(Activity activity){//, List<UploadMultimedia> uploadMultimedia) {
        Log.d("Bg Service","Stop Service");
        mIsUpdating.setValue(false);
    }

    //----------------Multimedia to compress------------------------
    public void getNonCompressesMultimedia(Activity activity) {
       // mIsUploading.setValue(false);
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
    public void sendPost(String event_id, String status, List<UploadMultimedia> resultList) {
       // mIsUploading.setValue(true);
        NewsfeedRepository postNewsFeedRepository = NewsfeedRepository.getInstance();
        multimediaUploadLiveData = postNewsFeedRepository.postNewsFeed(event_id, status, resultList);//,mediaFile,mediaFileThumb);
    }
    public MutableLiveData<LoginOrganizer> getPostStatus() {
       // mIsUploading.setValue(false);
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

    private void showratedialouge(final String from, final String id, final String attnId) {

        myDialog = new Dialog(activityVar);
        myDialog.setContentView(R.layout.dialouge_msg_layout);
        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id

        myDialog.show();


        Button cancelbtn = myDialog.findViewById(R.id.canclebtn);
        Button ratebtn = myDialog.findViewById(R.id.ratebtn);

        final EditText etmsg = myDialog.findViewById(R.id.etmsg);

        final TextView counttv = myDialog.findViewById(R.id.counttv);
        final TextView nametv = myDialog.findViewById(R.id.nametv);

        nametv.setText("To " + "Admin");

        etmsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                count = 250 - s.length();
                counttv.setText(count + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etmsg.getText().toString().length() > 0) {

                    String msg = StringEscapeUtils.escapeJava(etmsg.getText().toString());
                    dialog.cancel();
                    if (from.equalsIgnoreCase("reportPost")) {

                        if (ConnectionDetector.getInstance(activityVar).isConnectingToInternet()) {
                            newsfeedHide = newsRepository.ReportPost("1", id, msg);

                            newsRepository.getPostActivity().observe((LifecycleOwner) activityVar, new Observer<LoginOrganizer>() {
                                @Override
                                public void onChanged(LoginOrganizer loginOrganizer) {
                                    if (loginOrganizer != null) {
                                        List<Header> heaserList = loginOrganizer.getHeader();
                                        Snackbar.make(NewsFeedFragment.cl_main, heaserList.get(0).getMsg(), Snackbar.LENGTH_SHORT).show();
                                        myDialog.cancel();

                                    }
                                }
                            });
                        } else {
                            myDialog.cancel();
                            Snackbar.make(NewsFeedFragment.cl_main, "No Internet Connection", Snackbar.LENGTH_SHORT).show();
                        }
                    }else if (from.equalsIgnoreCase("reportUser")) {
                        /*newsfeedHide = newsRepository.ReportUser("1", attnId,id, msg);
                        myDialog.cancel();*/
                        if (ConnectionDetector.getInstance(activityVar).isConnectingToInternet()) {
                            newsfeedHide = newsRepository.ReportUser("1", attnId,id, msg);
                            newsRepository.getPostActivity().observe((LifecycleOwner) activityVar, new Observer<LoginOrganizer>() {
                                @Override
                                public void onChanged(LoginOrganizer loginOrganizer) {
                                    if (loginOrganizer != null) {
                                        List<Header> heaserList = loginOrganizer.getHeader();
                                        Snackbar.make(NewsFeedFragment.cl_main, heaserList.get(0).getMsg(), Snackbar.LENGTH_SHORT).show();
                                        myDialog.cancel();

                                    }
                                }
                            });
                        } else {
                            myDialog.cancel();
                            Snackbar.make(NewsFeedFragment.cl_main, "No Internet Connection", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }

                else {
                    Toast.makeText(activityVar, "Enter Something", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}