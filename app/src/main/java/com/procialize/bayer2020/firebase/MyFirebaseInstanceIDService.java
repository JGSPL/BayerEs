package com.procialize.bayer2020.firebase;

/**
 * Created by Rahul on 22-02-2018.
 */

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService  extends FirebaseInstanceIdService {

    private static final String TAG = "mFirebaseIIDService";
    private static final String SUBSCRIBE_TO = "userABC";
    private static final String SUBSCRIBE_TO_chat = "OneToOneChat";


    @Override
    public void onTokenRefresh() {
        /*
          This method is invoked whenever the token refreshes
          OPTIONAL: If you want to send messages to this application instance
          or manage this apps subscriptions on the server side,
          you can send this token to your server.
        */
        String token = FirebaseInstanceId.getInstance().getToken();

        // Once the token is generated, subscribe to topic with the userId
        FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO);
        FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO_chat);

        Log.i(TAG, "onTokenRefresh completed with token: " + token);
    }
}
