package com.procialize.eventapp.ui.newsFeedComment.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LifecycleOwner;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.percolate.mentions.Mentionable;
import com.percolate.mentions.Mentions;
import com.percolate.mentions.QueryListener;
import com.percolate.mentions.SuggestionsListener;
import com.procialize.eventapp.BuildConfig;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Constants.Constant;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.Database.EventAppDB;
import com.procialize.eventapp.GetterSetter.Header;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.MainActivity;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.SharedPreferencesConstant;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.costumTools.RecyclerItemClickListener;
import com.procialize.eventapp.ui.attendee.roomDB.TableAttendee;
import com.procialize.eventapp.ui.attendee.view.AttendeeDetailActivity;
import com.procialize.eventapp.ui.attendee.viewmodel.AttendeeDatabaseViewModel;
import com.procialize.eventapp.ui.newsFeedComment.adapter.CommentAdapter;
import com.procialize.eventapp.ui.newsFeedComment.adapter.GifEmojiAdapter;
import com.procialize.eventapp.ui.newsFeedComment.model.Comment;
import com.procialize.eventapp.ui.newsFeedComment.model.CommentDetail;
import com.procialize.eventapp.ui.newsFeedComment.model.GifResponse;
import com.procialize.eventapp.ui.newsFeedComment.model.GifResult;
import com.procialize.eventapp.ui.newsFeedComment.model.LikePost;
import com.procialize.eventapp.ui.newsFeedComment.viewModel.CommentViewModel;
import com.procialize.eventapp.ui.newsFeedPost.viewModel.PostNewsFeedViewModel;
import com.procialize.eventapp.ui.newsfeed.adapter.SwipeMultimediaAdapter;
import com.procialize.eventapp.ui.newsfeed.model.FetchNewsfeedMultiple;
import com.procialize.eventapp.ui.newsfeed.model.Mention;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;
import com.procialize.eventapp.ui.newsfeed.networking.NewsfeedRepository;
import com.procialize.eventapp.ui.newsfeed.view.NewsFeedFragment;
import com.procialize.eventapp.ui.newsfeed.viewmodel.NewsFeedDatabaseViewModel;
import com.procialize.eventapp.ui.tagging.adapter.UsersAdapter;
import com.procialize.eventapp.ui.tagging.model.TaggingComment;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jzvd.JzvdStd;

import static com.procialize.eventapp.Utility.CommonFunction.getLocalBitmapUri;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_5;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.IS_GOD;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_ATTENDEE_ID;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.NEWS_FEED_MEDIA_PATH;


