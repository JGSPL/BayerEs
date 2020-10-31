package com.procialize.eventapp.ui.attendeeChat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Database.EventAppDB;
import com.procialize.eventapp.GetterSetter.Header;
import com.procialize.eventapp.GetterSetter.LoginOrganizer;
import com.procialize.eventapp.MainActivity;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.KeyboardUtility;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.SharedPreferencesConstant;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.costumTools.ScalingUtilities;
import com.procialize.eventapp.costumTools.TouchImageView;
import com.procialize.eventapp.firebase.MySingleton;
import com.procialize.eventapp.ui.attendee.model.Attendee;
import com.procialize.eventapp.ui.attendee.view.AttendeeDetailActivity;
import com.procialize.eventapp.ui.attendeeChat.activity.AttendeeChatDetail;
import com.procialize.eventapp.ui.attendeeChat.adapter.MessageAdapter;
import com.procialize.eventapp.ui.attendeeChat.model.Messages;
import com.procialize.eventapp.ui.attendeeChat.roomDb.Table_Attendee_Chatcount;
import com.procialize.eventapp.ui.newsfeed.adapter.NewsFeedAdapter;
import com.procialize.eventapp.ui.newsfeed.view.NewsFeedFragment;
import com.procialize.eventapp.ui.profile.view.ProfileActivity;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.http.Url;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.eventapp.Utility.Utility.MY_PERMISSIONS_REQUEST_CAMERA;
import static com.procialize.eventapp.Utility.Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
import static com.procialize.eventapp.Utility.Utility.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;


public class ChatActivity extends AppCompatActivity {

    private String mChatUser;
    TextView mUserName;
    TextView mUserLastSeen;
    CircleImageView mUserImage;
    private FirebaseAuth mAuth;
    private Dialog myDialog;
    String mCurrentUserId;

    DatabaseReference mDatabaseReference;
    private DatabaseReference mRootReference;

    private ImageView mChatSendButton, mChatAddButton;
    private EditText mMessageView;

    private RecyclerView mMessagesList;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private MessageAdapter mMessageAdapter;

    public static final int TOTAL_ITEM_TO_LOAD = 25;
    private int mCurrentPage = 1;
    private int Totalpage = 0;

    //Solution for descending list on refresh
    private int itemPos = 0;
    private String mLastKey = "";
    private String mPrevKey = "";

    private static final int GALLERY_PICK = 1;
    StorageReference mImageStorage;

    String userChoosenTask;
    int SELECT_FILE = 3, REQUEST_TAKE_PHOTO = 2, REQUEST_VIDEO_CAPTURE = 4;
    File file = null;
    String mCurrentPhotoPath;
    private File filePathImageCamera;
    Uri imageUri;
    String videoUrl;
    private Uri uri;
    private String pathToStoredVideo;
    public static File videoFile;
    Uri video;
    private ProgressBar progressBar;
    String lname, company, city, designation, attendee_type, mobile, email, attendeeid, firebase_id, firstMessage, page, loginUser;
    LinearLayout lineaeSend;
    public static String videoflag = "0";
    ConnectionDetector cd;
    Attendee attendee;
    OkHttpClient mClient = new OkHttpClient();

    String refreshedToken = "";//add your user refresh tokens who are logged in with firebase.

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAA4xkbUmY:APA91bHJtoRFEI_jxvR7jJEIA0M-Wa4adoRiLGWIMiWdAgEg5CLjsJBRuByvHHj-764l5zVRav8N_qwn_etLCzUHsL-xfhJTrQQFSYkHRurjID5haW2TpfZF1JRDw0y4vKBoqVg6Lldb";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;
    ImageView backImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mChatAddButton = (ImageView) findViewById(R.id.chatAddButton);
        mChatSendButton = (ImageView) findViewById(R.id.chatSendButton);
        lineaeSend = findViewById(R.id.lineaeSend);
        mMessageView = (EditText) findViewById(R.id.chatMessageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        cd = ConnectionDetector.getInstance(this);

        getWindow().setBackgroundDrawable(getDrawable(R.drawable.chat_bg));
       getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        //-----GETING FROM INTENT----
        // mChatUser = getIntent().getStringExtra("user_id");
        //final String userName = getIntent().getStringExtra("user_name");
        //String loginUser_name = getIntent().getStringExtra("loginUser_name");
        //String sProfilepic = getIntent().getStringExtra("sProfilepic");
        // final String prof_pic = getIntent().getStringExtra("rProfilepic");

        final RelativeLayout linMain = findViewById(R.id.linMain);

        Intent intent = getIntent();
        page = intent.getStringExtra("page");
        firstMessage = intent.getStringExtra("firstMessage");
        attendee = (Attendee) getIntent().getSerializableExtra("Attendee");

        final String userName = attendee.getFirst_name();
        lname = attendee.getLast_name();
        company = attendee.getCompany_name();
        city = attendee.getCity();
        designation = attendee.getDesignation();
        final String prof_pic = attendee.getProfile_picture();
        attendee_type = attendee.getAttendee_type();
        mobile = attendee.getMobile();
        email = attendee.getEmail();
        attendeeid = attendee.getAttendee_id();
        firebase_id = attendee.getFirebase_id();
        mChatUser = attendee.getFirebase_id();
        loginUser = SharedPreference.getPref(this,SharedPreferencesConstant.KEY_FNAME);

        //.........................set Data on Attendee Chat table......................//
        List<Table_Attendee_Chatcount> attenChatCount = EventAppDB.getDatabase(getApplicationContext()).attendeeChatDao().getSingleAttendee(firebase_id);
        if (attenChatCount.size() > 0) {
            EventAppDB.getDatabase(getApplicationContext()).attendeeChatDao().updateIsRead( firebase_id);
            EventAppDB.getDatabase(this).attendeeChatDao().updateChatCount(0, firebase_id);


        } else {
            Table_Attendee_Chatcount attChat = new Table_Attendee_Chatcount();
            attChat.setChatCount_receId(firebase_id);
            attChat.setChat_count(0);
            attChat.setChat_count_read("0");
            attChat.setChat_mess("");
            EventAppDB.getDatabase(getApplicationContext()).attendeeChatDao().insertAttendee(attChat);
        }

       // EventAppDB.getDatabase(this).attendeeChatDao().updateIsRead(firebase_id);
       // int unreadMsgCount = EventAppDB.getDatabase(this).attendeeChatDao().getChatCountId(firebase_id);


        //---SETTING ONLINE------
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        //----ADDING ACTION BAR-----

        //---INFLATING APP BAR LAYOUT INTO ACTION BAR----


        View actionBarView = findViewById(R.id.headerlayout); // root View id from that link

        //---ADDING DATA ON ACTION BAR----
        mUserName = (TextView) actionBarView.findViewById(R.id.textView3);
        mUserLastSeen = (TextView) actionBarView.findViewById(R.id.textView5);
        mUserImage = (CircleImageView) actionBarView.findViewById(R.id.circleImageView);
        LinearLayout linBack = actionBarView.findViewById(R.id.linBack);
         backImage = actionBarView.findViewById(R.id.backImage);
        backImage.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);

