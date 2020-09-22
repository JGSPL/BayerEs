package com.procialize.eventapp.ui.attendeeChat;

import android.app.Activity;
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
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.costumTools.ScalingUtilities;
import com.procialize.eventapp.ui.attendee.view.AttendeeDetailActivity;
import com.procialize.eventapp.ui.attendeeChat.activity.AttendeeChatDetail;
import com.procialize.eventapp.ui.attendeeChat.adapter.MessageAdapter;
import com.procialize.eventapp.ui.attendeeChat.model.Messages;
import com.procialize.eventapp.ui.profile.view.ProfileActivity;
import com.squareup.picasso.Picasso;

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

import cn.jzvd.JzvdStd;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.eventapp.Utility.Utility.MY_PERMISSIONS_REQUEST_CAMERA;
import static com.procialize.eventapp.Utility.Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
import static com.procialize.eventapp.Utility.Utility.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;


public class ChatActivity extends AppCompatActivity {

    private String mChatUser;
    TextView mUserName;
    TextView mUserLastSeen;
    CircleImageView mUserImage;
    private FirebaseAuth mAuth;

    String mCurrentUserId;

    DatabaseReference mDatabaseReference;
    private DatabaseReference mRootReference;

    private ImageView mChatSendButton,mChatAddButton;
    private EditText mMessageView;

    private RecyclerView mMessagesList;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private MessageAdapter mMessageAdapter;

    public static final int TOTAL_ITEM_TO_LOAD = 10;
    private int mCurrentPage = 1;

    //Solution for descending list on refresh
    private int itemPos = 0;
    private String mLastKey="";
    private String mPrevKey="";

    private static final int GALLERY_PICK=1;
    StorageReference mImageStorage;

