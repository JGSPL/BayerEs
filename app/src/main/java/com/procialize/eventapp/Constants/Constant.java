package com.procialize.eventapp.Constants;

import android.content.Context;
import android.widget.Toast;

public class Constant {

    public static final String BASE_URL = "https://stage-admin.procialize.live/baseapp/api/v1/";
    public static final String KEY_PATH = "path";
    public static final String REQUEST_IMAGE = "IMAGE";
    public static final String REQUEST_VIDEO = "VIDEO";
    public static final String REQUEST_CAMERA = "CAMERA";
    public static final String FOLDER_DIRECTORY = "/MultiImageDemo";
    public static final String IMAGE_DIRECTORY = "/Images";
    public static final String VIDEO_DIRECTORY = "/Video";

    public static void displayToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
