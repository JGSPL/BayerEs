package com.procialize.bayer2020.ui.newsFeedPost.view;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.percolate.mentions.Mentionable;
import com.percolate.mentions.Mentions;
import com.percolate.mentions.QueryListener;
import com.percolate.mentions.SuggestionsListener;
import com.procialize.bayer2020.ConnectionDetector;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.Database.EventAppDB;
import com.procialize.bayer2020.GetterSetter.LoginOrganizer;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.CommonFirebase;
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.SharedPreferencesConstant;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.costumTools.RecyclerItemClickListener;
import com.procialize.bayer2020.ui.attendee.roomDB.TableAttendee;
import com.procialize.bayer2020.ui.attendee.viewmodel.AttendeeDatabaseViewModel;
import com.procialize.bayer2020.ui.newsFeedPost.adapter.ViewPagerMultimediaAdapter;
import com.procialize.bayer2020.ui.newsFeedPost.model.SelectedImages;
import com.procialize.bayer2020.ui.newsFeedPost.viewModel.PostNewsFeedViewModel;
import com.procialize.bayer2020.ui.newsfeed.model.Mention;
import com.procialize.bayer2020.ui.tagging.adapter.UsersAdapter;
import com.procialize.bayer2020.ui.tagging.model.TaggingComment;
import com.yanzhenjie.album.AlbumFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;

public class PostNewActivity extends AppCompatActivity implements View.OnClickListener, QueryListener, SuggestionsListener {

    LinearLayout ll_upload_media, ll_media_dots, linear, ll_info, ll_inner_layout,ll_post_status;//, ll_post
    EditText et_post;
    TextView btn_post, tv_count, tv_name, txtUploadImg, tv_header, tv_total_count, textData;
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
    ImageView iv_back, iv_profile, imguploadimg;
    String event_id, api_token, postText = "";
    UsersAdapter usersAdapter;
    private Mentions mentions;
    AttendeeDatabaseViewModel attendeeDatabaseViewModel;
    List<TableAttendee> attendeeList = null;
    View view_top, view_left, view_right, view_bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new);
