package com.procialize.eventapp.Constants;

import android.content.Context;
import android.widget.Toast;

public class Constant {

    public static final String BASE_URL = "https://stage-admin.procialize.live/baseapp/api/v1/";
    public static final String TENOR_URL = "https://api.tenor.com/v1/";
    public static final String KEY_PATH = "path";
    public static final String REQUEST_IMAGE = "IMAGE";
    public static final String REQUEST_VIDEO = "VIDEO";
    public static final String REQUEST_CAMERA = "CAMERA";
    public static final String FOLDER_DIRECTORY = "/EventApp";
    public static final String IMAGE_DIRECTORY = "/Images";
    public static final String VIDEO_DIRECTORY = "/Video";
    public static final String BROADCAST_UPLOAD_MULTIMEDIA_ACTION = "com.procialize.eventsapp.UPLOAD_MULTIMEDIA";
    public static final String MY_PREFS_NAME = "eventInfo";
    public static String folderName = "EventApp";


    public static void displayToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
