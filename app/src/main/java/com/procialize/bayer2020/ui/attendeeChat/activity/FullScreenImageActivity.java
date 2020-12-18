package com.procialize.bayer2020.ui.attendeeChat.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.CommonFirebase;
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.costumTools.TouchImageView;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_4;


public class FullScreenImageActivity extends AppCompatActivity {

    private TouchImageView mImageView;
    private ImageView ivUser;
    private TextView tvUser;
    private ProgressDialog progressDialog;
    String type;
    JzvdStd videoplayer;
    RelativeLayout rlMain;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        bindViews();
        CommonFirebase.crashlytics("FullScreenImage", "");
        CommonFirebase.firbaseAnalytics(this, "FullScreenImage", "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setValues();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.gc();
        finish();
        JzvdStd.releaseAllVideos();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    private void bindViews() {
        progressDialog = new ProgressDialog(this);
        mImageView = (TouchImageView) findViewById(R.id.imageView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ivUser = (ImageView) toolbar.findViewById(R.id.avatar);
        tvUser = (TextView) toolbar.findViewById(R.id.title);
        videoplayer = findViewById(R.id.videoplayer);
        rlMain = findViewById(R.id.rlMain);
        //iv_back.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.getNavigationIcon().setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)), PorterDuff.Mode.SRC_ATOP);

        CommonFunction.showBackgroundImage(this, rlMain);

    }

    private void setValues() {
        String nameUser, urlPhotoUser, urlPhotoClick;
        nameUser = getIntent().getStringExtra("nameUser");
        urlPhotoUser = getIntent().getStringExtra("urlPhotoUser");
        urlPhotoClick = getIntent().getStringExtra("urlPhotoClick");
        type = getIntent().getStringExtra("type");

        Log.i("TAG", "imagem recebida " + urlPhotoClick);
       // tvUser.setText(nameUser); // Name
       // Glide.with(this).load(urlPhotoUser).centerCrop().override(40, 40).into(ivUser);
        if (type.equalsIgnoreCase("image")) {
            videoplayer.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);

            mImageView.setVisibility(View.VISIBLE);
            Glide.with(this).load((urlPhotoClick))
                    .placeholder(R.drawable.gallery_placeholder)
                    .apply(RequestOptions.skipMemoryCacheOf(false))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).fitCenter()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(mImageView);
        }else{
            videoplayer.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.GONE);
            videoplayer.setUp(urlPhotoClick, ""
                    , JzvdStd.SCREEN_NORMAL);
            JzvdStd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_ADAPTER);
            toolbar.setVisibility(View.GONE);



            Glide.with(this)
                    .asBitmap()
                    .load(urlPhotoClick)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(videoplayer.thumbImageView);



        }
    }
    }