        ImageView ivattDetail = actionBarView.findViewById(R.id.ivattDetail);
        mUserName.setText(userName);
        mUserLastSeen.setText(designation + " - " + city);

        mMessageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessageView.requestFocus();

                InputMethodManager imm = (InputMethodManager)getSystemService(ChatActivity.this.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            }
        });


        Picasso.with(ChatActivity.this).load(prof_pic).placeholder(R.drawable.profilepic_placeholder).into(mUserImage);
        linBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ivattDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, AttendeeChatDetail.class);
                intent.putExtra("attendeeid", attendeeid);
                intent.putExtra("firebase_id", firebase_id);

                intent.putExtra("fname", userName);
                intent.putExtra("lname", lname);
                intent.putExtra("company", company);
                intent.putExtra("city", city);
                intent.putExtra("designation", designation);
                intent.putExtra("prof_pic", prof_pic);
                intent.putExtra("attendee_type", attendee_type);
                intent.putExtra("mobile", mobile);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        mRootReference = FirebaseDatabase.getInstance().getReference();
        mImageStorage = FirebaseStorage.getInstance().getReference();

        try {

            mAuth = FirebaseAuth.getInstance();
            mCurrentUserId = mAuth.getCurrentUser().getUid();
        } catch (Exception e) {

        }

        mMessageAdapter = new MessageAdapter(messagesList, userName/*,loginUser_name,sProfilepic*/, prof_pic);

        mMessagesList = (RecyclerView) findViewById(R.id.recycleViewMessageList);
        //mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.message_swipe_layout);
        mLinearLayoutManager = new LinearLayoutManager(ChatActivity.this);

        // mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayoutManager);
        mMessagesList.setAdapter(mMessageAdapter);
        mMessageAdapter.notifyDataSetChanged();



        if (messagesList.size() == 0) {
            progressBar.setVisibility(View.GONE);
        }
        if (cd.isConnectingToInternet()) {

            loadMessages();
        } else {
            Utility.createShortSnackBar(linMain, "No internet connection");

        }

        //----ADDING LAST SEEN-----

        mRootReference.child("users").child(mChatUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                String onlineValue=dataSnapshot.child("online").getValue().toString();
                //  String imageValue = dataSnapshot.child("thumb_image").getValue().toString();

               /* if(onlineValue.equals("true")){
                    mUserLastSeen.setText("online");
                }
                else{
                    GetTimeAgo getTimeAgo = new GetTimeAgo();
                    long lastTime = Long.parseLong(onlineValue);
                    String lastSeen = getTimeAgo.getTimeAgo(lastTime,getApplicationContext());
                    mUserLastSeen.setText(lastSeen);
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //----ADDING SEEN OF MESSAGES----
        mRootReference.child("chats").child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.hasChild(mChatUser)) {

                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen", false);
                    chatAddMap.put("time_stamp", ServerValue.TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("chats/" + mChatUser + "/" + mCurrentUserId, chatAddMap);
                    chatUserMap.put("chats/" + mCurrentUserId + "/" + mChatUser, chatAddMap);

                    mRootReference.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                //Toast.makeText(getApplicationContext(), "Successfully Added chats feature", Toast.LENGTH_SHORT).show();
                            } else {
                                // Toast.makeText(getApplicationContext(), "Cannot Add chats feature", Toast.LENGTH_SHORT).show();
                            }
                        }


                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Something went wrong.. Please go back..", Toast.LENGTH_SHORT).show();
            }
        });

        if (page.equalsIgnoreCase("AttendeeDetail")) {
            if (firstMessage != null) {
                String current_user_ref = "messages/" + mCurrentUserId + "/" + mChatUser;
                String chat_user_ref = "messages/" + mChatUser + "/" + mCurrentUserId;

                DatabaseReference user_message_push = mRootReference.child("messages")
                        .child(mCurrentUserId).child(mChatUser).push();

                String push_id = user_message_push.getKey();

                Map messageMap = new HashMap();
                messageMap.put("message", firstMessage);
                messageMap.put("seen", false);
                messageMap.put("type", "text");
                messageMap.put("time", ServerValue.TIMESTAMP);
                messageMap.put("from", mCurrentUserId);

                Map messageUserMap = new HashMap();
                messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
                messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

                mRootReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {

                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Log.e("CHAT_ACTIVITY", "Cannot add message to database");
                        } else {
                            Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                            mMessageView.setText("");

                            // Check for new messages
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                            //Send Text Notification
                            TOPIC = "/topics/userABC"; //topic has to match what the receiver subscribed to
                            NOTIFICATION_TITLE = firebase_id + "@"+currentUser.getUid();
                            NOTIFICATION_MESSAGE = firstMessage;

                            JSONObject notification = new JSONObject();
                            JSONObject notifcationBody = new JSONObject();
                            try {
                                notifcationBody.put("title", NOTIFICATION_TITLE);
                                notifcationBody.put("titleMain", loginUser+" sent you a message");

                                notifcationBody.put("message", NOTIFICATION_MESSAGE);

                                notification.put("to", TOPIC);
                                notification.put("data", notifcationBody);
                            } catch (JSONException e) {
                                Log.e(TAG, "onCreate: " + e.getMessage() );
                            }
                            sendNotification(notification);
                        }

                    }
                });


            }
        }

        //----SEND MESSAGE--BUTTON----

        mChatSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = mMessageView.getText().toString().trim();
                mMessageView.setText("");
                if (!TextUtils.isEmpty(message)) {

                    String current_user_ref = "messages/" + mCurrentUserId + "/" + mChatUser;
                    String chat_user_ref = "messages/" + mChatUser + "/" + mCurrentUserId;

                    DatabaseReference user_message_push = mRootReference.child("messages")
                            .child(mCurrentUserId).child(mChatUser).push();

                    String push_id = user_message_push.getKey();

                    Map messageMap = new HashMap();
                    messageMap.put("message", message);
                    messageMap.put("seen", false);
                    messageMap.put("type", "text");
                    messageMap.put("time", ServerValue.TIMESTAMP);
                    messageMap.put("from", mCurrentUserId);

                    Map messageUserMap = new HashMap();
                    messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
                    messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

                    mRootReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {

                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Log.e("CHAT_ACTIVITY", "Cannot add message to database");
                            } else {
                                Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                                mMessageView.setText("");
                            }

                        }
                    });

                //    sendMessage(jsonArray,"Chat Notification",mChatUser +" "+ mCurrentUserId,"Http:\\google.com",message);


                    //TODO: Get the ID of the chat the user is taking part in
                    String chatID = push_id;

                    // Check for new messages
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                    //Send Text Notification
                    TOPIC = "/topics/userABC"; //topic has to match what the receiver subscribed to
                    NOTIFICATION_TITLE = firebase_id + "@"+currentUser.getUid();
                    NOTIFICATION_MESSAGE = message;

                    JSONObject notification = new JSONObject();
                    JSONObject notifcationBody = new JSONObject();
                    try {
                        notifcationBody.put("title", NOTIFICATION_TITLE);
                        notifcationBody.put("titleMain", loginUser+" sent you a message");
                        notifcationBody.put("message", NOTIFICATION_MESSAGE);

                        notification.put("to", TOPIC);
                        notification.put("data", notifcationBody);
                    } catch (JSONException e) {
                        Log.e(TAG, "onCreate: " + e.getMessage() );
                    }
                    sendNotification(notification);





                } else {
                    Toast.makeText(ChatActivity.this, "Please enter any message", Toast.LENGTH_SHORT).show();

                }

            }
        });



        mChatAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectOption();
               /* Intent galleryIntent=new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"),GALLERY_PICK);*/
            }
        });

        //----LOADING 10 MESSAGES ON SWIPE REFRESH----


