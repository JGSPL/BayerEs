package com.procialize.bayer2020.ui.newsfeed.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.procialize.bayer2020.ConnectionDetector;
import com.procialize.bayer2020.Database.EventAppDB;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.attendee.model.Attendee;
import com.procialize.bayer2020.ui.attendee.roomDB.TableAttendee;
import com.procialize.bayer2020.ui.attendee.view.AttendeeDetailActivity;
import com.procialize.bayer2020.ui.newsfeed.PaginationUtils.PaginationAdapterCallback;
import com.procialize.bayer2020.ui.newsfeed.model.News_feed_media;
import com.procialize.bayer2020.ui.newsfeed.model.Newsfeed_detail;
import com.procialize.bayer2020.ui.newsfeed.viewmodel.NewsFeedDatabaseViewModel;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JzvdStd;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_5;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.NEWS_FEED_MEDIA_PATH;
import static com.procialize.bayer2020.ui.newsfeed.view.NewsFeedFragment.cl_main;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.NewsViewHolder> {

    Context context;
    ArrayList<Newsfeed_detail> feed_detail;
    FeedAdapterListner listener;
    String mediaPath = "";
    public static int swipableAdapterPosition = 0;
    ConnectionDetector cd;
    private PaginationAdapterCallback mCallback;
    private boolean retryPageLoad = false;
    private boolean isLoadingAdded = false;
    String eventColor1, eventColor2, eventColor3, eventColor4, eventColor5;
    String substring;
    String spannedString;
    String postStatus;
    NewsFeedDatabaseViewModel newsFeedDatabaseViewModel;

    public NewsFeedAdapter() {

    }

    public NewsFeedAdapter(Context context,/* ArrayList<Newsfeed_detail> feed_detail,*/ FeedAdapterListner listener) {
        this.context = context;
        // this.feed_detail = feed_detail;
        this.listener = listener;
        this.feed_detail = new ArrayList<>();

        //  this.mCallback = (PaginationAdapterCallback) context;

        cd = new ConnectionDetector();
        eventColor1 = SharedPreference.getPref(context, EVENT_COLOR_1);
        eventColor2 = SharedPreference.getPref(context, EVENT_COLOR_2);
        eventColor3 = SharedPreference.getPref(context, EVENT_COLOR_3);
        eventColor4 = SharedPreference.getPref(context, EVENT_COLOR_4);
        eventColor5 = SharedPreference.getPref(context, EVENT_COLOR_5);

        newsFeedDatabaseViewModel = ViewModelProviders.of((FragmentActivity) context).get(NewsFeedDatabaseViewModel.class);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.newsfeed_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsViewHolder holder, final int position) {
        final Newsfeed_detail feedData = feed_detail.get(position);
        try {

            if (feedData.getAttendee_id()==null ) {
                holder.root.setVisibility(View.GONE);
            } else {
                holder.root.setVisibility(View.VISIBLE);
            }
           /* if (feedData.getFirst_name().equalsIgnoreCase("null") || (feedData.getFirst_name().equalsIgnoreCase("") || (feedData == null) || feedData.getAttendee_id()==null) || (feedData.getFirst_name().equalsIgnoreCase(null))) {
                holder.root.setVisibility(View.GONE);
            } else {
                holder.root.setVisibility(View.VISIBLE);
            }*/

//            setDynamicColor(holder);


            mediaPath = SharedPreference.getPref(context, NEWS_FEED_MEDIA_PATH);


            if (!feedData.getLast_name().equalsIgnoreCase("null")) {
                holder.nameTv.setText(feedData.getFirst_name() + " " + feedData.getLast_name());
            } else {
                holder.nameTv.setText(feedData.getFirst_name());
            }

            if (feedData.getCity_id() != null && !(feedData.getCity_id().equalsIgnoreCase(""))) {
               // holder.designationTv.setText(feedData.getDesignation() + " - " + feedData.getCity_id());
                holder.designationTv.setText(feedData.getCompany_name());

            } else {
                holder.designationTv.setText(feedData.getCompany_name());
            }

            if(feedData.getQa_reply().equalsIgnoreCase("")){
                holder.ll__adminswipe.setVisibility(View.GONE);
                holder.QuestionTv.setVisibility(View.GONE);
                holder.replyadminTv.setVisibility(View.GONE);
            }else{
                holder.ll__adminswipe.setVisibility(View.VISIBLE);
                holder.QuestionTv.setVisibility(View.VISIBLE);
                holder.replyadminTv.setVisibility(View.VISIBLE);
                if (feedData.getAdmin_profile_picture() != null) {
                    Glide.with(context).load((feedData.getAdmin_profile_picture().trim()))
                            .placeholder(R.drawable.profilepic_placeholder)
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).fitCenter()
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    //feedprogress.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    //feedprogress.setVisibility(View.GONE);
                                    return false;
                                }
                            }).into(holder.adminIV);

                }
                if (!feedData.getQa_reply().isEmpty() && feedData.getQa_reply() != null) {
                    holder.adminTv.setText(StringEscapeUtils.unescapeJava(feedData.getQa_reply()));
                    holder.adminTv.setVisibility(View.VISIBLE);
                } else {
                    holder.adminTv.setVisibility(View.GONE);
                }
            }

            if (feedData.getProfile_pic() != null) {
                Glide.with(context).load((feedData.getProfile_pic().trim()))
                        .placeholder(R.drawable.profilepic_placeholder)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).fitCenter()
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                //feedprogress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                //feedprogress.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(holder.profileIV);

            } else {
                holder.progressView.setVisibility(View.GONE);
                holder.profileIV.setImageResource(R.drawable.profilepic_placeholder);
            }

            String dateTime = feedData.getPost_date();
            if (!dateTime.isEmpty()) {
                String convertedDate = CommonFunction.convertDate(dateTime);
                holder.dateTv.setText(convertedDate);
            }

            /*holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (feedData.getNews_feed_media().size() > 0) {
                        listener.onContactSelected(feed_detail.get(position), position);
                    }
                }
            });*/

            if (feedData.getTotal_comments().equalsIgnoreCase("1")) {
                holder.tv_comment.setText(feedData.getTotal_comments() + " Comment");
            } else {
                holder.tv_comment.setText(feedData.getTotal_comments() + " Comments");
            }
            if (feedData.getLike_flag().equalsIgnoreCase("0")) {
                holder.iv_like.setImageDrawable(context.getDrawable(R.drawable.ic_like));
            } else {
                holder.iv_like.setImageDrawable(context.getDrawable(R.drawable.ic_active_like));
            }

            if (feedData.getTotal_likes().equalsIgnoreCase("1")) {
                holder.tv_like.setText(feedData.getTotal_likes() + " Like");
            } else {
                holder.tv_like.setText(feedData.getTotal_likes() + " Likes");
            }

            holder.vp_slider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (feedData.getNews_feed_media().size() > 0) {
                        listener.onContactSelected(feed_detail.get(position), position);
                    }
                }
            });

            holder.tv_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCommentClick(feed_detail.get(position), position, swipableAdapterPosition);
                }
            });
            holder.iv_comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCommentClick(feed_detail.get(position), position, swipableAdapterPosition);
                }
            });
            holder.tv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onLikeClick(feed_detail.get(position), position);
                }
            });

            holder.moreIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.moreTvFollowOnClick(v, feed_detail.get(position), position);

                }
            });

            holder.iv_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onShareClick(feed_detail.get(position), position, swipableAdapterPosition);

                }
            });

            holder.iv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cd.isConnectingToInternet()) {
                        listener.likeTvViewOnClick(v, feed_detail.get(position), position, holder.iv_like, holder.tv_like);
                    } else {
                        Utility.createShortSnackBar(cl_main, "No Internet Connection");

                    }
                }
            });


            if (!feedData.getPost_status().isEmpty() && feedData.getPost_status() != null) {
                holder.tv_status.setText(StringEscapeUtils.unescapeJava(feedData.getPost_status()));
                holder.tv_status.setVisibility(View.VISIBLE);
            } else {
                holder.tv_status.setVisibility(View.GONE);
            }


            if (feedData.getNews_feed_media() != null) {
                if (feedData.getNews_feed_media().size() > 0) {
                    holder.vp_slider.setVisibility(View.VISIBLE);
                    holder.ll_dots.setVisibility(View.VISIBLE);
                    List<News_feed_media> news_feed_media1 = feedData.getNews_feed_media();
                    final ArrayList<String> imagesSelectednew = new ArrayList<>();
                    final ArrayList<String> imagesSelectednew1 = new ArrayList<>();
                    for (int i = 0; i < news_feed_media1.size(); i++) {
                        // imagesSelectednew.add(newsFeedPath /*ApiConstant.newsfeedwall*/ + news_feed_media1.get(i).getMediaFile());
                        imagesSelectednew.add(mediaPath + news_feed_media1.get(i).getMedia_file());
                        if (news_feed_media1.get(i).getMedia_file().contains("mp4")) {
                            // imagesSelectednew1.add(newsFeedPath /*ApiConstant.newsfeedwall*/ + news_feed_media1.get(i).getThumb_image());
                            imagesSelectednew1.add(mediaPath + news_feed_media1.get(i).getThumb_image());
                        } else {
                            imagesSelectednew1.add("");
                        }
                    }

                    final SwipeMultimediaAdapter swipepagerAdapter = new SwipeMultimediaAdapter(context, imagesSelectednew, imagesSelectednew1, news_feed_media1);
                    holder.vp_slider.setAdapter(swipepagerAdapter);
                    swipepagerAdapter.notifyDataSetChanged();
                    holder.vp_slider.setCurrentItem(0);

                    if (imagesSelectednew.size() > 1) {
                        //CommonFunction.
                        Utility.setupPagerIndidcatorDots(context, 0, holder.ll_dots, imagesSelectednew.size());
                        holder.vp_slider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                JzvdStd.goOnPlayOnPause();
                            }

                            @Override
                            public void onPageSelected(int position1) {
                                JzvdStd.goOnPlayOnPause();
                                swipableAdapterPosition = position1;
                                Utility.setupPagerIndidcatorDots(context, position1, holder.ll_dots, imagesSelectednew.size());
                                //setupPagerIndidcatorDots(position1, holder.ll_dots, imagesSelectednew.size());
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                                JzvdStd.goOnPlayOnPause();
                            }
                        });
                    } else {
                        holder.ll_dots.setVisibility(View.INVISIBLE);
                    }
                } else /*if (feedData.getNews_feed_media().size() == 0)*/ {
                    holder.ll_dots.setVisibility(View.INVISIBLE);
                    holder.vp_slider.setVisibility(View.GONE);
                }
            }
            /**
             * Code for HTML text + Tagging
             */
            if (feedData.getPost_status().contains("\n")) {
                postStatus = feedData.getPost_status().trim().replace("\n", "<br/>");
            } else {
                postStatus = feedData.getPost_status().trim();
            }
            spannedString = String.valueOf(Jsoup.parse(postStatus)).trim();//Html.fromHtml(feedData.getPost_status(), Html.FROM_HTML_MODE_COMPACT).toString();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Spanned strPost = Html.fromHtml(spannedString, Html.FROM_HTML_MODE_COMPACT);
                holder.testdata.setText(Utility.trimTrailingWhitespace(strPost));
            } else {
                Spanned strPost = Html.fromHtml(spannedString);
                holder.testdata.setText(Utility.trimTrailingWhitespace(strPost));
            }

            final SpannableStringBuilder stringBuilder = new SpannableStringBuilder(holder.testdata.getText());
            if (feedData.getPost_status() != null) {

                if (feedData.getPost_status().isEmpty()) {
                    holder.tv_status.setVisibility(View.GONE);
                } else {
                    holder.tv_status.setVisibility(View.VISIBLE);
                }

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
                                                EventAppDB eventAppDB = EventAppDB.getDatabase(context);
                                                newsFeedDatabaseViewModel.getAttendeeDetailsFromId(context, attendeeid);
                                                newsFeedDatabaseViewModel.getAttendeeDetails().observe((LifecycleOwner) context, new Observer<List<TableAttendee>>() {
                                                    @Override
                                                    public void onChanged(List<TableAttendee> tableAttendees) {
                                                        if (tableAttendees != null) {
                                                           // Intent intent = new Intent(context, AttendeeDetailActivity.class);
                                                            final Attendee attendee = new Attendee();
                                                            attendee.setMobile(tableAttendees.get(0).getMobile());
                                                            attendee.setEmail(tableAttendees.get(0).getEmail());
                                                            attendee.setFirebase_id(tableAttendees.get(0).getFirebase_id());
                                                            attendee.setFirebase_name(tableAttendees.get(0).getFirebase_name());
                                                            attendee.setFirebase_username(tableAttendees.get(0).getFirebase_username());
                                                            attendee.setAttendee_id(tableAttendees.get(0).getAttendee_id());
                                                            attendee.setFirst_name(tableAttendees.get(0).getFirst_name());
                                                            attendee.setLast_name(tableAttendees.get(0).getLast_name());
                                                            attendee.setCity(tableAttendees.get(0).getCity());
                                                            attendee.setDesignation(tableAttendees.get(0).getDesignation());
                                                            attendee.setCompany_name(tableAttendees.get(0).getCompany_name());
                                                            attendee.setAttendee_type(tableAttendees.get(0).getAttendee_type());
                                                            attendee.setTotal_sms(tableAttendees.get(0).getTotal_sms());
                                                            attendee.setProfile_picture(tableAttendees.get(0).getProfile_picture());
                                                            attendee.setFirebase_status(tableAttendees.get(0).getFirebase_status());
                                                            if(tableAttendees.get(0).getFirebase_id()!=null) {
                                                                if (tableAttendees.get(0).getFirebase_id().equalsIgnoreCase("0")) {
                                                                    context.startActivity(new Intent(context, AttendeeDetailActivity.class)
                                                                            .putExtra("Attendee", (Serializable) attendee));
                                                                } else {
                                                                    if (tableAttendees.get(0).getFirebase_status().equalsIgnoreCase("0")) {
                                                                        context.startActivity(new Intent(context, AttendeeDetailActivity.class)
                                                                                .putExtra("Attendee", (Serializable) attendee));
                                                                    } /*else {
                                                                        context.startActivity(new Intent(context, ChatActivity.class)
                                                                                .putExtra("page", "ListPage")
                                                                                .putExtra("firstMessage", "")
                                                                                .putExtra("Attendee", (Serializable) attendee));
                                                                    }*/
                                                                }
                                                            }


                                                        }
                                                        if (newsFeedDatabaseViewModel != null && newsFeedDatabaseViewModel.getAttendeeDetails().hasObservers()) {
                                                            newsFeedDatabaseViewModel.getAttendeeDetails().removeObservers((LifecycleOwner) context);
                                                        }
                                                    }
                                                });
                                            }
                                        }, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        stringBuilder.replace(start, end + 1, substring);
                                        //holder.testdata.setText(stringBuilder, TextView.BufferType.SPANNABLE);
                                        holder.tv_status.setMovementMethod(LinkMovementMethod.getInstance());
                                        holder.tv_status.setText(stringBuilder);
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
                                                newsFeedDatabaseViewModel.getAttendeeDetailsFromId(context, attendeeid);
                                                newsFeedDatabaseViewModel.getAttendeeDetails().observe((LifecycleOwner) context, new Observer<List<TableAttendee>>() {
                                                    @Override
                                                    public void onChanged(List<TableAttendee> tableAttendees) {
                                                        if (tableAttendees != null) {
                                                            final Attendee attendee = new Attendee();
                                                            attendee.setMobile(tableAttendees.get(0).getMobile());
                                                            attendee.setEmail(tableAttendees.get(0).getEmail());
                                                            attendee.setFirebase_id(tableAttendees.get(0).getFirebase_id());
                                                            attendee.setFirebase_name(tableAttendees.get(0).getFirebase_name());
                                                            attendee.setFirebase_username(tableAttendees.get(0).getFirebase_username());
                                                            attendee.setAttendee_id(tableAttendees.get(0).getAttendee_id());
                                                            attendee.setFirst_name(tableAttendees.get(0).getFirst_name());
                                                            attendee.setLast_name(tableAttendees.get(0).getLast_name());
                                                            attendee.setCity(tableAttendees.get(0).getCity());
                                                            attendee.setDesignation(tableAttendees.get(0).getDesignation());
                                                            attendee.setCompany_name(tableAttendees.get(0).getCompany_name());
                                                            attendee.setAttendee_type(tableAttendees.get(0).getAttendee_type());
                                                            attendee.setTotal_sms(tableAttendees.get(0).getTotal_sms());
                                                            attendee.setProfile_picture(tableAttendees.get(0).getProfile_picture());
                                                            attendee.setFirebase_status(tableAttendees.get(0).getFirebase_status());
                                                            if(tableAttendees.get(0).getFirebase_id()!=null) {
                                                                if (tableAttendees.get(0).getFirebase_id().equalsIgnoreCase("0")) {
                                                                    context.startActivity(new Intent(context, AttendeeDetailActivity.class)
                                                                            .putExtra("Attendee", (Serializable) attendee));
                                                                } else {
                                                                    if (tableAttendees.get(0).getFirebase_status().equalsIgnoreCase("0")) {
                                                                        context.startActivity(new Intent(context, AttendeeDetailActivity.class)
                                                                                .putExtra("Attendee", (Serializable) attendee));
                                                                    } /*else {
                                                                        context.startActivity(new Intent(context, ChatActivity.class)
                                                                                .putExtra("page", "ListPage")
                                                                                .putExtra("firstMessage", "")
                                                                                .putExtra("Attendee", (Serializable) attendee));
                                                                    }*/
                                                                }
                                                            }
                                                        }

                                                        if (newsFeedDatabaseViewModel != null && newsFeedDatabaseViewModel.getAttendeeDetails().hasObservers()) {
                                                            newsFeedDatabaseViewModel.getAttendeeDetails().removeObservers((LifecycleOwner) context);
                                                        }
                                                    }
                                                });
                                            }
                                        }, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                        stringBuilder.replace(start, end + 1, substring);
                                        //holder.testdata.setText(stringBuilder, TextView.BufferType.SPANNABLE);
                                        holder.tv_status.setMovementMethod(LinkMovementMethod.getInstance());

                                        holder.tv_status.setText(stringBuilder);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                holder.tv_status.setText(stringBuilder);
            } else {
                holder.tv_status.setVisibility(View.GONE);
            }
           /* }else
            {
                holder.tv_status.setText(holder.testdata.getText());
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != feed_detail ? feed_detail.size() : 0);
        //return feed_detail.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface FeedAdapterListner {
        void onContactSelected(Newsfeed_detail feed, int position);

        void onCommentClick(Newsfeed_detail feed, int position, int swipeablePosition);

        void onLikeClick(Newsfeed_detail feed, int position);

        void onShareClick(Newsfeed_detail feed, int position, int swipeablePosition);

        void onSliderClick(Newsfeed_detail feed, int position);

        void moreTvFollowOnClick(View v, Newsfeed_detail feed, int position);

        void likeTvViewOnClick(View v, Newsfeed_detail feed, int position, ImageView likeimage, TextView liketext);

    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv;
        TextView designationTv, tv_concat, companyTv, dateTv, tv_status, testdata;
        TextView tv_like, tv_comment;
        ImageView moreIV, profileIV, iv_comments, iv_like, iv_share;
        ProgressBar progressView;
        ViewPager vp_slider;
        LinearLayout ll_dots, ll_bottom;
        LinearLayout root;
        View v_divider;
        TextView replyadminTv,QuestionTv,adminTv;
        ImageView adminIV;
        LinearLayout ll__adminswipe;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.nameTv);
            designationTv = itemView.findViewById(R.id.designationTv);
            moreIV = itemView.findViewById(R.id.moreIV);
            iv_comments = itemView.findViewById(R.id.iv_comments);
            nameTv = itemView.findViewById(R.id.nameTv);
            companyTv = itemView.findViewById(R.id.companyTv);
            designationTv = itemView.findViewById(R.id.designationTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            tv_concat = itemView.findViewById(R.id.tv_concat);
            tv_status = itemView.findViewById(R.id.tv_status);
            testdata = itemView.findViewById(R.id.testdata);

            tv_like = itemView.findViewById(R.id.tv_like);
            tv_comment = itemView.findViewById(R.id.tv_comment);
            ll_dots = itemView.findViewById(R.id.ll_dots);
            vp_slider = itemView.findViewById(R.id.vp_slider);

            profileIV = itemView.findViewById(R.id.profileIV);
            iv_like = itemView.findViewById(R.id.iv_like);
            iv_share = itemView.findViewById(R.id.iv_share);

            progressView = itemView.findViewById(R.id.progressView);
            root = itemView.findViewById(R.id.root);
            moreIV = itemView.findViewById(R.id.moreIV);

            ll_bottom = itemView.findViewById(R.id.ll_bottom);
            v_divider = itemView.findViewById(R.id.v_divider);

            replyadminTv = itemView.findViewById(R.id.replyadminTv);
            adminIV = itemView.findViewById(R.id.adminIV);
            QuestionTv = itemView.findViewById(R.id.QuestionTv);
            adminTv = itemView.findViewById(R.id.adminTv);

            ll__adminswipe = itemView.findViewById(R.id.ll__adminswipe);

        }
    }


    public void add(Newsfeed_detail r) {
        feed_detail.add(r);
        //notifyItemInserted(attendeeLists.size() - 1);
        notifyItemInserted(feed_detail.size() - 1);
    }

    public void addAll(List<Newsfeed_detail> moveResults) {
        for (Newsfeed_detail result : moveResults) {
            add(result);
        }
    }

    public void remove(Newsfeed_detail r) {
        int position = feed_detail.indexOf(r);
        if (position > -1) {
            feed_detail.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Newsfeed_detail());
    }

    public void removeLoadingFooter() {
        try {
            isLoadingAdded = false;

            int position = feed_detail.size() - 1;
            Newsfeed_detail result = getItem(position);

            if (result != null) {
                feed_detail.remove(position);
                notifyItemRemoved(position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Newsfeed_detail getItem(int position) {
        return feed_detail.get(position);
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(feed_detail.size() - 1);
    }


    public List<Newsfeed_detail> getNewsFeedList() {
        return feed_detail;
    }


    public void setDynamicColor(@NonNull final NewsViewHolder holder) {
        holder.nameTv.setTextColor(Color.parseColor(eventColor1));
        holder.tv_status.setTextColor(Color.parseColor(eventColor3));
        String eventColor3Opacity40 = eventColor3.replace("#", "");
        holder.dateTv.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        holder.designationTv.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        holder.tv_concat.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        holder.companyTv.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        holder.tv_like.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        holder.tv_comment.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        holder.root.setBackgroundColor(Color.parseColor(eventColor2));
        holder.v_divider.setBackgroundColor(Color.parseColor("#8C" + eventColor3Opacity40));

        int color = Color.parseColor(eventColor1);
        holder.moreIV.setColorFilter(Color.parseColor(eventColor3), PorterDuff.Mode.SRC_ATOP);
        holder.iv_like.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        holder.iv_comments.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        holder.iv_share.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        holder.moreIV.setAlpha(180);
        holder.iv_like.setAlpha(180);
        holder.iv_comments.setAlpha(180);
        holder.iv_share.setAlpha(180);
    }
}