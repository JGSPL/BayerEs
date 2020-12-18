package com.procialize.bayer2020.ui.newsFeedDetails.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

import static android.view.View.GONE;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.NEWS_FEED_MEDIA_PATH;

public class NewsfeedDetailsInLandscape extends AppCompatActivity {
    JzvdStd videoplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed_details_in_landscape);

        String mediaPath = SharedPreference.getPref(NewsfeedDetailsInLandscape.this, NEWS_FEED_MEDIA_PATH);
        videoplayer = findViewById(R.id.videoplayer);
        String urlPhotoClick = getIntent().getStringExtra("urlPhotoClick");
        String thumbImage = getIntent().getStringExtra("thumbImage");
        videoplayer.setUp(mediaPath + urlPhotoClick, ""
                , JzvdStd.SCREEN_FULLSCREEN);
        JzvdStd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_ADAPTER);

        Glide.with(videoplayer)
                .load(thumbImage.trim())
                //.placeholder(R.mipmap.placeholder)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)).centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(videoplayer.thumbImageView);

        videoplayer.thumbImageView.setVisibility(View.VISIBLE);
        videoplayer.fullscreenButton.setImageResource(cn.jzvd.R.drawable.jz_shrink);
        videoplayer.fullscreenButton.setVisibility(View.INVISIBLE);
        videoplayer.backButton.setVisibility(View.INVISIBLE);
        videoplayer.tinyBackImageView.setVisibility(View.INVISIBLE);
        videoplayer.batteryTimeLayout.setVisibility(View.INVISIBLE);
        if (videoplayer.jzDataSource.urlsMap.size() == 1) {
            videoplayer.clarity.setVisibility(GONE);
        } else {
            videoplayer.clarity.setText(videoplayer.jzDataSource.getCurrentKey().toString());
            videoplayer.clarity.setVisibility(View.VISIBLE);
        }
        videoplayer.changeStartButtonSize((int) getResources().getDimension(cn.jzvd.R.dimen.jz_start_button_w_h_fullscreen));
        videoplayer.setSystemTimeAndBattery();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        JzvdStd.releaseAllVideos();
        //startActivity(new Intent(NewsfeedDetailsInLandscape.this, MainActivity.class));
        finish();
    }
}