/*
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (cd.isConnectingToInternet()) {

                    itemPos = 0;
                    mCurrentPage++;
                    loadMoreMessages();
                } else {
                    Utility.createShortSnackBar(linMain, "No internet connection");
                    mSwipeRefreshLayout.setRefreshing(false);

                }



            }
        });
*/

        mMessagesList.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                JzvdStd.goOnPlayOnPause();
            }
        });

        mMessagesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(-1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d("-----","end");
                    if (cd.isConnectingToInternet()) {

                        if(mCurrentPage<=Totalpage) {
                            itemPos = 0;
                            mCurrentPage++;
                            loadMoreMessages();
                        }
                    } else {
                        Utility.createShortSnackBar(linMain, "No internet connection");
                      //  mSwipeRefreshLayout.setRefreshing(false);

                    }
                }else{
                }
            }
        });



/*
        mMessagesList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int ydy = 0;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int offset = dy - ydy;
                ydy = dy;
                boolean shouldRefresh = (mLinearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0)
                        && (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING) && offset > 30;
                if (shouldRefresh) {
                    //swipeRefreshLayout.setRefreshing(true);
                    //Refresh to load data here.
                    */
/*if (cd.isConnectingToInternet()) {

                        itemPos = 0;
                        mCurrentPage++;
                        loadMoreMessages();
                    } else {
                        Utility.createShortSnackBar(linMain, "No internet connection");
                        mSwipeRefreshLayout.setRefreshing(false);

                    }*//*

                    return;
                }
                boolean shouldPullUpRefresh = mLinearLayoutManager.findLastCompletelyVisibleItemPosition() == mLinearLayoutManager.getChildCount() - 1
                        && recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING && offset < -30;
                if (shouldPullUpRefresh) {
                    //swipeRefreshLayout.setRefreshing(true);
                    //refresh to load data here.
                    if (cd.isConnectingToInternet()) {

                        itemPos = 0;
                        mCurrentPage++;
                        loadMoreMessages();
                    } else {
                        Utility.createShortSnackBar(linMain, "No internet connection");
                        mSwipeRefreshLayout.setRefreshing(false);

                    }


                    return;
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
*/

    }

    //---FIRST 10 MESSAGES WILL LOAD ON START----
    private void loadMessages() {
        try {
            DatabaseReference messageRef = mRootReference.child("messages").child(mCurrentUserId).child(mChatUser);
            Query messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEM_TO_LOAD);
            final Query totalmessage = messageRef.orderByKey();

            DatabaseReference ref = mRootReference.child("messages").child(mCurrentUserId).child(mChatUser);

            //add listener to updated ref
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //use the for loop here to step over each child and retrieve data
                    int i=0;

                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        //String valueFromDB = childSnapshot.getValue(String.class);
                        Messages messages = (Messages) dataSnapshot.getValue(Messages.class);
                        i++;
                        Log.i("Jimit", messages.getMessage() + "  "+ String.valueOf(i));
                    }


                    if (i < TOTAL_ITEM_TO_LOAD) {
                        Totalpage = 1;
                    } else {
                        if ((int) (i % TOTAL_ITEM_TO_LOAD) == 0) {
                            Totalpage = (int) (i / TOTAL_ITEM_TO_LOAD);
                        } else {
                            Totalpage = (int) (i / TOTAL_ITEM_TO_LOAD) + 1;
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            messageQuery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Messages messages = (Messages) dataSnapshot.getValue(Messages.class);

                    itemPos++;

                    if (itemPos == 1) {
                        String mMessageKey = dataSnapshot.getKey();

                        mLastKey = mMessageKey;
                        mPrevKey = mMessageKey;
                    }

                    progressBar.setVisibility(View.GONE);

                    messagesList.add(messages);
                    mMessageAdapter.notifyDataSetChanged();

                    mMessagesList.scrollToPosition(messagesList.size() - 1);

                  //  mSwipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //---ON REFRESHING 10 MORE MESSAGES WILL LOAD----
    private void loadMoreMessages() {

        DatabaseReference messageRef = mRootReference.child("messages").child(mCurrentUserId).child(mChatUser);
        Query messageQuery = messageRef.orderByKey().endAt(mLastKey).limitToLast(25);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Messages message = (Messages) dataSnapshot.getValue(Messages.class);
                String messageKey = dataSnapshot.getKey();


                if (!mPrevKey.equals(messageKey)) {
                    messagesList.add(itemPos++, message);
                   // mMessageAdapter.notifyItemRangeChanged(0, mMessageAdapter.getItemCount());


                } else {
                    mPrevKey = mLastKey;
                }

                if (itemPos == 1) {
                    String mMessageKey = dataSnapshot.getKey();
                    mLastKey = mMessageKey;
                }

                progressBar.setVisibility(View.GONE);
                mMessageAdapter.notifyDataSetChanged();

              //  mSwipeRefreshLayout.setRefreshing(false);

              //  mLinearLayoutManager.scrollToPositionWithOffset(10, 0);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //---THIS FUNCTION IS CALLED WHEN SYSTEM ACTIVITY IS CALLED---
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //---FOR PICKING IMAGE FROM GALLERY ACTIVITY AND SENDING---
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            //---GETTING IMAGE DATA IN FORM OF URI--
            Uri imageUri = data.getData();
            showMediaialouge(this, imageUri, "image", data);


        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            // Uri imageUri /*= data.getData()*/;

            //String compressedImagePath = compressImage(mCurrentPhotoPath);
            // Uri imageUri = Uri.fromFile(new File(compressedImagePath));

            showMediaialouge(this, imageUri, "image", data);


        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURE) {
            uri = data.getData();

            showMediaialouge(this, uri, "video", data);


        } else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_FILE) {

            uri = data.getData();
            showMediaialouge(this, uri, "videofile", data);

           /* ArrayList<String> supportedMedia = new ArrayList<String>();

            supportedMedia.add(".mp4");
            supportedMedia.add(".mov");
            supportedMedia.add(".3gp");


            videoUrl = ScalingUtilities.getPath(ChatActivity.this, data.getData());
            videoFile = new File(videoUrl);
            file = new File(videoUrl);
            progressBar.setVisibility(View.VISIBLE);

            String fileExtnesion = videoUrl.substring(videoUrl.lastIndexOf("."));

            if (supportedMedia.contains(fileExtnesion)) {


                long file_size = Integer.parseInt(String.valueOf(videoFile.length()));


                try {
                    MediaPlayer mplayer = new MediaPlayer();
                    mplayer.reset();
                    mplayer.setDataSource(videoUrl);
                    mplayer.prepare();

                    long totalFileDuration = mplayer.getDuration();
                    Log.i("android", "data is " + totalFileDuration);

                    int sec = (int) ((totalFileDuration / (1000)));

                    Log.i("android", "data is " + sec);

                    Bitmap b = ThumbnailUtils.createVideoThumbnail(videoUrl, MediaStore.Video.Thumbnails.MINI_KIND);

                            final String thumbImage  = BitMapToString((b));

                    file = videoFile;
                    Uri video = Uri.parse(videoUrl);
                    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                    mediaMetadataRetriever.setDataSource(videoUrl);


                    pathToStoredVideo = getRealPathFromURIPathVideo(video, ChatActivity.this);
                    Log.d("video", "Recorded Video Path " + pathToStoredVideo);




                    Toast.makeText(ChatActivity.this, "Video selected",
                            Toast.LENGTH_SHORT).show();

                    final String current_user_ref = "messages/" + mCurrentUserId + "/" + mChatUser;
                    final String chat_user_ref = "messages/" + mChatUser + "/" + mCurrentUserId;

                    DatabaseReference user_message_push = mRootReference.child("messages")
                            .child(mCurrentUserId).child(mChatUser).push();

                    final String push_id = user_message_push.getKey();



// StorageReference
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    final StorageReference photoRef = *//*storageRef.child("message_video").child(".mp4");*//*mImageStorage.child("message_videos").child(push_id + ".mp4");
// add File/URI
                    photoRef.putFile(Uri.fromFile(file))
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Upload succeeded
                                    Toast.makeText(getApplicationContext(), "Upload Success...", Toast.LENGTH_SHORT).show();

                                    progressBar.setVisibility(View.GONE);

                                    // String download_url = taskSnapshot.getResult().getDownloadUrl().toString();
                                    String download_url = taskSnapshot.getDownloadUrl().toString();
                                    Map messageMap = new HashMap();
                                    messageMap.put("message", download_url);
                                    messageMap.put("seen", false);
                                    messageMap.put("type", "video");
                                    messageMap.put("time", ServerValue.TIMESTAMP);
                                    messageMap.put("from", mCurrentUserId);
                                    messageMap.put("thumb_img", thumbImage);


                                    Map messageUserMap = new HashMap();
                                    messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
                                    messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

                                    mRootReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {

                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if (databaseError != null) {
                                                Log.e("CHAT_ACTIVITY", "Cannot add message to database");
                                            } else {
                                                Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                                                mMessageView.setText("");
                                            }

                                        }
                                    });
                                }


                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Upload failed
                                    Toast.makeText(getApplicationContext(), "Upload failed...", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    //calculating progress percentage
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                    //displaying percentage in progress dialog
                                    //progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                                }
                            });



                    //}
                } catch (Exception e) {


                    Log.i("android", "exception is " + e.getLocalizedMessage() + " " + e.getStackTrace());

                }


            } else {


                Toast.makeText(ChatActivity.this, "Only .mp4,.mov,.3gp File formats allowed ", Toast.LENGTH_SHORT).show();

            }*/
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        //mDatabaseReference.child(mCurrentUserId).child("online").setValue("true");
    }

    @Override
    protected void onStop() {
        super.onStop();
        // mDatabaseReference.child(mCurrentUserId).child("online").setValue(ServerValue.TIMESTAMP);

    }

    //Choose Share option
    private void selectOption() {
        final CharSequence[] items = {"Image From gallery", "Image From camera", "Video from gallery", "Video From camera", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Image/video");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(ChatActivity.this);

                if (items[item].equals("Image From gallery")) {
                    userChoosenTask = "Image From gallery";
                    Intent galleryIntent = new Intent();
                    galleryIntent.setType("image/*");
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), GALLERY_PICK);
                        /*Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        //  videoCaptureIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
//                        videoCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); //0 means low & 1 means high
                        if (videoCaptureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(videoCaptureIntent, REQUEST_VIDEO_CAPTURE);
                        }*/

                } else if (items[item].equals("Image From camera")) {

                    userChoosenTask = "Image From camera";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // Android M Permission check
                        if (ChatActivity.this.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED && ChatActivity.this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                            final String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
                            final String[] permissionswrite = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"};
                            ActivityCompat.requestPermissions(ChatActivity.this, permissionswrite, 0);
                            ActivityCompat.requestPermissions(ChatActivity.this, permissions, 0);
                        } else {

                            cameraTask();

                        }

                    } else {
                        cameraTask();

                    }

                } else if (items[item].equals("Video from gallery")) {

                    userChoosenTask = "Video from gallery";
                    if (result)
                        videogalleryIntent();

                } else if (items[item].equals("Video From camera")) {

                    if (result) {
                        userChoosenTask = "Video From camera";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            // Android M Permission check
                            if (ChatActivity.this.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED
                                    && ChatActivity.this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                                final String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
                                final String[] permissionswrite = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"};
                                ActivityCompat.requestPermissions(ChatActivity.this, permissionswrite, 0);
                                ActivityCompat.requestPermissions(ChatActivity.this, permissions, 0);
                            } else {
                                if (ChatActivity.this.checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    final String[] permissions = new String[]{android.Manifest.permission.CAMERA};
                                    ActivityCompat.requestPermissions(ChatActivity.this,
                                            permissions, REQUEST_TAKE_PHOTO);
                                } else {


                                    Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                                    if (videoCaptureIntent.resolveActivity(getPackageManager()) != null) {
                                        startActivityForResult(videoCaptureIntent, REQUEST_VIDEO_CAPTURE);
                                    }
                                }

                            }

                        } else {
                            Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                            if (videoCaptureIntent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(videoCaptureIntent, REQUEST_VIDEO_CAPTURE);
                            }

                        }

                    }

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void videogalleryIntent() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"video/*"});
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    public void cameraTask() {


        Log.i("android", "startStorage");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            // Android M Permission check
            if (this.checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                final String[] permissions = new String[]{android.Manifest.permission.CAMERA};
                ActivityCompat.requestPermissions(ChatActivity.this,
                        permissions, REQUEST_TAKE_PHOTO);
            } else {

                //startCamera();
                //  captureImage();
                dispatchTakePictureIntent();

            }

        } else {
            //startCamera();
            // captureImage();
            dispatchTakePictureIntent();

        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go
            file = null;
            try {
                file = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }

            // Continue only if the File was successfully created
            if (file != null) {
                /*Uri photoURI*/
                imageUri = FileProvider.getUriForFile(this,
                        "com.procialize.eventapp.android.fileprovider",
                        file);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
               /* String nomeFoto = DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString();
                filePathImageCamera = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), nomeFoto+"camera.jpg");
                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.singhkshitiz.letschat.android.fileprovider",
                        file);
                it.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                startActivityForResult(it, REQUEST_TAKE_PHOTO);*/
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private String getRealPathFromURIImage(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null,
                null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURIImage(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

        // by setting this field as true, the actual bitmap pixels are not
        // loaded in the memory. Just the bounds are loaded. If
        // you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        // max Height and width values of the compressed image is taken as
        // 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        // width and height values are set maintaining the aspect ratio of the
        // image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        // setting inSampleSize value allows to load a scaled down version of
        // the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

        // inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

        // this options allow android to claim the bitmap memory if it runs low
        // on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            // load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
                    Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
                middleY - bmp.getHeight() / 2, new Paint(
                        Paint.FILTER_BITMAP_FLAG));

        // check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();

        try {
            out = new FileOutputStream(filename);

            // write the compressed bitmap at the destination specified by
            // filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory()
                .getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/"
                + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    public int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private String getRealPathFromURIPathVideo(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        JzvdStd.releaseAllVideos();
        Log.v("MyApp", "onDestroy");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraTask();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //profileActivityViewModel.cameraIntent(ProfileActivity.this);
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                       /* Intent intent = getIntent();
                        CommonFunction.saveBackgroundImage(ChatActivity.this, intent.getStringExtra("eventBg"));
                        CommonFunction.showBackgroundImage(this, ll_main);*/
                    } catch (Exception e) {
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {

        JzvdStd.releaseAllVideos();
        EventAppDB.getDatabase(getApplicationContext()).attendeeChatDao().updateIsReadZero( firebase_id);

        finish();

        KeyboardUtility.hideSoftKeyboard(this);

    }


    private void showMediaialouge(Context context, final Uri url, final String type, final Intent data) {

        // myDialog = new Dialog(this);
        myDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        myDialog.setContentView(R.layout.dialog_chat_item);
        myDialog.setCancelable(false);
        // myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id

        myDialog.show();

        Button cancelbtn = myDialog.findViewById(R.id.canclebtn);
        final ImageView chatSendButton = myDialog.findViewById(R.id.chatSendButton);
        LinearLayout ll_main = myDialog.findViewById(R.id.ll_main);
        TextView title = myDialog.findViewById(R.id.title);
        TouchImageView imageView = myDialog.findViewById(R.id.imageView);
        JzvdStd videoplayer = myDialog.findViewById(R.id.videoplayer);
        ImageView imgCancel = myDialog.findViewById(R.id.imgCancel);
        final ProgressBar progessLoad = myDialog.findViewById(R.id.progessLoad);
        final LinearLayout linSend = myDialog.findViewById(R.id.linSend);

        ImageView imgback = myDialog.findViewById(R.id.imgback);

        ll_main.setBackgroundColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_2)));
        //title.setTextColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_3)));

        cancelbtn.setTextColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_1)));

        // imgCancel.setColorFilter(Color.parseColor(SharedPreference.getPref(context,EVENT_COLOR_4)), PorterDuff.Mode.SRC_ATOP);
        linSend.setBackgroundColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_4)));

        chatSendButton.setColorFilter(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_2)), PorterDuff.Mode.SRC_ATOP);


        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.hideKeyboard(v);
                myDialog.dismiss();
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.hideKeyboard(v);
                myDialog.dismiss();
            }
        });
        if (type.equalsIgnoreCase("image")) {
            videoplayer.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            Glide.with(this).load((url))
                    .placeholder(R.drawable.gallery_placeholder)
                    .apply(RequestOptions.skipMemoryCacheOf(false))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).fitCenter()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(imageView);
        } else if (type.equalsIgnoreCase("video")) {
            videoplayer.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            String uri_to_string;
            uri_to_string = url.toString();
            videoplayer.setUp(ScalingUtilities.getPath(ChatActivity.this, data.getData()), ""
                    , JzvdStd.SCREEN_NORMAL);
            JzvdStd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);


            Glide.with(this)
                    .asBitmap()
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(videoplayer.thumbImageView);


        } else if (type.equalsIgnoreCase("videofile")) {
            videoplayer.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            String uri_to_string;
            uri_to_string = url.toString();
            videoplayer.setUp(ScalingUtilities.getPath(ChatActivity.this, data.getData()), ""
                    , JzvdStd.SCREEN_NORMAL);
            JzvdStd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);


            Glide.with(this)
                    .asBitmap()
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(videoplayer.thumbImageView);


        }


        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.hideKeyboard(v);
                myDialog.dismiss();
            }
        });

        linSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                progessLoad.setVisibility(View.VISIBLE);
                chatSendButton.setEnabled(false);
                chatSendButton.setClickable(false);
                linSend.setEnabled(false);
                linSend.setClickable(false);

                if (type.equalsIgnoreCase("image")) {
                    final String current_user_ref = "messages/" + mCurrentUserId + "/" + mChatUser;
                    final String chat_user_ref = "messages/" + mChatUser + "/" + mCurrentUserId;

                    DatabaseReference user_message_push = mRootReference.child("messages")
                            .child(mCurrentUserId).child(mChatUser).push();

                    final String push_id = user_message_push.getKey();

                    //---PUSHING IMAGE INTO STORAGE---
                    StorageReference filepath = mImageStorage.child("message_images").child(push_id + ".jpg");
                    filepath.putFile(url).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if (task.isSuccessful()) {

                                @SuppressWarnings("VisibleForTests") final String download_url = task.getResult().getDownloadUrl().toString();

                                Map messageMap = new HashMap();
                                messageMap.put("message", download_url);
                                messageMap.put("seen", false);
                                messageMap.put("type", "image");
                                messageMap.put("time", ServerValue.TIMESTAMP);
                                messageMap.put("from", mCurrentUserId);

                                Map messageUserMap = new HashMap();
                                messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
                                messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

                                mRootReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {

                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if (databaseError != null) {
                                            progessLoad.setVisibility(View.GONE);

                                            Log.e("CHAT_ACTIVITY", "Cannot add message to database");
                                        } else {
                                            progessLoad.setVisibility(View.GONE);

                                            Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                                            mMessageView.setText("");
                                            myDialog.dismiss();

                                            // Check for new messages
                                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                                            //Send Text Notification
                                            TOPIC = "/topics/userABC"; //topic has to match what the receiver subscribed to
                                            NOTIFICATION_TITLE = firebase_id + "@"+currentUser.getUid();
                                            NOTIFICATION_MESSAGE = "image";

                                            JSONObject notification = new JSONObject();
                                            JSONObject notifcationBody = new JSONObject();
                                            try {
                                                notifcationBody.put("title", NOTIFICATION_TITLE);
                                                notifcationBody.put("message", NOTIFICATION_MESSAGE);
                                                notifcationBody.put("titleMain", loginUser+" sent you an image");

                                                notifcationBody.put("image", download_url);

                                                notification.put("to", TOPIC);
                                                notification.put("data", notifcationBody);
                                            } catch (JSONException e) {
                                                Log.e(TAG, "onCreate: " + e.getMessage() );
                                            }
                                            sendNotification(notification);
                                        }

                                    }
                                });
                            }

                        }
                    });
                } else if (type.equalsIgnoreCase("video")) {
                    ArrayList<String> supportedMedia = new ArrayList<String>();
                    progessLoad.setVisibility(View.VISIBLE);

                    supportedMedia.add(".mp4");
                    supportedMedia.add(".mov");
                    supportedMedia.add(".3gp");


                    videoUrl = ScalingUtilities.getPath(ChatActivity.this, data.getData());
//                    pathToStoredVideo = getRealPathFromURIPathVideo(uri, ChatActivity.this);
                    videoFile = new File(videoUrl);
                    file = new File(videoUrl);

                    String fileExtnesion = videoUrl.substring(videoUrl.lastIndexOf("."));

                    if (supportedMedia.contains(fileExtnesion)) {


                        long file_size = Integer.parseInt(String.valueOf(videoFile.length()));
//                long fileMb = AudioPost.bytesToMeg(file_size);

                        try {
                            MediaPlayer mplayer = new MediaPlayer();
                            mplayer.reset();
                            mplayer.setDataSource(videoUrl);
                            mplayer.prepare();

                            long totalFileDuration = mplayer.getDuration();
                            Log.i("android", "data is " + totalFileDuration);

                            int sec = (int) ((totalFileDuration / (1000)));

                            Log.i("android", "data is " + sec);

                            Bitmap b = ThumbnailUtils.createVideoThumbnail(videoUrl, MediaStore.Video.Thumbnails.MINI_KIND);

                            file = videoFile;

                            if (sec > 120) {
                                Toast.makeText(ChatActivity.this, "Select an video not more than 2 minutes",
                                        Toast.LENGTH_SHORT).show();
                                // finish();
                            } else {
                                progessLoad.setVisibility(View.VISIBLE);

                                pathToStoredVideo = getRealPathFromURIPathVideo(uri, ChatActivity.this);
                                Log.d("video", "Recorded Video Path " + pathToStoredVideo);

                                //Store the video to your server
                                file = new File(pathToStoredVideo);


                                final String current_user_ref = "messages/" + mCurrentUserId + "/" + mChatUser;
                                final String chat_user_ref = "messages/" + mChatUser + "/" + mCurrentUserId;

                                DatabaseReference user_message_push = mRootReference.child("messages")
                                        .child(mCurrentUserId).child(mChatUser).push();

                                final String push_id = user_message_push.getKey();

                                //
// StorageReference
                                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                final StorageReference photoRef = mImageStorage.child("message_videos").child(push_id + ".mp4");
// add File/URI
                                photoRef.putFile(Uri.fromFile(file))
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                // Upload succeeded
                                                Toast.makeText(getApplicationContext(), "Upload Success...", Toast.LENGTH_SHORT).show();
                                                // String download_url = taskSnapshot.getResult().getDownloadUrl().toString();

                                                progessLoad.setVisibility(View.GONE);

                                                final String download_url = taskSnapshot.getDownloadUrl().toString();
                                                Map messageMap = new HashMap();
                                                messageMap.put("message", download_url);
                                                messageMap.put("seen", false);
                                                messageMap.put("type", "video");
                                                messageMap.put("time", ServerValue.TIMESTAMP);
                                                messageMap.put("from", mCurrentUserId);
                                                // messageMap.put("thumb_img", thumbImage);


                                                Map messageUserMap = new HashMap();
                                                messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
                                                messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

                                                mRootReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {

                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                        if (databaseError != null) {
                                                            Log.e("CHAT_ACTIVITY", "Cannot add message to database");
                                                            myDialog.dismiss();
                                                            progessLoad.setVisibility(View.GONE);

                                                        } else {
                                                            Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                                                            mMessageView.setText("");
                                                            myDialog.dismiss();
                                                            progessLoad.setVisibility(View.GONE);

                                                            // Check for new messages
                                                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                                                            //Send Text Notification
                                                            TOPIC = "/topics/userABC"; //topic has to match what the receiver subscribed to
                                                            NOTIFICATION_TITLE = firebase_id + "@"+currentUser.getUid();
                                                            NOTIFICATION_MESSAGE = "Image";

                                                            JSONObject notification = new JSONObject();
                                                            JSONObject notifcationBody = new JSONObject();
                                                            try {
                                                                notifcationBody.put("title", NOTIFICATION_TITLE);
                                                                notifcationBody.put("message", NOTIFICATION_MESSAGE);
                                                                notifcationBody.put("titleMain", loginUser+" sent you a video");
                                                                notifcationBody.put("image", download_url);


                                                                notification.put("to", TOPIC);
                                                                notification.put("data", notifcationBody);
                                                            } catch (JSONException e) {
                                                                Log.e(TAG, "onCreate: " + e.getMessage() );
                                                            }
                                                            sendNotification(notification);

                                                        }

                                                    }
                                                });
                                            }


                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Upload failed
                                                Toast.makeText(getApplicationContext(), "Upload failed...", Toast.LENGTH_SHORT).show();
                                                progessLoad.setVisibility(View.GONE);

                                            }
                                        }).addOnProgressListener(
                                        new OnProgressListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                //calculating progress percentage
                                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                            }
                                        });

                                Toast.makeText(ChatActivity.this, "Video selected",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {

                            progessLoad.setVisibility(View.GONE);

                            Log.i("android", "exception is " + e.getLocalizedMessage() + " " + e.getStackTrace());

                        }


                    } else {

                        progessLoad.setVisibility(View.GONE);

                        Toast.makeText(ChatActivity.this, "Only .mp4,.mov,.3gp File formats allowed ", Toast.LENGTH_SHORT).show();

                    }
                } else if (type.equalsIgnoreCase("videofile")) {
                    ArrayList<String> supportedMedia = new ArrayList<String>();

                    supportedMedia.add(".mp4");
                    supportedMedia.add(".mov");
                    supportedMedia.add(".3gp");


                    videoUrl = ScalingUtilities.getPath(ChatActivity.this, data.getData());
                    videoFile = new File(videoUrl);
                    file = new File(videoUrl);
                    progressBar.setVisibility(View.VISIBLE);

                    String fileExtnesion = videoUrl.substring(videoUrl.lastIndexOf("."));

                    if (supportedMedia.contains(fileExtnesion)) {


                        long file_size = Integer.parseInt(String.valueOf(videoFile.length()));


                        try {
                            MediaPlayer mplayer = new MediaPlayer();
                            mplayer.reset();
                            mplayer.setDataSource(videoUrl);
                            mplayer.prepare();

                            long totalFileDuration = mplayer.getDuration();
                            Log.i("android", "data is " + totalFileDuration);

                            int sec = (int) ((totalFileDuration / (1000)));

                            Log.i("android", "data is " + sec);

                            Bitmap b = ThumbnailUtils.createVideoThumbnail(videoUrl, MediaStore.Video.Thumbnails.MINI_KIND);

                            final String thumbImage = BitMapToString((b));

                            file = videoFile;
                            Uri video = Uri.parse(videoUrl);
                            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                            mediaMetadataRetriever.setDataSource(videoUrl);


                            pathToStoredVideo = getRealPathFromURIPathVideo(video, ChatActivity.this);
                            Log.d("video", "Recorded Video Path " + pathToStoredVideo);


                            Toast.makeText(ChatActivity.this, "Video selected",
                                    Toast.LENGTH_SHORT).show();

                            final String current_user_ref = "messages/" + mCurrentUserId + "/" + mChatUser;
                            final String chat_user_ref = "messages/" + mChatUser + "/" + mCurrentUserId;

                            DatabaseReference user_message_push = mRootReference.child("messages")
                                    .child(mCurrentUserId).child(mChatUser).push();

                            final String push_id = user_message_push.getKey();


// StorageReference
                            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                            final StorageReference photoRef = /*storageRef.child("message_video").child(".mp4");*/mImageStorage.child("message_videos").child(push_id + ".mp4");
// add File/URI
                            photoRef.putFile(Uri.fromFile(file))
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            // Upload succeeded
                                            //Toast.makeText(getApplicationContext(), "Upload Success...", Toast.LENGTH_SHORT).show();


                                            // String download_url = taskSnapshot.getResult().getDownloadUrl().toString();
                                            final String download_url = taskSnapshot.getDownloadUrl().toString();
                                            Map messageMap = new HashMap();
                                            messageMap.put("message", download_url);
                                            messageMap.put("seen", false);
                                            messageMap.put("type", "video");
                                            messageMap.put("time", ServerValue.TIMESTAMP);
                                            messageMap.put("from", mCurrentUserId);
                                            messageMap.put("thumb_img", thumbImage);


                                            Map messageUserMap = new HashMap();
                                            messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
                                            messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

                                            mRootReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {

                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    if (databaseError != null) {
                                                        Log.e("CHAT_ACTIVITY", "Cannot add message to database");
                                                    } else {
                                                        Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                                                        mMessageView.setText("");
                                                        progessLoad.setVisibility(View.GONE);
                                                        myDialog.dismiss();

                                                        // Check for new messages
                                                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                                                        //Send Text Notification
                                                        TOPIC = "/topics/userABC"; //topic has to match what the receiver subscribed to
                                                        NOTIFICATION_TITLE = firebase_id + "@"+currentUser.getUid();
                                                        NOTIFICATION_MESSAGE = "Image";

                                                        JSONObject notification = new JSONObject();
                                                        JSONObject notifcationBody = new JSONObject();
                                                        try {
                                                            notifcationBody.put("title", NOTIFICATION_TITLE);
                                                            notifcationBody.put("message", NOTIFICATION_MESSAGE);
                                                            notifcationBody.put("titleMain", loginUser+" sent you a video");
                                                            notifcationBody.put("image", download_url);

                                                            notification.put("to", TOPIC);
                                                            notification.put("data", notifcationBody);
                                                        } catch (JSONException e) {
                                                            Log.e(TAG, "onCreate: " + e.getMessage() );
                                                        }
                                                        sendNotification(notification);
                                                    }

                                                }
                                            });
                                        }


                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Upload failed
                                            Toast.makeText(getApplicationContext(), "Upload failed...", Toast.LENGTH_SHORT).show();
                                            progessLoad.setVisibility(View.GONE);
                                            myDialog.dismiss();
                                        }
                                    }).addOnProgressListener(
                                    new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                            //calculating progress percentage
                                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                            //displaying percentage in progress dialog
                                            //progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                                        }
                                    });


                            //}
                        } catch (Exception e) {


                            Log.i("android", "exception is " + e.getLocalizedMessage() + " " + e.getStackTrace());
                            progessLoad.setVisibility(View.GONE);
                            myDialog.dismiss();

                        }


                    } else {


                        Toast.makeText(ChatActivity.this, "Only .mp4,.mov,.3gp File formats allowed ", Toast.LENGTH_SHORT).show();
                        progessLoad.setVisibility(View.GONE);
                        myDialog.dismiss();

                    }
                }


            }
        });
    }
    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChatActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
    @Override
    protected void onResume() {

        super.onResume();
        backImage.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);

        List<Table_Attendee_Chatcount> attenChatCount = EventAppDB.getDatabase(getApplicationContext()).attendeeChatDao().getSingleAttendee(firebase_id);
        if (attenChatCount.size() > 0) {
            EventAppDB.getDatabase(getApplicationContext()).attendeeChatDao().updateIsRead( firebase_id);
            EventAppDB.getDatabase(this).attendeeChatDao().updateChatCount(0, firebase_id);


        } else {
            Table_Attendee_Chatcount attChat = new Table_Attendee_Chatcount();
            attChat.setChatCount_receId(firebase_id);
            attChat.setChat_count(1);
            attChat.setChat_count_read("1");
            attChat.setChat_mess("");
            EventAppDB.getDatabase(getApplicationContext()).attendeeChatDao().insertAttendee(attChat);
        }
    }

    @Override
    protected void onPause() {

        super.onPause();
        EventAppDB.getDatabase(getApplicationContext()).attendeeChatDao().updateIsReadZero( firebase_id);

    }


}


