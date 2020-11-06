package com.procialize.eventapp.ui.spotgroupChat.view;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.google.firebase.storage.StorageReference;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.Constants.APIService;
import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.Database.EventAppDB;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.SharedPreferencesConstant;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.ui.agenda.model.Agenda;
import com.procialize.eventapp.ui.attendee.model.Attendee;
import com.procialize.eventapp.ui.attendeeChat.ChatActivity;
import com.procialize.eventapp.ui.attendeeChat.activity.AttendeeChatDetail;
import com.procialize.eventapp.ui.attendeeChat.model.Messages;
import com.procialize.eventapp.ui.attendeeChat.roomDb.Table_Attendee_Chatcount;
import com.procialize.eventapp.ui.newsFeedComment.view.CommentActivity;
import com.procialize.eventapp.ui.spotgroupChat.adapter.GroupMessageAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jzvd.JzvdStd;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;

public class SpotGroupChatFragment extends Fragment {
    String api_token, eventid;
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
    private GroupMessageAdapter mMessageAdapter;

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
    Agenda agenda;
    String sessionId;

    String refreshedToken = "";//add your user refresh tokens who are logged in with firebase.

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAA4xkbUmY:APA91bHJtoRFEI_jxvR7jJEIA0M-Wa4adoRiLGWIMiWdAgEg5CLjsJBRuByvHHj-764l5zVRav8N_qwn_etLCzUHsL-xfhJTrQQFSYkHRurjID5haW2TpfZF1JRDw0y4vKBoqVg6Lldb";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;
    ImageView backImage;
    FrameLayout fl_post_comment;
    public static SpotGroupChatFragment newInstance() {

        return new SpotGroupChatFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chat, container, false);

       
        new RefreashToken(getActivity()).callGetRefreashToken(getActivity());
        api_token = SharedPreference.getPref(getActivity(), AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(getActivity(), EVENT_ID);
        mChatAddButton = (ImageView) root.findViewById(R.id.chatAddButton);
        mChatSendButton = (ImageView) root.findViewById(R.id.chatSendButton);
        lineaeSend = root.findViewById(R.id.lineaeSend);
        mMessageView = (EditText) root.findViewById(R.id.chatMessageView);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        cd = ConnectionDetector.getInstance(getContext());
        fl_post_comment = root.findViewById(R.id.fl_post_comment);
        agenda = (Agenda) getArguments().getSerializable("agendaDetails");
        sessionId = agenda.getSession_id();


        //-----GETING FROM INTENT----
        // mChatUser = getIntent().getStringExtra("user_id");
        //final String userName = getIntent().getStringExtra("user_name");
        //String loginUser_name = getIntent().getStringExtra("loginUser_name");
        //String sProfilepic = getIntent().getStringExtra("sProfilepic");
        // final String prof_pic = getIntent().getStringExtra("rProfilepic");

        final RelativeLayout linMain = root.findViewById(R.id.linMain);


        loginUser = SharedPreference.getPref(getContext(), SharedPreferencesConstant.KEY_FNAME);


        //---SETTING ONLINE------
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        //----ADDING ACTION BAR-----

        //---INFLATING APP BAR LAYOUT INTO ACTION BAR----


        View actionBarView = root.findViewById(R.id.headerlayout); // root View id from that link

        //---ADDING DATA ON ACTION BAR----
        mUserName = (TextView) actionBarView.findViewById(R.id.textView3);
        mUserLastSeen = (TextView) actionBarView.findViewById(R.id.textView5);
        mUserImage = (CircleImageView) actionBarView.findViewById(R.id.circleImageView);
        LinearLayout linBack = actionBarView.findViewById(R.id.linBack);
        backImage = actionBarView.findViewById(R.id.backImage);
        backImage.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
        fl_post_comment.setBackgroundColor(Color.parseColor(SharedPreference.getPref(getContext(), EVENT_COLOR_2)));

        int color4 = Color.parseColor(SharedPreference.getPref(getContext(), EVENT_COLOR_4));
        mChatSendButton.setColorFilter(color4, PorterDuff.Mode.SRC_ATOP);
        ImageView ivattDetail = actionBarView.findViewById(R.id.ivattDetail);
        mUserLastSeen.setText(designation + " - " + city);


        mRootReference = FirebaseDatabase.getInstance().getReference();
        mImageStorage = FirebaseStorage.getInstance().getReference();

        try {

            mAuth = FirebaseAuth.getInstance();
            mCurrentUserId = mAuth.getCurrentUser().getUid();
        } catch (Exception e) {

        }

        mMessageAdapter = new GroupMessageAdapter(messagesList/*, userName, prof_pic*/);

        mMessagesList = (RecyclerView) root.findViewById(R.id.recycleViewMessageList);
        //mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.message_swipe_layout);
        mLinearLayoutManager = new LinearLayoutManager(getContext());

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




        //----ADDING SEEN OF MESSAGES----
      /*  mRootReference.child("chatsgroup").child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.hasChild(mChatUser)) {

                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen", false);
                    chatAddMap.put("time_stamp", ServerValue.TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("chatsgroup/"+ mCurrentUserId, chatAddMap);
                //    chatUserMap.put("chats/" + mCurrentUserId + "/" + mChatUser, chatAddMap);

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
                Toast.makeText(getContext(), "Something went wrong.. Please go back..", Toast.LENGTH_SHORT).show();
            }
        });*/


        //----SEND MESSAGE--BUTTON----

        fl_post_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = mMessageView.getText().toString().trim();
                mMessageView.setText("");
                if (!TextUtils.isEmpty(message)) {

                    String current_user_ref = "session"+sessionId+"/" ;
                    //String chat_user_ref = "session"+sessionId+"/" + mChatUser + "/" + mCurrentUserId;

                    DatabaseReference user_message_push = mRootReference.child("session"+sessionId+"/")
                            .push();

                    String push_id = user_message_push.getKey();

                    Map messageMap = new HashMap();
                    messageMap.put("message", message);
                    messageMap.put("seen", false);
                    messageMap.put("type", "text");
                    messageMap.put("time", ServerValue.TIMESTAMP);
                    messageMap.put("from", mCurrentUserId);

                    Map messageUserMap = new HashMap();
                    messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
                    //messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

                    mRootReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {

                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Log.e("CHAT_ACTIVITY", "Cannot add message to database");
                            } else {
                                Toast.makeText(getContext(), "Message sent", Toast.LENGTH_SHORT).show();
                                mMessageView.setText("");
                            }

                        }
                    });

                    //    sendMessage(jsonArray,"Chat Notification",mChatUser +" "+ mCurrentUserId,"Http:\\google.com",message);


                    //TODO: Get the ID of the chat the user is taking part in
                    String chatID = push_id;

                    // Check for new messages
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


                } else {
                    Toast.makeText(getContext(), "Please enter any message", Toast.LENGTH_SHORT).show();

                }

            }
        });



        mChatAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // selectOption();
            }
        });

        //----LOADING 10 MESSAGES ON SWIPE REFRESH----



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






        return root;
    }

    //---FIRST 10 MESSAGES WILL LOAD ON START----
    private void loadMessages() {
        try {
            DatabaseReference messageRef = mRootReference.child("session"+sessionId+"/");
            Query messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEM_TO_LOAD);
            final Query totalmessage = messageRef.orderByKey();

            DatabaseReference ref = mRootReference.child("session"+sessionId+"/");

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

        DatabaseReference messageRef = mRootReference.child("session"+sessionId+"/");
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
}
