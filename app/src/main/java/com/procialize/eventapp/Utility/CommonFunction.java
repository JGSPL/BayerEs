package com.procialize.eventapp.Utility;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.core.content.FileProvider;

import com.procialize.eventapp.BuildConfig;
import com.procialize.eventapp.Constants.Constant;
import com.procialize.eventapp.R;
import com.procialize.eventapp.ui.newsfeed.model.News_feed_media;
import com.procialize.eventapp.ui.profile.view.ProfileActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static com.procialize.eventapp.Constants.Constant.FOLDER_DIRECTORY;
import static com.procialize.eventapp.Constants.Constant.IMAGE_DIRECTORY;
import static com.procialize.eventapp.Constants.Constant.VIDEO_DIRECTORY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;

public class CommonFunction {

    public static void saveBackgroundImage(Context context, String url) {
        boolean result = Utility.checkWritePermission(context);
        if(result) {
            String mediaPath = SharedPreference.getPref(context, EVENT_LIST_MEDIA_PATH);
            Picasso.with(context).load(mediaPath + url).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    String root = Environment.getExternalStorageDirectory().toString();
                    File myDir = new File(root + "/" + FOLDER_DIRECTORY);
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }

                    String name = "background.jpg";
                    myDir = new File(myDir, name);
                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(myDir);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                        out.flush();
                        out.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    Log.d("Failed", "failed");
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {


                }
            });
        }

    }

    public static void showBackgroundImage(Context context, View view){
        try {

            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/" + Constant.FOLDER_DIRECTORY + "/" + "background.jpg");
            Resources res =context.getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            view.setBackgroundDrawable(bd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String saveImage(Context context, Bitmap myBitmap, String imageName) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + FOLDER_DIRECTORY + IMAGE_DIRECTORY);
        if (!wallpaperDirectory.exists()) {  // have the object build the directory structure, if needed.
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, imageName);
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(context,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());
            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public static String saveVideo(Context context, Uri uri) {
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + FOLDER_DIRECTORY + VIDEO_DIRECTORY);
        if (!wallpaperDirectory.exists()) {  // have the object build the directory structure, if needed.
            wallpaperDirectory.mkdirs();
        }

        try {
            AssetFileDescriptor videoAsset = context.getContentResolver().openAssetFileDescriptor(uri, "r");
            FileInputStream fis = videoAsset.createInputStream();
            File file;
            file = new File(wallpaperDirectory, context.getResources().getString(R.string.app_name) + "_" + System.currentTimeMillis() + ".mp4");
            FileOutputStream fos = new FileOutputStream(file);

            byte[] buf = new byte[1024];
            int len;
            while ((len = fis.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
            fis.close();
            fos.close();

            MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, new String[]{"video/mp4"}, null);
            return uri.getPath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    /**
     * Hide keyboard
     */

    public static void hideKeyboard(Activity activity) {

        if (activity.getCurrentFocus() != null) {

            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void call(Context activity, String number) {
        if (number != null && number.length() > 2) {
            String uri = "tel:" + number;
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(uri));
            activity.startActivity(intent);
        }
    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String convertDate(String Date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date sourceDate = null;
        try {
            sourceDate = dateFormat.parse(Date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
        //SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy HH:mm aa");
        String targetdatevalue = targetFormat.format(sourceDate);
        return targetdatevalue;
    }

    public static String convertDateToDDMMYYYY(String Date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date sourceDate = null;
        try {
            sourceDate = dateFormat.parse(Date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd-mm-yyyy");
        //SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy HH:mm aa");
        String targetdatevalue = targetFormat.format(sourceDate);
        return targetdatevalue;
    }

    static public Uri getLocalBitmapUri(Bitmap bmp, Context context) {
        Uri bmpUri = null;
        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
//            bmpUri = Uri.fromFile(file);
            bmpUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".android.fileprovider", file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

}
