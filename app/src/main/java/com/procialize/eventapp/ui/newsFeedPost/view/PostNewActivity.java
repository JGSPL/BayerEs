package com.procialize.eventapp.ui.newsFeedPost.view;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Constants.Constant;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.Database.EventAppDB;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.MainActivity;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.SharedPreferencesConstant;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.ui.newsFeedPost.adapter.ViewPagerMultimediaAdapter;
import com.procialize.eventapp.ui.newsFeedPost.model.SelectedImages;
import com.procialize.eventapp.ui.newsFeedPost.viewModel.PostNewsFeedViewModel;
import com.yanzhenjie.album.AlbumFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;

public class PostNewActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout ll_upload_media, ll_media_dots, linear;
    EditText et_post;
    TextView btn_post, tv_count,tv_name;
    PostNewsFeedViewModel postNewsFeedViewModel;
    ArrayList<SelectedImages> resultList = new ArrayList<>();
    private ArrayList<AlbumFile> mAlbumFiles = new ArrayList<>();//Array For selected images and videos
    File from, too, thumbpath, thumb, tothumb, tovideo;
    String videofile, imagefile;
    ArrayList<Integer> videoPositionArray = new ArrayList<Integer>();
    ViewPager vp_media;
    ViewPagerMultimediaAdapter viewPagerAdapter;
    private int dotscount;
    ConnectionDetector cd;
    EventAppDB eventAppDB;
    ImageView iv_back,iv_profile;
    String event_id, api_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new);
