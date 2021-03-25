package com.procialize.bayer2020.ui.newsfeed.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.procialize.bayer2020.App;
import com.procialize.bayer2020.Constants.AnimateFirstDisplayListener;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.newsFeedDetails.view.NewsFeedDetailsActivity;
import com.procialize.bayer2020.ui.newsFeedDetails.view.NewsfeedDetailsInLandscape;
import com.procialize.bayer2020.ui.newsfeed.model.News_feed_media;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;


public class SwipeMultimediaAdapter extends PagerAdapter implements CacheListener {

    FrameLayout farme;
    public ImageView myImage;
    // public MyJZVideoPlayerStandard videoview;
    public JzvdStd videoview;
    String MY_PREFS_NAME = "ProcializeInfo";
    ImageView thumbimg;
    private List<String> images;
    private List<String> thumbImages;
    private LayoutInflater inflater;
    List<News_feed_media> news_feed_media = new ArrayList<>();
    private Context context;
    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public SwipeMultimediaAdapter(Context context, List<String> images, List<String> thumbImages, List<News_feed_media> news_feed_media) {
        this.context = context;
        this.images = images;
        this.thumbImages = thumbImages;
        this.news_feed_media = news_feed_media;
        inflater = LayoutInflater.from(context);

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
        App.getProxy(context).unregisterCacheListener(this);
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
        farme = myImageLayout.findViewById(R.id.farme);

        final ProgressBar progressBar = myImageLayout.findViewById(R.id.progressbar);

        if ((firstLevelFilter.contains(".png") || firstLevelFilter.contains(".jpg") || firstLevelFilter.contains(".jpeg") || firstLevelFilter.contains(".gif")
        || firstLevelFilter.contains(".PNG") || firstLevelFilter.contains(".JPG") || firstLevelFilter.contains(".JPEG") || firstLevelFilter.contains(".GIF"))) {
            myImage.setVisibility(View.VISIBLE);
            videoview.setVisibility(View.GONE);
            farme.setVisibility(View.GONE);
            JzvdStd.goOnPlayOnPause();

            if (firstLevelFilter.contains("gif")||firstLevelFilter.contains("GIF")) {
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
                        try {
                            Utility.createShortSnackBar(view, message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        progressBar.setVisibility(View.GONE);
                    }
                });

//                Glide.with(context)
//                        .load(firstLevelFilter.trim())
//                        //.placeholder(R.mipmap.placeholder)
//                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).centerCrop()
//                        .listener(new RequestListener<Drawable>() {
//                            @Override
//                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                progressBar.setVisibility(View.GONE);
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                progressBar.setVisibility(View.GONE);
//                                return false;
//                            }
//                        }).into(myImage);
            }
        } else if (firstLevelFilter.contains(".mp4")) {
            myImage.setVisibility(View.GONE);
            videoview.setVisibility(View.VISIBLE);
            farme.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);

           /* videoview.fullscreenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, LandscapeVideoActivity.class);
                    intent.putExtra("url",firstLevelFilter.trim());
                    context.startActivity(intent);
                }
            });*/
            checkCachedState(firstLevelFilter.trim());
            startVideo(firstLevelFilter.trim(), thumbImage);
//            // progressBar.setVisibility(View.GONE);
////            VideoCacheFragment fragment=new VideoCacheFragment().newInstance(firstLevelFilter.trim(),context);
//            videoview.setUp(firstLevelFilter.trim(), ""
//                    , JzvdStd.SCREEN_NORMAL);
//            JzvdStd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);
//
//            Glide.with(videoview)
//                    .load(thumbImage.trim())
//                    //.placeholder(R.mipmap.placeholder)
//                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)).centerCrop()
//                    .listener(new RequestListener<Drawable>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            progressBar.setVisibility(View.GONE);
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            progressBar.setVisibility(View.GONE);
//                            return false;
//                        }
//                    }).into(videoview.thumbImageView);


        }

        view.addView(myImageLayout, 0);

        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(ConnectionDetector.getInstance(context).isConnectingToInternet()) {
                if (news_feed_media.size() > 0) {

                    Intent edit = new Intent(context, NewsFeedDetailsActivity.class);
                    edit.putExtra("position", position);
                    edit.putExtra("page", "newsfeed");
                    edit.putExtra("media_list", (Serializable) news_feed_media);

                    context.startActivity(edit);
                }
                //}
            }
        });

        videoview.thumbImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(ConnectionDetector.getInstance(context).isConnectingToInternet()) {
                if (news_feed_media.size() > 0) {
                    Intent edit = new Intent(context, NewsFeedDetailsActivity.class);
                    edit.putExtra("position", position);
                    edit.putExtra("page", "newsfeed");
                    edit.putExtra("media_list", (Serializable) news_feed_media);
                    context.startActivity(edit);
                }
                //}
            }
        });

        videoview.fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ChatActivity.videoflag = "1";
                JzvdStd.releaseAllVideos();
                Intent comment = new Intent(context, NewsfeedDetailsInLandscape.class);
                comment.putExtra("urlPhotoClick", news_feed_media.get(position).getMedia_file());
                comment.putExtra("thumbImage", thumbImages.get(position));
                context.startActivity(comment);
            }
        });
        return myImageLayout;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void onCacheAvailable(File file, String url, int percentsAvailable) {
        Log.d("LOG_TAG", String.format("onCacheAvailable. percents: %d, file: %s, url: %s", percentsAvailable, file, url));
    }


    private void checkCachedState(String url) {
        HttpProxyCacheServer proxy = App.getProxy(context);
        boolean fullyCached = proxy.isCached(url);
//        setCachedState(fullyCached);
//        if (fullyCached) {
//            progressBar.setSecondaryProgress(100);
//        }
    }


    private void startVideo(String url, String thumbImage) {
        HttpProxyCacheServer proxy = App.getProxy(context);
        proxy.registerCacheListener(this, url);
        String proxyUrl = proxy.getProxyUrl(url);
        Log.d("LOG_TAG", "Use proxy url " + proxyUrl + " instead of original url " + url);

        videoview.setUp(proxyUrl.trim(), ""
                , JzvdStd.SCREEN_NORMAL);
        //JzvdStd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);
        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);
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

    }
}