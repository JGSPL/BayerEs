package com.procialize.eventapp.ui.spotgroupChat.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.procialize.eventapp.App;
import com.procialize.eventapp.Database.EventAppDB;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.ui.attendee.model.Attendee;
import com.procialize.eventapp.ui.attendee.roomDB.TableAttendee;
import com.procialize.eventapp.ui.attendeeChat.ChatActivity;
import com.procialize.eventapp.ui.attendeeChat.activity.FullScreenImageActivity;
import com.procialize.eventapp.ui.attendeeChat.model.Messages;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_4;

public class GroupMessageAdapter extends RecyclerView.Adapter<GroupMessageAdapter.MessageViewHolder> implements CacheListener {

    private List<Messages> mMessagesList;
    private FirebaseAuth mAuth;
    DatabaseReference mDatabaseReference ;
    Context context;
    String current_user_id, receiverUser, sName, sprofilpic, rprofilPic;
    Bitmap b;
    String date = "";
    ;
    String uName, uImage;
    //-----GETTING LIST OF ALL MESSAGES FROM CHAT ACTIVITY ----
    public GroupMessageAdapter(List<Messages> mMessagesList/*, String receiverUser, String rprofilPic*/) {
        this.mMessagesList = mMessagesList;
        this.receiverUser = receiverUser;
        // this.sName = sName;
        // this.sprofilpic = sprofilpic;
        this.rprofilPic = rprofilPic;

    }


