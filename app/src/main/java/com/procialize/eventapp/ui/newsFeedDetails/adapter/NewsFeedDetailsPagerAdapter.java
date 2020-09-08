package com.procialize.eventapp.ui.newsFeedDetails.adapter;

import android.content.Context;
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
import com.github.chrisbanes.photoview.PhotoView;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.SharedPreferencesConstant;

import java.util.LinkedHashMap;
import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

import static android.content.Context.MODE_PRIVATE;

public class NewsFeedDetailsPagerAdapter extends PagerAdapter {

    public PhotoView myImage;
    public JzvdStd videoview;
    public TextView name;
    private List<String> images;
    private List<String> imagesThumb;
    private LayoutInflater inflater;
    private Context context;
    String newsFeedPath="";

    public NewsFeedDetailsPagerAdapter(Context context, List<String> images, List<String> imagesThumb) {
        this.context = context;
        this.images = images;
        this.imagesThumb = imagesThumb;
        inflater = LayoutInflater.from(context);
        newsFeedPath = SharedPreference.getPref(context, SharedPreferencesConstant.NEWS_FEED_MEDIA_PATH);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(( View ) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.pager_adapter_newsfeed_details, view, false);

        final String firstLevelFilter = newsFeedPath+images.get(position).trim();
        final String thumbImage = newsFeedPath+imagesThumb.get(position).trim();


        myImage = myImageLayout.findViewById(R.id.image);
        videoview = myImageLayout.findViewById(R.id.videoview);
        final ProgressBar progressBar = myImageLayout.findViewById(R.id.progressbar);

        if ((firstLevelFilter.contains(".png") || firstLevelFilter.contains(".jpg") || firstLevelFilter.contains(".jpeg") || firstLevelFilter.contains("gif"))) {
            myImage.setVisibility(View.VISIBLE);
            videoview.setVisibility(View.GONE);
            JzvdStd.goOnPlayOnPause();
            if (firstLevelFilter.contains("gif")) {
                progressBar.setVisibility(View.GONE);
                Glide.with(videoview).load(firstLevelFilter.trim()).into(myImage);
            } else {
                Glide.with(context).load(firstLevelFilter.trim())
                        .placeholder(R.mipmap.placeholder)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
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
            progressBar.setVisibility(View.GONE);
            videoview.setUp(firstLevelFilter.trim(),""
                    , JzvdStd.SCREEN_NORMAL);

            JzvdStd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_ADAPTER);

            Glide.with(videoview).load(thumbImage.trim()).into(videoview.thumbImageView);
        }


        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

}
