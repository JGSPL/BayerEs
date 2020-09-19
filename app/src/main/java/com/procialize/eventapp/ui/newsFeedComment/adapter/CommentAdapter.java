package com.procialize.eventapp.ui.newsFeedComment.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.ui.attendee.roomDB.TableAttendee;
import com.procialize.eventapp.ui.attendee.view.AttendeeDetailActivity;
import com.procialize.eventapp.ui.newsFeedComment.model.Comment;
import com.procialize.eventapp.ui.newsFeedComment.model.CommentDetail;
import com.procialize.eventapp.ui.newsfeed.adapter.SwipeMultimediaAdapter;
import com.procialize.eventapp.ui.newsfeed.model.News_feed_media;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;
import com.procialize.eventapp.ui.newsfeed.viewmodel.NewsFeedDatabaseViewModel;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JzvdStd;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_5;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.NewsViewHolder> {

    Context context;
    List<CommentDetail> commentDetails;
    String name1 = "",substring;
    CommentAdapterListner listener;
    String eventColor1, eventColor2, eventColor3, eventColor4, eventColor5;
    NewsFeedDatabaseViewModel newsFeedDatabaseViewModel;
    public CommentAdapter(Context context, List<CommentDetail> commentDetails, CommentAdapterListner listener) {
        this.context = context;
        this.commentDetails = commentDetails;
        this.listener = listener;

        newsFeedDatabaseViewModel = ViewModelProviders.of((FragmentActivity) context).get(NewsFeedDatabaseViewModel.class);
        eventColor1 = SharedPreference.getPref(context, EVENT_COLOR_1);
        eventColor2 = SharedPreference.getPref(context, EVENT_COLOR_2);
        eventColor3 = SharedPreference.getPref(context, EVENT_COLOR_3);
        eventColor4 = SharedPreference.getPref(context, EVENT_COLOR_4);
        eventColor5 = SharedPreference.getPref(context, EVENT_COLOR_5);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_comments, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsViewHolder holder, final int position) {
        //Newsfeed_detail feedData = feed_detail.get(position);
        CommentDetail comments = commentDetails.get(position);

        if(comments.getProfile_picture().trim()!=null)
        {

            Glide.with(context)
                    .load(comments.getProfile_picture().trim())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.progressView.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progressView.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(holder.iv_profile);
        }

        //holder.tv_name.setText(comments.getFirst_name()+" "+comments.getLast_name()+" "+comments.getComment());
        //String name1 = "<font color='"+colorActive+"'>" + comments.getFirst_name() + " " + comments.getLast_name() + " " + "</font>";
        if (comments.getComment().contains("gif")) {
            //name1 = "<font color='#D81B60'>" + comments.getFirst_name() + " " + comments.getLast_name() + " " + "</font>";
            name1 = "<font color='"+eventColor1+"'>" + comments.getFirst_name() + " " + comments.getLast_name() + " " + "</font>";
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
                holder.tv_name.setText(Html.fromHtml(name1));
            } else {
                holder.tv_name.setText(Html.fromHtml(name1, Html.FROM_HTML_MODE_LEGACY));
            }
            holder.fl_gif.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(comments.getComment())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.pb_gif.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.pb_gif.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(holder.iv_gif);
        } else {
            //name1 = "<font color='#D81B60'>" + comments.getFirst_name() + " " + comments.getLast_name() + " " + "</font>" + comments.getComment();
           /* name1 = "<font color='"+eventColor1 +"'>" + comments.getFirst_name() + " " + comments.getLast_name() + " " + "</font>" + " " +
                    "<font color='"+eventColor3 +"'>" + comments.getComment() + "</font>" ;*/
            holder.fl_gif.setVisibility(View.GONE);

            try {
                
                holder.testdata.setText(StringEscapeUtils.unescapeJava(comments.getComment()));

                final SpannableStringBuilder stringBuilder = new SpannableStringBuilder(holder.testdata.getText());
                if (comments.getComment() != null) {

                    holder.tv_name.setVisibility(View.VISIBLE);
                    //  holder.commentTv.setVisibility(View.VISIBLE);
//                    holder.wallNotificationText.setText(getEmojiFromString(notificationImageStatus));
                    int flag = 0;
                    for (int i = 0; i < stringBuilder.length(); i++) {
                        String sample = stringBuilder.toString();
                        if ((stringBuilder.charAt(i) == '<')) {
                            try {
                                String text = "<";
                                String text1 = ">";

                                if (flag == 0) {
                                    int start = sample.indexOf(text, i);
                                    int end = sample.indexOf(text1, i);

                                    Log.v("Indexes of", "Start : " + start + "," + end);
                                    try {
                                        substring = sample.substring(start, end + 1);
                                        Log.v("String names: ", substring);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                    if (substring.contains("<")) {
                                        if (sample.contains(substring)) {
                                            substring = substring.replace("<", "");
                                            substring = substring.replace(">", "");
                                            int index = substring.indexOf("^");
//                                    substring = substring.replace("^", "");
                                            final String attendeeid = substring.substring(0, index);
                                            substring = substring.substring(index + 1, substring.length());


                                            stringBuilder.setSpan(stringBuilder, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            stringBuilder.setSpan(new ForegroundColorSpan(Color.RED), start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                                            stringBuilder.setSpan(new ClickableSpan() {
                                                @Override
                                                public void onClick(View widget) {
                                                    newsFeedDatabaseViewModel.getAttendeeDetailsFromId(context,attendeeid);
                                                    newsFeedDatabaseViewModel.getAttendeeDetails().observe((LifecycleOwner) context,new Observer<List<TableAttendee>>() {
                                                        @Override
                                                        public void onChanged(List<TableAttendee> tableAttendees) {
                                                            if(tableAttendees!=null) {
                                                                Intent intent = new Intent(context, AttendeeDetailActivity.class);
                                                                intent.putExtra("fname", tableAttendees.get(0).getFirst_name());
                                                                intent.putExtra("lname", tableAttendees.get(0).getLast_name());
                                                                intent.putExtra("company", tableAttendees.get(0).getCompany_name());
                                                                intent.putExtra("city", tableAttendees.get(0).getCity());
                                                                intent.putExtra("designation", tableAttendees.get(0).getDesignation());
                                                                intent.putExtra("prof_pic", tableAttendees.get(0).getProfile_picture());
                                                                intent.putExtra("attendee_type", tableAttendees.get(0).getAttendee_type());
                                                                intent.putExtra("mobile", tableAttendees.get(0).getMobile());
                                                                intent.putExtra("email", tableAttendees.get(0).getEmail());
                                                                context.startActivity(intent);
                                                            }
                                                        }
                                                    });
                                                }
                                            }, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            stringBuilder.replace(start, end + 1, substring);
                                            holder.testdata.setText(stringBuilder, TextView.BufferType.SPANNABLE);
                                            holder.tv_name.setMovementMethod(LinkMovementMethod.getInstance());
                                            //holder.tv_name.setText(stringBuilder);
                                            SpannableStringBuilder stringBuilder1 = SpannableStringBuilder.valueOf("<font color='"+eventColor3 +"'>"
                                                    + stringBuilder + "</font>");
                                            holder.tv_name.setText(stringBuilder1);
                                            flag = 1;
                                        }
                                    }
                                } else {

                                    int start = sample.indexOf(text, i);
                                    int end = sample.indexOf(text1, i);

                                    Log.v("Indexes of", "Start : " + start + "," + end);
                                    try {
                                        substring = sample.substring(start, end + 1);
                                        Log.v("String names: ", substring);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (substring.contains("<")) {
                                        if (sample.contains(substring)) {
                                            substring = substring.replace("<", "");
                                            substring = substring.replace(">", "");
                                            int index = substring.indexOf("^");
//                                    substring = substring.replace("^", "");
                                            final String attendeeid = substring.substring(0, index);
                                            substring = substring.substring(index + 1, substring.length());


                                            stringBuilder.setSpan(stringBuilder, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            stringBuilder.setSpan(new ForegroundColorSpan(Color.RED), start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                            stringBuilder.setSpan(new ClickableSpan() {
                                                @Override
                                                public void onClick(View widget) {
                                                    newsFeedDatabaseViewModel.getAttendeeDetailsFromId(context,attendeeid);
                                                    newsFeedDatabaseViewModel.getAttendeeDetails().observe((LifecycleOwner) context,new Observer<List<TableAttendee>>() {
                                                        @Override
                                                        public void onChanged(List<TableAttendee> tableAttendees) {
                                                            if(tableAttendees!=null) {
                                                                Intent intent = new Intent(context, AttendeeDetailActivity.class);
                                                                intent.putExtra("fname", tableAttendees.get(0).getFirst_name());
                                                                intent.putExtra("lname", tableAttendees.get(0).getLast_name());
                                                                intent.putExtra("company", tableAttendees.get(0).getCompany_name());
                                                                intent.putExtra("city", tableAttendees.get(0).getCity());
                                                                intent.putExtra("designation", tableAttendees.get(0).getDesignation());
                                                                intent.putExtra("prof_pic", tableAttendees.get(0).getProfile_picture());
                                                                intent.putExtra("attendee_type", tableAttendees.get(0).getAttendee_type());
                                                                intent.putExtra("mobile", tableAttendees.get(0).getMobile());
                                                                intent.putExtra("email", tableAttendees.get(0).getEmail());
                                                                context.startActivity(intent);
                                                            }
                                                        }
                                                    });
                                                }
                                            }, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                            stringBuilder.replace(start, end + 1, substring);
                                            holder.testdata.setText(stringBuilder, TextView.BufferType.SPANNABLE);
                                            holder.tv_name.setMovementMethod(LinkMovementMethod.getInstance());
                                            SpannableStringBuilder stringBuilder1 = SpannableStringBuilder.valueOf("<font color='"+eventColor3 +"'>" +
                                                    stringBuilder + "</font>");
                                            holder.tv_name.setText(stringBuilder1);

                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    // holder.commentTv.setText(stringBuilder);
                    String name1 = "<font color='"+eventColor1+"'>" + comments.getFirst_name() + " " + comments.getLast_name() + " " + "</font>"; //set Black color of name

                    /*name1 = "<font color='"+eventColor1 +"'>" + comments.getFirst_name() + " " + comments.getLast_name() + " " + "</font>" + " " +
                            "<font color='"+eventColor3 +"'>" + stringBuilder + "</font>" ;*/

                    holder.tv_name.setMovementMethod(LinkMovementMethod.getInstance());
                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
                        holder.tv_name.setText(Html.fromHtml(name1));
                        holder.tv_name.append(stringBuilder);
                    } else {
                        holder.tv_name.setText(Html.fromHtml(name1, Html.FROM_HTML_MODE_LEGACY));   //set text
                        holder.tv_name.append(stringBuilder);   //append text into textView
                    }
                } else {
                    //holder.tv_name.setVisibility(View.GONE);
                }
            }catch (IllegalArgumentException e)
            {e.printStackTrace();}


        }
        /*holder.tv_name.setMovementMethod(LinkMovementMethod.getInstance());
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            holder.tv_name.setText(Html.fromHtml(name1));
            //holder.tv_name.append(stringBuilder);
        } else {
            holder.tv_name.setText(Html.fromHtml(name1, Html.FROM_HTML_MODE_LEGACY));   //set text
            // holder.tv_name.append(stringBuilder);   //append text into textView
        }*/
        holder.tv_date_time.setText(CommonFunction.convertDate(comments.getDateTime()));

        String eventColor3Opacity40 = eventColor3.replace("#", "");
        holder.tv_date_time.setTextColor(Color.parseColor("#66" + eventColor3Opacity40));
        int color = Color.parseColor( eventColor3);
        holder.iv_options.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        holder.iv_options.setAlpha(150);
        holder.ll_root.setBackgroundColor(Color.parseColor(eventColor2));
        holder.v_divider.setBackgroundColor(Color.parseColor(eventColor3));
    }

    @Override
    public int getItemCount() {
        return commentDetails.size();
    }

    public interface CommentAdapterListner {
        void onMoreSelected(CommentDetail comment, int position);
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_date_time,testdata;
        FrameLayout fl_gif;
        ImageView iv_gif, iv_options,iv_profile;
        ProgressBar pb_gif,progressView;
        LinearLayout ll_root;
        View v_divider;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            testdata = itemView.findViewById(R.id.testdata);
            tv_date_time = itemView.findViewById(R.id.tv_date_time);
            fl_gif = itemView.findViewById(R.id.fl_gif);
            iv_gif = itemView.findViewById(R.id.iv_gif);
            iv_options = itemView.findViewById(R.id.iv_options);
            iv_profile = itemView.findViewById(R.id.iv_profile);
            pb_gif = itemView.findViewById(R.id.pb_gif);
            progressView = itemView.findViewById(R.id.progressView);
            ll_root = itemView.findViewById(R.id.ll_root);
            v_divider = itemView.findViewById(R.id.v_divider);

            iv_options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMoreSelected(commentDetails.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }
}