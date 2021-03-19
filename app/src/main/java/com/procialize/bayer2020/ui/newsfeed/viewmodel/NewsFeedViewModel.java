package com.procialize.bayer2020.ui.newsfeed.viewmodel;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.procialize.bayer2020.BuildConfig;
import com.procialize.bayer2020.ConnectionDetector;
import com.procialize.bayer2020.Constants.APIService;
import com.procialize.bayer2020.Constants.Constant;
import com.procialize.bayer2020.Database.EventAppDB;
import com.procialize.bayer2020.GetterSetter.Header;
import com.procialize.bayer2020.GetterSetter.LoginOrganizer;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.newsFeedComment.model.LikePost;
import com.procialize.bayer2020.ui.newsFeedComment.view.CommentActivity;
import com.procialize.bayer2020.ui.newsFeedDetails.view.NewsFeedDetailsActivity;
import com.procialize.bayer2020.ui.newsFeedLike.view.LikeActivity;
import com.procialize.bayer2020.ui.newsFeedPost.networking.PostNewsFeedRepository;
import com.procialize.bayer2020.ui.newsFeedPost.roomDB.UploadMultimedia;
import com.procialize.bayer2020.ui.newsFeedPost.service.BackgroundServiceToCompressMedia;
import com.procialize.bayer2020.ui.newsfeed.adapter.NewsFeedAdapter;
import com.procialize.bayer2020.ui.newsfeed.model.FetchNewsfeedMultiple;
import com.procialize.bayer2020.ui.newsfeed.model.News_feed_media;
import com.procialize.bayer2020.ui.newsfeed.model.Newsfeed_detail;
import com.procialize.bayer2020.ui.newsfeed.networking.NewsfeedRepository;
import com.procialize.bayer2020.ui.newsfeed.view.NewsFeedFragment;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.IS_GOD;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_ATTENDEE_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.NEWS_FEED_MEDIA_PATH;


public class NewsFeedViewModel extends ViewModel {
    Dialog dialog, myDialog;
    ProgressDialog dialogShare;
    private String ATTENDEE_STATUS = "0";
    private String ATTENDEE_ID = "";
    private Activity activityVar;
    ConnectionDetector connectionDetector;
    private MutableLiveData<FetchNewsfeedMultiple> mutableLiveData = new MutableLiveData<>();
    private NewsfeedRepository newsRepository;
    private LiveData<List<UploadMultimedia>> nonCompressedMultimediaMutableLiveData = new MutableLiveData<>();
    private LiveData<List<UploadMultimedia>> newsFeedDataToUpload = new MutableLiveData<>();
    private LiveData<List<String>> folderUniqueIdMutableLiveData = new MutableLiveData<>();
    private LiveData<Integer> folderUniqueIdCount = new MutableLiveData<>();
    MutableLiveData<LoginOrganizer> multimediaUploadLiveData = new MutableLiveData<>();
    MutableLiveData<Boolean> isUpdatedIntoDB = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsUpdating = new MutableLiveData<>();
    MutableLiveData<LoginOrganizer> newsfeedReport = new MutableLiveData<>();
    MutableLiveData<LoginOrganizer> newsfeedHide = new MutableLiveData<>();
    MutableLiveData<LikePost> newsfeedLike = new MutableLiveData<>();

    //private MutableLiveData<Boolean> mIsUploading = new MutableLiveData<>();
    MutableLiveData<Boolean> isValid = new MutableLiveData<>();
    String noOfLikes = "0";
    String likeStatus = "";
    String strPath = "", mediaPath = "",dataToShare="";
    NewsFeedAdapter adapter;
    MutableLiveData<LikePost> liketPostUpdate = new MutableLiveData<>();


    private APIService newsfeedApi;


