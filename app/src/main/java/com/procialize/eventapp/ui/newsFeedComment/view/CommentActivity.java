package com.procialize.eventapp.ui.newsFeedComment.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.ui.newsFeedComment.adapter.CommentAdapter;
import com.procialize.eventapp.ui.newsFeedComment.adapter.GifEmojiAdapter;
import com.procialize.eventapp.ui.newsFeedComment.model.Comment;
import com.procialize.eventapp.ui.newsFeedComment.model.CommentDetail;
import com.procialize.eventapp.ui.newsFeedComment.model.GifResponse;
import com.procialize.eventapp.ui.newsFeedComment.model.GifResult;
import com.procialize.eventapp.ui.newsFeedComment.model.LikePost;
import com.procialize.eventapp.ui.newsFeedComment.viewModel.CommentViewModel;
import com.procialize.eventapp.ui.newsFeedDetails.adapter.NewsFeedDetailsPagerAdapter;
import com.procialize.eventapp.ui.newsFeedDetails.view.NewsFeedDetailsActivity;
import com.procialize.eventapp.ui.newsFeedLike.model.Like;
import com.procialize.eventapp.ui.newsFeedPost.viewModel.PostNewsFeedViewModel;
import com.procialize.eventapp.ui.newsfeed.adapter.SwipeMultimediaAdapter;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;
import com.yanzhenjie.album.AlbumFile;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.eventapp.Constants.Constant.MY_PREFS_NAME;
import static com.procialize.eventapp.Constants.Constant.NEWS_FEED_MEDIA_PATH;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener, GifEmojiAdapter.GifEmojiAdapterListner, CommentAdapter.CommentAdapterListner {

    private static final String API_KEY = "TVG20YJW1MXR";
    ImageView iv_gif, iv_back_gif, iv_likes, iv_comments, iv_share,iv_profile;
    EditText et_comment, et_search_gif;
    FrameLayout fl_gif_container, fl_post_comment;
    LinearLayout ll_comment_container,
            ll_media_dots, ll_main;
    CommentViewModel commentViewModel;
    String anon_id;
    RecyclerView rv_gif, rv_comments;
    ProgressBar pb_emoji;
    ViewPager vp_media;
    private String position;
    private Newsfeed_detail newsfeed_detail;
    public static int swipableAdapterPosition = 0;
    private TextView tv_status, tv_name, tv_designation, tv_date_time, tv_no_of_comments, tv_no_of_likes;
    String event_id = "1";
    ConnectionDetector connectionDetector;
    String commentText = "";
    BottomSheetDialog dialog;
    Dialog contentDialog;
    List<CommentDetail> commentList = new ArrayList<>();
    String noOfLikes = "0";
    String likeStatus="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        commentViewModel = ViewModelProviders.of(this).get(CommentViewModel.class);
        connectionDetector = ConnectionDetector.getInstance(this);

        Intent intent = getIntent();
        try {
            newsfeed_detail = (Newsfeed_detail) getIntent().getSerializableExtra("Newsfeed_detail");
            position = intent.getStringExtra("position");
        } catch (Exception e) {
            e.printStackTrace();
        }

        iv_gif = findViewById(R.id.iv_gif);
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
        ll_comment_container = findViewById(R.id.ll_comment_container);
        ll_media_dots = findViewById(R.id.ll_media_dots);
        ll_main = findViewById(R.id.ll_main);
        vp_media = findViewById(R.id.vp_media);
        tv_status = findViewById(R.id.tv_status);
        tv_name = findViewById(R.id.tv_name);
        tv_designation = findViewById(R.id.tv_designation);
        tv_date_time = findViewById(R.id.tv_date_time);
        tv_no_of_comments = findViewById(R.id.tv_no_of_comments);
        tv_no_of_likes = findViewById(R.id.tv_no_of_likes);

        String postStatus = newsfeed_detail.getPost_status().trim();
        if (!postStatus.trim().isEmpty()) {
            tv_status.setText(postStatus);
            tv_status.setVisibility(View.VISIBLE);
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


        if(newsfeed_detail.getProfile_pic().trim()!=null) {
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
    }

    public void getComments() {
        if (connectionDetector.isConnectingToInternet()) {
            commentViewModel.getComment(event_id, newsfeed_detail.getNews_feed_id(), "20", "1");
            commentViewModel.getCommentList().observe(this, new Observer<Comment>() {
                @Override
                public void onChanged(Comment comment) {
                    if (comment != null) {
                        commentList = comment.getCommentDetails();
                        setupCommentAdapter(commentList);
                        showCommentCount(commentList);
                    }
                }
            });
        } else {
            Snackbar.make(ll_main, "No Internet Connection", Snackbar.LENGTH_SHORT).show();
        }
    }

    public void setupPagerAdapter() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String mediaPath = prefs.getString(NEWS_FEED_MEDIA_PATH, "");

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
        vp_media.setCurrentItem(Integer.parseInt(position));

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
            case R.id.iv_gif:
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
                break;
            case R.id.fl_post_comment:
                commentText = et_comment.getText().toString();
                commentViewModel.validation(commentText);
                commentViewModel.getIsValid().observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (aBoolean) {
                            commentViewModel.postComment(event_id, newsfeed_detail.getNews_feed_id(), commentText, "1");
                            commentViewModel.postCommentResponse().observe(CommentActivity.this, new Observer<LoginOrganizer>() {
                                @Override
                                public void onChanged(LoginOrganizer loginOrganizer) {
                                    Snackbar.make(ll_main, "Success", Snackbar.LENGTH_SHORT).show();
                                    et_comment.setText("");
                                    commentText = et_comment.getText().toString();
                                    getComments();
                                }
                            });
                        } else {
                            Snackbar.make(ll_main, "Please Enter Comment", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.iv_likes:
                noOfLikes = "0";
                commentViewModel.likePost(event_id, newsfeed_detail.getNews_feed_id());
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
                                Snackbar.make(ll_main, likePost.getHeader().get(0).getMsg(), Snackbar.LENGTH_SHORT).show();
                            }
                            if (commentViewModel != null && commentViewModel.likePostData().hasObservers()) {
                                commentViewModel.likePostData().removeObservers(CommentActivity.this);
                            }
                        } else {
                            Snackbar.make(ll_main, "Failure..", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
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

        commentViewModel.postComment(event_id, newsfeed_detail.getNews_feed_id(), result.getMedia().get(0).getGif().getUrl(), "2");
        commentViewModel.postCommentResponse().observe(CommentActivity.this, new Observer<LoginOrganizer>() {
            @Override
            public void onChanged(LoginOrganizer loginOrganizer) {
                Snackbar.make(ll_main, "Success", Snackbar.LENGTH_SHORT).show();
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
            openMoreOptions(this, newsfeed_detail, comment, position, ll_main);
        } else {
            Snackbar.make(ll_main, "No Internet Connection", Snackbar.LENGTH_SHORT).show();
        }
    }

    public void openMoreOptions(Activity activity, Newsfeed_detail newsfeed_detail, CommentDetail commentDetail, int position, LinearLayout ll_main) {
        dialog = new BottomSheetDialog(activity);
        dialog.setContentView(R.layout.botomcommentdialouge);
        TextView reportTv = dialog.findViewById(R.id.reportTv);
        TextView hideTv = dialog.findViewById(R.id.hideTv);
        TextView deleteTv = dialog.findViewById(R.id.deleteTv);
        TextView reportuserTv = dialog.findViewById(R.id.reportuserTv);
        TextView cancelTv = dialog.findViewById(R.id.cancelTv);
        /*if (user.get(SessionManager.ATTENDEE_STATUS).equalsIgnoreCase("1")) {
            if (user_id.equalsIgnoreCase(comment.getAttendeeId())) {
                deleteTv.setVisibility(View.VISIBLE);
                reportuserTv.setVisibility(View.GONE);
                hideTv.setVisibility(View.GONE);
                reportTv.setVisibility(View.GONE);
            } else {
                deleteTv.setVisibility(View.VISIBLE);
                reportuserTv.setVisibility(View.GONE);
                hideTv.setVisibility(View.GONE);
                reportTv.setVisibility(View.GONE);
            }
        } else if (user_id.equalsIgnoreCase(comment.getAttendeeId())) {
            deleteTv.setVisibility(View.VISIBLE);
            reportuserTv.setVisibility(View.GONE);
            hideTv.setVisibility(View.GONE);
            reportTv.setVisibility(View.GONE);
        } else {
            deleteTv.setVisibility(View.GONE);
            reportuserTv.setVisibility(View.VISIBLE);
            hideTv.setVisibility(View.VISIBLE);
            reportTv.setVisibility(View.VISIBLE);
        }
*/

        deleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentViewModel.deleteComment(event_id, newsfeed_detail.getNews_feed_id(), commentDetail.getComment_id(), position);
                commentViewModel.commentDelete().observe(CommentActivity.this, new Observer<LoginOrganizer>() {
                    @Override
                    public void onChanged(LoginOrganizer loginOrganizer) {
                        if (loginOrganizer.getHeader().get(0).getType().equalsIgnoreCase("success")) {
                            commentList.clear();
                            dialog.dismiss();
                            Snackbar.make(ll_main, loginOrganizer.getHeader().get(0).getMsg(), Snackbar.LENGTH_SHORT).show();
                            getComments();
                        } else {
                            dialog.dismiss();
                            Snackbar.make(ll_main, loginOrganizer.getHeader().get(0).getMsg(), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        hideTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentViewModel.hideComment(event_id, commentDetail.getComment_id());
                commentViewModel.commentHide().observe(CommentActivity.this, new Observer<LoginOrganizer>() {
                    @Override
                    public void onChanged(LoginOrganizer loginOrganizer) {
                        if (loginOrganizer.getHeader().get(0).getType().equalsIgnoreCase("success")) {
                            dialog.dismiss();
                            Snackbar.make(ll_main, loginOrganizer.getHeader().get(0).getMsg(), Snackbar.LENGTH_SHORT).show();
                            getComments();
                        } else {
                            dialog.dismiss();
                            Snackbar.make(ll_main, loginOrganizer.getHeader().get(0).getMsg(), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        reportTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContentdialouge("reportComment", commentDetail.getComment_id());
            }
        });

        reportuserTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContentdialouge("reportUser", commentDetail.getComment_id());
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

    private void showContentdialouge(final String from, final String commentId) {

        contentDialog = new Dialog(this);
        contentDialog.setContentView(R.layout.dialouge_msg_layout);
        contentDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id

        contentDialog.show();


        Button cancelbtn = contentDialog.findViewById(R.id.canclebtn);
        Button ratebtn = contentDialog.findViewById(R.id.ratebtn);

        final EditText etmsg = contentDialog.findViewById(R.id.etmsg);

        final TextView counttv = contentDialog.findViewById(R.id.counttv);
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
                        commentViewModel.reportUser("1", newsfeed_detail.getAttendee_id(), newsfeed_detail.getNews_feed_id(), content);
                        commentViewModel.reportUserData().observe(CommentActivity.this, new Observer<LoginOrganizer>() {
                            @Override
                            public void onChanged(LoginOrganizer loginOrganizer) {
                                if (loginOrganizer.getHeader().get(0).getType().equalsIgnoreCase("success")) {
                                    contentDialog.dismiss();
                                    Snackbar.make(ll_main, loginOrganizer.getHeader().get(0).getMsg(), Snackbar.LENGTH_SHORT).show();
                                    getComments();
                                } else {
                                    contentDialog.dismiss();
                                    Snackbar.make(ll_main, loginOrganizer.getHeader().get(0).getMsg(), Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else if (from.equalsIgnoreCase("reportComment")) {
                        commentViewModel.reportComment("1", commentId, content);
                        commentViewModel.reportCommentPostData().observe(CommentActivity.this, new Observer<LoginOrganizer>() {
                            @Override
                            public void onChanged(LoginOrganizer loginOrganizer) {
                                if (loginOrganizer.getHeader().get(0).getType().equalsIgnoreCase("success")) {
                                    contentDialog.dismiss();
                                    Snackbar.make(ll_main, loginOrganizer.getHeader().get(0).getMsg(), Snackbar.LENGTH_SHORT).show();
                                    getComments();
                                } else {
                                    contentDialog.dismiss();
                                    Snackbar.make(ll_main, loginOrganizer.getHeader().get(0).getMsg(), Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    Snackbar.make(ll_main, "Please enter message", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }


}