//Call Refresh token
        new RefreashToken(this).callGetRefreashToken(this);

        eventAppDB = EventAppDB.getDatabase(this);
        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(this, EVENT_ID);

        Log.d("tot_count", String.valueOf(eventAppDB.uploadMultimediaDao().getRowCount()));
        postNewsFeedViewModel = ViewModelProviders.of(this).get(PostNewsFeedViewModel.class);
        cd = ConnectionDetector.getInstance(this);
        ll_upload_media = findViewById(R.id.ll_upload_media);
        iv_back = findViewById(R.id.iv_back);
        iv_profile = findViewById(R.id.iv_profile);
        ll_media_dots = findViewById(R.id.ll_media_dots);
        linear = findViewById(R.id.linear);
        btn_post = findViewById(R.id.btn_post);
        tv_count = findViewById(R.id.tv_count);
        tv_name = findViewById(R.id.tv_name);
        et_post = findViewById(R.id.et_post);
        iv_back.setOnClickListener(this);

        String profilePic = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_PROFILE_PIC);
        String fName = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_FNAME);
        String lName = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_LNAME);
        String designation = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_DESIGNATION);
        String city = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_CITY);
        tv_name.setText(fName + " "+lName);
        Glide.with(PostNewActivity.this)
                .load(profilePic)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(iv_profile);

        et_post.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_count.setText(String.valueOf(s.length()));
                /*if (s.length() > 0) {
                    btn_post.setTextColor(getResources().getColor(R.color.white));
                    btn_post.setBackgroundColor(Color.parseColor(colorActive));
                } else {
                    btn_post.setTextColor(Color.parseColor(colorActive));
                    btn_post.setBackgroundColor(getResources().getColor(R.color.white));
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        btn_post.setOnClickListener(this);

        vp_media = findViewById(R.id.vp_media);
        ll_upload_media.setOnClickListener(this);

        postNewsFeedViewModel.albumLoader(PostNewActivity.this);
        postNewsFeedViewModel.init();
        /*postNewsFeedViewModel.getSelectedMedia().observe(this, newsResponse -> {
            if(newsResponse.size() > 0) {
                int feedList = newsResponse.get(0).getMediaType();
            }
        });*/

        postNewsFeedViewModel.getSelectedMedia().observe(this, new Observer<ArrayList<AlbumFile>>() {
            @Override
            public void onChanged(@Nullable ArrayList<AlbumFile> result) {
                try {
                    resultList.clear();
                    mAlbumFiles = result;

                    //Get Original paths from selected arraylist
                    List selectedFileList = new ArrayList();
                    for (int i = 0; i < resultList.size(); i++) {
                        selectedFileList.add(resultList.get(i).getmOriginalFilePath());
                    }

                    //Get Data from Album
                    List albumFileList = new ArrayList();
                    for (int k = 0; k < mAlbumFiles.size(); k++) {
                        albumFileList.add(mAlbumFiles.get(k).getPath());
                    }

                    //If user deselects image/video then remove that from resultList
                    if (selectedFileList.size() > 0) {
                        for (int l = 0; l < selectedFileList.size(); l++) {
                            if (!albumFileList.contains(selectedFileList.get(l))) {
                                selectedFileList.remove(l);
                                resultList.remove(l);
                            }
                        }
                    }

                    String strMediaType;

                    for (int j = 0; j < mAlbumFiles.size(); j++) {
                        from = null;
                        too = null;
                        thumbpath = null;
                        thumb = null;
                        tothumb = null;
                        tovideo = null;
                        //To check selected image/video is already present in previous arraylist
                        if (!selectedFileList.contains(mAlbumFiles.get(j).getPath())) {
                            if (mAlbumFiles.get(j).getMediaType() == AlbumFile.TYPE_VIDEO) {
                                strMediaType = "video";
                                Random r = new Random();
                                int i1 = r.nextInt(80 - 65) + 65;
                                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                videofile = "video_" + i1 + timeStamp + "_";
                                from = new File(mAlbumFiles.get(j).getPath());

                                String root = Environment.getExternalStorageDirectory().toString();
                                File moviesDir = new File(root + Constant.FOLDER_DIRECTORY + Constant.VIDEO_DIRECTORY);

                                if (from != null) {
                                    postNewsFeedViewModel.copyFile(mAlbumFiles.get(j).getPath(), from.getName(), moviesDir.toString());
                                }


                                try {
                                    thumb = new File(moviesDir + "/" + from.getName());
                                    if (moviesDir.exists()) {
                                        tovideo = new File(moviesDir, videofile + ".mp4");
                                        if (thumb.exists())
                                            thumb.renameTo(tovideo);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    thumb = new File(mAlbumFiles.get(j).getPath());
                                }


                            } else {
                                strMediaType = "image";
                                Random r = new Random();
                                int i1 = r.nextInt(80 - 65) + 65;
                                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                imagefile = "image_" + i1 + timeStamp + "_";
                                from = new File(mAlbumFiles.get(j).getPath());

                                String root = Environment.getExternalStorageDirectory().toString();
                                File moviesDir = new File(root + Constant.FOLDER_DIRECTORY + Constant.VIDEO_DIRECTORY);
                                if (from != null) {
                                    postNewsFeedViewModel.copyFile(mAlbumFiles.get(j).getPath(), from.getName(), moviesDir.toString());
                                }
                                try {
                                    thumb = new File(moviesDir + "/" + from.getName());
                                    if (moviesDir.exists()) {
                                        if (from.getName().contains("gif")) {
                                            tovideo = new File(moviesDir, imagefile + ".gif");
                                        } else {
                                            tovideo = new File(moviesDir, imagefile + ".png");
                                        }
                                        if (thumb.exists())
                                            thumb.renameTo(tovideo);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    thumb = new File(mAlbumFiles.get(j).getPath());
                                }
                            }

                            resultList.add(new SelectedImages(tovideo.getPath(),
                                    tovideo.getPath(), mAlbumFiles.get(j).getThumbPath(), false, strMediaType, mAlbumFiles.get(j).getMimeType()));
                            if (mAlbumFiles.get(j).getMediaType() == AlbumFile.TYPE_VIDEO) {

                                videoPositionArray.add(j);
                            }
                        }
                    }

                    try {
                        Log.d("Result List Size", "" + resultList.size());
                            /*if (videoPositionArray.size() > 0) {
                                if (mAlbumFiles.get(videoPositionArray.get(0)).getMediaType() == AlbumFile.TYPE_VIDEO) {
                                    String strPath = mAlbumFiles.get(videoPositionArray.get(0)).getPath();
                                    executeCutVideoCommand(startMsForVideoCutting, endMsForVideoCutting, Uri.parse(strPath), videoPositionArray.get(0));
                                }
                            } else {*/
                        setPagerAdapter(resultList);
                        //}
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.ll_upload_media:
                postNewsFeedViewModel.selectAlbum(this);
                break;
            case R.id.btn_post:
                if (cd.isConnectingToInternet()) {
                    //Call Refresh token
                    new RefreashToken(this).callGetRefreashToken(this);

                    String postText = et_post.getText().toString().trim();
                    btn_post.setEnabled(false);
                    if (resultList.size() == 0) {
                        postNewsFeedViewModel.validation(postText);
                        postNewsFeedViewModel.getIsValid().observe(this, new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean aBoolean) {
                                if (aBoolean) {
                                    postNewsFeedViewModel.sendPost(api_token, event_id, postText, resultList);
                                    postNewsFeedViewModel.getStatus().observe(PostNewActivity.this, new Observer<LoginOrganizer>() {
                                        @Override
                                        public void onChanged(@Nullable LoginOrganizer result) {
                                            if (result != null) {
                                                btn_post.setEnabled(true);
                                                String status = result.getHeader().get(0).getType();
                                                String message = result.getHeader().get(0).getMsg();

                                                Utility.createShortSnackBar(linear, message);
                                                postNewsFeedViewModel.startNewsFeedFragment(PostNewActivity.this);
                                            } else {
                                                btn_post.setEnabled(true);

                                                Utility.createShortSnackBar(linear, "Error occured, Please try after some time..");
                                            }
                                        }
                                    });
                                } else {
                                    btn_post.setEnabled(true);
                                    Utility.createShortSnackBar(linear, "Please enter some status to post");

                                }
                            }
                        });
                    } else {
                        try {
                            /*eventAppDB = EventAppDB.getDatabase(this);
                            Log.d("Tot_Count",String.valueOf(eventAppDB.uploadMultimediaDao().getRowCount()));
                            Date date = new Date();
                            long time = date.getTime();
                            Timestamp ts = new Timestamp(time);

                            for (int i = 0; i < resultList.size(); i++) {
                                UploadMultimedia uploadMultimedia = new UploadMultimedia();
                                uploadMultimedia.setMultimedia_id(String.valueOf(eventAppDB.uploadMultimediaDao().getRowCount()+1));
                                uploadMultimedia.setMedia_file(resultList.get(i).getmOriginalFilePath());
                                uploadMultimedia.setMedia_file_thumb(resultList.get(i).getmThumbPath());
                                uploadMultimedia.setNews_feed_id("");
                                uploadMultimedia.setIs_uploaded("0");
                                uploadMultimedia.setMedia_type(resultList.get(i).getmMediaType());
                                uploadMultimedia.setCompressedPath("");
                                uploadMultimedia.setFolderUniqueId(ts.toString());

                                eventAppDB.uploadMultimediaDao().insertMultimediaToUpload(uploadMultimedia);
                            }*/
                            postNewsFeedViewModel.insertMultimediaIntoDB(this, postText, resultList);
                            postNewsFeedViewModel.getDBStatus().observe(this, new Observer<Boolean>() {
                                @Override
                                public void onChanged(Boolean aBoolean) {
                                    if (aBoolean) {
                                        postNewsFeedViewModel.startNewsFeedFragment(PostNewActivity.this);
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    btn_post.setEnabled(true);
                    Utility.createShortSnackBar(linear, "No Internet Connection");
                }
                break;
        }
    }

    private void setPagerAdapter(List<SelectedImages> resultList) {
        viewPagerAdapter = new ViewPagerMultimediaAdapter(PostNewActivity.this, resultList);
        viewPagerAdapter.notifyDataSetChanged();
        vp_media.setAdapter(viewPagerAdapter);
        vp_media.setCurrentItem(0);
        dotscount = viewPagerAdapter.getCount();
        setupPagerIndidcatorDots(0, ll_media_dots, dotscount);

        vp_media.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setupPagerIndidcatorDots(position, ll_media_dots, dotscount);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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