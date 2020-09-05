package com.procialize.eventapp.ui.newsfeed.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.LongDef;
import android.widget.ImageView;
import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.procialize.eventapp.BuildConfig;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Constants.Constant;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.ui.home.viewmodel.HomeViewModel;
import com.procialize.eventapp.ui.newsFeedComment.view.CommentActivity;
import com.procialize.eventapp.ui.newsFeedPost.roomDB.UploadMultimedia;
import com.procialize.eventapp.ui.newsFeedPost.service.BackgroundServiceToCompressMedia;
import com.procialize.eventapp.ui.newsFeedPost.view.PostNewActivity;
import com.procialize.eventapp.ui.newsfeed.adapter.NewsFeedAdapter;
import com.procialize.eventapp.ui.newsfeed.model.FetchNewsfeedMultiple;
import com.procialize.eventapp.ui.newsfeed.model.News_feed_media;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;
import com.procialize.eventapp.ui.newsfeed.viewmodel.NewsFeedViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.eventapp.Constants.Constant.MY_PREFS_NAME;
import static com.procialize.eventapp.Constants.Constant.NEWS_FEED_MEDIA_PATH;
import static com.procialize.eventapp.ui.newsfeed.adapter.NewsFeedAdapter.swipableAdapterPosition;

public class NewsFeedFragment extends Fragment implements NewsFeedAdapter.FeedAdapterListner, View.OnClickListener {
    ArrayList<Newsfeed_detail> newsfeedArrayList = new ArrayList<>();
    // List<Newsfeed_detail> feedList = new ArrayList<>();
    NewsFeedAdapter newsfeedAdapter;
    RecyclerView recycler_feed;
    NewsFeedViewModel newsfeedViewModel;
    View root;
    SwipeRefreshLayout feedrefresh;
    LinearLayout ll_whats_on_mind;
    String eventid = "1";
    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjYiLCJmaXJzdF9uYW1lIjoiQXBhcm5hIiwibWlkZGxlX25hbWUiOiIiLCJsYXN0X25hbWUiOiJCYWRoYW4iLCJtb2JpbGUiOiI4ODMwNDE2NzkwIiwiZW1haWwiOiJhcGFybmFAcHJvY2lhbGl6ZS5pbiIsInJlZnJlc2hfdG9rZW4iOiJlNTcwY2JhMTMxODUwYjgzYjc4ZjE4M2FlZDI0MmM3MjI3YzQ1MTVhIiwidXNlcl90eXBlIjoiQSIsInZlcmlmeV9vdHAiOiIxIiwicHJvZmlsZV9waWMiOiIgaHR0cHM6XC9cL3N0YWdlLWFkbWluLnByb2NpYWxpemUubGl2ZVwvYmFzZWFwcFwvdXBsb2Fkc1wvdXNlclwvZGVmYXVsdC5wbmciLCJ0aW1lIjoxNTk4ODUxMTU3LCJleHBpcnlfdGltZSI6MTU5ODg1NDc1N30.QtQwWAr8TKKGAqLwTbJtosSxDhrDrhdyH_sH1A-0qes";
    ConnectionDetector connectionDetector;
    UploadMultimediaBackgroundReceiver mReceiver;
    IntentFilter mFilter;
    public static ConstraintLayout cl_main;
    private TextView tv_uploding_multimedia;
    String reaction_type;
    String strPath;
    private Dialog dialogShare;


