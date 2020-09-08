package com.procialize.eventapp.ui.newsFeedDetails.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Constants.Constant;
import com.procialize.eventapp.R;
import com.procialize.eventapp.ui.newsFeedDetails.adapter.NewsFeedDetailsPagerAdapter;
import com.procialize.eventapp.ui.newsFeedDetails.viewModel.NewsFeedDetailsViewModel;
import com.procialize.eventapp.ui.newsfeed.model.News_feed_media;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jzvd.JzvdStd;

import static com.procialize.eventapp.Constants.Constant.MY_PREFS_NAME;
import static com.procialize.eventapp.Constants.Constant.NEWS_FEED_MEDIA_PATH;
import static com.procialize.eventapp.Utility.CommonFunction.getLocalBitmapUri;

public class NewsFeedDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_save, btn_share;
    ViewPager vp_media;
    NewsFeedDetailsViewModel newsFeedDetailsViewModel;
    private String position;
    private Newsfeed_detail newsfeed_detail;
    List<News_feed_media> news_feed_media;
    private int mediaPosition = 0;
    private String strPath, url, imgname;
    boolean isShare;
    String newsFeedPath;
    LinearLayout ll_dots;
    int shareOrSaveImagePosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed_details);

        newsFeedDetailsViewModel = ViewModelProviders.of(this).get(NewsFeedDetailsViewModel.class);

        Intent intent = getIntent();
        try {
           // newsfeed_detail = (Newsfeed_detail) getIntent().getSerializableExtra("Newsfeed_detail");
            news_feed_media = (ArrayList<News_feed_media>) getIntent().getSerializableExtra("media_list");
            mediaPosition = getIntent().getIntExtra("position", 0);

        } catch (Exception e) {
            e.printStackTrace();
        }

        SharedPreferences prefs = this.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
         newsFeedPath = prefs.getString(NEWS_FEED_MEDIA_PATH, "");


        btn_save = findViewById(R.id.btn_save);
        btn_share = findViewById(R.id.btn_share);
        vp_media = findViewById(R.id.vp_media);
        ll_dots = findViewById(R.id.ll_media_dots);


        btn_save.setOnClickListener(this);
        btn_share.setOnClickListener(this);

        setupPagerAdapter();
    }

    public void setupPagerAdapter()
    {

            final ArrayList<String> imagesSelectednew = new ArrayList<>();
            final ArrayList<String> imagesSelectednew1 = new ArrayList<>();
        final ImageView[] ivArrayDotsPager;

        for (int i = 0; i < news_feed_media.size(); i++) {
                imagesSelectednew.add(news_feed_media.get(i).getMedia_file());
                imagesSelectednew1.add(news_feed_media.get(i).getThumb_image());
            }

            NewsFeedDetailsPagerAdapter swipepagerAdapter = new NewsFeedDetailsPagerAdapter(NewsFeedDetailsActivity.this, imagesSelectednew, imagesSelectednew1);
            vp_media.setAdapter(swipepagerAdapter);
            swipepagerAdapter.notifyDataSetChanged();
            vp_media.setCurrentItem(mediaPosition);

        if (imagesSelectednew.size() > 1) {
            ivArrayDotsPager = new ImageView[imagesSelectednew.size()];
            //setupPagerIndidcatorDots(0, ll_dots, imagesSelectednew.size());
            vp_media.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    JzvdStd.releaseAllVideos();
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.pause();
                }

                @Override
                public void onPageSelected(int position1) {
                    shareOrSaveImagePosition = position1;
                    JzvdStd.releaseAllVideos();
                    setupPagerIndidcatorDots(position1, ll_dots, imagesSelectednew.size());
                   /* NewsfeedAdapter.ViewHolder viewHolder = new NewsfeedAdapter.ViewHolder();
                    if (viewHolder.VideoView != null) {
                        viewHolder.VideoView.pause();
                    }*/


                    //-----------------------------------------------------------------------------------------------
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.pause();
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    JzvdStd.releaseAllVideos();
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.pause();
                }
            });
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                /*String strUrlPathForSave = "";
                String strMediaTypeForSave = "";
                if (strMediaTypeForSave.equalsIgnoreCase("image")) {
                    Bitmap bitmap = null;
                    newsFeedDetailsViewModel.saveImage(this, bitmap);
                } else {
                    newsFeedDetailsViewModel.saveVideo(this, strUrlPathForSave);
                }*/
                if (ConnectionDetector.getInstance(this).isConnectingToInternet()) {
                    isShare = false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if (NewsFeedDetailsActivity.this.checkSelfPermission(
                                "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                            final String[] permissions = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                            ActivityCompat.requestPermissions(NewsFeedDetailsActivity.this, permissions, 0);
                        }
                        else if (NewsFeedDetailsActivity.this.checkSelfPermission(
                                "android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                            final String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                            ActivityCompat.requestPermissions(NewsFeedDetailsActivity.this, permissions, 0);
                        }
                        else {

                            url = newsFeedPath + news_feed_media.get(shareOrSaveImagePosition).getMedia_file();
                            if (url.contains("mp4")) {
                                new DownloadFile().execute(url);
                            } else {
                                url = newsFeedPath/*Constant.newsfeedwall*/ +news_feed_media.get(shareOrSaveImagePosition).getMedia_file();
                                //String root = Environment.getExternalStorageDirectory().toString();
                              /*  try {

                                    PicassoTrustAll.getInstance(NewsFeedDetailsActivity.this)
                                            .load(url)
                                            .into(new Target() {
                                                      @Override
                                                      public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                          try {
                                                              String root = Environment.getExternalStorageDirectory().toString();
                                                              File myDir = new File(root + "/" + Constant.folderName);

                                                              if (!myDir.exists()) {
                                                                  myDir.mkdirs();
                                                              }
                                                              Date date = new Date();
                                                              //getTime() returns current time in milliseconds
                                                              long time = date.getTime();
                                                              Toast.makeText(NewsFeedDetailsActivity.this,
                                                                      "Download completed- check folder " + Constant.folderName,
                                                                      Toast.LENGTH_SHORT).show();
                                                              String name = imgname + time + ".jpg";
                                                              myDir = new File(myDir, name);
                                                              FileOutputStream out = new FileOutputStream(myDir);
                                                              bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                                                              out.flush();
                                                              out.close();
                                                          } catch (Exception e) {
                                                          }
                                                      }

                                                      @Override
                                                      public void onBitmapFailed(Drawable errorDrawable) {
                                                      }

                                                      @Override
                                                      public void onPrepareLoad(Drawable placeHolderDrawable) {
                                                      }
                                                  }
                                            );

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }*/

                            }
                        }
                    }
                } else {
                    Toast.makeText(getBaseContext(), "No Internet Connection",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_share:
                isShare = true;
               /* url = ApiConstant.newsfeedwall + news_feed_media.get(shareOrSaveImagePosition).getMediaFile();
                if (url.contains("mp4")) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                    // Add data to the intent, the receiving app will decide
                    // what to do with it.
                    share.putExtra(Intent.EXTRA_SUBJECT, "");
                    share.putExtra(Intent.EXTRA_TEXT, url);

                    startActivity(Intent.createChooser(share, "Share link!"));
                } else {
                    shareImage(url, NewsFeedDetailsActivity.this);
                }*/

                //final List<news_feed_media> newsFeedMedia = myList;
                String type = news_feed_media.get(shareOrSaveImagePosition).getMedia_type();
                if (ConnectionDetector.getInstance(this).isConnectingToInternet()) {
                    if (news_feed_media.size() > 0) {

                        if (type.equals("Video")) {
                            boolean isPresentFile = false;
                            File dir = new File(Environment.getExternalStorageDirectory().toString() + "/" + Constant.folderName);
                            if (dir.isDirectory()) {
                                String[] children = dir.list();
                                for (int i = 0; i < children.length; i++) {
                                    String filename = children[i].toString();
                                    if (news_feed_media.get(shareOrSaveImagePosition).getMedia_file().equals(filename)) {
                                        isPresentFile = true;
                                    }
                                }
                            }

                            if (!isPresentFile) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(NewsFeedDetailsActivity.this);
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

                                                new DownloadFile().execute(newsFeedPath/*Constant.newsfeedwall*/ + news_feed_media.get(shareOrSaveImagePosition).getMedia_file());
                                            }
                                        });
                                builder.show();

                            } else if (isPresentFile) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(NewsFeedDetailsActivity.this);
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
                                                new DownloadFile().execute(newsFeedPath/*Constant.newsfeedwall*/ + news_feed_media.get(shareOrSaveImagePosition).getMedia_file());
                                            }
                                        });
                                builder.show();
                            }
                        } else {
                            shareImage(newsFeedPath/*Constant.newsfeedwall*/ + news_feed_media.get(shareOrSaveImagePosition).getMedia_file(), NewsFeedDetailsActivity.this);
                        }
                    } else {
                        // shareTextUrl(date + "\n" + heading, StringEscapeUtils.unescapeJava(heading));
                    }
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JzvdStd.releaseAllVideos();
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
            this.progressDialog = new ProgressDialog(NewsFeedDetailsActivity.this);
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
                    Log.d("NewsFeedDetailsActivity", "Progress: " + (int) ((total * 100) / lengthOfFile));

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

            if (isShare) {
                ContentValues content = new ContentValues(4);
                content.put(MediaStore.Video.VideoColumns.DATE_ADDED,
                        System.currentTimeMillis() / 1000);
                content.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
                content.put(MediaStore.Video.Media.DATA, strPath);

                ContentResolver resolver = getContentResolver();
                Uri uri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, content);

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("video/*");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Shared via MRGE app");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(sharingIntent, "Shared via MRGE app"));
            } else {

                // Display File path after downloading
                Toast.makeText(getApplicationContext(),
                        message, Toast.LENGTH_LONG).show();
            }
        }
    }
    static public void shareImage(String url, final Context context) {
        final Dialog dialog = new Dialog(context);
        Picasso.with(context).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                dialog.dismiss();
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_SUBJECT, "Shared via MRGE app");
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap, context));
                context.startActivity(Intent.createChooser(i, "Shared via MRGE app"));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                dialog.dismiss();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_progress);
                dialog.show();

            }
        });
        // }

    }

    private void setupPagerIndidcatorDots(int currentPage, LinearLayout ll_dots, int size) {

        TextView[] dots = new TextView[size];
        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(Color.parseColor("#343434"));
            ll_dots.addView(dots[i]);
        }

        try {
            if (dots.length > 0) {
                if (dots.length != currentPage) {
                    dots[currentPage].setTextColor(Color.parseColor("#A2A2A2"));
                   // dots[currentPage].setTextColor(Color.parseColor(colorActive));
                }
            }
        } catch (Exception e) {

        }

    }


}