package com.procialize.eventapp.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.procialize.eventapp.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.procialize.eventapp.constants.Constant.FOLDER_DIRECTORY;
import static com.procialize.eventapp.constants.Constant.IMAGE_DIRECTORY;
import static com.procialize.eventapp.constants.Constant.VIDEO_DIRECTORY;

public class CommonFunction {

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

            InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
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
}
