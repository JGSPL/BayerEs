package com.procialize.bayer2020.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.procialize.bayer2020.Constants.Constant;
import com.procialize.bayer2020.Database.EventAppDB;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.SharedPreferencesConstant;
import com.procialize.bayer2020.ui.attendee.model.Attendee;
import com.procialize.bayer2020.ui.attendee.roomDB.TableAttendee;
import com.procialize.bayer2020.ui.attendeeChat.ChatActivity;
import com.procialize.bayer2020.ui.attendeeChat.roomDb.Table_Attendee_Chatcount;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String ADMIN_CHANNEL_ID = "admin_channel";
    int Count = 0;
    PendingIntent contentIntent;
    Bitmap bitmap;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
       /* final Intent intent = new Intent(this, MainActivity.class);*/
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt(3000);

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
                R.mipmap.ic_launcher);

        String Fireid = SharedPreference.getPref(this, SharedPreferencesConstant.FIREBASE_ID);

        String CombineId = remoteMessage.getData().get("title");
        String[] domains = CombineId.split("@");
        String senderId = domains[1];
        String receiverId = domains[0];

        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (Fireid.equalsIgnoreCase(receiverId)) {

            List<Table_Attendee_Chatcount> attenChatCount = EventAppDB.getDatabase(getApplicationContext()).attendeeChatDao().getSingleAttendee(senderId);
            if (attenChatCount.size() > 0) {
                String read  = EventAppDB.getDatabase(getApplicationContext()).attendeeChatDao().getReadId(senderId);
                if(read.equalsIgnoreCase("0")) {
                    Count = EventAppDB.getDatabase(getApplicationContext()).attendeeChatDao().getChatCountId(senderId);
                    Count++;
                    EventAppDB.getDatabase(getApplicationContext()).attendeeChatDao().updateChatCount(Count, senderId);
                    if(remoteMessage.getData().get("message").equalsIgnoreCase("image")) {

                        String imageUri = remoteMessage.getData().get("image");
                        long when = System.currentTimeMillis();
                        bitmap = getBitmapfromUrl(imageUri);

                        TableAttendee tableAttendees = EventAppDB.getDatabase(getApplicationContext()).attendeeDao().getAttendeeDetailsFromFireId(senderId);
                        final Attendee attendee = new Attendee();
                        try {
                            attendee.setFirebase_status(tableAttendees.getFirebase_status());
                            attendee.setMobile(tableAttendees.getMobile());
                            attendee.setEmail(tableAttendees.getEmail());
                            attendee.setFirebase_id(tableAttendees.getFirebase_id());
                            attendee.setFirebase_name(tableAttendees.getFirebase_name());
                            attendee.setFirebase_username(tableAttendees.getFirebase_username());
                            attendee.setAttendee_id(tableAttendees.getAttendee_id());
                            attendee.setFirst_name(tableAttendees.getFirst_name());
                            attendee.setLast_name(tableAttendees.getLast_name());
                            attendee.setCity(tableAttendees.getCity());
                            attendee.setDesignation(tableAttendees.getDesignation());
                            attendee.setCompany_name(tableAttendees.getCompany_name());
                            attendee.setAttendee_type(tableAttendees.getAttendee_type());
                            attendee.setTotal_sms(tableAttendees.getTotal_sms());
                            attendee.setProfile_picture(tableAttendees.getProfile_picture());
                            attendee.setFirebase_status(tableAttendees.getFirebase_status());
                        }catch(Exception e){

                        }

                        if(attendee!=null) {

                            Intent notificationIntent = new Intent(getApplicationContext(),
                                    ChatActivity.class);
                            notificationIntent.putExtra("Attendee", (Serializable)attendee);
                            notificationIntent.putExtra("firstMessage","");
                            notificationIntent.putExtra("page", "Notification");
                            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            contentIntent = PendingIntent.getActivity(
                                    getApplicationContext(), new Random().nextInt(),
                                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        }

                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setLargeIcon(bitmap)
                                .setColorized(true)
                                .setWhen(when)
                                .setContentTitle(remoteMessage.getData().get("titleMain"))
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                //.setContentText(remoteMessage.getData().get("message"))
                                .setAutoCancel(true)
                                .setSound(notificationSoundUri)
                                .setStyle(new NotificationCompat.BigPictureStyle()
                                        .bigPicture(bitmap).bigLargeIcon(null))
                                .setContentIntent(contentIntent);



                        //Set notification color to match your app color template
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
                        }
                        notificationManager.notify(notificationID, notificationBuilder.build());
                        Intent broadcastIntent = new Intent(Constant.BROADCAST_ACTION_FOR_EVENT_Chat);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);


                        notificationBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                        notificationBuilder.setContentIntent(contentIntent);
                        notificationBuilder.setAutoCancel(true);



                    }else{

                        TableAttendee tableAttendees = EventAppDB.getDatabase(getApplicationContext()).attendeeDao().getAttendeeDetailsFromFireId(senderId);
                        final Attendee attendee = new Attendee();
                        try {
                            attendee.setFirebase_status(tableAttendees.getFirebase_status());
                            attendee.setMobile(tableAttendees.getMobile());
                            attendee.setEmail(tableAttendees.getEmail());
                            attendee.setFirebase_id(tableAttendees.getFirebase_id());
                            attendee.setFirebase_name(tableAttendees.getFirebase_name());
                            attendee.setFirebase_username(tableAttendees.getFirebase_username());
                            attendee.setAttendee_id(tableAttendees.getAttendee_id());
                            attendee.setFirst_name(tableAttendees.getFirst_name());
                            attendee.setLast_name(tableAttendees.getLast_name());
                            attendee.setCity(tableAttendees.getCity());
                            attendee.setDesignation(tableAttendees.getDesignation());
                            attendee.setCompany_name(tableAttendees.getCompany_name());
                            attendee.setAttendee_type(tableAttendees.getAttendee_type());
                            attendee.setTotal_sms(tableAttendees.getTotal_sms());
                            attendee.setProfile_picture(tableAttendees.getProfile_picture());
                            attendee.setFirebase_status(tableAttendees.getFirebase_status());
                        }catch(Exception e){

                        }

                        if(attendee!=null) {

                            Intent notificationIntent = new Intent(getApplicationContext(),
                                    ChatActivity.class);
                            notificationIntent.putExtra("Attendee", (Serializable)attendee);
                            notificationIntent.putExtra("firstMessage","");
                            notificationIntent.putExtra("page", "Notification");
                            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            contentIntent = PendingIntent.getActivity(
                                    getApplicationContext(), new Random().nextInt(),
                                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            // startActivity(notificationIntent);



                        }
                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setLargeIcon(largeIcon)
                                 .setContentTitle(remoteMessage.getData().get("titleMain"))
                                .setContentText(remoteMessage.getData().get("message"))
                                .setAutoCancel(true)
                                .setSound(notificationSoundUri)
                                .setContentIntent(contentIntent);
                        //Set notification color to match your app color template
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
                        }


                        notificationManager.notify(notificationID, notificationBuilder.build());
                        Intent broadcastIntent = new Intent(Constant.BROADCAST_ACTION_FOR_EVENT_Chat);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);

                        notificationBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                        notificationBuilder.setContentIntent(contentIntent);
                        notificationBuilder.setAutoCancel(true);


                    }



                }

            } else {
                Table_Attendee_Chatcount attChat = new Table_Attendee_Chatcount();
                attChat.setChatCount_receId(senderId);
                attChat.setChat_count(1);
                attChat.setChat_count_read("0");
                attChat.setChat_mess("");
                EventAppDB.getDatabase(getApplicationContext()).attendeeChatDao().insertAttendee(attChat);

                TableAttendee tableAttendees = EventAppDB.getDatabase(getApplicationContext()).attendeeDao().getAttendeeDetailsFromFireId(senderId);
                final Attendee attendee = new Attendee();
                try {
                    attendee.setFirebase_status(tableAttendees.getFirebase_status());
                    attendee.setMobile(tableAttendees.getMobile());
                    attendee.setEmail(tableAttendees.getEmail());
                    attendee.setFirebase_id(tableAttendees.getFirebase_id());
                    attendee.setFirebase_name(tableAttendees.getFirebase_name());
                    attendee.setFirebase_username(tableAttendees.getFirebase_username());
                    attendee.setAttendee_id(tableAttendees.getAttendee_id());
                    attendee.setFirst_name(tableAttendees.getFirst_name());
                    attendee.setLast_name(tableAttendees.getLast_name());
                    attendee.setCity(tableAttendees.getCity());
                    attendee.setDesignation(tableAttendees.getDesignation());
                    attendee.setCompany_name(tableAttendees.getCompany_name());
                    attendee.setAttendee_type(tableAttendees.getAttendee_type());
                    attendee.setTotal_sms(tableAttendees.getTotal_sms());
                    attendee.setProfile_picture(tableAttendees.getProfile_picture());
                    attendee.setFirebase_status(tableAttendees.getFirebase_status());
                }catch(Exception e){

                }
                if(attendee!=null) {

                    Intent notificationIntent = new Intent(getApplicationContext(),
                            ChatActivity.class);
                    notificationIntent.putExtra("Attendee", (Serializable)attendee);
                    notificationIntent.putExtra("firstMessage", "");
                    notificationIntent.putExtra("page", "Notification");
                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    contentIntent = PendingIntent.getActivity(
                            getApplicationContext(), new Random().nextInt(),
                            notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                }

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(largeIcon)
                        // .setContentTitle(remoteMessage.getData().get("title"))
                        .setContentText(remoteMessage.getData().get("message"))
                        .setAutoCancel(true)
                        .setSound(notificationSoundUri)
                        .setContentIntent(contentIntent);


                //Set notification color to match your app color template
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                notificationManager.notify(notificationID, notificationBuilder.build());
                Intent broadcastIntent = new Intent(Constant.BROADCAST_ACTION_FOR_EVENT_Chat);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);


            }




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

}