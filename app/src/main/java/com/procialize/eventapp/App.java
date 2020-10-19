package com.procialize.eventapp;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.crashlytics.android.Crashlytics;
import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.KLog;
import com.google.firebase.FirebaseApp;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.procialize.eventapp.Constants.Constant;
import com.procialize.eventapp.Utility.Utils;

import io.fabric.sdk.android.Fabric;

/**
 * @author Alexey Danilov (danikula@gmail.com).
 */
public class App extends Application {

    private HttpProxyCacheServer proxy;

    @Override
    public void onCreate() {
        if (Constant.Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
        }
        super.onCreate();
        KLog.init(BuildConfig.DEBUG, "[VideoCache]:");
        initImageLoader(getApplicationContext());
        Fabric.with(this, new Crashlytics());


    }

    public static HttpProxyCacheServer getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .cacheDirectory(Utils.getVideoCacheDir(this))
                .build();
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
//		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }
}
