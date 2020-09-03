package com.procialize.eventapp.ui.newsFeedComment.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.ui.newsFeedComment.adapter.GifEmojiAdapter;
import com.procialize.eventapp.ui.newsFeedComment.model.GifResponse;
import com.procialize.eventapp.ui.newsFeedComment.model.GifResult;
import com.procialize.eventapp.ui.newsFeedComment.viewModel.CommentViewModel;
import com.procialize.eventapp.ui.newsFeedDetails.adapter.NewsFeedDetailsPagerAdapter;
import com.procialize.eventapp.ui.newsFeedDetails.view.NewsFeedDetailsActivity;
import com.procialize.eventapp.ui.newsFeedPost.viewModel.PostNewsFeedViewModel;
import com.procialize.eventapp.ui.newsfeed.adapter.SwipeMultimediaAdapter;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.eventapp.Constants.Constant.MY_PREFS_NAME;
import static com.procialize.eventapp.Constants.Constant.NEWS_FEED_MEDIA_PATH;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener, GifEmojiAdapter.GifEmojiAdapterListner {

    private static final String API_KEY = "TVG20YJW1MXR";
    ImageView iv_gif, iv_back_gif;
    EditText et_comment, et_search_gif;
    FrameLayout fl_gif_container, fl_post_comment;
    LinearLayout ll_comment_container,
            ll_media_dots, ll_main;
    CommentViewModel commentViewModel;
    String anon_id;
    RecyclerView rv_gif;
    ProgressBar pb_emoji;
    ViewPager vp_media;
    private String position;
    private Newsfeed_detail newsfeed_detail;
    public static int swipableAdapterPosition = 0;
    private TextView tv_status, tv_name, tv_designation, tv_date_time;
    String event_id = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        commentViewModel = ViewModelProviders.of(this).get(CommentViewModel.class);

        Intent intent = getIntent();
        try {
            newsfeed_detail = (Newsfeed_detail) getIntent().getSerializableExtra("Newsfeed_detail");
            position = intent.getStringExtra("position");
        } catch (Exception e) {
            e.printStackTrace();
        }

        iv_gif = findViewById(R.id.iv_gif);
        rv_gif = findViewById(R.id.rv_gif);
        pb_emoji = findViewById(R.id.pb_emoji);
        iv_gif.setOnClickListener(this);
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

        if (newsfeed_detail.getNews_feed_media().size() > 0) {
            vp_media.setVisibility(View.VISIBLE);
            ll_media_dots.setVisibility(View.VISIBLE);
            setupPagerAdapter();
        } else {
            vp_media.setVisibility(View.GONE);
            ll_media_dots.setVisibility(View.GONE);
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
                String comment = et_comment.getText().toString();
                commentViewModel.validation(comment);
                commentViewModel.getIsValid().observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (aBoolean) {
                            commentViewModel.postComment(event_id, newsfeed_detail.getNews_feed_id(), comment, "1");
                            commentViewModel.postCommentResponse().observe(CommentActivity.this, new Observer<LoginOrganizer>() {
                                @Override
                                public void onChanged(LoginOrganizer loginOrganizer) {
                                    Snackbar.make(ll_main, "Success", Snackbar.LENGTH_SHORT).show();
                                    et_comment.setText("");
                                }
                            });
                        } else {
                            Snackbar.make(ll_main, "Please Enter Comment", Snackbar.LENGTH_SHORT).show();
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