    String userChoosenTask;
    int SELECT_FILE = 3, REQUEST_TAKE_PHOTO = 2, REQUEST_VIDEO_CAPTURE = 4 ;
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
    String  lname, company, city, designation,  attendee_type,mobile,email,attendeeid,firebase_id,firstMessage, page;
    LinearLayout lineaeSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        mChatAddButton = (ImageView)findViewById(R.id.chatAddButton);
        mChatSendButton = (ImageView)findViewById(R.id.chatSendButton);
        lineaeSend = findViewById(R.id.lineaeSend);
        mMessageView = (EditText)findViewById(R.id.chatMessageView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        //-----GETING FROM INTENT----
        mChatUser = getIntent().getStringExtra("user_id");
        final String userName = getIntent().getStringExtra("user_name");
        String loginUser_name = getIntent().getStringExtra("loginUser_name");
        String sProfilepic = getIntent().getStringExtra("sProfilepic");
        final String prof_pic = getIntent().getStringExtra("rProfilepic");

        Intent intent = getIntent();
        lname = intent.getStringExtra("lname");
        company = intent.getStringExtra("company");
        city = intent.getStringExtra("city");
        designation = intent.getStringExtra("designation");
        attendee_type = intent.getStringExtra("attendee_type");
        mobile = intent.getStringExtra("mobile");
        email = intent.getStringExtra("email");
        attendeeid = intent.getStringExtra("attendeeid");
        firebase_id = intent.getStringExtra("firebase_id");
        firstMessage = intent.getStringExtra("Message");
        page = intent.getStringExtra("page");

        //---SETTING ONLINE------
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        //----ADDING ACTION BAR-----

        //---INFLATING APP BAR LAYOUT INTO ACTION BAR----


        View actionBarView = findViewById( R.id.headerlayout ); // root View id from that link

        //---ADDING DATA ON ACTION BAR----
        mUserName=(TextView) actionBarView.findViewById(R.id.textView3);
        mUserLastSeen = (TextView) actionBarView.findViewById(R.id.textView5);
        mUserImage = (CircleImageView) actionBarView.findViewById(R.id.circleImageView);
        LinearLayout linBack = actionBarView.findViewById(R.id.linBack);
        ImageView ivattDetail = actionBarView.findViewById(R.id.ivattDetail);
        mUserName.setText(userName);
        mUserLastSeen.setText(designation + " - "+ city);

        mUserName.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_1)));
        lineaeSend.setBackgroundColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_1)));
        int color = Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_1));
        mChatAddButton.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        mChatSendButton.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)), PorterDuff.Mode.SRC_ATOP);

        Picasso.with(ChatActivity.this).load(prof_pic).placeholder(R.drawable.profilepic_placeholder).into(mUserImage);
        linBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivattDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, AttendeeChatDetail.class);
                intent.putExtra("attendeeid",attendeeid);
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

        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();

        mMessageAdapter = new MessageAdapter(messagesList, userName,loginUser_name,sProfilepic,prof_pic);

        mMessagesList = (RecyclerView)findViewById(R.id.recycleViewMessageList);
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.message_swipe_layout);
        mLinearLayoutManager = new LinearLayoutManager(ChatActivity.this);

       // mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayoutManager);
        mMessagesList.setAdapter(mMessageAdapter);
        if(messagesList.size()==0){
            progressBar.setVisibility(View.GONE);
        }

        loadMessages();

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

                if(!dataSnapshot.hasChild(mChatUser)){

                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen",false);
                    chatAddMap.put("time_stamp", ServerValue.TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("chats/"+mChatUser+"/"+mCurrentUserId,chatAddMap);
                    chatUserMap.put("chats/"+mCurrentUserId+"/"+mChatUser,chatAddMap);

                    mRootReference.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError == null){
                                Toast.makeText(getApplicationContext(), "Successfully Added chats feature", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Cannot Add chats feature", Toast.LENGTH_SHORT).show();
                        }


                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Something went wrong.. Please go back..", Toast.LENGTH_SHORT).show();
            }
        });

        if(page.equalsIgnoreCase("AttendeeDetail")){
            if(firstMessage!=null){
                String current_user_ref = "messages/"+mCurrentUserId+"/"+mChatUser;
                String chat_user_ref = "messages/"+ mChatUser +"/"+mCurrentUserId;

                DatabaseReference user_message_push = mRootReference.child("messages")
                        .child(mCurrentUserId).child(mChatUser).push();

                String push_id = user_message_push.getKey();

                Map messageMap = new HashMap();
                messageMap.put("message",firstMessage);
                messageMap.put("seen",false);
                messageMap.put("type","text");
                messageMap.put("time", ServerValue.TIMESTAMP);
                messageMap.put("from",mCurrentUserId);

                Map messageUserMap = new HashMap();
                messageUserMap.put(current_user_ref+"/"+push_id,messageMap);
                messageUserMap.put(chat_user_ref+"/"+push_id,messageMap);

                mRootReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener(){

                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError != null){
                            Log.e("CHAT_ACTIVITY","Cannot add message to database");
                        }
                        else{
                            Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                            mMessageView.setText("");
                        }

                    }
                });


            }
        }

        //----SEND MESSAGE--BUTTON----

        mChatSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = mMessageView.getText().toString();
                mMessageView.setText("");
                if(!TextUtils.isEmpty(message)){

                   String current_user_ref = "messages/"+mCurrentUserId+"/"+mChatUser;
                    String chat_user_ref = "messages/"+ mChatUser +"/"+mCurrentUserId;

                    DatabaseReference user_message_push = mRootReference.child("messages")
                            .child(mCurrentUserId).child(mChatUser).push();

                    String push_id = user_message_push.getKey();

                    Map messageMap = new HashMap();
                    messageMap.put("message",message);
                    messageMap.put("seen",false);
                    messageMap.put("type","text");
                    messageMap.put("time", ServerValue.TIMESTAMP);
                    messageMap.put("from",mCurrentUserId);

                    Map messageUserMap = new HashMap();
                    messageUserMap.put(current_user_ref+"/"+push_id,messageMap);
                    messageUserMap.put(chat_user_ref+"/"+push_id,messageMap);

                    mRootReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener(){

                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError != null){
                                Log.e("CHAT_ACTIVITY","Cannot add message to database");
                            }
                            else{
                                Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                                mMessageView.setText("");
                            }

                        }
                    });


                    //TODO: Get the ID of the chat the user is taking part in
                    String chatID = push_id;

                    // Check for new messages
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
/*
                    if (currentUser != null){
                        String UID = currentUser.getUid();
                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                        Query query = rootRef.child("message_read_states").child(chatID).orderByChild(UID).equalTo(false);
                        query.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                String messageID = dataSnapshot.getKey();
                                //TODO: Handle Notification here, using the messageID
                                // A datasnapshot received here will be a new message that the user has not read
                                // If you want to display data about the message or chat,
                                // Use the chatID and/or messageID and declare a new
                                // SingleValueEventListener here, and add it to the chat/message DatabaseReference.
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                String messageID = dataSnapshot.getKey();
                                //TODO: Remove the notification
                                // If the user reads the message in the app, before checking the notification
                                // then the notification is no longer relevant, remove it here.
                                // In onChildAdded you could use the messageID(s) to keep track of the notifications
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
*/




                }

            }
        });

        //----THE WRAP CONTENT OF IMAGE VIEW IS GIVING ERROR--- SO REMOVING THIS FUNCTIONALITY-------


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
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                itemPos = 0;
                mCurrentPage++;
                loadMoreMessages();
               /*if(mSwipeRefreshLayout.isRefreshing()==true){
                   mSwipeRefreshLayout.setRefreshing(false);
               }*/
               /* if(messagesListds2.size()>0){
                    messagesList.clear();
                }
                loadMessages();*/


            }
        });

    }

   //---FIRST 10 MESSAGES WILL LOAD ON START----
    private void loadMessages() {

        DatabaseReference messageRef = mRootReference.child("messages").child(mCurrentUserId).child(mChatUser);
        Query messageQuery = messageRef.limitToLast(mCurrentPage*TOTAL_ITEM_TO_LOAD);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Messages messages = (Messages) dataSnapshot.getValue(Messages.class);

                itemPos++;

                if(itemPos == 1){
                    String mMessageKey = dataSnapshot.getKey();

                    mLastKey = mMessageKey;
                    mPrevKey = mMessageKey;
                }

                progressBar.setVisibility(View.GONE);

                messagesList.add(messages);
                mMessageAdapter.notifyDataSetChanged();

                mMessagesList.scrollToPosition(messagesList.size()-1);

                mSwipeRefreshLayout.setRefreshing(false);
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

    //---ON REFRESHING 10 MORE MESSAGES WILL LOAD----
    private void loadMoreMessages() {

        DatabaseReference messageRef = mRootReference.child("messages").child(mCurrentUserId).child(mChatUser);
        Query messageQuery = messageRef.orderByKey().endAt(mLastKey).limitToLast(10);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Messages message = (Messages) dataSnapshot.getValue(Messages.class);
                String messageKey = dataSnapshot.getKey();


                if(!mPrevKey.equals(messageKey)){
                    messagesList.add(itemPos++,message);

                }
                else{
                    mPrevKey = mLastKey;
                }

                if(itemPos == 1){
                    String mMessageKey = dataSnapshot.getKey();
                    mLastKey = mMessageKey;
                }

                progressBar.setVisibility(View.GONE);
                mMessageAdapter.notifyDataSetChanged();

                mSwipeRefreshLayout.setRefreshing(false);

                mLinearLayoutManager.scrollToPositionWithOffset(10,0);
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
        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            //---GETTING IMAGE DATA IN FORM OF URI--
            Uri imageUri = data.getData();
            final String current_user_ref = "messages/"+mCurrentUserId+"/"+mChatUser;
            final String chat_user_ref = "messages/"+ mChatUser +"/"+mCurrentUserId;

            DatabaseReference user_message_push = mRootReference.child("messages")
                    .child(mCurrentUserId).child(mChatUser).push();

            final String push_id = user_message_push.getKey();

            //---PUSHING IMAGE INTO STORAGE---
            StorageReference filepath = mImageStorage.child("message_images").child(push_id+".jpg");
            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if(task.isSuccessful()){

                        @SuppressWarnings("VisibleForTests")
                       String download_url = task.getResult().getDownloadUrl().toString();

                        Map messageMap = new HashMap();
                        messageMap.put("message",download_url);
                        messageMap.put("seen",false);
                        messageMap.put("type","image");
                        messageMap.put("time", ServerValue.TIMESTAMP);
                        messageMap.put("from",mCurrentUserId);

                        Map messageUserMap = new HashMap();
                        messageUserMap.put(current_user_ref+"/"+push_id,messageMap);
                        messageUserMap.put(chat_user_ref+"/"+push_id,messageMap);

                        mRootReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener(){

                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if(databaseError != null){
                                    Log.e("CHAT_ACTIVITY","Cannot add message to database");
                                }
                                else{
                                    Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                                    mMessageView.setText("");
                                }

                            }
                        });
                    }

                }
            });


        }else  if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
           // Uri imageUri /*= data.getData()*/;

            //String compressedImagePath = compressImage(mCurrentPhotoPath);
           // Uri imageUri = Uri.fromFile(new File(compressedImagePath));


            final String current_user_ref = "messages/"+mCurrentUserId+"/"+mChatUser;
            final String chat_user_ref = "messages/"+ mChatUser +"/"+mCurrentUserId;

            DatabaseReference user_message_push = mRootReference.child("messages")
                    .child(mCurrentUserId).child(mChatUser).push();

            final String push_id = user_message_push.getKey();
            progressBar.setVisibility(View.VISIBLE);


            //---PUSHING IMAGE INTO STORAGE---
            StorageReference filepath = mImageStorage.child("message_images").child(push_id+".jpg");
            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if(task.isSuccessful()){
                        progressBar.setVisibility(View.GONE);


                        @SuppressWarnings("VisibleForTests")
                        String download_url = task.getResult().getDownloadUrl().toString();

                        Map messageMap = new HashMap();
                        messageMap.put("message",download_url);
                        messageMap.put("seen",false);
                        messageMap.put("type","image");
                        messageMap.put("time", ServerValue.TIMESTAMP);
                        messageMap.put("from",mCurrentUserId);

                        Map messageUserMap = new HashMap();
                        messageUserMap.put(current_user_ref+"/"+push_id,messageMap);
                        messageUserMap.put(chat_user_ref+"/"+push_id,messageMap);

                        mRootReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener(){

                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if(databaseError != null){
                                    Log.e("CHAT_ACTIVITY","Cannot add message to database");
                                }
                                else{
                                    Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                                    mMessageView.setText("");
                                }

                            }
                        });
                    }

                }
            });
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURE) {
            uri = data.getData();

            ArrayList<String> supportedMedia = new ArrayList<String>();

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

                    if (sec > 15) {
                        Toast.makeText(ChatActivity.this, "Select an video not more than 15 seconds",
                                Toast.LENGTH_SHORT).show();
                       // finish();
                    } else {
                        progressBar.setVisibility(View.VISIBLE);

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
                        final StorageReference photoRef = /*storageRef.child("message_video").child(".mp4");*/mImageStorage.child("message_videos").child(push_id + ".mp4");
// add File/URI
                        photoRef.putFile(Uri.fromFile(file))
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // Upload succeeded
                                        Toast.makeText(getApplicationContext(), "Upload Success...", Toast.LENGTH_SHORT).show();
                                        // String download_url = taskSnapshot.getResult().getDownloadUrl().toString();

                                        progressBar.setVisibility(View.GONE);

                                        String download_url = taskSnapshot.getDownloadUrl().toString();
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

                                    }
                                });

                        Toast.makeText(ChatActivity.this, "Video selected",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {


                    Log.i("android", "exception is " + e.getLocalizedMessage() + " " + e.getStackTrace());

                }


            } else {


                Toast.makeText(ChatActivity.this, "Only .mp4,.mov,.3gp File formats allowed ", Toast.LENGTH_SHORT).show();

            }

           /* uri = data.getData();
            MediaPlayer mp = MediaPlayer.create(this, uri);
            int duration = mp.getDuration();
            mp.release();

            progressBar.setVisibility(View.VISIBLE);


            pathToStoredVideo = getRealPathFromURIPathVideo(uri, ChatActivity.this);
            Log.d("video", "Recorded Video Path " + pathToStoredVideo);

            //Store the video to your server
            file = new File(pathToStoredVideo);

            Bitmap b = ThumbnailUtils.createVideoThumbnail(pathToStoredVideo, MediaStore.Video.Thumbnails.MINI_KIND);

            final String current_user_ref = "messages/" + mCurrentUserId + "/" + mChatUser;
            final String chat_user_ref = "messages/" + mChatUser + "/" + mCurrentUserId;

            DatabaseReference user_message_push = mRootReference.child("messages")
                    .child(mCurrentUserId).child(mChatUser).push();

            final String push_id = user_message_push.getKey();

            //
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
                            // String download_url = taskSnapshot.getResult().getDownloadUrl().toString();

                            progressBar.setVisibility(View.GONE);

                            String download_url = taskSnapshot.getDownloadUrl().toString();
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

                        }
                    });
*/





        }else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_FILE) {

            Uri Videouri = data.getData();
//                displayRecordedVideo.setVideoURI(uri);
//                displayRecordedVideo.start();
            ArrayList<String> supportedMedia = new ArrayList<String>();

            supportedMedia.add(".mp4");
            supportedMedia.add(".mov");
            supportedMedia.add(".3gp");


            videoUrl = ScalingUtilities.getPath(ChatActivity.this, data.getData());
//                    pathToStoredVideo = getRealPathFromURIPathVideo(uri, ChatActivity.this);
            videoFile = new File(videoUrl);
            file = new File(videoUrl);
            progressBar.setVisibility(View.VISIBLE);

            String fileExtnesion = videoUrl.substring(videoUrl.lastIndexOf("."));

            if (supportedMedia.contains(fileExtnesion)) {


                long file_size = Integer.parseInt(String.valueOf(videoFile.length()));
//                long fileMb = AudioPost.bytesToMeg(file_size);


                //if (fileMb >= 16)
                // Toast.makeText(VideoPost.this, "Upload a video not more than 30 MB in size",
                //        Toast.LENGTH_SHORT).show();

                //  else {
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
                    //Store the video to your server
                    // videoview.setMediaController(mediacontrolle);



                    Toast.makeText(ChatActivity.this, "Video selected",
                            Toast.LENGTH_SHORT).show();

                    final String current_user_ref = "messages/" + mCurrentUserId + "/" + mChatUser;
                    final String chat_user_ref = "messages/" + mChatUser + "/" + mCurrentUserId;

                    DatabaseReference user_message_push = mRootReference.child("messages")
                            .child(mCurrentUserId).child(mChatUser).push();

                    final String push_id = user_message_push.getKey();


                    //push video in storage
// Create instance of
//
//
// StorageReference
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    final StorageReference photoRef = /*storageRef.child("message_video").child(".mp4");*/mImageStorage.child("message_videos").child(push_id + ".mp4");
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

            }
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
        final CharSequence[] items = {"Image From gallery", "Image From camera", "Video from gallery", "Video From camera" ,"Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Image/video");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(ChatActivity.this);

                if (items[item].equals("Image From gallery")) {
                        userChoosenTask = "Image From gallery";
                        Intent galleryIntent=new Intent();
                        galleryIntent.setType("image/*");
                        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"),GALLERY_PICK);
                        /*Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        //  videoCaptureIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
//                        videoCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); //0 means low & 1 means high
                        if (videoCaptureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(videoCaptureIntent, REQUEST_VIDEO_CAPTURE);
                        }*/

                }else  if (items[item].equals("Image From camera")) {

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

                }
                else if (items[item].equals("Video from gallery")) {

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
                                final String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE" };
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

                }else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                    finish();
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
                /*Uri photoURI*/imageUri = FileProvider.getUriForFile(this,
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


    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    @Override
    protected void onDestroy()
    {
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



}


 /*
            ActionBar action = getSupportActionBar();
            LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View actionBarView = inflater.inflate(R.layout.app_bar_layout,null);
            action.setCustomView(actionBarView);
        */