//Call Refresh token
        new RefreashToken(this).callGetRefreashToken(this);

        eventAppDB = EventAppDB.getDatabase(this);
        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(this, EVENT_ID);

        CommonFirebase.crashlytics("PostActivity", api_token);
        CommonFirebase.firbaseAnalytics(this, "PostActivity", api_token);
        Log.d("tot_count", String.valueOf(eventAppDB.uploadMultimediaDao().getRowCount()));
        postNewsFeedViewModel = ViewModelProviders.of(this).get(PostNewsFeedViewModel.class);
        attendeeDatabaseViewModel = ViewModelProviders.of(this).get(AttendeeDatabaseViewModel.class);

        cd = ConnectionDetector.getInstance(this);
        ll_upload_media = findViewById(R.id.ll_upload_media);
        iv_back = findViewById(R.id.iv_back);
        iv_profile = findViewById(R.id.iv_profile);
        ll_media_dots = findViewById(R.id.ll_media_dots);
        ll_info = findViewById(R.id.ll_info);
        //ll_post = findViewById(R.id.ll_post);
        ll_inner_layout = findViewById(R.id.ll_inner_layout);
        ll_post_status = findViewById(R.id.ll_post_status);
        linear = findViewById(R.id.linear);
        btn_post = findViewById(R.id.btn_post);
        tv_count = findViewById(R.id.tv_count);
        tv_name = findViewById(R.id.tv_name);
        tv_header = findViewById(R.id.tv_header);
        txtUploadImg = findViewById(R.id.txtUploadImg);
        tv_total_count = findViewById(R.id.tv_total_count);
        textData = findViewById(R.id.textData);
        view_top = findViewById(R.id.view_top);
        view_left = findViewById(R.id.view_left);
        view_right = findViewById(R.id.view_right);
        view_bottom = findViewById(R.id.view_bottom);
        et_post = findViewById(R.id.et_post);
        imguploadimg = findViewById(R.id.imguploadimg);
        iv_back.setOnClickListener(this);

      //  setDynamicColor();

        String profilePic = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_PROFILE_PIC);
        String fName = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_FNAME);
        String lName = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_LNAME);
        String designation = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_DESIGNATION);
        String city = SharedPreference.getPref(this, SharedPreferencesConstant.KEY_CITY);
        tv_name.setText(fName + " " + lName);
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
                /*if (s.length() > 0 || resultList.size()>0) {
                    btn_post.setTextColor(Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_1)));
                    btn_post.setBackgroundColor(Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_2)));
                    ll_post.setBackgroundColor(Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_1)));
                } else {
                    btn_post.setTextColor(Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_2)));
                    btn_post.setBackgroundColor(Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_1)));
                    ll_post.setBackgroundColor(Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_2)));
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

                    /*for (int j = 0; j < mAlbumFiles.size(); j++) {
                        from = null;
                        too = null;
                        thumbpath = null;
                        thumb = null;
                        tothumb = null;
                        tovideo = null;
                        //To check selected image/video is already present in previous arraylist
                        Log.d("org_path->",(mAlbumFiles.get(j).getPath()));
                        if (!selectedFileList.contains(mAlbumFiles.get(j).getPath())) {
                            if (mAlbumFiles.get(j).getMediaType() == AlbumFile.TYPE_VIDEO) {
                                strMediaType = "video";

                                Calendar calendar = Calendar.getInstance();
                                //Returns current time in millis
                                long timeStamp = calendar.getTimeInMillis();
                                videofile = "video_" *//*+ i1 *//*+ timeStamp + "_";
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
                                Calendar calendar = Calendar.getInstance();
                                //Returns current time in millis
                                long timeStamp = calendar.getTimeInMillis();
                                imagefile = "image_" *//*+ i1*//* + timeStamp + "_";
                                from = new File(mAlbumFiles.get(j).getPath());

                                String root = Environment.getExternalStorageDirectory().toString();
                                File moviesDir = new File(root + Constant.FOLDER_DIRECTORY + Constant.IMAGE_DIRECTORY);
                                Log.d("dest_path",moviesDir.getPath()+"/"+from.getName());
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
                    }*/

                    try {
                        for (int j = 0; j < mAlbumFiles.size(); j++) {
                            if (!selectedFileList.contains(mAlbumFiles.get(j).getPath())) {
                                if (mAlbumFiles.get(j).getMediaType() == AlbumFile.TYPE_VIDEO) {
                                    strMediaType = "video";
                                } else {
                                    strMediaType = "image";
                                }
                                resultList.add(new SelectedImages(mAlbumFiles.get(j).getPath(),
                                        mAlbumFiles.get(j).getPath(), mAlbumFiles.get(j).getThumbPath(), false, strMediaType, mAlbumFiles.get(j).getMimeType()));
                                if (mAlbumFiles.get(j).getMediaType() == AlbumFile.TYPE_VIDEO) {
                                    videoPositionArray.add(j);
                                }
                            }
                        }
                        setPagerAdapter(resultList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //Tagging Functionality
        attendeeList = new ArrayList<TableAttendee>();
        attendeeDatabaseViewModel.getAttendeeDetails(this);
        attendeeDatabaseViewModel.getAttendeeList().observeForever(new Observer<List<TableAttendee>>() {
            @Override
            public void onChanged(List<TableAttendee> tableAttendees) {
                attendeeList = tableAttendees;
            }
        });
        mentions = new Mentions.Builder(this, et_post)
                .suggestionsListener(this)
                .queryListener(this)
                .build();

        setupMentionsList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.ll_upload_media:
                //postNewsFeedViewModel.selectAlbum(this);
                postNewsFeedViewModel.selectImage(this);

                break;
            case R.id.btn_post:
                Utility.hideKeyboard(v);
                if (cd.isConnectingToInternet()) {
                    //Call Refresh token
                    new RefreashToken(this).callGetRefreashToken(this);

                    postText = et_post.getText().toString().trim();
                    final TaggingComment comment = new TaggingComment();
                    comment.setComment(postText);
                    comment.setMentions(mentions.getInsertedMentions());
                    textData.setText(postText);

                    postText = highlightMentions(textData, comment.getMentions());

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

                                                //btn_post.setEnabled(true);
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
                            btn_post.setEnabled(false);
                            Date date = new Date();
                            long time = date.getTime();

                            postNewsFeedViewModel.insertMultimediaIntoDB(this, postText, resultList,String.valueOf(time));
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
        for (int j = 0; j < resultList.size(); j++) {
            Log.d("mpath", resultList.get(j).getmPath());
        }
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

    private void setDynamicColor() {
        CommonFunction.showBackgroundImage(this, linear);
        ll_info.setBackgroundColor(Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_2)));
        tv_name.setTextColor(Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_1)));
        ll_upload_media.setBackgroundColor(Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_1)));
        ll_inner_layout.setBackgroundColor(Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_2)));
        et_post.setBackgroundColor(Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_2)));
        ll_post_status.setBackgroundColor(Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_1)));

        et_post.setTextColor(Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_1)));
        et_post.setHintTextColor(Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_1)));
        txtUploadImg.setTextColor(Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_1)));
        tv_count.setTextColor(Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_3)));
        tv_total_count.setTextColor(Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_3)));
        btn_post.setTextColor(Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_4)));
        //btn_post.setBackgroundColor(Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_1)));
        //ll_post.setBackgroundColor(Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_4)));
        view_top.setBackgroundColor(Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_4)));
        view_left.setBackgroundColor(Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_4)));
        view_right.setBackgroundColor(Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_4)));
        view_bottom.setBackgroundColor(Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_4)));
        int color1 = Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_1));
        imguploadimg.setColorFilter(color1, PorterDuff.Mode.SRC_ATOP);

        int color4 = Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_4));
        iv_back.setColorFilter(color4, PorterDuff.Mode.SRC_ATOP);
        tv_header.setTextColor(Color.parseColor(SharedPreference.getPref(PostNewActivity.this, EVENT_COLOR_4)));
    }

    //Tagging Functionality


    private void setupMentionsList() {
        final RecyclerView mentionsList = findViewById(R.id.mentions_list);
        mentionsList.setLayoutManager(new LinearLayoutManager(this));
        usersAdapter = new UsersAdapter(this);
        mentionsList.setAdapter(usersAdapter);
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(et_post.getWindowToken(), 0);
        // set on item click listener
        mentionsList.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
                final TableAttendee user = usersAdapter.getItem(position);

                /*
                 * We are creating a mentions object which implements the
                 * <code>Mentionable</code> interface this allows the library to set the offset
                 * and length of the mention.
                 */
                if (user != null) {
                    final Mention mention = new Mention();
                    mention.setMentionName(user.getFirst_name() + " " + user.getLast_name());
                    mention.setMentionid(user.getAttendee_id());
                    mentions.insertMention(mention);
                }
            }
        }));
    }

    @Override
    public void onQueryReceived(String s) {
        postNewsFeedViewModel.searchUsers(s, attendeeList);
        postNewsFeedViewModel.getAttendeeList().observe(this, new Observer<List<TableAttendee>>() {
            @Override
            public void onChanged(List<TableAttendee> tableAttendees) {
                if (tableAttendees != null && !tableAttendees.isEmpty()) {
                    ArrayList<String> arr = new ArrayList<String>(tableAttendees.size());
                    for (int j = 0; j < tableAttendees.size(); j++) {
                        arr.add(tableAttendees.get(j).getAttendee_id());
                    }

                    for (int i = 0; i < mentions.getInsertedMentions().size(); i++) {
                        String mentionName = mentions.getInsertedMentions().get(i).getMentionid();
                        if (arr.contains(mentionName)) {
                            int index = arr.indexOf(mentionName);
                            tableAttendees.remove(index);
                            arr.clear();
                            for (int j = 0; j < tableAttendees.size(); j++) {
                                arr.add(tableAttendees.get(j).getAttendee_id());
                            }
                        }
                    }
                    usersAdapter.clear();
                    usersAdapter.addAll(tableAttendees);
                    postNewsFeedViewModel.showMentionsList(PostNewActivity.this, true);
                } else {
                    postNewsFeedViewModel.showMentionsList(PostNewActivity.this, false);
                }
                if (postNewsFeedViewModel != null && postNewsFeedViewModel.getAttendeeList().hasObservers()) {
                    postNewsFeedViewModel.getAttendeeList().removeObservers(PostNewActivity.this);
                }
            }
        });
    }

    @Override
    public void displaySuggestions(boolean b) {
        if (b) {
            com.percolate.caffeine.ViewUtils.showView(this, R.id.mentions_list_layout);
        } else {
            com.percolate.caffeine.ViewUtils.hideView(this, R.id.mentions_list_layout);
        }
    }

    //Tagging function By Aparna
    private String highlightMentions(TextView commentTextView, final List<Mentionable> mentions) {
        final SpannableStringBuilder spannable = new SpannableStringBuilder(commentTextView.getText());
        for (int i = 0; i < mentions.size(); i++) {
            EventAppDB eventAppDB = EventAppDB.getDatabase(PostNewActivity.this);
            String mentionNameFromDb = eventAppDB.attendeeDao().getMentionNameFromAttendeeId(String.valueOf(mentions.get(i).getMentionid()));
            if (mentionNameFromDb.isEmpty()) {
                mentionNameFromDb = "<" + mentions.get(i).getMentionid() + "^" + mentions.get(i).getMentionName() + ">";
            }
            int offset = mentions.get(i).getMentionOffset();
            int length = mentions.get(i).getMentionLength();
            if (i != 0) {
                for (int j = 0; j < i; j++) {
                    offset = offset + mentions.get(j).getMentionid().length() + 3;
                }
            }
            spannable.setSpan(mentionNameFromDb, offset, offset + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.replace(offset, offset + length, (CharSequence) mentionNameFromDb);
            //String mentionedData = data.replace(mentionName, mentionNameFromDb);
            commentTextView.setText(spannable, TextView.BufferType.SPANNABLE);
        }
        return commentTextView.getText().toString();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}