    public static NewsFeedFragment newInstance() {

        return new NewsFeedFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newsfeedViewModel = ViewModelProviders.of(this).get(NewsFeedViewModel.class);

        Log.d("On news feed fragment","Yes");

        root = inflater.inflate(R.layout.fragment_home, container, false);
        cl_main = root.findViewById(R.id.cl_main);
        recycler_feed = root.findViewById(R.id.recycler_feed);
        feedrefresh = root.findViewById(R.id.feedrefresh);
        ll_whats_on_mind = root.findViewById(R.id.ll_whats_on_mind);
        ll_whats_on_mind.setOnClickListener(this);
        tv_uploding_multimedia = root.findViewById(R.id.tv_uploding_multimedia);
        connectionDetector = ConnectionDetector.getInstance(getActivity());

        feedrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                feedrefresh.setRefreshing(false);
                if (connectionDetector.isConnectingToInternet()) {
                    init();
                }
            }
        });

        init();

        if (!CommonFunction.isMyServiceRunning(getActivity(), BackgroundServiceToCompressMedia.class)) {
           /* Intent intent = new Intent(getActivity(), BackgroundServiceToCompressMedia.class);
            getActivity().startService(intent);*/
            /*newsfeedViewModel.getNonCompressesMultimedia(getActivity());
            newsfeedViewModel.getNonCompressedMedia().observe(getActivity(), new Observer<List<UploadMultimedia>>() {
                @Override
                public void onChanged(List<UploadMultimedia> uploadMultimedia) {
                    Log.d("count_of_non_compressed", uploadMultimedia.size() + "");
                    if (uploadMultimedia.size() > 0) {
                        newsfeedViewModel.startBackgroundService(getActivity(), uploadMultimedia);
                        newsfeedViewModel.getIsUpdating().observe(getActivity(), new Observer<Boolean>() {
                            @Override
                            public void onChanged(@Nullable Boolean aBoolean) {
                                if (aBoolean) {
                                    showProgressBar();
                                } else {
                                    hideProgressBar();
                                }
                            }
                        });
                    } *//*else {
                        Intent broadcastIntent = new Intent(Constant.BROADCAST_UPLOAD_MULTIMEDIA_ACTION);
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(broadcastIntent);
                    }*//*
                }
            });*/
        }
        newsfeedViewModel.startBackgroundService(getActivity());
        newsfeedViewModel.getIsUpdating().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    showProgressBar();
                } else {
                    hideProgressBar();
                }
            }
        });
        mReceiver = new UploadMultimediaBackgroundReceiver();
        mFilter = new IntentFilter(Constant.BROADCAST_UPLOAD_MULTIMEDIA_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, mFilter);

        return root;
    }

    void init() {
        if (connectionDetector.isConnectingToInternet()) {
            newsfeedViewModel.init("","");

            newsfeedViewModel.getNewsRepository().observe(this, new Observer<FetchNewsfeedMultiple>() {
                @Override
                public void onChanged(FetchNewsfeedMultiple fetchNewsfeedMultiple) {
                    List<Newsfeed_detail> feedList = fetchNewsfeedMultiple.getNewsfeed_detail();
                    if (newsfeedArrayList.size() > 0) {
                        newsfeedArrayList.clear();
                    }
                    newsfeedArrayList.addAll(feedList);
                    String mediaPath = fetchNewsfeedMultiple.getMedia_path();
                    SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(NEWS_FEED_MEDIA_PATH, mediaPath);
                    editor.commit();
                    newsfeedAdapter.notifyDataSetChanged();
                }
            });
        }
        setupRecyclerView();

    }

    private void setupRecyclerView() {
        if (newsfeedAdapter == null) {
            newsfeedAdapter = new NewsFeedAdapter(getContext(), newsfeedArrayList, NewsFeedFragment.this);
            recycler_feed.setLayoutManager(new LinearLayoutManager(getContext()));
            recycler_feed.setAdapter(newsfeedAdapter);
            recycler_feed.setItemAnimator(new DefaultItemAnimator());
            recycler_feed.setNestedScrollingEnabled(true);
        } else {
            newsfeedAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onContactSelected(Newsfeed_detail feed, int position) {
        newsfeedViewModel.openNewsFeedDetails(getActivity(), feed, position);
    }

    @Override
    public void onCommentClick(Newsfeed_detail feed, int position) {
        newsfeedViewModel.openCommentPage(getActivity(), feed, position);
    }

    @Override
    public void onLikeClick(Newsfeed_detail feed, int position) {
        newsfeedViewModel.openLikePage(getActivity(), feed, position);
    }

    @Override
    public void onSliderClick(Newsfeed_detail feed, int position) {

        newsfeedViewModel.openFeedDetails(getActivity(), feed, position);

    }

    @Override
    public void moreTvFollowOnClick(View v, Newsfeed_detail feed, int position) {
        newsfeedViewModel.openMoreDetails(  getActivity(), feed, position);

    }

    @Override
    public void likeTvViewOnClick(View v, Newsfeed_detail feed, int position, ImageView likeimage, TextView liketext) {
        int count = Integer.parseInt(feed.getTotal_likes());

        Drawable drawables = likeimage.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawables).getBitmap();

        Bitmap bitmap2 = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_like)).getBitmap();


