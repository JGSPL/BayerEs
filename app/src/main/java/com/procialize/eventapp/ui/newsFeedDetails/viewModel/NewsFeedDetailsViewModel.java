package com.procialize.eventapp.ui.newsFeedDetails.viewModel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.eventapp.Constants.Constant;
import com.procialize.eventapp.Utility.CommonFunction;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsFeedDetailsViewModel extends ViewModel {
    String strPath;
    private String fileName;

    public void saveImage(Activity activity, Bitmap bitmap) {

            Date date = new Date();
            long time = date.getTime();
            String name = +time + ".jpg";
            CommonFunction.saveImage(activity, bitmap, name);

    }

    public void saveVideo(Activity activity, final String urlPath)
    {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... voids) {

                int count;
                try {
                    URL url = new URL(urlPath);
                    URLConnection connection = url.openConnection();
                    connection.connect();

                    // getting file length
                    int lengthOfFile = connection.getContentLength();

                    InputStream input = new BufferedInputStream(url.openStream(), 8192);
                    String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                    //Extract file name from URL
                    fileName = urlPath.substring(urlPath.lastIndexOf('/') + 1, urlPath.length());

                    String folder = Environment.getExternalStorageDirectory().toString() + "/" + Constant.FOLDER_DIRECTORY + "/";
                    //Create androiddeft folder if it does not exist
                    File directory = new File(folder);
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }
                    strPath = folder + fileName;
                    // Output stream to write file
                    OutputStream output = new FileOutputStream(folder + fileName);

                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        // publishing the progress....
                        // After this onProgressUpdate will be called
                        publishProgress("" + (int) ((total * 100) / lengthOfFile));
                        Log.d("ImageMultipleActivity", "Progress: " + (int) ((total * 100) / lengthOfFile));

                        // writing data to file
                        output.write(data, 0, count);
                    }

                    // flushing output
                    output.flush();

                    // closing streams
                    output.close();
                    input.close();
                    return "Download completed- check folder " + Constant.FOLDER_DIRECTORY;

                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(String aVoid) {
                super.onPostExecute(aVoid);
/*                List<NicePlace> currentPlaces = mNicePlaces.getValue();
                currentPlaces.add(nicePlace);
                mNicePlaces.postValue(currentPlaces);
                mIsUpdating.postValue(false);*/
            }
        }.execute();
    }

    public void shareMedia(Activity activity, String urlPath, String media_type) {

    }

}
