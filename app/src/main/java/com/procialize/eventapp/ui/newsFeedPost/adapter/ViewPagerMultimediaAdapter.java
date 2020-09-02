package com.procialize.eventapp.ui.newsFeedPost.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.procialize.eventapp.R;
import com.procialize.eventapp.ui.newsFeedPost.model.SelectedImages;

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


        if (albumFile.contains("png") || albumFile.contains("jpg") || albumFile.contains("jpeg") || albumFile.contains("gif")) {
            imageView.setVisibility(View.VISIBLE);
            videoview.setVisibility(View.GONE);

            progressBar.setVisibility(View.GONE);
            JzvdStd.goOnPlayOnPause();
            //imageView.setImageURI(Uri.parse(images.get(position)));
            Glide.with(videoview).load(imagePathList.get(position).getmPath()).into(imageView);


        } else if (albumFile.contains("mp4")) {
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