public class CommentActivity extends AppCompatActivity implements View.OnClickListener, GifEmojiAdapter.GifEmojiAdapterListner,
        CommentAdapter.CommentAdapterListner, QueryListener, SuggestionsListener {

    private static final String API_KEY = "TVG20YJW1MXR";
    ImageView iv_gif, iv_back_gif, iv_likes, iv_comments, iv_share, iv_profile, iv_back, moreIV, iv_send;
    EditText et_comment, et_search_gif;
    FrameLayout fl_gif_container, fl_post_comment;
    LinearLayout ll_comment_container, ll_media_dots, ll_main, ll_root;
    CommentViewModel commentViewModel;
    String anon_id;
    RecyclerView rv_gif, rv_comments;
    ProgressBar pb_emoji;
    ViewPager vp_media;
    private String mediaPosition, newsfeedId;
    int positionOfList;
    private Newsfeed_detail newsfeed_detail;
    public static int swipableAdapterPosition = 0;
    private TextView tv_status, tv_name, tv_designation, tv_date_time, tv_no_of_comments, tv_no_of_likes, tv_header, testdataPost, textData;
    String event_id;
    ConnectionDetector connectionDetector;
    String commentText = "";
    BottomSheetDialog dialog;
    Dialog contentDialog;
    List<CommentDetail> commentList = new ArrayList<>();
    String noOfLikes = "0", likeStatus = "", api_token, substring;
    private String strPath, eventColor1, eventColor2, eventColor3, eventColor4, eventColor5, ATTENDEE_STATUS, ATTENDEE_ID;
    public Dialog dialogShare;
    View v_divider;
    NewsFeedDatabaseViewModel newsFeedDatabaseViewModel;
    PostNewsFeedViewModel postNewsFeedViewModel;
    UsersAdapter usersAdapter;
    private Mentions mentions;
    AttendeeDatabaseViewModel attendeeDatabaseViewModel;
    List<TableAttendee> attendeeList = null;
    private static NewsfeedRepository newsRepository;
    Dialog  myDialog;
    String spannedString;
    String postStatus ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        newsRepository = NewsfeedRepository.getInstance();
        //Call Refresh token
        new RefreashToken(this).callGetRefreashToken(this);

        eventColor1 = SharedPreference.getPref(this, EVENT_COLOR_1);
        eventColor2 = SharedPreference.getPref(this, EVENT_COLOR_2);
        eventColor3 = SharedPreference.getPref(this, EVENT_COLOR_3);
        eventColor4 = SharedPreference.getPref(this, EVENT_COLOR_4);
        eventColor5 = SharedPreference.getPref(this, EVENT_COLOR_5);
        ATTENDEE_STATUS = SharedPreference.getPref(this, IS_GOD);
        ATTENDEE_ID = SharedPreference.getPref(this, KEY_ATTENDEE_ID);

        commentViewModel = ViewModelProviders.of(this).get(CommentViewModel.class);
        newsFeedDatabaseViewModel = ViewModelProviders.of(this).get(NewsFeedDatabaseViewModel.class);
        postNewsFeedViewModel = ViewModelProviders.of(this).get(PostNewsFeedViewModel.class);
        attendeeDatabaseViewModel = ViewModelProviders.of(this).get(AttendeeDatabaseViewModel.class);

        connectionDetector = ConnectionDetector.getInstance(this);
        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(this, EVENT_ID);
        Intent intent = getIntent();
        try {
            newsfeed_detail = (Newsfeed_detail) getIntent().getSerializableExtra("Newsfeed_detail");
            newsfeedId = intent.getStringExtra("newsfeedId");
            mediaPosition = intent.getStringExtra("position");
            swipableAdapterPosition = Integer.parseInt(mediaPosition);
            positionOfList = Integer.parseInt("positionOfList");
        } catch (Exception e) {
            e.printStackTrace();
        }


        tv_header = findViewById(R.id.tv_header);
        iv_back = findViewById(R.id.iv_back);
        iv_gif = findViewById(R.id.iv_gif);
        iv_send = findViewById(R.id.iv_send);
        moreIV = findViewById(R.id.moreIV);
        iv_likes = findViewById(R.id.iv_likes);
        iv_profile = findViewById(R.id.iv_profile);
        iv_comments = findViewById(R.id.iv_comments);
        iv_share = findViewById(R.id.iv_share);
        rv_gif = findViewById(R.id.rv_gif);
        rv_comments = findViewById(R.id.rv_comments);
        pb_emoji = findViewById(R.id.pb_emoji);
        iv_gif.setOnClickListener(this);
        iv_likes.setOnClickListener(this);
        et_comment = findViewById(R.id.et_comment);
        iv_back_gif = findViewById(R.id.iv_back_gif);
        et_search_gif = findViewById(R.id.et_search_gif);
        fl_gif_container = findViewById(R.id.fl_gif_container);
        fl_post_comment = findViewById(R.id.fl_post_comment);
        fl_post_comment.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        ll_comment_container = findViewById(R.id.ll_comment_container);
        ll_media_dots = findViewById(R.id.ll_media_dots);
        ll_main = findViewById(R.id.ll_main);
        ll_root = findViewById(R.id.ll_root);
        vp_media = findViewById(R.id.vp_media);
        tv_status = findViewById(R.id.tv_status);
        tv_name = findViewById(R.id.tv_name);
        tv_designation = findViewById(R.id.tv_designation);
        tv_date_time = findViewById(R.id.tv_date_time);
        tv_no_of_comments = findViewById(R.id.tv_no_of_comments);
        tv_no_of_likes = findViewById(R.id.tv_no_of_likes);
        testdataPost = findViewById(R.id.testdataPost);
        textData = findViewById(R.id.textData);
        v_divider = findViewById(R.id.v_divider);
        tv_no_of_likes.setOnClickListener(this);
        moreIV.setOnClickListener(this);
        CommonFunction.showBackgroundImage(this, ll_main);


        iv_share.setOnClickListener(this);
        iv_back_gif.setOnClickListener(this);

        if (ConnectionDetector.getInstance(this).isConnectingToInternet()) {
            if (newsfeed_detail == null || newsfeed_detail.getAttendee_id() == null) {
                commentViewModel.getNewsFeedDetails(api_token, event_id, newsfeedId);
                commentViewModel.newsFeedDeatils().observe(this, new Observer<FetchNewsfeedMultiple>() {
                    @Override
                    public void onChanged(FetchNewsfeedMultiple fetchNewsfeedMultiple) {
                        newsfeed_detail = fetchNewsfeedMultiple.getNewsfeed_detail().get(0);
                    }
                });
            }
        } else {
            Utility.createShortSnackBar(ll_main, "No Internet Connection");
        }
        try {
            /*String postStatus = newsfeed_detail.getPost_status().trim();
            if (!postStatus.trim().isEmpty()) {
                tv_status.setText(postStatus);
                tv_status.setVisibility(View.VISIBLE);
            } else {
                tv_status.setVisibility(View.GONE);
            }
            testdataPost.setText(postStatus);*/

            /**
             * Code for HTML text + Tagging
             */
            if (newsfeed_detail.getPost_status().contains("\n")) {
                postStatus = newsfeed_detail.getPost_status().trim().replace("\n", "<br/>");
            } else {
                postStatus = newsfeed_detail.getPost_status().trim();
            }
            spannedString = String.valueOf(Jsoup.parse(postStatus)).trim();//Html.fromHtml(feedData.getPost_status(), Html.FROM_HTML_MODE_COMPACT).toString();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Spanned strPost = Html.fromHtml(spannedString, Html.FROM_HTML_MODE_COMPACT);
                testdataPost.setText(Utility.trimTrailingWhitespace(strPost));
            }else
            {
                Spanned strPost = Html.fromHtml(spannedString);
                testdataPost.setText(Utility.trimTrailingWhitespace(strPost));
            }

            final SpannableStringBuilder stringBuilder = new SpannableStringBuilder(testdataPost.getText());
            if (newsfeed_detail.getPost_status() != null) {

                if (newsfeed_detail.getPost_status().isEmpty()) {
                    tv_status.setVisibility(View.GONE);
                } else {
                    tv_status.setVisibility(View.VISIBLE);
                }
                int flag = 0;
                for (int i = 0; i < stringBuilder.length(); i++) {
                    String sample = stringBuilder.toString();
                    if ((stringBuilder.charAt(i) == '<')) {
                        try {
                            String text = "<";
                            String text1 = ">";

                            if (flag == 0) {
                                int start = sample.indexOf(text, i);
                                int end = sample.indexOf(text1, i);

                                Log.v("Indexes of", "Start : " + start + "," + end);
                                try {
                                    substring = sample.substring(start, end + 1);
                                    Log.v("String names: ", substring);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                if (substring.contains("<")) {
                                    if (sample.contains(substring)) {
                                        substring = substring.replace("<", "");
                                        substring = substring.replace(">", "");
                                        int index = substring.indexOf("^");
//                                    substring = substring.replace("^", "");
                                        final String attendeeid = substring.substring(0, index);
                                        substring = substring.substring(index + 1, substring.length());


                                        stringBuilder.setSpan(stringBuilder, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        stringBuilder.setSpan(new ForegroundColorSpan(Color.RED), start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                                        stringBuilder.setSpan(new ClickableSpan() {
                                            @Override
                                            public void onClick(View widget) {
                                                EventAppDB eventAppDB = EventAppDB.getDatabase(CommentActivity.this);
                                                newsFeedDatabaseViewModel.getAttendeeDetailsFromId(CommentActivity.this, attendeeid);
                                                newsFeedDatabaseViewModel.getAttendeeDetails().observe(CommentActivity.this, new Observer<List<TableAttendee>>() {
                                                    @Override
                                                    public void onChanged(List<TableAttendee> tableAttendees) {
                                                        if (tableAttendees != null) {
                                                            Intent intent = new Intent(CommentActivity.this, AttendeeDetailActivity.class);
                                                            intent.putExtra("fname", tableAttendees.get(0).getFirst_name());
                                                            intent.putExtra("lname", tableAttendees.get(0).getLast_name());
                                                            intent.putExtra("company", tableAttendees.get(0).getCompany_name());
                                                            intent.putExtra("city", tableAttendees.get(0).getCity());
                                                            intent.putExtra("designation", tableAttendees.get(0).getDesignation());
                                                            intent.putExtra("prof_pic", tableAttendees.get(0).getProfile_picture());
                                                            intent.putExtra("attendee_type", tableAttendees.get(0).getAttendee_type());
                                                            intent.putExtra("mobile", tableAttendees.get(0).getMobile());
                                                            intent.putExtra("email", tableAttendees.get(0).getEmail());
                                                            startActivity(intent);
                                                        }
                                                    }
                                                });
                                            }
                                        }, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        stringBuilder.replace(start, end + 1, substring);
                                        //holder.testdata.setText(stringBuilder, TextView.BufferType.SPANNABLE);
                                        tv_status.setMovementMethod(LinkMovementMethod.getInstance());
                                        tv_status.setText(stringBuilder);
                                        flag = 1;
                                    }
                                }
                            } else {

                                int start = sample.indexOf(text, i);
                                int end = sample.indexOf(text1, i);

                                Log.v("Indexes of", "Start : " + start + "," + end);
                                try {
                                    substring = sample.substring(start, end + 1);
                                    Log.v("String names: ", substring);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (substring.contains("<")) {
                                    if (sample.contains(substring)) {
                                        substring = substring.replace("<", "");
                                        substring = substring.replace(">", "");
                                        int index = substring.indexOf("^");
//                                    substring = substring.replace("^", "");
                                        final String attendeeid = substring.substring(0, index);
                                        substring = substring.substring(index + 1, substring.length());


                                        stringBuilder.setSpan(stringBuilder, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        stringBuilder.setSpan(new ForegroundColorSpan(Color.RED), start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                        stringBuilder.setSpan(new ClickableSpan() {
                                            @Override
                                            public void onClick(View widget) {
                                                newsFeedDatabaseViewModel.getAttendeeDetailsFromId(CommentActivity.this, attendeeid);
                                                newsFeedDatabaseViewModel.getAttendeeDetails().observe(CommentActivity.this, new Observer<List<TableAttendee>>() {
                                                    @Override
                                                    public void onChanged(List<TableAttendee> tableAttendees) {
                                                        if (tableAttendees != null) {
                                                            Intent intent = new Intent(CommentActivity.this, AttendeeDetailActivity.class);
                                                            intent.putExtra("fname", tableAttendees.get(0).getFirst_name());
                                                            intent.putExtra("lname", tableAttendees.get(0).getLast_name());
                                                            intent.putExtra("company", tableAttendees.get(0).getCompany_name());
                                                            intent.putExtra("city", tableAttendees.get(0).getCity());
                                                            intent.putExtra("designation", tableAttendees.get(0).getDesignation());
                                                            intent.putExtra("prof_pic", tableAttendees.get(0).getProfile_picture());
                                                            intent.putExtra("attendee_type", tableAttendees.get(0).getAttendee_type());
                                                            intent.putExtra("mobile", tableAttendees.get(0).getMobile());
                                                            intent.putExtra("email", tableAttendees.get(0).getEmail());
                                                            startActivity(intent);
                                                        }
                                                    }
                                                });
                                            }
                                        }, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                        stringBuilder.replace(start, end + 1, substring);
                                        //holder.testdata.setText(stringBuilder, TextView.BufferType.SPANNABLE);
                                        tv_status.setMovementMethod(LinkMovementMethod.getInstance());
                                        tv_status.setText(stringBuilder);

                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                tv_status.setText(stringBuilder);
            } else {
                tv_status.setVisibility(View.GONE);
            }

            String name = newsfeed_detail.getFirst_name() + " " + newsfeed_detail.getLast_name();
            if (!name.trim().isEmpty()) {
                tv_name.setText(name);
                tv_name.setVisibility(View.VISIBLE);
            } else {
                tv_name.setVisibility(View.GONE);
            }

            String designation = newsfeed_detail.getDesignation();
            String city = newsfeed_detail.getCity_id();
            if (!designation.trim().isEmpty() && !city.trim().isEmpty()) {
                tv_designation.setText(designation + " - " + city);
                tv_designation.setVisibility(View.VISIBLE);
            } else if (!designation.trim().isEmpty() && city.trim().isEmpty()) {
                tv_designation.setText(designation);
                tv_designation.setVisibility(View.VISIBLE);
            } else if (designation.trim().isEmpty() && !city.trim().isEmpty()) {
                tv_designation.setText(city);
                tv_designation.setVisibility(View.VISIBLE);
            } else {
                tv_designation.setVisibility(View.GONE);
            }

            String dateTime = newsfeed_detail.getPost_date();
            if (!dateTime.isEmpty()) {
                String convertedDate = CommonFunction.convertDate(dateTime);
                tv_date_time.setText(convertedDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        et_search_gif.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    commentViewModel.searchGif(s.toString(), API_KEY, anon_id);
                    commentViewModel.searchGifList().observe(CommentActivity.this, new Observer<GifResponse>() {
                        @Override
                        public void onChanged(GifResponse listMutableLiveData) {
                            List<GifResult> listResult = listMutableLiveData.getResults();
                            setupGifAdapter(listResult);
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        if (newsfeed_detail.getProfile_pic().trim() != null) {
            Glide.with(this)
                    .load(newsfeed_detail.getProfile_pic().trim())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            //pro.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            //iv_profile.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(iv_profile);
        }

        if (newsfeed_detail.getNews_feed_media().size() > 0) {
            vp_media.setVisibility(View.VISIBLE);
            ll_media_dots.setVisibility(View.VISIBLE);
            setupPagerAdapter();
        } else {
            vp_media.setVisibility(View.GONE);
            ll_media_dots.setVisibility(View.GONE);
        }

        if (newsfeed_detail.getTotal_comments().equalsIgnoreCase("1")) {
            tv_no_of_comments.setText(newsfeed_detail.getTotal_comments() + " Comment");
        } else {
            tv_no_of_comments.setText(newsfeed_detail.getTotal_comments() + " Comments");
        }

        if (newsfeed_detail.getTotal_likes().equalsIgnoreCase("1")) {
            tv_no_of_likes.setText(newsfeed_detail.getTotal_likes() + " Like");
        } else {
            tv_no_of_likes.setText(newsfeed_detail.getTotal_likes() + " Likes");
        }

        if (newsfeed_detail.getLike_flag().equalsIgnoreCase("0")) {
            iv_likes.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
        } else {
            iv_likes.setImageDrawable(getResources().getDrawable(R.drawable.ic_active_like));
           /* int color = Color.parseColor(colorActive);
            iv_likes.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);*/
        }

        /*iv_comments.setImageDrawable(getResources().getDrawable(R.drawable.ic_comment));
        iv_share.setImageDrawable(getResources().getDrawable(R.drawable.ic_share));*/
        getComments();
        setDynamicColor();

        //Tagging Functionality
        attendeeList = new ArrayList<TableAttendee>();
        attendeeDatabaseViewModel.getAttendeeDetails(this);
        attendeeDatabaseViewModel.getAttendeeList().observeForever(new Observer<List<TableAttendee>>() {
            @Override
            public void onChanged(List<TableAttendee> tableAttendees) {
                attendeeList = tableAttendees;
            }
        });
        mentions = new Mentions.Builder(this, et_comment)
                .suggestionsListener(this)
                .queryListener(this)
                .build();

        setupMentionsList();
    }

    public void getComments() {
        if (connectionDetector.isConnectingToInternet()) {
            String newsFeedId = newsfeed_detail.getNews_feed_id();
            commentViewModel.getComment(api_token, event_id, newsFeedId, "100", "1");
            commentViewModel.getCommentList().observeForever( new Observer<Comment>() {
                @Override
                public void onChanged(Comment comment) {
                    if (comment != null) {
                        commentList.clear();
                        commentList = comment.getCommentDetails();
                        setupCommentAdapter(commentList);
                        showCommentCount(commentList);
                    }else
                    { setupCommentAdapter(commentList);}
                }
            });
        } else {
            Utility.createShortSnackBar(ll_main, "No Internet Connection");
        }
    }

    public void setupPagerAdapter() {

        String mediaPath = SharedPreference.getPref(this, SharedPreferencesConstant.NEWS_FEED_MEDIA_PATH);

        final ArrayList<String> imagesSelectednew = new ArrayList<>();
        final ArrayList<String> imagesSelectednew1 = new ArrayList<>();
        for (int i = 0; i < newsfeed_detail.getNews_feed_media().size(); i++) {
            imagesSelectednew.add(mediaPath + newsfeed_detail.getNews_feed_media().get(i).getMedia_file());
            imagesSelectednew1.add(mediaPath + newsfeed_detail.getNews_feed_media().get(i).getThumb_image());
        }

        setupPagerIndidcatorDots(0, ll_media_dots, imagesSelectednew.size());
        SwipeMultimediaAdapter swipepagerAdapter = new SwipeMultimediaAdapter(CommentActivity.this, imagesSelectednew, imagesSelectednew1, newsfeed_detail.getNews_feed_media());
        vp_media.setAdapter(swipepagerAdapter);
        swipepagerAdapter.notifyDataSetChanged();
        vp_media.setCurrentItem(Integer.parseInt(mediaPosition));

        if (imagesSelectednew.size() > 1) {
            setupPagerIndidcatorDots(0, ll_media_dots, imagesSelectednew.size());
            vp_media.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    JzvdStd.goOnPlayOnPause();
                }

                @Override
                public void onPageSelected(int position1) {
                    JzvdStd.goOnPlayOnPause();
                    swipableAdapterPosition = position1;
                    setupPagerIndidcatorDots(position1, ll_media_dots, imagesSelectednew.size());
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    JzvdStd.goOnPlayOnPause();
                }
            });
        } else {
            ll_media_dots.setVisibility(View.GONE);
        }


    }

    public void setupCommentAdapter(List<CommentDetail> commentList) {
        CommentAdapter commentAdapter = new CommentAdapter(CommentActivity.this, commentList, CommentActivity.this);
        rv_comments.setLayoutManager(new LinearLayoutManager(this));
        rv_comments.setAdapter(commentAdapter);
        commentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                JzvdStd.releaseAllVideos();
                onBackPressed();
                break;
            case R.id.tv_no_of_likes:
                JzvdStd.releaseAllVideos();
                tv_no_of_likes.setEnabled(false);
                commentViewModel.openLikePage(this, newsfeed_detail, Integer.parseInt(mediaPosition));
                break;
            case R.id.iv_gif:
                JzvdStd.releaseAllVideos();
                if (connectionDetector.isConnectingToInternet()) {
                    ll_comment_container.setVisibility(View.GONE);
                    if (fl_gif_container.getVisibility() == View.GONE) {
                        try {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            et_comment.setTextColor(Color.parseColor("#0000"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        ll_comment_container.setVisibility(View.GONE);
                        fl_gif_container.setVisibility(View.VISIBLE);

                        //Get Gif Id
                        commentViewModel.GetId(API_KEY);
                        commentViewModel.getAnonId().observe(this, new Observer<String>() {
                            @Override
                            public void onChanged(@Nullable String result) {
                                anon_id = result;
                            }
                        });

                        //Get GIF images
                        commentViewModel.GetGif(API_KEY, anon_id);
                        commentViewModel.getGifList().observe(this, new Observer<GifResponse>() {
                            @Override
                            public void onChanged(GifResponse listMutableLiveData) {
                                List<GifResult> listResult = listMutableLiveData.getResults();
                                setupGifAdapter(listResult);
                            }
                        });


                    } else {
                        ll_comment_container.setVisibility(View.VISIBLE);
                        fl_gif_container.setVisibility(View.GONE);
                        try {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Utility.createShortSnackBar(ll_main, "No Internet Connection..!");
                }
                break;
            case R.id.fl_post_comment:
                if (connectionDetector.isConnectingToInternet()) {
                    commentText = et_comment.getText().toString();
                    final TaggingComment comment = new TaggingComment();
                    comment.setComment(commentText);
                    comment.setMentions(mentions.getInsertedMentions());
                    textData.setText(commentText);

                    fl_post_comment.setEnabled(false);

                    commentText = highlightMentions(textData, comment.getMentions());
                    commentViewModel.validation(commentText);
                    commentViewModel.getIsValid().observe(this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            if (aBoolean) {
                                Utility.hideKeyboard(ll_main);
                                commentViewModel.postComment(api_token, event_id, newsfeed_detail.getNews_feed_id(), commentText, "1");
                                commentViewModel.postCommentResponse().observe(CommentActivity.this, new Observer<LoginOrganizer>() {
                                    @Override
                                    public void onChanged(LoginOrganizer loginOrganizer) {
                                        fl_post_comment.setEnabled(true);
                                        Utility.createShortSnackBar(ll_main, "Success");
                                        et_comment.setText("");
                                        commentText = et_comment.getText().toString();
                                        getComments();
                                    }
                                });
                            } else {
                                fl_post_comment.setEnabled(true);
                                Utility.createShortSnackBar(ll_main, "Please Enter Comment");
                            }
                        }
                    });
                } else {
                    Utility.createShortSnackBar(ll_main, "No Internet Connection..!");
                }
                break;
            case R.id.iv_likes:
                if (connectionDetector.isConnectingToInternet()) {
                    noOfLikes = "0";
                    commentViewModel.likePost(api_token, event_id, newsfeed_detail.getNews_feed_id());
                    commentViewModel.likePostData().observe(CommentActivity.this, new Observer<LikePost>() {
                        @Override
                        public void onChanged(LikePost likePost) {
                            if (likePost != null) {
                                String status = likePost.getHeader().get(0).getType();
                                if (status.equalsIgnoreCase("success")) {
                                    likeStatus = likePost.getLike_status();
                                    noOfLikes = tv_no_of_likes.getText().toString().split(" ")[0];
                                    if (likeStatus.equalsIgnoreCase("1")) {
                                        showLikeCount(Integer.parseInt(noOfLikes) + 1);
                                        iv_likes.setImageDrawable(getDrawable(R.drawable.ic_active_like));

                                        newsfeed_detail.setLike_flag("1");
                                        newsfeed_detail.setTotal_likes(Integer.parseInt(noOfLikes) + 1 + "");

                                        noOfLikes = "0";
                                        likeStatus = "";
                                    } else {
                                        if (Integer.parseInt(noOfLikes) > 0) {
                                            showLikeCount(Integer.parseInt(noOfLikes) - 1);
                                            iv_likes.setImageDrawable(getDrawable(R.drawable.ic_like));
                                            noOfLikes = "0";

                                        }
                                        noOfLikes = "0";
                                        likeStatus = "";
                                    }
                                    Utility.createShortSnackBar(ll_main, likePost.getHeader().get(0).getMsg());
                                }
                                if (commentViewModel != null && commentViewModel.likePostData().hasObservers()) {
                                    commentViewModel.likePostData().removeObservers(CommentActivity.this);
                                }
                            } else {
                                Utility.createShortSnackBar(ll_main, "Failure..");
                            }
                        }
                    });
                } else {
                    Utility.createShortSnackBar(ll_main, "No Internet Connection..!");
                }
                break;
            case R.id.iv_share:
                iv_share.setEnabled(false);
                if (connectionDetector.isConnectingToInternet()) {
                    if (newsfeed_detail.getNews_feed_media().size() > 0) {

                        if (newsfeed_detail.getNews_feed_media().size() < swipableAdapterPosition) {
                            swipableAdapterPosition = 0;
                        }
                        if (newsfeed_detail.getNews_feed_media().get(swipableAdapterPosition).getMedia_type().equalsIgnoreCase("Video")) {
                            boolean isPresentFile = false;
                            File dir = new File(Environment.getExternalStorageDirectory().toString() + "/" + Constant.FOLDER_DIRECTORY + Constant.VIDEO_DIRECTORY);
                            if (dir.isDirectory()) {
                                String[] children = dir.list();
                                for (int i = 0; i < children.length; i++) {
                                    String filename = children[i].toString();
                                    if (newsfeed_detail.getNews_feed_media().get(swipableAdapterPosition).getMedia_file().equals(filename)) {
                                        isPresentFile = true;
                                    }
                                }
                            }

                            if (!isPresentFile) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                builder.setTitle("Download and Share");
                                builder.setMessage("Video will be share only after download,\nDo you want to continue for download and share?");
                                builder.setNegativeButton("NO",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                iv_share.setEnabled(true);
                                                dialog.dismiss();
                                            }
                                        });
                                builder.setPositiveButton("YES",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                iv_share.setEnabled(true);
                                                String newsFeedPath = SharedPreference.getPref(CommentActivity.this, NEWS_FEED_MEDIA_PATH);
                                                new DownloadFile().execute(/*ApiConstant.newsfeedwall*/newsFeedPath + newsfeed_detail.getNews_feed_media().get(swipableAdapterPosition).getMedia_file());
                                            }
                                        });
                                builder.show();

                            } else if (isPresentFile) {
                                String folder = Environment.getExternalStorageDirectory().toString() + "/" + Constant.FOLDER_DIRECTORY + Constant.VIDEO_DIRECTORY + "/";
                                //Create androiddeft folder if it does not exist
                                File directory = new File(folder);
                                if (!directory.exists()) {
                                    directory.mkdirs();
                                }
                                strPath = folder + newsfeed_detail.getNews_feed_media().get(swipableAdapterPosition).getMedia_file();
                              /*              ContentValues content = new ContentValues(4);
                                            content.put(MediaStore.Video.VideoColumns.DATE_ADDED,
                                                    System.currentTimeMillis() / 1000);
                                            content.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
                                            content.put(MediaStore.Video.Media.DATA, strPath);
                                            ContentResolver resolver = getActivity().getContentResolver();
                                            Uri uri =strPath; resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, content);*/
                                Uri contentUri = FileProvider.getUriForFile(CommentActivity.this,
                                        BuildConfig.APPLICATION_ID + ".android.fileprovider", new File(strPath));

                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                sharingIntent.setType("video/*");
                                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Shared via MRGE app");
                                sharingIntent.putExtra(Intent.EXTRA_TEXT, "");
                                sharingIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                                startActivity(Intent.createChooser(sharingIntent, "Shared via MRGE app"));
                                iv_share.setEnabled(true);
                            }
                        } else {
                            iv_share.setEnabled(true);
                            dialogShare = new Dialog(this);
                            dialogShare.show();
                            String newsFeedPath = SharedPreference.getPref(CommentActivity.this, NEWS_FEED_MEDIA_PATH);
                            shareImage(/*newsfeed_detail.getPost_date() + "\n" +*/ newsfeed_detail.getPost_status(), newsFeedPath + newsfeed_detail.getNews_feed_media().get(swipableAdapterPosition).getMedia_file(), this);
                        }
                    } else {
                        iv_share.setEnabled(true);
                        shareTextUrl(/*newsfeed_detail.getPost_date() + "\n" +*/ newsfeed_detail.getPost_status(), StringEscapeUtils.unescapeJava(newsfeed_detail.getPost_status()));
                    }
                } else {
                    iv_share.setEnabled(true);
                    Utility.createShortSnackBar(ll_main, "No Internet Connection..!");
                }
                break;
            case R.id.iv_back_gif:
                if (fl_gif_container.getVisibility() == View.VISIBLE) {
                    ll_comment_container.setVisibility(View.VISIBLE);
                    fl_gif_container.setVisibility(View.GONE);
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.moreIV:
                openMoreDetailsForPost(newsfeed_detail);
                break;
        }
    }

    public void setupGifAdapter(List<GifResult> listResult) {
        pb_emoji.setVisibility(View.GONE);
        GifEmojiAdapter gifEmojiAdapter = new GifEmojiAdapter(CommentActivity.this, listResult, this);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(CommentActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rv_gif.setLayoutManager(horizontalLayoutManagaer);
        rv_gif.setAdapter(gifEmojiAdapter);
        gifEmojiAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGifSelected(GifResult result) {
        ll_comment_container.setVisibility(View.VISIBLE);
        fl_gif_container.setVisibility(View.GONE);

        commentViewModel.postComment(api_token, event_id, newsfeed_detail.getNews_feed_id(), result.getMedia().get(0).getGif().getUrl(), "2");
        commentViewModel.postCommentResponse().observe(CommentActivity.this, new Observer<LoginOrganizer>() {
            @Override
            public void onChanged(LoginOrganizer loginOrganizer) {
                Utility.createShortSnackBar(ll_main, "Success");
                et_comment.setText("");
                getComments();
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

    public void showCommentCount(List<CommentDetail> commentList) {
        if (commentList.size() == 1) {
            tv_no_of_comments.setText(commentList.size() + " Comment");
        } else {
            tv_no_of_comments.setText(commentList.size() + " Comments");
        }
    }

    public void showLikeCount(int likeCount) {
        if (likeCount == 1) {
            tv_no_of_likes.setText(likeCount + " Like");
        } else {
            tv_no_of_likes.setText(likeCount + " Likes");
        }
    }

    @Override
    public void onMoreSelected(final CommentDetail comment, final int position) {
        if (connectionDetector.isConnectingToInternet()) {
            openMoreOptionsForComment(this, newsfeed_detail, comment, position, ll_main);
        } else {
            Utility.createShortSnackBar(ll_main, "No Internet Connection");
        }
    }

    public void openMoreOptionsForComment(Activity activity, final Newsfeed_detail newsfeed_detail, final CommentDetail commentDetail,
                                final int position, final LinearLayout ll_main) {
        dialog = new BottomSheetDialog(activity);
        dialog.setContentView(R.layout.botomcommentdialouge);
        TextView reportTv = dialog.findViewById(R.id.reportTv);
        TextView hideTv = dialog.findViewById(R.id.hideTv);
        TextView deleteTv = dialog.findViewById(R.id.deleteTv);
        TextView reportuserTv = dialog.findViewById(R.id.reportuserTv);
        TextView cancelTv = dialog.findViewById(R.id.cancelTv);
        if (ATTENDEE_STATUS.equalsIgnoreCase("1")) {
            reportTv.setVisibility(View.VISIBLE);
            hideTv.setVisibility(View.VISIBLE);
            deleteTv.setVisibility(View.VISIBLE);
            reportuserTv.setVisibility(View.VISIBLE);
            cancelTv.setVisibility(View.VISIBLE);
            //editIV.setVisibility(View.VISIBLE);
        } else {
            if (ATTENDEE_ID.equalsIgnoreCase(commentDetail.getUser_id())) {
                reportTv.setVisibility(View.GONE);
                hideTv.setVisibility(View.GONE);
                reportuserTv.setVisibility(View.GONE);

                deleteTv.setVisibility(View.VISIBLE);
                cancelTv.setVisibility(View.VISIBLE);
                //editIV.setVisibility(View.VISIBLE);
            } else {
                reportTv.setVisibility(View.VISIBLE);
                hideTv.setVisibility(View.VISIBLE);
                reportuserTv.setVisibility(View.VISIBLE);

                deleteTv.setVisibility(View.GONE);
                cancelTv.setVisibility(View.VISIBLE);
            }
        }

        deleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentViewModel.deleteComment(api_token, event_id, newsfeed_detail.getNews_feed_id(), commentDetail.getComment_id(), position);
                commentViewModel.commentDelete().observe(CommentActivity.this, new Observer<LoginOrganizer>() {
                    @Override
                    public void onChanged(LoginOrganizer loginOrganizer) {
                        if (loginOrganizer.getHeader().get(0).getType().equalsIgnoreCase("success")) {
                            commentList.clear();
                            dialog.dismiss();
                            Utility.createShortSnackBar(ll_main, loginOrganizer.getHeader().get(0).getMsg());
                            getComments();
                        } else {
                            dialog.dismiss();
                            Utility.createShortSnackBar(ll_main, loginOrganizer.getHeader().get(0).getMsg());
                        }
                    }
                });
            }
        });

        hideTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentViewModel.hideComment(api_token, event_id, commentDetail.getComment_id());
                commentViewModel.commentHide().observe(CommentActivity.this, new Observer<LoginOrganizer>() {
                    @Override
                    public void onChanged(LoginOrganizer loginOrganizer) {
                        if (loginOrganizer.getHeader().get(0).getType().equalsIgnoreCase("success")) {
                            dialog.dismiss();
                            Utility.createShortSnackBar(ll_main, loginOrganizer.getHeader().get(0).getMsg());
                            getComments();
                        } else {
                            dialog.dismiss();
                            Utility.createShortSnackBar(ll_main, loginOrganizer.getHeader().get(0).getMsg());
                        }
                    }
                });
            }
        });

        reportTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContentdialougeForComment("reportComment", commentDetail.getComment_id(), commentDetail.getUser_id());
            }
        });

        reportuserTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContentdialougeForComment("reportUser", commentDetail.getComment_id(), commentDetail.getUser_id());
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

    private void showContentdialougeForComment(final String from, final String commentId, final String userId) {

        contentDialog = new Dialog(this);
        contentDialog.setContentView(R.layout.dialouge_msg_layout);
        contentDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id

        contentDialog.show();

        Button cancelbtn = contentDialog.findViewById(R.id.canclebtn);
        Button ratebtn = contentDialog.findViewById(R.id.ratebtn);

/*        ratebtn.setBackgroundColor(Color.parseColor(SharedPreference.getPref(CommentActivity.this, EVENT_COLOR_1)));
        cancelbtn.setBackgroundColor(Color.parseColor(SharedPreference.getPref(CommentActivity.this, EVENT_COLOR_1)));

        ratebtn.setTextColor(Color.parseColor(SharedPreference.getPref(CommentActivity.this, EVENT_COLOR_4)));
        cancelbtn.setTextColor(Color.parseColor(SharedPreference.getPref(CommentActivity.this, EVENT_COLOR_4)));

        final EditText etmsg = contentDialog.findViewById(R.id.etmsg);

        final TextView counttv = contentDialog.findViewById(R.id.counttv);*/
        TextView title = contentDialog.findViewById(R.id.title);
        final LinearLayout ll_main = contentDialog.findViewById(R.id.ll_main);
        final TextView counttv = contentDialog.findViewById(R.id.counttv);
        final EditText etmsg = contentDialog.findViewById(R.id.etmsg);

        ll_main.setBackgroundColor(Color.parseColor(SharedPreference.getPref(CommentActivity.this,EVENT_COLOR_2)));
        title.setTextColor(Color.parseColor(SharedPreference.getPref(CommentActivity.this, EVENT_COLOR_3)));
        counttv.setTextColor(Color.parseColor(SharedPreference.getPref(CommentActivity.this, EVENT_COLOR_3)));
        etmsg.setTextColor(Color.parseColor(SharedPreference.getPref(CommentActivity.this, EVENT_COLOR_3)));
        etmsg.setHintTextColor(Color.parseColor(SharedPreference.getPref(CommentActivity.this, EVENT_COLOR_3)));
        ratebtn.setBackgroundColor(Color.parseColor(SharedPreference.getPref(CommentActivity.this,EVENT_COLOR_3)));
        cancelbtn.setBackgroundColor(Color.parseColor(SharedPreference.getPref(CommentActivity.this,EVENT_COLOR_3)));

        ratebtn.setTextColor(Color.parseColor(SharedPreference.getPref(CommentActivity.this,EVENT_COLOR_2)));
        cancelbtn.setTextColor(Color.parseColor(SharedPreference.getPref(CommentActivity.this,EVENT_COLOR_2)));

        final TextView nametv = contentDialog.findViewById(R.id.nametv);

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
                contentDialog.dismiss();
            }
        });

        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etmsg.getText().toString().length() > 0) {

                    String content = StringEscapeUtils.escapeJava(etmsg.getText().toString());
                    dialog.cancel();
                    if (from.equalsIgnoreCase("reportUser")) {
                        commentViewModel.reportUser(api_token, event_id, userId, commentId, content);
                        commentViewModel.reportUserData().observe(CommentActivity.this, new Observer<LoginOrganizer>() {
                            @Override
                            public void onChanged(LoginOrganizer loginOrganizer) {
                                if (loginOrganizer.getHeader().get(0).getType().equalsIgnoreCase("success")) {
                                    contentDialog.dismiss();
                                    Utility.createShortSnackBar(ll_main, loginOrganizer.getHeader().get(0).getMsg());
                                    getComments();
                                } else {
                                    contentDialog.dismiss();
                                    Utility.createShortSnackBar(ll_main, loginOrganizer.getHeader().get(0).getMsg());
                                }
                            }
                        });
                    } else if (from.equalsIgnoreCase("reportComment")) {
                        commentViewModel.reportComment(api_token, event_id, commentId, content);
                        commentViewModel.reportCommentPostData().observe(CommentActivity.this, new Observer<LoginOrganizer>() {
                            @Override
                            public void onChanged(LoginOrganizer loginOrganizer) {
                                if (loginOrganizer.getHeader().get(0).getType().equalsIgnoreCase("success")) {
                                    contentDialog.dismiss();
                                    Utility.createShortSnackBar(ll_main, loginOrganizer.getHeader().get(0).getMsg());
                                    getComments();
                                } else {
                                    contentDialog.dismiss();
                                    Utility.createShortSnackBar(ll_main, loginOrganizer.getHeader().get(0).getMsg());
                                }
                            }
                        });
                    }
                } else {
                    Utility.createShortSnackBar(ll_main, "Please enter message");
                }
            }
        });
    }

    public void shareImage(final String data, String url, final Context context) {
        Picasso.with(context).load(url).into(new com.squareup.picasso.Target() {
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
            this.progressDialog = new ProgressDialog(CommentActivity.this);
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
                String folder = Environment.getExternalStorageDirectory().toString() + "/" + Constant.FOLDER_DIRECTORY + Constant.VIDEO_DIRECTORY + "/Downloads/";

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

            ContentValues content = new ContentValues(4);
            content.put(MediaStore.Video.VideoColumns.DATE_ADDED,
                    System.currentTimeMillis() / 1000);
            content.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
            content.put(MediaStore.Video.Media.DATA, strPath);

            ContentResolver resolver = getContentResolver();
            Uri uri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, content);

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("video/*");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Shared via Event app");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(sharingIntent, "Shared via Event app"));
        }
    }

    public void setDynamicColor() {
        tv_name.setTextColor(Color.parseColor(eventColor1));
        tv_status.setTextColor(Color.parseColor(eventColor3));
        String eventColor3Opacity40 = eventColor3.replace("#", "");
        tv_date_time.setTextColor(Color.parseColor("#66" + eventColor3Opacity40));
        tv_designation.setTextColor(Color.parseColor("#66" + eventColor3Opacity40));
        tv_no_of_likes.setTextColor(Color.parseColor("#66" + eventColor3Opacity40));
        tv_no_of_comments.setTextColor(Color.parseColor("#66" + eventColor3Opacity40));
        ll_root.setBackgroundColor(Color.parseColor(eventColor2));
        v_divider.setBackgroundColor(Color.parseColor("#66" + eventColor3Opacity40));

        int color = Color.parseColor(eventColor1);
        moreIV.setColorFilter(Color.parseColor(eventColor3), PorterDuff.Mode.SRC_ATOP);
        iv_send.setColorFilter(getResources().getColor(R.color.transperent));
        iv_likes.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        iv_comments.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        iv_share.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        moreIV.setAlpha(150);
        iv_likes.setAlpha(180);
        iv_comments.setAlpha(180);
        iv_share.setAlpha(180);
       // iv_send.setAlpha(180);
        fl_post_comment.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)));

        int color4 = Color.parseColor(SharedPreference.getPref(CommentActivity.this, EVENT_COLOR_4));
        iv_back.setColorFilter(color4, PorterDuff.Mode.SRC_ATOP);
        tv_header.setTextColor(Color.parseColor(SharedPreference.getPref(CommentActivity.this, EVENT_COLOR_4)));
    }

    private void setupMentionsList() {
        final RecyclerView mentionsList = findViewById(R.id.mentions_list);
        mentionsList.setLayoutManager(new LinearLayoutManager(this));
        usersAdapter = new UsersAdapter(this);
        mentionsList.setAdapter(usersAdapter);
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(et_comment.getWindowToken(), 0);
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
                    postNewsFeedViewModel.showMentionsList(CommentActivity.this, true);
                } else {
                    postNewsFeedViewModel.showMentionsList(CommentActivity.this, false);
                }
                if (postNewsFeedViewModel != null && postNewsFeedViewModel.getAttendeeList().hasObservers()) {
                    postNewsFeedViewModel.getAttendeeList().removeObservers(CommentActivity.this);
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
            EventAppDB eventAppDB = EventAppDB.getDatabase(CommentActivity.this);
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
        super.onBackPressed();
        JzvdStd.releaseAllVideos();
    }

    //------------------Open More dot features------------------
    public void openMoreDetailsForPost(final Newsfeed_detail feed) {
        ATTENDEE_STATUS = SharedPreference.getPref(this, IS_GOD);
        ATTENDEE_ID = SharedPreference.getPref(this, KEY_ATTENDEE_ID);
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.botomfeeddialouge);

        TextView reportTv = dialog.findViewById(R.id.reportTv);
        TextView hideTv = dialog.findViewById(R.id.hideTv);
        TextView deleteTv = dialog.findViewById(R.id.deleteTv);
        TextView reportuserTv = dialog.findViewById(R.id.reportuserTv);
        TextView cancelTv = dialog.findViewById(R.id.cancelTv);

        if (ATTENDEE_STATUS.equalsIgnoreCase("1")) {
            reportTv.setVisibility(View.VISIBLE);
            hideTv.setVisibility(View.VISIBLE);
            deleteTv.setVisibility(View.VISIBLE);
            reportuserTv.setVisibility(View.VISIBLE);
            cancelTv.setVisibility(View.VISIBLE);
        } else {
            if (ATTENDEE_ID.equalsIgnoreCase(feed.getAttendee_id())) {
                reportTv.setVisibility(View.GONE);
                hideTv.setVisibility(View.GONE);
                reportuserTv.setVisibility(View.GONE);
                deleteTv.setVisibility(View.VISIBLE);
                cancelTv.setVisibility(View.VISIBLE);
            } else {
                reportTv.setVisibility(View.VISIBLE);
                hideTv.setVisibility(View.VISIBLE);
                reportuserTv.setVisibility(View.VISIBLE);
                deleteTv.setVisibility(View.GONE);
                cancelTv.setVisibility(View.VISIBLE);
            }
        }

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        reportTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showratedialougeForPost(v.getContext(),api_token,"reportPost", feed.getNews_feed_id(),feed.getAttendee_id(),event_id);
            }
        });
        reportuserTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showratedialougeForPost(v.getContext(),api_token,"reportUser", feed.getNews_feed_id(),feed.getAttendee_id(),event_id);
            }
        });
        hideTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectionDetector.getInstance(CommentActivity.this).isConnectingToInternet()) {
                   newsRepository.PostHide(api_token,event_id, feed.getNews_feed_id());
                    newsRepository.getPostActivity().observe(CommentActivity.this, new Observer<LoginOrganizer>() {
                        @Override
                        public void onChanged(LoginOrganizer loginOrganizer) {
                            if (loginOrganizer != null) {

                                NewsFeedFragment.newsfeedAdapter.getNewsFeedList().remove(positionOfList);
                                NewsFeedFragment.newsfeedAdapter.notifyItemRemoved(positionOfList);

                                List<Header> heaserList = loginOrganizer.getHeader();
                                Utility.createShortSnackBar(ll_main,heaserList.get(0).getMsg());
                                dialog.cancel();
                                //startActivity(new Intent(CommentActivity.this, MainActivity.class));
                                finish();
                            }
                        }
                    });
                } else {
                    dialog.cancel();
                    Utility.createShortSnackBar(ll_main,"No Internet Connection");
                }
            }
        });
        deleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectionDetector.getInstance(CommentActivity.this).isConnectingToInternet()) {
                    newsRepository.DeletePost(api_token, event_id, feed.getNews_feed_id());
                    newsRepository.getPostActivity().observe(CommentActivity.this, new Observer<LoginOrganizer>() {
                        @Override
                        public void onChanged(LoginOrganizer loginOrganizer) {
                        if (loginOrganizer != null) {
                            List<Header> heaserList = loginOrganizer.getHeader();
                            Utility.createShortSnackBar(ll_main, heaserList.get(0).getMsg());
                            dialog.cancel();
                            //startActivity(new Intent(CommentActivity.this, MainActivity.class));
                            finish();
                        }
                        }
                    });
                } else {
                    dialog.cancel();
                    Utility.createShortSnackBar(ll_main, "No Internet Connection");
                }
            }
        });
        dialog.show();
    }

    private void showratedialougeForPost(Context context,final String api_token, final String from,
                                  final String newsfeedIdId, final String attnId, final String eventId) {

        myDialog = new Dialog(CommentActivity.this);
        myDialog.setContentView(R.layout.dialouge_msg_layout);
        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id
        myDialog.show();

        Button cancelbtn = myDialog.findViewById(R.id.canclebtn);
        Button ratebtn = myDialog.findViewById(R.id.ratebtn);

        TextView title = myDialog.findViewById(R.id.title);
        final LinearLayout ll_main = myDialog.findViewById(R.id.ll_main);
        final TextView counttv = myDialog.findViewById(R.id.counttv);
        final EditText etmsg = myDialog.findViewById(R.id.etmsg);

        ll_main.setBackgroundColor(Color.parseColor(SharedPreference.getPref(CommentActivity.this,EVENT_COLOR_2)));
        title.setTextColor(Color.parseColor(SharedPreference.getPref(CommentActivity.this, EVENT_COLOR_3)));
        counttv.setTextColor(Color.parseColor(SharedPreference.getPref(CommentActivity.this, EVENT_COLOR_3)));
        etmsg.setTextColor(Color.parseColor(SharedPreference.getPref(CommentActivity.this, EVENT_COLOR_3)));
        etmsg.setHintTextColor(Color.parseColor(SharedPreference.getPref(CommentActivity.this, EVENT_COLOR_3)));
        ratebtn.setBackgroundColor(Color.parseColor(SharedPreference.getPref(CommentActivity.this,EVENT_COLOR_3)));
        cancelbtn.setBackgroundColor(Color.parseColor(SharedPreference.getPref(CommentActivity.this,EVENT_COLOR_3)));

        ratebtn.setTextColor(Color.parseColor(SharedPreference.getPref(CommentActivity.this,EVENT_COLOR_2)));
        cancelbtn.setTextColor(Color.parseColor(SharedPreference.getPref(CommentActivity.this,EVENT_COLOR_2)));
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
                Utility.hideKeyboard(v);
                myDialog.dismiss();
            }
        });

        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (etmsg.getText().toString().length() > 0) {
                    String msg = StringEscapeUtils.escapeJava(etmsg.getText().toString());
                    dialog.cancel();
                    Utility.hideKeyboard(v);
                    if (from.equalsIgnoreCase("reportPost")) {
                        if (ConnectionDetector.getInstance(CommentActivity.this).isConnectingToInternet()) {
                            newsRepository.ReportPost(api_token,eventId, newsfeedIdId, msg);
                            newsRepository.getPostActivity().observe((LifecycleOwner) CommentActivity.this, new Observer<LoginOrganizer>() {
                                @Override
                                public void onChanged(LoginOrganizer loginOrganizer) {
                                    if (loginOrganizer != null) {
                                        List<Header> heaserList = loginOrganizer.getHeader();
                                        Utility.createShortSnackBar(NewsFeedFragment.cl_main, heaserList.get(0).getMsg());
                                        myDialog.cancel();
                                        Utility.hideKeyboard(v);
                                        //startActivity(new Intent(CommentActivity.this,MainActivity.class));
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Utility.hideKeyboard(v);
                            myDialog.cancel();
                            Utility.createShortSnackBar(NewsFeedFragment.cl_main, "No Internet Connection");
                        }
                    }
                    else if (from.equalsIgnoreCase("reportUser")) {
                        if (ConnectionDetector.getInstance(CommentActivity.this).isConnectingToInternet()) {
                            Utility.hideKeyboard(v);
                            newsRepository.ReportUser(api_token,eventId, attnId,newsfeedIdId, msg);
                            newsRepository.getPostActivity().observe((LifecycleOwner) CommentActivity.this, new Observer<LoginOrganizer>() {
                                @Override
                                public void onChanged(LoginOrganizer loginOrganizer) {
                                    if (loginOrganizer != null) {
                                        List<Header> heaserList = loginOrganizer.getHeader();
                                        Utility.createShortSnackBar(NewsFeedFragment.cl_main, heaserList.get(0).getMsg());
                                        myDialog.cancel();
                                        Utility.hideKeyboard(v);
                                        //startActivity(new Intent(CommentActivity.this,MainActivity.class));
                                        finish();
                                    }
                                }
                            });
                        } else {
                            myDialog.cancel();
                            Utility.hideKeyboard(v);
                            Utility.createShortSnackBar(NewsFeedFragment.cl_main, "No Internet Connection");
                        }
                    }
                }
                else {
                    Utility.createShortSnackBar(NewsFeedFragment.cl_main, "Enter Something");
                }
            }
        });
    }
}