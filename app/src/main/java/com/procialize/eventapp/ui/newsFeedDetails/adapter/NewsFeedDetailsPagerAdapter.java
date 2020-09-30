package com.procialize.eventapp.ui.newsFeedDetails.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.github.chrisbanes.photoview.PhotoView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.procialize.eventapp.App;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.SharedPreferencesConstant;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import static android.content.Context.MODE_PRIVATE;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.procialize.eventapp.Utility.Utility;

public class NewsFeedDetailsPagerAdapter extends PagerAdapter implements CacheListener {

    public PhotoView myImage;
    public JzvdStd videoview;
    public TextView name;
    private List<String> images;
    private List<String> imagesThumb;
    private LayoutInflater inflater;
    private Context context;
    String newsFeedPath="";
    private DisplayImageOptions options;

    public NewsFeedDetailsPagerAdapter(Context context, List<String> images, List<String> imagesThumb) {
        this.context = context;
        this.images = images;
        this.imagesThumb = imagesThumb;
        inflater = LayoutInflater.from(context);
        newsFeedPath = SharedPreference.getPref(context, SharedPreferencesConstant.NEWS_FEED_MEDIA_PATH);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.placeholder)
                .showImageForEmptyUri(R.mipmap.placeholder)
                .showImageOnFail(R.mipmap.placeholder)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(10))
                .build();
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
                ImageLoader.getInstance().displayImage(firstLevelFilter.trim(), myImage, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        String message = null;
                        switch (failReason.getType()) {
                            case IO_ERROR:
                                message = "file not downloded ";
                                break;
                            case DECODING_ERROR:
                                message = "file not downloded ";
                                break;
                            case NETWORK_DENIED:
                                message = "file not downloded ";
                                break;
                            case OUT_OF_MEMORY:
                                message = "file not downloded ";
                                break;
                            case UNKNOWN:
                                message = "file not downloded ";
                                break;
                        }
//                        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                        Utility.createShortSnackBar(view, message);

                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
//                Glide.with(context).load(firstLevelFilter.trim())
//                        .placeholder(R.mipmap.placeholder)
//                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        progressBar.setVisibility(View.GONE);
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        progressBar.setVisibility(View.GONE);
//                        return false;
//                    }
//                }).into(myImage);
            }
        } else if (firstLevelFilter.contains(".mp4")) {
            myImage.setVisibility(View.GONE);
            videoview.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
//            videoview.setUp(firstLevelFilter.trim(),""
//                    , JzvdStd.SCREEN_NORMAL);
//
//            JzvdStd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_ADAPTER);
//
//            Glide.with(videoview).load(thumbImage.trim()).into(videoview.thumbImageView);

            checkCachedState(firstLevelFilter.trim());
            startVideo(firstLevelFilter.trim(), thumbImage);
        }


        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    private void checkCachedState(String url) {
        HttpProxyCacheServer proxy = App.getProxy(context);
        boolean fullyCached = proxy.isCached(url);
//        setCachedState(fullyCached);
//        if (fullyCached) {
//            progressBar.setSecondaryProgress(100);
//        }
    }

    @Override
    public void onCacheAvailable(File file, String url, int percentsAvailable) {
        Log.d("LOG_TAG", String.format("onCacheAvailable. percents: %d, file: %s, url: %s", percentsAvailable, file, url));
    }

    private void startVideo(String url, String thumbImage) {
        HttpProxyCacheServer proxy = App.getProxy(context);
        proxy.registerCacheListener(this, url);
        String proxyUrl = proxy.getProxyUrl(url);
        Log.d("LOG_TAG", "Use proxy url " + proxyUrl + " instead of original url " + url);

        videoview.setUp(proxyUrl.trim(), ""
                , JzvdStd.SCREEN_NORMAL);
        JzvdStd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);

        Glide.with(videoview)
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
                }).into(videoview.thumbImageView);

//        ImageLoader.getInstance().displayImage(thumbImage.trim(), videoview.thumbImageView, options, new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
////                progressBar.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                String message = null;
//                switch (failReason.getType()) {
//                    case IO_ERROR:
//                        message = "Input/Output error";
//                        break;
//                    case DECODING_ERROR:
//                        message = "Image can't be decoded";
//                        break;
//                    case NETWORK_DENIED:
//                        message = "Downloads are denied";
//                        break;
//                    case OUT_OF_MEMORY:
//                        message = "Out Of Memory error";
//                        break;
//                    case UNKNOWN:
//                        message = "Unknown error";
//                        break;
//                }
//                Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
//
////                progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
////                progressBar.setVisibility(View.GONE);
//            }
//        });

    }
}
