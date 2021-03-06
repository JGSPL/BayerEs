package com.procialize.bayer2020.ui.qapost.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.ui.qapost.model.SelectedImages;

import java.util.List;

import cn.jzvd.JzvdStd;

public class ViewPagerMultimediaAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    List<SelectedImages> imagePathList;

    public ViewPagerMultimediaAdapter(Context context, List<SelectedImages> imagePathList) {
        this.context = context;
        this.imagePathList = imagePathList;

    }

    @Override
    public int getCount() {
        return imagePathList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        JzvdStd videoview;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.post_news_feed_pager_item, null);


        ImageView imageView = (ImageView) view.findViewById(R.id.iv_news_feed);
        videoview = view.findViewById(R.id.videoview);

        final ProgressBar progressBar = view.findViewById(R.id.pb_news_feed);
        String albumFile = imagePathList.get(position).getmPath();


        if (albumFile.contains("png") || albumFile.contains("jpg") || albumFile.contains("jpeg") || albumFile.contains("gif")
                || albumFile.contains("JPG")  || albumFile.contains("JPEG") || albumFile.contains("PNG") || albumFile.contains("GIF")) {
            imageView.setVisibility(View.VISIBLE);
            videoview.setVisibility(View.GONE);

            progressBar.setVisibility(View.GONE);
            JzvdStd.goOnPlayOnPause();
            //imageView.setImageURI(Uri.parse(images.get(position)));
            //Glide.with(videoview).load(imagePathList.get(position).getmPath()).into(imageView);
            Glide.with(imageView).load( imagePathList.get(position).getmPath().trim())
                    .apply(RequestOptions.skipMemoryCacheOf(false))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.e("loadfailed","loadfailed");
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            Log.e("ready","ready");
                            return false;
                        }
                    }).into(imageView);

        } else /*if (albumFile.contains("mp4"))*/ {
            imageView.setVisibility(View.GONE);
            videoview.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            String videoPath = imagePathList.get(position).getmPath();
            videoview.setUp(videoPath, ""
                    , JzvdStd.SCREEN_NORMAL);
            Glide.with(videoview).load(videoPath).into(videoview.thumbImageView);
        }

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        JzvdStd.releaseAllVideos();
        JzvdStd.releaseAllVideos();
        vp.removeView(view);
    }

}
