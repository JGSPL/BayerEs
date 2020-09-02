package com.procialize.eventapp.ui.newsfeed.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.eventapp.R;
import com.procialize.eventapp.ui.newsfeed.model.News_feed_media;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

import static android.content.Context.MODE_PRIVATE;


public class SwipeMultimediaAdapter extends PagerAdapter {

    public ImageView myImage;
    // public MyJZVideoPlayerStandard videoview;
    public JzvdStd videoview;
    String MY_PREFS_NAME = "ProcializeInfo";
    ImageView  thumbimg;
    private List<String> images;
    private List<String> thumbImages;
    private LayoutInflater inflater;
    List<News_feed_media> news_feed_media = new ArrayList<>();
    private Context context;

    public SwipeMultimediaAdapter(Context context, List<String> images, List<String> thumbImages, List<News_feed_media> news_feed_media) {
        this.context = context;
        this.images = images;
        this.thumbImages = thumbImages;
        this.news_feed_media = news_feed_media;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View myImageLayout = inflater.inflate(R.layout.slide_multiple, view, false);
        final String firstLevelFilter = images.get(position);
        final String thumbImage = thumbImages.get(position);

        myImage = myImageLayout.findViewById(R.id.image);
        videoview = myImageLayout.findViewById(R.id.videoview);

        final ProgressBar progressBar = myImageLayout.findViewById(R.id.progressbar);

        if ((firstLevelFilter.contains(".png") || firstLevelFilter.contains(".jpg") || firstLevelFilter.contains(".jpeg") || firstLevelFilter.contains(".gif"))) {
            myImage.setVisibility(View.VISIBLE);
            videoview.setVisibility(View.GONE);
            JzvdStd.goOnPlayOnPause();

            if (firstLevelFilter.contains("gif")) {
                progressBar.setVisibility(View.GONE);
                Glide.with(videoview).load(firstLevelFilter).into(myImage);
            } else {
                Glide.with(context)
                        .load(firstLevelFilter.trim())
                        //.placeholder(R.mipmap.placeholder)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).centerCrop()
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(myImage);
            }
        } else if (firstLevelFilter.contains(".mp4")) {
            myImage.setVisibility(View.GONE);
            videoview.setVisibility(View.VISIBLE);
            // progressBar.setVisibility(View.GONE);
            videoview.setUp(firstLevelFilter.trim(), ""
                    , JzvdStd.SCREEN_NORMAL);
            JzvdStd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);

            Glide.with(videoview)
                    .load(thumbImage.trim())
                    //.placeholder(R.mipmap.placeholder)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)).centerCrop()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                             progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                              progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(videoview.thumbImageView);
        }

        view.addView(myImageLayout, 0);

        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}