package com.procialize.eventapp.ui.attendeeChat.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.SharedPreference;
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


/**
 * Created by Preeti on 3/27/2018.
 */



public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> implements CacheListener {

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
    public MessageAdapter(List<Messages> mMessagesList, String receiverUser, /*String sName, String sprofilpic,*/String rprofilPic) {
        this.mMessagesList = mMessagesList;
        this.receiverUser = receiverUser;
       // this.sName = sName;
       // this.sprofilpic = sprofilpic;
        this.rprofilPic = rprofilPic;

    }


    //---CREATING SINGLE HOLDER AND RETURNING ITS VIEW---
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout2,parent,false);
        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        return new MessageViewHolder(view);
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


            //---DELETE FUNCTION---
/*
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    CharSequence options[] = new CharSequence[]{ "Delete","Cancel" };
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delete this message");
                    builder.setItems(options,new AlertDialog.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if(which == 0){
                                */
/*
                                        ....CODE FOR DELETING THE MESSAGE IS YET TO BE WRITTEN HERE...
                                 *//*

                                long mesPos = getAdapterPosition();
                                String mesId = mMessagesList.get((int)mesPos).toString();
                                Log.e("Message Id is ", mesId);
                                Log.e("Message is : ",mMessagesList.get((int)mesPos).getMessage());

                            }

                            if(which == 1){

                            }

                        }
                    });
                    builder.show();

                    return true;
                }
            });
*/

        }


    }

    //----SETTING EACH HOLDER WITH DATA----
    @Override
    public void onBindViewHolder(final MessageViewHolder holder, int position) {


       // String current_user_id = mAuth.getCurrentUser().getUid();
        final Messages mes = mMessagesList.get(position);
        String from_user_id = mes.getFrom();
        String message_type = mes.getType();

        holder.innerRl2.setBackgroundColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_1)));
       // holder.innerRl1.setBackgroundColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_2)));

        holder.messageText2.setTextColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_2)));
        if(from_user_id!=null) {
            if (from_user_id.equals(current_user_id)) {
                holder.messageSingleLayout2.setVisibility(View.GONE);
                holder.messageSingleLayout3.setVisibility(View.VISIBLE);
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

                    holder.displayTime2.setText(time_of_message);

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
                /*holder.displayName2.setText(sName);

                Glide.with(context).load((sprofilpic))
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
                        }).into(holder.profileImage2);*/

                // holder.messageText.setText(mes.getMessage());
                if (message_type.equals("text")) {
                    holder.videoplayer2.setVisibility(View.GONE);
                    holder.messageText2.setVisibility(View.VISIBLE);

                    holder.messageText2.setText(mes.getMessage());
                    holder.messageImage2.setVisibility(View.GONE);
                    holder.progressBarRight.setVisibility(View.INVISIBLE);

                } else if (message_type.equals("video")) {
                    holder.messageText2.setVisibility(View.GONE);
                    holder.progressBarRight.setVisibility(View.GONE);
                    holder.messageImage2.setVisibility(View.GONE);
                    holder.videoplayer2.setVisibility(View.VISIBLE);

                /*holder.videoplayer2.setUp(mes.getMessage()
                        , JzvdStd.SCREEN_WINDOW_NORMAL, "");

                Glide.with(context)
                        .asBitmap()
                        .load(mes.getMessage())
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(holder.videoplayer2.thumbImageView);*/
                    HttpProxyCacheServer proxy = App.getProxy(context);
                    proxy.registerCacheListener(this, mes.getMessage());
                    String proxyUrl = proxy.getProxyUrl(mes.getMessage());
                    Log.d("LOG_TAG", "Use proxy url " + proxyUrl + " instead of original url " + mes.getMessage());

                    holder.videoplayer2.setUp(mes.getMessage(), ""
                            , JzvdStd.SCREEN_NORMAL);
                    JzvdStd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);


                    Glide.with(context)
                            .asBitmap()
                            .load(mes.getMessage())
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .into(holder.videoplayer2.thumbImageView);


                } else if (mes.getType().equalsIgnoreCase("image")) {

                    holder.messageText2.setVisibility(View.GONE);
                    holder.messageImage2.setVisibility(View.VISIBLE);
                    //Picasso.with(holder.profileImage.getContext()).load(mes.getMessage()).placeholder(R.drawable.user_img).into(holder.messageImage);
                    holder.videoplayer2.setVisibility(View.GONE);

                    Glide.with(context).load((mes.getMessage()))
                            .placeholder(R.drawable.gallery_placeholder)
                            .apply(RequestOptions.skipMemoryCacheOf(false))
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).fitCenter()
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    holder.progressBarRight.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    holder.progressBarRight.setVisibility(View.GONE);
                                    return false;
                                }
                            }).into(holder.messageImage2);


                }

            } else {

                holder.messageSingleLayout3.setVisibility(View.GONE);
                holder.messageSingleLayout2.setVisibility(View.VISIBLE);
                //----CHANGING TIMESTAMP TO TIME-----

                long timeStamp = mes.getTime();
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTimeInMillis(timeStamp);
                String cal[] = calendar.getTime().toString().split(" ");
                //  String time_of_message = cal[2] + " " + cal[1] + " "+ cal[5]+ "  "+ cal[3].substring(0, 5);
                Log.e("TIME IS : ", calendar.getTime().toString());
                String time = cal[3];

                SimpleDateFormat date12Format = new SimpleDateFormat("HH:mm:ss");

                SimpleDateFormat date24Format = new SimpleDateFormat("hh:mm a");

                try {
                    System.out.println(date24Format.format(date12Format.parse(time)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {

                    // String time_of_message = cal[2] + " " +cal[1] + " " +  cal[5]+ ", " + /*cal[3].substring(0, 5)*/date24Format.format(date12Format.parse(time));
                    String time_of_message = date24Format.format(date12Format.parse(time));

                    holder.displayTime.setText(time_of_message);

                } catch (ParseException e) {


                    e.printStackTrace();
                }
                //  holder.displayTime.setText(time_of_message);

                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(from_user_id);

                //---ADDING NAME THUMB_IMAGE TO THE HOLDER----
                holder.displayName.setText(receiverUser);
                Glide.with(context).load((rprofilPic))
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
                    holder.messageText.setVisibility(View.VISIBLE);
                    holder.messageText.setText(mes.getMessage());
                    holder.messageImage.setVisibility(View.GONE);
                    holder.progressBarLeft.setVisibility(View.INVISIBLE);
                    holder.videoplayer.setVisibility(View.GONE);

                } else if (message_type.equals("video")) {

                    holder.messageText.setVisibility(View.GONE);
                    holder.progressBarLeft.setVisibility(View.GONE);
                    holder.videoplayer.setVisibility(View.VISIBLE);
                    holder.messageImage.setVisibility(View.GONE);

               /* holder.videoplayer.setUp(mes.getMessage()
                        , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");*/
                    HttpProxyCacheServer proxy = App.getProxy(context);
                    proxy.registerCacheListener(this, mes.getMessage());
                    String proxyUrl = proxy.getProxyUrl(mes.getMessage());
                    Log.d("LOG_TAG", "Use proxy url " + proxyUrl + " instead of original url " + mes.getMessage());

                    holder.videoplayer.setUp(mes.getMessage(), ""
                            , JzvdStd.SCREEN_NORMAL);
                    JzvdStd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);


                    Glide.with(context)
                            .asBitmap()
                            .load(mes.getMessage())
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .into(holder.videoplayer.thumbImageView);


                } else if (message_type.equalsIgnoreCase("image")) {

                    holder.messageText.setVisibility(View.GONE);
                    holder.messageImage.setVisibility(View.VISIBLE);
                    //Picasso.with(holder.profileImage.getContext()).load(mes.getMessage()).placeholder(R.drawable.user_img).into(holder.messageImage);
                    holder.videoplayer.setVisibility(View.GONE);


                    Glide.with(context).load((mes.getMessage()))
                            .placeholder(R.drawable.gallery_placeholder)
                            .apply(RequestOptions.skipMemoryCacheOf(false))
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    holder.progressBarLeft.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    holder.progressBarLeft.setVisibility(View.GONE);
                                    return false;
                                }
                            }).into(holder.messageImage);


                }
            }
        }else{
            holder.innerRl1.setVisibility(View.GONE);
            holder.innerRl2.setVisibility(View.GONE);
            holder.displayTime.setVisibility(View.GONE);


        }

        holder.linMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mes.getType().equalsIgnoreCase(("image"))) {
                    Intent comment = new Intent(context, FullScreenImageActivity.class);
                    comment.putExtra("nameUser", uName);
                    comment.putExtra("urlPhotoUser", uImage);
                    comment.putExtra("type", mes.getType());

                    comment.putExtra("urlPhotoClick", mes.getMessage());
                    JzvdStd.releaseAllVideos();

                    context.startActivity(comment);

                }else if(mes.getType().equalsIgnoreCase(("video"))) {
                    JzvdStd.releaseAllVideos();

                    Intent comment = new Intent(context, FullScreenImageActivity.class);
                    comment.putExtra("nameUser", uName);

                    comment.putExtra("type", mes.getType());
                    comment.putExtra("urlPhotoUser", uImage);
                    comment.putExtra("urlPhotoClick", mes.getMessage());
                    context.startActivity(comment);

                }


            }
        });
        ChatActivity.videoflag = "0";
        holder.videoplayer.fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ChatActivity.videoflag = "1";
                JzvdStd.releaseAllVideos();

                Intent comment = new Intent(context, FullScreenImageActivity.class);
                comment.putExtra("nameUser", uName);

                comment.putExtra("type", mes.getType());
                comment.putExtra("urlPhotoUser", uImage);
                comment.putExtra("urlPhotoClick", mes.getMessage());
                context.startActivity(comment);
            }
        });

        holder.videoplayer2.fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ChatActivity.videoflag = "1";
                JzvdStd.releaseAllVideos();

                Intent comment = new Intent(context, FullScreenImageActivity.class);
                comment.putExtra("nameUser", uName);

                comment.putExtra("type", mes.getType());
                comment.putExtra("urlPhotoUser", uImage);
                comment.putExtra("urlPhotoClick", mes.getMessage());
                context.startActivity(comment);
            }
        });

       /* if(from_user_id.equals(current_user_id)){
            holder.messageText.setBackgroundColor(Color.WHITE);
            //holder.messageText.setBackgroundResource(R.drawable.message_text_background);
            holder.messageText.setTextColor(Color.BLACK);
        }
        else{

            holder.messageText.setBackgroundResource(R.drawable.message_text_background);
            holder.messageText.setTextColor(Color.WHITE);
        }*/

    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public int getItemViewType(int position) {
        /*if (directQuestionLists.get(position).getReceiver_id().equalsIgnoreCase(att_id)) {
            return 1;
        } else if (directQuestionLists.get(position).getSender_id().equalsIgnoreCase(att_id)) {
            return 2;
        } else {
            return 0;
        }*/
         return position;
    }

    //---NO OF ITEMS TO BE ADDED----
    @Override
    public int getItemCount() {
        return mMessagesList.size();
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
/*
    //----FOR SENDING IMAGE----
        if(message_type.equals("text")){

            holder.messageText.setText(mes.getMessage());
            holder.messageImage.setVisibility(View.INVISIBLE);

        }
        else{

            holder.messageText.setVisibility(View.INVISIBLE);
            Picasso.with(holder.profileImage.getContext()).load(mes.getMessage()).placeholder(R.drawable.user_img).into(holder.messageImage);

        }
    */




       /* if(from_user_id.equals(current_user_id)){
            holder.messageText.setBackgroundColor(Color.WHITE);
            //holder.messageText.setBackgroundResource(R.drawable.message_text_background);
            holder.messageText.setTextColor(Color.BLACK);
        }
        else{

            holder.messageText.setBackgroundResource(R.drawable.message_text_background);
            holder.messageText.setTextColor(Color.WHITE);
      }
            */