//        if(!drawables[2].equals(R.drawable.ic_like)){
        if (bitmap != bitmap2) {
            reaction_type = "";
            feed.setLike_flag("");
            likeimage.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
//            likeimage.setBackgroundResource(R.drawable.ic_like);
            if (ConnectionDetector.getInstance(getContext()).isConnectingToInternet()) {
                newsfeedViewModel.openLikeimg(eventid,feed.getNews_feed_id());
            } else {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
            try {

                if (count > 0) {
                    count = count - 1;
                    feed.setTotal_likes(String.valueOf(count));

                    if (count == 1) {
                        liketext.setText(count + " Like");
                    } else {
                        liketext.setText(count + " Likes");
                    }

                    feed.setTotal_likes(String.valueOf(count));

                } else {
                    liketext.setText("0" + " Likes");
                    feed.setTotal_likes("0");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            feed.setLike_flag("1");
            likeimage.setImageDrawable(getResources().getDrawable(R.drawable.ic_active_like));
           /*int color = Color.parseColor(colorActive);
            likeimage.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);*/
            reaction_type = "0";
            if (ConnectionDetector.getInstance(getContext()).isConnectingToInternet()) {
                newsfeedViewModel.openLikeimg(eventid,feed.getNews_feed_id());
            } else {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

            try {

                count = count + 1;
                if (count == 1) {
                    liketext.setText(count + " Like");
                } else {
                    liketext.setText(count + " Likes");
                }

                feed.setTotal_likes(String.valueOf(count));

                feed.setTotal_likes(String.valueOf(count));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_whats_on_mind:
                startActivity(new Intent(getActivity(), PostNewActivity.class));
                break;
        }
    }

    private class UploadMultimediaBackgroundReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // progressbarForSubmit.setVisibility(View.GONE);
            Log.d("service end", "service end");
            try {
                newsfeedViewModel.stopBackgroundService(getActivity());
                uploadData();
           /* newsfeedViewModel.getMediaToUpload(getActivity());
            newsfeedViewModel.getMedia().observe(getActivity(), new Observer<List<UploadMultimedia>>() {
                @Override
                public void onChanged(List<UploadMultimedia> uploadMultimedia) {
                    Log.d("count_of_is_compressed1", uploadMultimedia.size() + "");
                    uploadData();
                }
            });*/
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void uploadData() {
        Log.d("Service End upload","in upload");
        if(newsfeedViewModel!=null) {
            newsfeedViewModel.getFolderUniqueId(getActivity());
            newsfeedViewModel.getFolderIdList().observe(getActivity(), new Observer<List<String>>() {
                @Override
                public void onChanged(List<String> folderUniqueIdList) {
                    for (int i = 0; i < folderUniqueIdList.size(); i++) {
                        final String folderUniqueId = folderUniqueIdList.get(i);
                        newsfeedViewModel.getNewsFeedDataAccrodingToFolderUniqueId(getActivity(), folderUniqueId);
                        newsfeedViewModel.getNewsFeedToUpload().observe(getActivity(), new Observer<List<UploadMultimedia>>() {
                            @Override
                            public void onChanged(List<UploadMultimedia> uploadMultimedia) {
                                if (uploadMultimedia.size() > 0) {
                                    String postText = uploadMultimedia.get(0).getPost_status();
                                    if (!postText.isEmpty()) {
                                        uploadMultimedia.remove(0);
                                    }

                                    newsfeedViewModel.sendPost(eventid, postText, uploadMultimedia);
                                    newsfeedViewModel.getPostStatus().observe(getActivity(), new Observer<LoginOrganizer>() {
                                        @Override
                                        public void onChanged(@Nullable LoginOrganizer result) {
                                            if (result != null) {

                                                newsfeedViewModel.updateisUplodedIntoDB(getActivity(), folderUniqueId);
                                                String status = result.getHeader().get(0).getType();
                                                String message = result.getHeader().get(0).getMsg();
                                                Snackbar.make(cl_main, message, Snackbar.LENGTH_LONG)
                                                        .show();
                                            } else {
                                                Snackbar.make(cl_main, "failure", Snackbar.LENGTH_LONG)
                                                        .show();
                                            }


                                            if (newsfeedViewModel != null && newsfeedViewModel.getPostStatus().hasObservers()) {
                                                newsfeedViewModel.getPostStatus().removeObservers(getActivity());
                                            }
                                        }

                                    });
                                }

                            }
                        });
                    }

                }
            });
        }

    }


    private void showProgressBar() {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        tv_uploding_multimedia.startAnimation(anim);
        tv_uploding_multimedia.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        tv_uploding_multimedia.clearAnimation();
        tv_uploding_multimedia.setVisibility(View.GONE);
    }

    @Override
    public void shareTvFollowOnClick(View v, Newsfeed_detail feedList, int position) {
        final List<News_feed_media> newsFeedMedia = feedList.getNews_feed_media();
        if (feedList != null) {
            if (connectionDetector.isConnectingToInternet()) {
                if (newsFeedMedia.size() > 0) {

                    if (newsFeedMedia.size() < swipableAdapterPosition) {
                        swipableAdapterPosition = 0;
                    }
                    if (newsFeedMedia.get(swipableAdapterPosition).getMedia_type().equalsIgnoreCase("Video")) {
                        boolean isPresentFile = false;
                        File dir = new File(Environment.getExternalStorageDirectory().toString() + "/" + Constant.folderName);
                        if (dir.isDirectory()) {
                            String[] children = dir.list();
                            for (int i = 0; i < children.length; i++) {
                                String filename = children[i].toString();
                                if (newsFeedMedia.get(swipableAdapterPosition).getMedia_file().equals(filename)) {
                                    isPresentFile = true;
                                }
                            }
                        }

                        if (!isPresentFile) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Download and Share");
                            builder.setMessage("Video will be share only after download,\nDo you want to continue for download and share?");
                            builder.setNegativeButton("NO",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            builder.setPositiveButton("YES",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                            String newsFeedPath = prefs.getString(NEWS_FEED_MEDIA_PATH, "");
                                            new DownloadFile().execute(/*ApiConstant.newsfeedwall*/newsFeedPath + newsFeedMedia.get(swipableAdapterPosition).getMedia_file());
                                        }
                                    });
                            builder.show();

                        } else if (isPresentFile) {
                            String folder = Environment.getExternalStorageDirectory().toString() + "/" + Constant.folderName + "/";
                            //Create androiddeft folder if it does not exist
                            File directory = new File(folder);
                            if (!directory.exists()) {
                                directory.mkdirs();
                            }
                            strPath = folder + newsFeedMedia.get(swipableAdapterPosition).getMedia_file();
                              /*              ContentValues content = new ContentValues(4);
                                            content.put(MediaStore.Video.VideoColumns.DATE_ADDED,
                                                    System.currentTimeMillis() / 1000);
                                            content.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
                                            content.put(MediaStore.Video.Media.DATA, strPath);
                                            ContentResolver resolver = getActivity().getContentResolver();
                                            Uri uri =strPath; resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, content);*/
                            Uri contentUri = FileProvider.getUriForFile(getActivity(),
                                    BuildConfig.APPLICATION_ID + ".android.fileprovider", new File(strPath));

                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.setType("video/*");
                            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Shared via Events app");
                            sharingIntent.putExtra(Intent.EXTRA_TEXT, "");
                            sharingIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                            startActivity(Intent.createChooser(sharingIntent, "Shared via Events app"));
                        }
                    } else {
                        dialogShare = new Dialog(getActivity());
                        dialogShare.show();
                        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                        // String newsFeedPath = prefs.getString(NEWS_FEED_MEDIA_PATH, "");
                        String newsFeedPath = "https://stage-admin.procialize.live/baseapp/uploads/news_feed_media/";
                        shareImage(feedList.getPost_date() + "\n" + feedList.getPost_status(), /*ApiConstant.newsfeedwall*/newsFeedPath + newsFeedMedia.get(swipableAdapterPosition).getMedia_file(), getContext());
                    }
                } else {
                    shareTextUrl(feedList.getPost_date() + "\n" + feedList.getPost_status(), StringEscapeUtils.unescapeJava(feedList.getPost_status()));
                }
            }
            else {
                Toast.makeText(getActivity(), "No Internet Connection.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void shareImage(final String data, String url, final Context context) {
        Picasso.with(context).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                dialogShare.dismiss();
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_SUBJECT, data);
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap, context));

                context.startActivity(Intent.createChooser(i, "Share Image"));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }

    static public Uri getLocalBitmapUri(Bitmap bmp, Context context) {
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

    private void shareTextUrl(String data, String url) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, data);
        share.putExtra(Intent.EXTRA_TEXT, url);

        startActivity(Intent.createChooser(share, "Share link!"));
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
            this.progressDialog = new ProgressDialog(getActivity());
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
                String folder = Environment.getExternalStorageDirectory().toString() + "/" + Constant.folderName + "/";


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
                return "Download completed- check folder " + Constant.folderName;

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
          /*  Toast.makeText(getActivity(),
                    message, Toast.LENGTH_LONG).show();*/

            Uri contentUri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".android.fileprovider", new File(strPath));
/*
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("video/*");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // Add data to the intent, the receiving app will decide
            // what to do with it.
            share.putExtra(Intent.EXTRA_SUBJECT,"");
            // share.putExtra(Intent.EXTRA_TEXT, ApiConstant.newsfeedwall + newsFeedMedia.get(swipableAdapterPosition).getMediaFile());
            share.putExtra(Intent.EXTRA_STREAM, contentUri);
            startActivity(Intent.createChooser(share, "Share link!"));*/

            ContentValues content = new ContentValues(4);
            content.put(MediaStore.Video.VideoColumns.DATE_ADDED,
                    System.currentTimeMillis() / 1000);
            content.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
            content.put(MediaStore.Video.Media.DATA, strPath);

            ContentResolver resolver = getActivity().getContentResolver();
            Uri uri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, content);

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("video/*");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Video Share");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(sharingIntent, "Share Video"));

        }
    }

}