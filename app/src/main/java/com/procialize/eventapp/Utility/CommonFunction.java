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
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.procialize.eventapp.BuildConfig;
import com.procialize.eventapp.Constants.Constant;
import com.procialize.eventapp.R;
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
import java.util.Calendar;
import java.util.Date;

import static com.procialize.eventapp.Constants.Constant.FOLDER_DIRECTORY;
import static com.procialize.eventapp.Constants.Constant.IMAGE_DIRECTORY;
import static com.procialize.eventapp.Constants.Constant.VIDEO_DIRECTORY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;

public class CommonFunction {

    public static void saveBackgroundImage(Context context, String url) {

        boolean result = Utility.checkWritePermission(context);
        if (result) {


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
                    File fdelete = new File(Uri.parse(myDir + "/" + name).getPath());
                    if (fdelete.exists()) {
                        if (fdelete.delete()) {
                            System.out.println("file Deleted :" + Uri.parse(myDir + "/" + name).getPath());
                        } else {
                            System.out.println("file not Deleted :" + Uri.parse(myDir + "/" + name).getPath());
                        }
                    }
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

    public static void showBackgroundImage(Context context, View view) {
        try {

            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/" + Constant.FOLDER_DIRECTORY + "/" + "background.jpg");
            Resources res = context.getResources();
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

    public static String convertDateToTime(String Date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date sourceDate = null;
        try {
            sourceDate = dateFormat.parse(Date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat targetFormat = new SimpleDateFormat("hh:mm aa");
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
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
        //SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy HH:mm aa");
        String targetdatevalue = targetFormat.format(sourceDate);
        return targetdatevalue;
    }

    public static String convertEventDate(String Date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date sourceDate = null;
        try {
            sourceDate = dateFormat.parse(Date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd, YYYY");
        //SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy HH:mm aa");
        String targetdatevalue = targetFormat.format(sourceDate);
        return targetdatevalue;
    }

    public static String convertAgendaDate(String Date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date sourceDate = null;
        try {
            sourceDate = dateFormat.parse(Date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd, YYYY");
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

    public static String stripquotes(String token) {
        if (token.charAt(0) == '"' && token.charAt(token.length() - 1) == '"') {
            return token.substring(1, token.length() - 1);
        }
        return token;
    }

    public static Date StringToDate(String sDate1) {
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    public static boolean isTimeBetweenTwoTime(String startTime, String endTime, String currentTime) throws ParseException {
        boolean valid = false;
        //Start Time
        //all times are from java.util.Date
        Date inTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(inTime);

        //Current Time
        Date checkTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(currentTime);
        Calendar calendar3 = Calendar.getInstance();
        calendar3.setTime(checkTime);

        //End Time
        Date finTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(finTime);

        if (endTime.compareTo(startTime) < 0) {
            calendar2.add(Calendar.DATE, 1);
            calendar3.add(Calendar.DATE, 1);
        }

        java.util.Date actualTime = calendar3.getTime();
        if ((actualTime.after(calendar1.getTime()) ||
                actualTime.compareTo(calendar1.getTime()) == 0) &&
                actualTime.before(calendar2.getTime())) {
            valid = true;
            return valid;
        } else {
            valid = false;
            return valid;
        }
    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                try {
                    String text;
                    int lineEndIndex;
                    ViewTreeObserver obs = tv.getViewTreeObserver();
                    obs.removeGlobalOnLayoutListener(this);
                    int lineCount = tv.getLineCount();
                    if (maxLine == 0) {
                        lineEndIndex = tv.getLayout().getLineEnd(0);
                        text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    } else if (maxLine > 0 && lineCount >= maxLine) {
                        lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                        text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    } else {
                        lineEndIndex = tv.getLayout().getLineEnd(lineCount - 1);
                        text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    }
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static SpannableStringBuilder
    addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                      final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {
                    tv.setLayoutParams(tv.getLayoutParams());
                    tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                    tv.invalidate();
                    if (viewMore) {
                        makeTextViewResizable(tv, -1, " View Less", false);
                    } else {
                        makeTextViewResizable(tv, 1, " View More", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);
        }
        return ssb;
    }
}