    public void init(Activity activity, String token, String eventId, String pagesize, String pagenumber) {
        activityVar = activity;
      /* if (mutableLiveData != null) {
            mutableLiveData.setValue(null);
        }*/


        newsRepository = NewsfeedRepository.getInstance();
        mutableLiveData = newsRepository.getNewsFeed(token, eventId, pagesize, pagenumber);

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
    public void openCommentPage(Activity activity, Newsfeed_detail feed, int position, int swipeablePosition) {
        activity.startActivity(new Intent(activity, CommentActivity.class)
                .putExtra("Newsfeed_detail", (Serializable) feed)
                .putExtra("newsfeedId", feed.getNews_feed_id())
                .putExtra("positionOfList", position+"")
                .putExtra("position", "" + swipeablePosition));
    }

    //---------------View Like details--------------------------------
    public void openLikePage(Activity activity, Newsfeed_detail feed, int position) {
        activity.startActivity(new Intent(activity, LikeActivity.class)
                .putExtra("Newsfeed_detail", (Serializable) feed)
                .putExtra("position", "" + position));
    }

    //---------------View Share details--------------------------------
    public void openShareTask(Activity activity, final Newsfeed_detail feed, int position) {
        activityVar = activity;
        final List<News_feed_media> newsFeedMedia = feed.getNews_feed_media();
        mediaPath = SharedPreference.getPref(activity, NEWS_FEED_MEDIA_PATH);

        if (feed != null) {
            if (ConnectionDetector.getInstance(activityVar).isConnectingToInternet()) {
                if (newsFeedMedia.size() > 0) {

                    if (newsFeedMedia.size() < position) {
                        position = 0;
                    }
                    if (newsFeedMedia.get(position).getMedia_type().equalsIgnoreCase("Video")) {
                        boolean isPresentFile = false;
                        File dir = new File(Environment.getExternalStorageDirectory().toString() + "/" + Constant.FOLDER_DIRECTORY);
                        if (dir.isDirectory()) {
                            String[] children = dir.list();
                            for (int i = 0; i < children.length; i++) {
                                String filename = children[i].toString();
                                if (newsFeedMedia.get(position).getMedia_file().equals(filename)) {
                                    isPresentFile = true;
                                }
                            }
                        }

                        if (!isPresentFile) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle("Download and Share");
                            builder.setMessage("Video will be share only after download,\nDo you want to continue for download and share?");
                            builder.setNegativeButton("NO",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            final int finalPosition = position;
                            builder.setPositiveButton("YES",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            String[] dataTOShare = new String[2];
                                            dataTOShare[0]=mediaPath + newsFeedMedia.get(finalPosition).getMedia_file();
                                            dataTOShare[1]=feed.getPost_status();
                                            new DownloadFile().execute(dataTOShare/*mediaPath + newsFeedMedia.get(finalPosition).getMedia_file()*/);
                                        }
                                    });
                            builder.show();

                        } else if (isPresentFile) {
                            String folder = Environment.getExternalStorageDirectory().toString() + Constant.FOLDER_DIRECTORY + "/";
                            //Create androiddeft folder if it does not exist
                            File directory = new File(folder);
                            if (!directory.exists()) {
                                directory.mkdirs();
                            }
                            strPath = folder + newsFeedMedia.get(position).getMedia_file();
                            Uri contentUri = FileProvider.getUriForFile(activity,

                                    BuildConfig.APPLICATION_ID + ".android.fileprovider", new File(strPath));

                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.setType("video/*");
                            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Shared via Pest Expert");
                            //sharingIntent.putExtra(Intent.EXTRA_TEXT, "Shared via Pest Expert");
                           // sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://bayer2020.page.link/newsfeed");
                            sharingIntent.putExtra(Intent.EXTRA_TEXT, "See Bayer Premise on Pest Expert 360° App.  "+"https://bayer2020.page.link/newsfeed" +
                                    ". You will be able to check more details like product USP, pests to control, dosage and application rate, etc. Check it out now. Download free Pest Expert 360° App.");
                            sharingIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                            activity.startActivity(Intent.createChooser(sharingIntent, "Shared via Pest Expert"));


                        }
                    } else {

                        shareImage(/*feed.getPost_date() + "\n" +*/ feed.getPost_status(), mediaPath + newsFeedMedia.get(position).getMedia_file().trim(), activityVar);
                    }
                } else {

                    shareTextUrl(/*feed.getPost_date() + "\n" +*/ feed.getPost_status(), StringEscapeUtils.unescapeJava(feed.getPost_status()));
                }
            } else {
                //Toast.makeText(activity, "No Internet Connection.", Toast.LENGTH_SHORT).show();
                Snackbar.make(NewsFeedFragment.cl_main, "No Internet Connection.", Snackbar.LENGTH_SHORT).show();

            }
        }
    }


    //---------------View Like Action mechanism--------------------------------
    public void openLikeimg(Activity activity, String token, String event_id, String newsfeedid, View v,
                            Newsfeed_detail feed, int position, final ImageView likeimage, final TextView liketext) {
        activityVar = activity;
        newsRepository = NewsfeedRepository.getInstance();
        if (ConnectionDetector.getInstance(activityVar).isConnectingToInternet()) {
            newsfeedLike = newsRepository.PostLike(token, event_id, newsfeedid);

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
                                LikeCount = 0;
                            } else {
                                if (Integer.parseInt(noOfLikes) > 0) {
                                    int LikeCount = Integer.parseInt(noOfLikes) - 1;
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
                    }

                    Snackbar.make(NewsFeedFragment.cl_main, loginOrganizer.getHeader().get(0).getMsg(), Snackbar.LENGTH_SHORT).show();
                    if (newsRepository.getLikeActivity().hasActiveObservers()) {

                        newsRepository.getLikeActivity().removeObservers((LifecycleOwner) activityVar);
                    }

                }

            });
        } else {
            Snackbar.make(NewsFeedFragment.cl_main, "No Internet Connection", Snackbar.LENGTH_SHORT).show();
        }

    }

    ///------------------Open More dot features------------------
    public void openMoreDetails(final Activity activity, final Newsfeed_detail feed, final int position,
                                final String token, final String eventId, NewsFeedAdapter newsadapter) {

        ATTENDEE_STATUS = SharedPreference.getPref(activity, IS_GOD);
        ATTENDEE_ID = SharedPreference.getPref(activity, KEY_ATTENDEE_ID);
        activityVar = activity;
        adapter = newsadapter;
        dialog = new BottomSheetDialog(activity);
        dialog.setContentView(R.layout.botomfeeddialouge);


        TextView reportTv = dialog.findViewById(R.id.reportTv);
        TextView hideTv = dialog.findViewById(R.id.hideTv);
        TextView deleteTv = dialog.findViewById(R.id.deleteTv);
        TextView reportuserTv = dialog.findViewById(R.id.reportuserTv);
        TextView blockuserTv = dialog.findViewById(R.id.blockuserTv);
        TextView cancelTv = dialog.findViewById(R.id.cancelTv);
        //TextView editIV = dialog.findViewById(R.id.editIV);


        if (ATTENDEE_STATUS.equalsIgnoreCase("1")) {
            reportTv.setVisibility(View.VISIBLE);
            hideTv.setVisibility(View.VISIBLE);
            deleteTv.setVisibility(View.VISIBLE);
            reportuserTv.setVisibility(View.VISIBLE);
            //blockuserTv.setVisibility(View.VISIBLE);
            cancelTv.setVisibility(View.VISIBLE);
            //editIV.setVisibility(View.VISIBLE);
        } else {
            if (ATTENDEE_ID.equalsIgnoreCase(feed.getAttendee_id())) {
                reportTv.setVisibility(View.GONE);
                hideTv.setVisibility(View.GONE);
                reportuserTv.setVisibility(View.GONE);
                //blockuserTv.setVisibility(View.GONE);

                deleteTv.setVisibility(View.VISIBLE);
                cancelTv.setVisibility(View.VISIBLE);
                //editIV.setVisibility(View.VISIBLE);
            } else {
                reportTv.setVisibility(View.VISIBLE);
                hideTv.setVisibility(View.VISIBLE);
                reportuserTv.setVisibility(View.VISIBLE);
                //blockuserTv.setVisibility(View.VISIBLE);

                deleteTv.setVisibility(View.GONE);
                cancelTv.setVisibility(View.VISIBLE);
            }
        }
        deleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectionDetector.getInstance(activityVar).isConnectingToInternet()) {
                    newsRepository = NewsfeedRepository.getInstance();
                    newsfeedHide = newsRepository.DeletePost(token, eventId, feed.getNews_feed_id());

                    newsRepository.getPostActivity().observe((LifecycleOwner) activity, new Observer<LoginOrganizer>() {
                        @Override
                        public void onChanged(LoginOrganizer loginOrganizer) {
                            if (loginOrganizer != null) {
                                adapter.getNewsFeedList().remove(position);
                                // remove the item from recycler view
                                // feedAdapter.removeItem(viewHolder.getAdapterPosition());
                                adapter.notifyItemRemoved(position);
                                adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                                List<Header> heaserList = loginOrganizer.getHeader();
                                Utility.createShortSnackBar(NewsFeedFragment.cl_main, heaserList.get(0).getMsg());
                                dialog.cancel();
                            }

                            if (newsRepository != null && newsRepository.getPostActivity().hasObservers()) {
                                newsRepository.getPostActivity().removeObservers((LifecycleOwner) activity);
                            }
                        }
                    });
                } else {
                    dialog.cancel();
                    Utility.createShortSnackBar(NewsFeedFragment.cl_main, "No Internet Connection");
                }
            }
        });


        hideTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsRepository = NewsfeedRepository.getInstance();

                if (ConnectionDetector.getInstance(activityVar).isConnectingToInternet()) {

                    newsfeedHide = newsRepository.PostHide(token, eventId, feed.getNews_feed_id());
                    newsRepository.getPostActivity().observe((LifecycleOwner) activity, new Observer<LoginOrganizer>() {
                        @Override
                        public void onChanged(LoginOrganizer loginOrganizer) {
                            if (loginOrganizer != null) {
                                adapter.getNewsFeedList().remove(position);
                                // remove the item from recycler view
                                // feedAdapter.removeItem(viewHolder.getAdapterPosition());
                                adapter.notifyItemRemoved(position);
                                List<Header> heaserList = loginOrganizer.getHeader();
                                Utility.createShortSnackBar(NewsFeedFragment.cl_main, heaserList.get(0).getMsg());
                                dialog.cancel();
                            }

                            if (newsRepository != null && newsRepository.getPostActivity().hasObservers()) {
                                newsRepository.getPostActivity().removeObservers((LifecycleOwner) activity);
                            }
                        }
                    });

                    /*newsfeedApi = ApiUtils.getAPIService();

                    newsfeedApi.PostHide(token, eventId, feed.getNews_feed_id()).enqueue(new Callback<LoginOrganizer>() {
                        @Override
                        public void onResponse(Call<LoginOrganizer> call,
                                               Response<LoginOrganizer> response) {
                            if (response.isSuccessful()) {
                                adapter.getNewsFeedList().remove(position);
                                // remove the item from recycler view
                                // feedAdapter.removeItem(viewHolder.getAdapterPosition());
                                adapter.notifyItemRemoved(position);
                                List<Header> heaserList = response.body().getHeader();
                                Utility.createShortSnackBar(NewsFeedFragment.cl_main, heaserList.get(0).getMsg());
                                dialog.cancel();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginOrganizer> call, Throwable t) {
                            dialog.cancel();
                            Utility.createShortSnackBar(NewsFeedFragment.cl_main, "Failure");
                        }
                    });*/
                } else {
                    dialog.cancel();
                    Utility.createShortSnackBar(NewsFeedFragment.cl_main, "No Internet Connection");
                }
            }
        });

        reportTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showratedialouge(v.getContext(), token, "reportPost", feed.getNews_feed_id(), feed.getAttendee_id(), eventId,"Report Post");
            }
        });

        reportuserTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showratedialouge(v.getContext(), token, "reportUser", feed.getNews_feed_id(), feed.getAttendee_id(), eventId, "Report User");
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
    public void startBackgroundService(Activity activity) {//, List<UploadMultimedia> uploadMultimedia) {
        try {
            Log.d("Bg Service", "Start Service");
            mIsUpdating.setValue(true);
            Intent intent = new Intent(activity, BackgroundServiceToCompressMedia.class);
            //intent.putExtra("MediaList", (Serializable) uploadMultimedia);
            activity.startService(intent);
        } catch (Exception e) {
            mIsUpdating.setValue(false);
        }
    }

    public void stopBackgroundService(Activity activity) {//, List<UploadMultimedia> uploadMultimedia) {
        Log.d("Bg Service", "Stop Service");
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


    //----------------get Folder Unique Id present------------------------
    public void getIsUniqueIdPresent(Activity activity,String folderUniqueId) {
        EventAppDB eventAppDB = EventAppDB.getDatabase(activity);
        folderUniqueIdCount = eventAppDB.newsFeedUniqueIdUploadedStartedDao().isUniqueIdPresent(folderUniqueId);
    }

    public LiveData<Integer> isUniqueIdPresent() {
        return folderUniqueIdCount;
    }

    //----------------Multimedia to compress------------------------
    /*public void getNewsFeedDataAccrodingToFolderUniqueId(Activity activity, String folderUniqueId) {
        EventAppDB eventAppDB = EventAppDB.getDatabase(activity);
        newsFeedDataToUpload = eventAppDB.uploadMultimediaDao().getMultimediaToUpload(folderUniqueId);
    }
*/
    public LiveData<List<UploadMultimedia>> getNewsFeedToUpload() {

        return newsFeedDataToUpload;
    }

    //-------------call to upload newsfeed---------------------
    public void sendPost(String token, String event_id, String status, List<UploadMultimedia> resultList) {
        // mIsUploading.setValue(true);
        NewsfeedRepository postNewsFeedRepository = NewsfeedRepository.getInstance();
        multimediaUploadLiveData = postNewsFeedRepository.postNewsFeed(token, event_id, status, resultList);//,mediaFile,mediaFileThumb);
    }

    public MutableLiveData<LoginOrganizer> getPostStatus() {
        // mIsUploading.setValue(false);
        mIsUpdating.setValue(false);
        return multimediaUploadLiveData;
    }

    //---------------Upldate uploaded flag to 1------------
    public void updateisUplodedIntoDB(Context context, String folderUniqueId) {
        NewsfeedRepository newsfeedRepository = NewsfeedRepository.getInstance();
        isUpdatedIntoDB = newsfeedRepository.updateIsUplodedIntoDb(context, folderUniqueId);
    }

    public MutableLiveData<Boolean> getIsUplodedStatus() {
        return isUpdatedIntoDB;
    }

    //----------------------------------------------------------------
    public LiveData<Boolean> getIsUpdating() {
        return mIsUpdating;
    }

    private void showratedialouge(Context context, final String api_token, final String from, final String id, final String attnId, final String eventId, final String heading) {

        myDialog = new Dialog(activityVar);
        myDialog.setContentView(R.layout.dialouge_msg_layout);
        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id

        myDialog.show();

        Button cancelbtn = myDialog.findViewById(R.id.canclebtn);
        Button ratebtn = myDialog.findViewById(R.id.ratebtn);
        TextView title = myDialog.findViewById(R.id.title);
        LinearLayout ll_main = myDialog.findViewById(R.id.ll_main);
        final TextView counttv = myDialog.findViewById(R.id.counttv);
        final EditText etmsg = myDialog.findViewById(R.id.etmsg);

        title.setText(heading);

        ll_main.setBackgroundColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_2)));
        title.setTextColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_3)));
        counttv.setTextColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_3)));
        etmsg.setTextColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_3)));
        etmsg.setHintTextColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_3)));
        ratebtn.setBackgroundColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_3)));
        cancelbtn.setBackgroundColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_3)));

        ratebtn.setTextColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_2)));
        cancelbtn.setTextColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_2)));


        final TextView nametv = myDialog.findViewById(R.id.nametv);

        nametv.setText("To " + "Admin");

        etmsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                count = 250 - s.length();
                counttv.setText(count + "/250");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.hideKeyboard(v);
                myDialog.dismiss();
            }
        });

        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (etmsg.getText().toString().length() > 0) {

                    newsRepository = NewsfeedRepository.getInstance();
                    String msg = StringEscapeUtils.escapeJava(etmsg.getText().toString());
                    dialog.cancel();
                    Utility.hideKeyboard(v);
                    if (from.equalsIgnoreCase("reportPost")) {

                        if (ConnectionDetector.getInstance(activityVar).isConnectingToInternet()) {
                            newsfeedHide = newsRepository.ReportPost(api_token, eventId, id, msg);

                            newsRepository.getPostActivity().observe((LifecycleOwner) activityVar, new Observer<LoginOrganizer>() {
                                @Override
                                public void onChanged(LoginOrganizer loginOrganizer) {
                                    if (loginOrganizer != null) {
                                        List<Header> heaserList = loginOrganizer.getHeader();
                                        Utility.createShortSnackBar(NewsFeedFragment.cl_main, heaserList.get(0).getMsg());
                                        myDialog.cancel();
                                        Utility.hideKeyboard(v);

                                    }
                                }
                            });
                        } else {
                            Utility.hideKeyboard(v);
                            myDialog.cancel();
                            Utility.createShortSnackBar(NewsFeedFragment.cl_main, "No Internet Connection");
                        }
                    } else if (from.equalsIgnoreCase("reportUser")) {
                        /*newsfeedHide = newsRepository.ReportUser("1", attnId,id, msg);
                        myDialog.cancel();*/
                        if (ConnectionDetector.getInstance(activityVar).isConnectingToInternet()) {
                            Utility.hideKeyboard(v);

                            newsfeedHide = newsRepository.ReportUser(api_token, eventId, attnId, id, msg);
                            newsRepository.getPostActivity().observe((LifecycleOwner) activityVar, new Observer<LoginOrganizer>() {
                                @Override
                                public void onChanged(LoginOrganizer loginOrganizer) {
                                    if (loginOrganizer != null) {
                                        List<Header> heaserList = loginOrganizer.getHeader();
                                        Utility.createShortSnackBar(NewsFeedFragment.cl_main, heaserList.get(0).getMsg());
                                        myDialog.cancel();
                                        Utility.hideKeyboard(v);
                                    }
                                }
                            });
                        } else {
                            myDialog.cancel();
                            Utility.hideKeyboard(v);
                            Utility.createShortSnackBar(NewsFeedFragment.cl_main, "No Internet Connection");
                        }
                    }
                } else {
                    Utility.createShortSnackBar(NewsFeedFragment.cl_main, "Enter Something");
                }
            }
        });
    }

    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(activityVar);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                dataToShare = f_url[1];
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());
                //Append timestamp to file name
                //fileName = timestamp + "_" + fileName;
                //External directory path to save file
                //folder = Environment.getExternalStorageDirectory() + File.separator + "androiddeft/";
                String folder = Environment.getExternalStorageDirectory().toString() + "/" + Constant.FOLDER_DIRECTORY + "/";


                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                strPath = folder + fileName;
                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d("ImageMultipleActivity", "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Download completed- check folder " + Constant.FOLDER_DIRECTORY;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            // Display File path after downloading
            String postStatus = "";
            String spannedString;
            CharSequence spannedString1;
            if (dataToShare.contains("\n")) {
                postStatus = dataToShare.trim().replace("\n", "<br/>");
            } else {
                postStatus = dataToShare.trim();
            }
            spannedString = String.valueOf(Jsoup.parse(postStatus)).trim();//Html.fromHtml(feedData.getPost_status(), Html.FROM_HTML_MODE_COMPACT).toString();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Spanned strPost = Html.fromHtml(spannedString, Html.FROM_HTML_MODE_COMPACT);
                spannedString1 = Utility.trimTrailingWhitespace(strPost);
            } else {
                Spanned strPost = Html.fromHtml(spannedString);
                spannedString1 = Utility.trimTrailingWhitespace(strPost);
            }


            Uri contentUri = FileProvider.getUriForFile(activityVar, BuildConfig.APPLICATION_ID + ".android.fileprovider", new File(strPath));

            ContentValues content = new ContentValues(4);
            content.put(MediaStore.Video.VideoColumns.DATE_ADDED,
                    System.currentTimeMillis() / 1000);
            content.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
            content.put(MediaStore.Video.Media.DATA, strPath);

            ContentResolver resolver = activityVar.getContentResolver();
            Uri uri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, content);

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("video/*");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Shared Via Event App");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "Shared Via Event App - "+spannedString1.toString());
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
            activityVar.startActivity(Intent.createChooser(sharingIntent, "Share Video"));

        }
    }

    public void shareImage(final String data, final String url, final Context context) {

        dialogShare = new ProgressDialog(context);
        dialogShare.setMessage("Please wait while loading...");
        dialogShare.show();
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                        Bitmap bitmap = getBitmapFromURL(url);
                        Uri uri = getLocalBitmapUri(bitmap, context);
                        if(uri!=null) {
                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.setType("image/*");
                            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, " Shared via Pest Expert");
                            sharingIntent.putExtra(Intent.EXTRA_TEXT, data);
                           // sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://bayer2020.page.link/newsfeed");
                            sharingIntent.putExtra(Intent.EXTRA_TEXT, "See Bayer Premise on Pest Expert 360° App.  "+"https://bayer2020.page.link/newsfeed" +
                                    ". You will be able to check more details like product USP, pests to control, dosage and application rate, etc. Check it out now. Download free Pest Expert 360° App.");
                            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                            context.startActivity(Intent.createChooser(sharingIntent, "Shared via Pest Expert"));
                            dialogShare.dismiss();
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();


      /*  Picasso.with(context).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                dialogShare.dismiss();
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("image/*");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, data + " Shared via Event app");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, data);
                sharingIntent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap, context));
                context.startActivity(Intent.createChooser(sharingIntent, "Shared via Event app"));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.d("error","error");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.d("placeHolderDrawable","placeHolderDrawable");
            }
        });*/
    }

    private void shareTextUrl(String data, String url) {
        String postStatus = "";
        String spannedString;
        CharSequence spannedString1;
        if (data.contains("\n")) {
            postStatus = data.trim().replace("\n", "<br/>");
        } else {
            postStatus = data.trim();
        }
        spannedString = String.valueOf(Jsoup.parse(postStatus)).trim();//Html.fromHtml(feedData.getPost_status(), Html.FROM_HTML_MODE_COMPACT).toString();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Spanned strPost = Html.fromHtml(spannedString, Html.FROM_HTML_MODE_COMPACT);
            spannedString1 = Utility.trimTrailingWhitespace(strPost);
        } else {
            Spanned strPost = Html.fromHtml(spannedString);
            spannedString1 = Utility.trimTrailingWhitespace(strPost);
        }

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, " Shared via Pest Expert");
        share.putExtra(Intent.EXTRA_TEXT, spannedString1.toString()+" "  + "https://bayer2020.page.link/newsfeed");
        activityVar.startActivity(Intent.createChooser(share, " Shared via Pest Expert"));
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Uri getLocalBitmapUri(Bitmap bmp, Context context) {
        Uri bmpUri = null;
        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
//            bmpUri = Uri.fromFile(file);
            bmpUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".android.fileprovider", file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    private class BitmapAsyncTask extends AsyncTask<String, String, String> {
        private ProgressDialog dialog1;
        Context activity;

        public BitmapAsyncTask(Context _activity) {
            activity = _activity;
            dialog1 = new ProgressDialog(_activity);
        }

        @Override
        protected void onPreExecute() {
            dialog1.setMessage("Doing something, please wait.");
            dialog1.show();
        }

        @Override
        protected String doInBackground(String... args) {
            Bitmap bitmap = getBitmapFromURL(args[1]);
            Uri uri = getLocalBitmapUri(bitmap, activity);

            return uri.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            // do UI work here
            if (dialog1.isShowing()) {
                dialog1.dismiss();
                //String[] str = result.split("#");
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("image/*");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, " Shared via Pest Expert");
                //sharingIntent.putExtra(Intent.EXTRA_TEXT, str[0]);
                sharingIntent.putExtra(Intent.EXTRA_STREAM, result);
                activity.startActivity(Intent.createChooser(sharingIntent, "Shared via Pest Expert"));
            }
        }
    }


    public void insertFolderUniqueIdIntoDB(Context context,String time) {
        PostNewsFeedRepository postNewsFeedRepository = PostNewsFeedRepository.getInstance();
        postNewsFeedRepository.insertFolderUniqueIdIntoDb(context,time);
    }

}