    //---CREATING SINGLE HOLDER AND RETURNING ITS VIEW---
    @Override
    public GroupMessageAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_livestreaming_comments,parent,false);
        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        return new GroupMessageAdapter.MessageViewHolder(view);
    }

    @Override
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
        // Log.d("LOG_TAG", String.format("onCacheAvailable. percents: %d, file: %s, url: %s", percentsAvailable, file, url));

    }

    //----RETURNING VIEW OF SINGLE HOLDER----
    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public TextView displayName;
        public TextView displayTime;
        public ImageView profileImage;
        public ImageView messageImage;

        public TextView messageText2;
        public TextView displayName2;
        public TextView displayTime2;
        public ImageView profileImage2;
        public ImageView messageImage2;
        RelativeLayout innerRl1,innerRl2;
        RelativeLayout messageSingleLayout3, messageSingleLayout2;
        ProgressBar progressBarRight, progressBarLeft;
        public JzvdStd videoplayer2, videoplayer;
        LinearLayout linMain;

        public MessageViewHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.message_text_layout);
            displayName = (TextView)itemView.findViewById(R.id.name_text_layout);
            displayTime = (TextView) itemView.findViewById(R.id.time_text_layout);
            profileImage = (ImageView)itemView.findViewById(R.id.message_profile_layout);
            messageImage = (ImageView)itemView.findViewById(R.id.message_image_layout);

            messageText2 = (TextView) itemView.findViewById(R.id.message_text_layout2);
            displayName2= (TextView)itemView.findViewById(R.id.name_text_layout2);
            displayTime2 = (TextView) itemView.findViewById(R.id.time_text_layout2);
            profileImage2 = (ImageView)itemView.findViewById(R.id.message_profile_layout2);
            messageImage2 = (ImageView)itemView.findViewById(R.id.message_image_layout2);

            messageSingleLayout3 = (RelativeLayout) itemView.findViewById(R.id.messageSingleLayout3);
            messageSingleLayout2 = (RelativeLayout)itemView.findViewById(R.id.messageSingleLayout2);

            innerRl1 = (RelativeLayout)itemView.findViewById(R.id.innerRl1);
            innerRl2 = (RelativeLayout)itemView.findViewById(R.id.innerRl2);


            progressBarRight = (ProgressBar) itemView.findViewById(R.id.progressBarRight);
            progressBarLeft = (ProgressBar) itemView.findViewById(R.id.progressBarLeft);

            videoplayer2 = (JzvdStd) itemView.findViewById(R.id.videoplayer2);
            videoplayer = (JzvdStd) itemView.findViewById(R.id.videoplayer);
            linMain = (LinearLayout)itemView.findViewById(R.id. linMain);

            context = itemView.getContext();


        }


    }

    //----SETTING EACH HOLDER WITH DATA----
    @Override
    public void onBindViewHolder(final GroupMessageAdapter.MessageViewHolder holder, int position) {


        // String current_user_id = mAuth.getCurrentUser().getUid();
        final Messages mes = mMessagesList.get(position);
        String from_user_id = mes.getFrom();
        String message_type = mes.getType();
        TableAttendee tableAttendees = EventAppDB.getDatabase(context).attendeeDao().getAttendeeDetailsFromFireId(from_user_id);
        final Attendee attendee = new Attendee();
try {
    // attendee.setFirebase_status(tableAttendees.getFirebase_status());
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
  //  attendee.setFirebase_status(tableAttendees.getFirebase_status());
}
catch(Exception e){

}


        holder.innerRl2.setBackgroundColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_1)));
         holder.linMain.setBackgroundColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_2)));
        String eventColor3 = SharedPreference.getPref(context, EVENT_COLOR_3);

        String eventColor3Opacity40 = eventColor3.replace("#", "");

        holder.messageText.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        holder.displayTime.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        holder.displayName.setTextColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_1)));

        if(from_user_id!=null) {
          //  if (from_user_id.equals(current_user_id)) {
                holder.messageSingleLayout3.setVisibility(View.GONE);
                holder.messageSingleLayout2.setVisibility(View.VISIBLE);
                //----CHANGING TIMESTAMP TO TIME-----

                long timeStamp = mes.getTime();
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTimeInMillis(timeStamp);
                String cal[] = calendar.getTime().toString().split(" ");

                String time = cal[3];

                SimpleDateFormat date12Format = new SimpleDateFormat("HH:mm:ss");

                SimpleDateFormat date24Format = new SimpleDateFormat("hh:mm a");

                try {
                    System.out.println(date24Format.format(date12Format.parse(time)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String time_of_message = null;
                try {

                    // time_of_message = cal[2] + " " +cal[1] + " " +  cal[5]+ ", " + /*cal[3].substring(0, 5)*/date24Format.format(date12Format.parse(time));
                    time_of_message = date24Format.format(date12Format.parse(time));

                    holder.displayTime.setText(time_of_message);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.e("TIME IS : ", calendar.getTime().toString());


                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(from_user_id);

                //---ADDING NAME THUMB_IMAGE TO THE HOLDER----
/*
            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String image = dataSnapshot.child("thumb_image").getValue().toString();
                    uImage= image;
                    uName = name;
                    holder.displayName2.setText(name);
                    Picasso.with(holder.profileImage2.getContext()).load(image).
                            placeholder(R.drawable.profilepic_placeholder).into(holder.profileImage2);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
*/
                holder.displayName.setText(attendee.getFirst_name() +" "+ attendee.getLast_name());

                Glide.with(context).load((attendee.getProfile_picture()))
                        .placeholder(R.drawable.profilepic_placeholder)
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
                        }).into(holder.profileImage);

                // holder.messageText.setText(mes.getMessage());
                if (message_type.equals("text")) {
                    holder.videoplayer2.setVisibility(View.GONE);
                    holder.messageText2.setVisibility(View.GONE);

                    holder.messageText.setText(mes.getMessage());
                    holder.messageImage2.setVisibility(View.GONE);
                    holder.progressBarLeft.setVisibility(View.INVISIBLE);

                }


        }else{
            holder.innerRl1.setVisibility(View.VISIBLE);
            holder.innerRl2.setVisibility(View.GONE);
            holder.displayTime.setVisibility(View.VISIBLE);


        }

    }



    //---NO OF ITEMS TO BE ADDED----
    @Override
    public int getItemCount() {
        return mMessagesList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    private void checkCachedState(String url) {
        HttpProxyCacheServer proxy = App.getProxy(context);
        boolean fullyCached = proxy.isCached(url);

    }



}
