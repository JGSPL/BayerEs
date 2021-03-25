package com.procialize.bayer2020.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.bayer2020.ConnectionDetector;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.Constants.Constant;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.MainActivity;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.livepoll.view.LivePollActivity;
import com.procialize.bayer2020.ui.newsFeedComment.view.CommentActivity;
import com.procialize.bayer2020.ui.newsfeed.model.FetchNewsfeedMultiple;
import com.procialize.bayer2020.ui.newsfeed.model.Newsfeed_detail;
import com.procialize.bayer2020.ui.notification.view.NotificationActivity;
import com.procialize.bayer2020.ui.quiz.view.QuizListingActivity;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.IS_LOGIN;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.KEY_ATTENDEE_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.notification_count;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String ADMIN_CHANNEL_ID = "admin_channel";
    int Count = 0;
    PendingIntent contentIntent;
    Bitmap bitmap;

    int notificationCount = 0;
    private ArrayList<Newsfeed_detail> newsfeed_detail = new ArrayList<>();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
       /* final Intent intent = new Intent(this, MainActivity.class);*/




        notificationCount++;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt(3000);
        String event_id = SharedPreference.getPref(this, EVENT_ID);
        String api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
      /*
        Apps targeting SDK 26 or above (Android O) must implement notification channels and add its notifications
        to at least one of them. Therefore, confirm if version is Oreo or higher, then setup notification channel
      */
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }

       /* intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);*/

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.notify_icon);

        if (remoteMessage.getData().size() > 0) {
            Log.d("Payload===>", "Message data payload: " + remoteMessage.getData());
        }
        String isLogin = SharedPreference.getPref(this, IS_LOGIN);
        if (isLogin.equalsIgnoreCase("true")) {



            //----------------------------------------------------------------------

                Log.e("test notification===>", remoteMessage.getData().toString());
                Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                String spannedString = String.valueOf(Jsoup.parse(remoteMessage.getData().get("content"))).trim();
                Spanned strPost = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    strPost = Html.fromHtml(spannedString, Html.FROM_HTML_MODE_COMPACT);
                } else {
                    strPost = Html.fromHtml(spannedString);
                }

                if (remoteMessage.getData().get("type").equalsIgnoreCase("AdminPost") ||
                        remoteMessage.getData().get("type").equalsIgnoreCase("UserPost")) {
                    if (!remoteMessage.getData().get("sender_id").equalsIgnoreCase(SharedPreference.getPref(getApplicationContext(), KEY_ATTENDEE_ID))) {
                        String imageUri = remoteMessage.getData().get("mediaUrl");
                        bitmap = getBitmapfromUrl(imageUri);
                        Intent notificationIntent = new Intent(getApplicationContext(),
                                MainActivity.class);
                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        contentIntent = PendingIntent.getActivity(getApplicationContext(),
                                new Random().nextInt(), notificationIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                        if (remoteMessage.getData().get("mediaType").equalsIgnoreCase("image")) {
                            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                                    .setSmallIcon(R.drawable.notify_icon)
                                    .setContentTitle(remoteMessage.getData().get("event_name"))
                                    .setStyle(new NotificationCompat.BigTextStyle().bigText(Utility.trimTrailingWhitespace(strPost)))
                                    .setContentText(Utility.trimTrailingWhitespace(strPost))
                                    .setAutoCancel(true)
                                    .setSound(notificationSoundUri).setStyle(new NotificationCompat.BigPictureStyle()
                                            .bigPicture(bitmap).bigLargeIcon(null)).setContentIntent(contentIntent);
                            //Set notification color to match your app color template
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
                            }
                            notificationManager.notify(notificationID, notificationBuilder.build());
                            Intent broadcastIntent = new Intent(Constant.BROADCAST_ACTION_FOR_EVENT_Chat);
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                        } else {
                            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                                    .setSmallIcon(R.drawable.notify_icon)
                                    .setContentTitle(remoteMessage.getData().get("event_name"))
                                    .setStyle(new NotificationCompat.BigTextStyle().bigText(Utility.trimTrailingWhitespace(strPost)))
                                    .setContentText(Utility.trimTrailingWhitespace(strPost))
                                    .setAutoCancel(true).setContentIntent(contentIntent);

                            //Set notification color to match your app color template
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
                            }
                            notificationManager.notify(notificationID, notificationBuilder.build());
                            Intent broadcastIntent = new Intent(Constant.BROADCAST_ACTION_FOR_EVENT_Chat);
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                        }

                        Intent broadcastIntent = new Intent(Constant.BROADCAST_NEW_POST_RECEIVED_ACTION);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }else{

                    }
                } else if (remoteMessage.getData().get("type").equalsIgnoreCase("Quiz")) {
                    Intent notificationIntent = new Intent(getApplicationContext(),
                            QuizListingActivity.class);
                /*notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP);

                contentIntent = PendingIntent.getActivity(getApplicationContext(),
                        new Random().nextInt(), notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);*/

                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                    stackBuilder.addParentStack(MainActivity.class);
                    stackBuilder.addNextIntent(notificationIntent);
                    contentIntent =
                            stackBuilder.getPendingIntent(new Random().nextInt(), PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                            .setSmallIcon(R.drawable.notify_icon)
                            //.setLargeIcon(largeIcon)
                            //.setLargeIcon(bitmap)
                            .setContentTitle(remoteMessage.getData().get("event_name"))
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(Utility.trimTrailingWhitespace(strPost)))
                            .setContentText(Utility.trimTrailingWhitespace(strPost))
                            .setAutoCancel(true).setContentIntent(contentIntent);
                    //Set notification color to match your app color template
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                    notificationManager.notify(notificationID, notificationBuilder.build());
                    Intent broadcastIntent = new Intent(Constant.BROADCAST_ACTION_FOR_EVENT_Chat);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);

                } else if (remoteMessage.getData().get("type").equalsIgnoreCase("LivePoll")) {
                    Intent notificationIntent = new Intent(getApplicationContext(),
                            LivePollActivity.class);
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    contentIntent = PendingIntent.getActivity(getApplicationContext(),
                            new Random().nextInt(), notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                            .setSmallIcon(R.drawable.notify_icon)
                            //.setLargeIcon(largeIcon)
                            //.setLargeIcon(bitmap)
                            .setContentTitle(remoteMessage.getData().get("event_name"))
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(Utility.trimTrailingWhitespace(strPost)))
                            .setContentText(Utility.trimTrailingWhitespace(strPost))
                            .setAutoCancel(true).setContentIntent(contentIntent);
                    //Set notification color to match your app color template
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                    notificationManager.notify(notificationID, notificationBuilder.build());
                    Intent broadcastIntent = new Intent(Constant.BROADCAST_ACTION_FOR_EVENT_Chat);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);

                } else if (remoteMessage.getData().get("type").equalsIgnoreCase("Comment") ||
                        remoteMessage.getData().get("type").equalsIgnoreCase("TagComment") ||
                        remoteMessage.getData().get("type").equalsIgnoreCase("TagNewsfeed")) {
                    String imageUri = remoteMessage.getData().get("mediaUrl");
                    bitmap = getBitmapfromUrl(imageUri);

                    newsfeed_detail = getNewsfeedDetails(remoteMessage.getData().get("id"), api_token, event_id);
                    if (newsfeed_detail.size() > 0) {
                        Intent notificationIntent = new Intent(getApplicationContext(),
                                CommentActivity.class);
                        notificationIntent.putExtra("Newsfeed_detail", (Serializable) newsfeed_detail.get(0));
                        notificationIntent.putExtra("newsfeedId", remoteMessage.getData().get("id"));
                        notificationIntent.putExtra("positionOfList", 0);
                        notificationIntent.putExtra("position", "0");

                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        contentIntent = PendingIntent.getActivity(getApplicationContext(),
                                new Random().nextInt(), notificationIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                                .setSmallIcon(R.drawable.notify_icon)
                                .setContentTitle(remoteMessage.getData().get("event_name"))
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(Utility.trimTrailingWhitespace(strPost)))
                                .setContentText(Utility.trimTrailingWhitespace(strPost))
                                .setAutoCancel(true).setContentIntent(contentIntent);
                        if (bitmap != null) {
                            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                                    .bigPicture(bitmap).bigLargeIcon(null)).setContentIntent(contentIntent);
                        }
                        //Set notification color to match your app color template
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
                        }
                        notificationManager.notify(notificationID, notificationBuilder.build());
                        Intent broadcastIntent = new Intent(Constant.BROADCAST_ACTION_FOR_EVENT_Chat);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                } else if (remoteMessage.getData().get("type").equalsIgnoreCase("Like")) {
                    newsfeed_detail = getNewsfeedDetails(remoteMessage.getData().get("id"), api_token, event_id);
                    if (newsfeed_detail.size() > 0) {
                        Intent notificationIntent = new Intent(getApplicationContext(),
                                CommentActivity.class);
                        notificationIntent.putExtra("Newsfeed_detail", (Serializable) newsfeed_detail.get(0));
                        notificationIntent.putExtra("newsfeedId", remoteMessage.getData().get("id"));
                        notificationIntent.putExtra("position", "0");
                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        contentIntent = PendingIntent.getActivity(getApplicationContext(),
                                new Random().nextInt(), notificationIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                                .setSmallIcon(R.drawable.notify_icon)
                                //.setLargeIcon(largeIcon)
                                //.setLargeIcon(bitmap)
                                .setContentTitle(remoteMessage.getData().get("event_name"))
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(Utility.trimTrailingWhitespace(strPost)))
                                .setContentText(Utility.trimTrailingWhitespace(strPost))
                                .setAutoCancel(true).setContentIntent(contentIntent);
                        //Set notification color to match your app color template
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
                        }
                        notificationManager.notify(notificationID, notificationBuilder.build());
                        Intent broadcastIntent = new Intent(Constant.BROADCAST_ACTION_FOR_EVENT_Chat);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                } else if (remoteMessage.getData().get("type").equalsIgnoreCase("Notification") ||
                        remoteMessage.getData().get("type").equalsIgnoreCase("TagNotification") ||
                        remoteMessage.getData().get("type").equalsIgnoreCase("GodNotification")) {
                    Intent notificationIntent = new Intent(getApplicationContext(),
                            NotificationActivity.class);
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    contentIntent = PendingIntent.getActivity(getApplicationContext(),
                            new Random().nextInt(), notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);


                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                            .setSmallIcon(R.drawable.notify_icon)
                            //.setLargeIcon(largeIcon)
                            //.setLargeIcon(bitmap)
                            .setContentTitle(remoteMessage.getData().get("event_name"))
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(Utility.trimTrailingWhitespace(strPost)))
                            .setContentText(Utility.trimTrailingWhitespace(strPost))
                            .setAutoCancel(true).setContentIntent(contentIntent);
                    //Set notification color to match your app color template
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                    if (remoteMessage.getData().get("mediaType").equalsIgnoreCase("image")) {

                        String imageUri = remoteMessage.getData().get("mediaUrl");
                        bitmap = getBitmapfromUrl(imageUri);
                        notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(bitmap));
                    }


                    notificationManager.notify(notificationID, notificationBuilder.build());
                    Intent broadcastIntent = new Intent(Constant.BROADCAST_ACTION_FOR_EVENT_Chat);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);

                }else if (remoteMessage.getData().get("type").equalsIgnoreCase("Reply") ) {
                    Intent notificationIntent = new Intent(getApplicationContext(),
                            MainActivity.class);
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    contentIntent = PendingIntent.getActivity(getApplicationContext(),
                            new Random().nextInt(), notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);


                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                            .setSmallIcon(R.drawable.notify_icon)
                            //.setLargeIcon(largeIcon)
                            //.setLargeIcon(bitmap)
                            .setContentTitle(remoteMessage.getData().get("event_name"))
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(Utility.trimTrailingWhitespace(strPost)))
                            .setContentText(Utility.trimTrailingWhitespace(strPost))
                            .setAutoCancel(true).setContentIntent(contentIntent);
                    //Set notification color to match your app color template
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
                    }


                    notificationManager.notify(notificationID, notificationBuilder.build());
                    Intent broadcastIntent = new Intent(Constant.BROADCAST_ACTION_FOR_EVENT_Chat);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);

                }
                else
                {
                    Intent notificationIntent = new Intent(getApplicationContext(),
                            MainActivity.class);
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    contentIntent = PendingIntent.getActivity(getApplicationContext(),
                            new Random().nextInt(), notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                            .setSmallIcon(R.drawable.notify_icon)
                            //.setLargeIcon(largeIcon)
                            //.setLargeIcon(bitmap)
                            .setContentTitle(remoteMessage.getData().get("event_name"))
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(Utility.trimTrailingWhitespace(strPost)))
                            .setContentText(Utility.trimTrailingWhitespace(strPost))
                            .setAutoCancel(true).setContentIntent(contentIntent);
                    //Set notification color to match your app color template
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                    notificationManager.notify(notificationID, notificationBuilder.build());
                    Intent broadcastIntent = new Intent(Constant.BROADCAST_ACTION_FOR_EVENT_Chat);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                }

            String strNotificationCount = SharedPreference.getPref(this, notification_count);

            if (strNotificationCount.isEmpty()) {
                strNotificationCount = "0";
            }

            notificationCount = Integer.parseInt(strNotificationCount);

            notificationCount = notificationCount + 1;
            HashMap<String, String> map_token = new HashMap<>();
            map_token.put(notification_count, notificationCount+"");
            SharedPreference.putPref(this, map_token);

            Intent broadcastIntent = new Intent(Constant.BROADCAST_ACTION_FOR_NOTIFICATION_COUNT);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager){
        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Device to devie notification";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

    private ArrayList<Newsfeed_detail> getNewsfeedDetails(final String newsfeedId, String api_token, String event_id) {
        if (ConnectionDetector.getInstance(this).isConnectingToInternet()) {
            ApiUtils.getAPIService().NewsFeedDetailFetch(api_token, event_id, newsfeedId).enqueue(
                    new Callback<FetchNewsfeedMultiple>() {
                        @Override
                        public void onResponse(Call<FetchNewsfeedMultiple> call,
                                               Response<FetchNewsfeedMultiple> response) {
                            if (response.isSuccessful()) {
                                String strEventList = response.body().getDetail();
                                RefreashToken refreashToken = new RefreashToken(getApplicationContext());
                                String data = refreashToken.decryptedData(strEventList);
                                Gson gson = new Gson();
                                newsfeed_detail = gson.fromJson(data, new TypeToken<ArrayList<Newsfeed_detail>>() {
                                }.getType());


                            } else {
                                newsfeed_detail = null;
                            }
                        }

                        @Override
                        public void onFailure(Call<FetchNewsfeedMultiple> call, Throwable t) {
                            newsfeed_detail = null;
                        }
                    });
        }
        return newsfeed_detail;
    }


}