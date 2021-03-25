package com.procialize.bayer2020.ui.notification.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.attendee.model.Attendee;
import com.procialize.bayer2020.ui.attendee.roomDB.TableAttendee;
import com.procialize.bayer2020.ui.attendee.view.AttendeeDetailActivity;
import com.procialize.bayer2020.ui.newsfeed.PaginationUtils.PaginationAdapterCallback;
import com.procialize.bayer2020.ui.newsfeed.viewmodel.NewsFeedDatabaseViewModel;
import com.procialize.bayer2020.ui.notification.model.NotificationList;

import org.jsoup.Jsoup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    Context context;
    List<NotificationList> notificationLists;
    String name1 = "", substring;
    NotificationAdapterListner listener;
    NewsFeedDatabaseViewModel newsFeedDatabaseViewModel;
    String spannedString;
    String postStatus;
    private PaginationAdapterCallback mCallback;
    private boolean retryPageLoad = false;
    private boolean isLoadingAdded = false;

    public NotificationAdapter(Context context, NotificationAdapterListner listener) {
        this.context = context;
        this.notificationLists = new ArrayList<>();
        this.listener = listener;

        newsFeedDatabaseViewModel = ViewModelProviders.of((FragmentActivity) context).get(NewsFeedDatabaseViewModel.class);

    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationViewHolder holder, final int position) {

        try {
            //Newsfeed_detail feedData = feed_detail.get(position);
            final NotificationList comments = notificationLists.get(position);

            if (comments.getNotification_id() != null) {
                holder.ll_root.setVisibility(View.VISIBLE);
            } else {
                holder.ll_root.setVisibility(View.GONE);
            }

            if (position + 2 == notificationLists.size()) {
                holder.v_divider.setVisibility(View.INVISIBLE);
            } else {
                holder.v_divider.setVisibility(View.VISIBLE);
            }

            //name1 = "<font color='#D81B60'>" + comments.getFirst_name() + " " + comments.getLast_name() + " " + "</font>" + comments.getComment();
           /* name1 = "<font color='"+eventColor1 +"'>" + comments.getFirst_name() + " " + comments.getLast_name() + " " + "</font>" + " " +
                    "<font color='"+eventColor3 +"'>" + comments.getComment() + "</font>" ;*/

            if (!comments.getMedia_type().isEmpty()) {
                holder.fl_gif.setVisibility(View.VISIBLE);
                if (comments.getContent() != null) {
                    holder.tv_comment.setVisibility(View.VISIBLE);
                    if (comments.getContent().contains("\n")) {
                        postStatus = comments.getContent().trim().replace("\n", "<br/>");
                    } else {
                        postStatus = comments.getContent().trim();
                    }
                    spannedString = String.valueOf(Jsoup.parse(postStatus)).trim();//Html.fromHtml(feedData.getPost_status(), Html.FROM_HTML_MODE_COMPACT).toString();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Spanned strPost = Html.fromHtml(spannedString, Html.FROM_HTML_MODE_COMPACT);
                        holder.testdata.setText(Utility.trimTrailingWhitespace(strPost));
                        holder.tv_comment.setText(Utility.trimTrailingWhitespace(strPost));

                    } else {
                        Spanned strPost = Html.fromHtml(spannedString);
                        holder.testdata.setText(Utility.trimTrailingWhitespace(strPost));
                        holder.tv_comment.setText(Utility.trimTrailingWhitespace(strPost));

                    }
                }
                name1 = comments.getFirst_name() + " " + comments.getLast_name();
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    holder.tv_name.setText(Html.fromHtml(name1));
                } else {
                    holder.tv_name.setText(Html.fromHtml(name1, Html.FROM_HTML_MODE_LEGACY));
                }
                Glide.with(context).load(comments.getMedia_url().trim()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.iv_gif.setImageResource(R.drawable.profilepic_placeholder);
                        holder.pb_gif.setVisibility(View.GONE);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.pb_gif.setVisibility(View.GONE);
                        return false;
                    }
                }).into(holder.iv_gif);
            } else {
                holder.fl_gif.setVisibility(View.GONE);

                try {

                    name1 = comments.getFirst_name() + " " + comments.getLast_name();
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        holder.tv_name.setText(Html.fromHtml(name1));
                    } else {
                        holder.tv_name.setText(Html.fromHtml(name1, Html.FROM_HTML_MODE_LEGACY));
                    }

                    /**
                     * Code for HTML text + Tagging
                     */
                    if (comments.getContent().contains("\n")) {
                        postStatus = comments.getContent().trim().replace("\n", "<br/>");
                    } else {
                        postStatus = comments.getContent().trim();
                    }
                    spannedString = String.valueOf(Jsoup.parse(postStatus)).trim();//Html.fromHtml(feedData.getPost_status(), Html.FROM_HTML_MODE_COMPACT).toString();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Spanned strPost = Html.fromHtml(spannedString, Html.FROM_HTML_MODE_COMPACT);
                        holder.testdata.setText(Utility.trimTrailingWhitespace(strPost));
                    } else {
                        Spanned strPost = Html.fromHtml(spannedString);
                        holder.testdata.setText(Utility.trimTrailingWhitespace(strPost));
                    }

                    //holder.testdata.setText(comments.getComment());//.replace("\n",System.getProperty("line.separator")));

                    final SpannableStringBuilder stringBuilder = new SpannableStringBuilder(holder.testdata.getText());
                    if (comments.getContent() != null) {
                        holder.tv_comment.setVisibility(View.VISIBLE);
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
                                                        newsFeedDatabaseViewModel.getAttendeeDetailsFromId(context, attendeeid);
                                                        newsFeedDatabaseViewModel.getAttendeeDetails().observe((LifecycleOwner) context, new Observer<List<TableAttendee>>() {
                                                            @Override
                                                            public void onChanged(List<TableAttendee> tableAttendees) {
                                                                if (tableAttendees != null) {
                                                                    if (tableAttendees.size() > 0) {
                                                                    /*Intent intent = new Intent(context, AttendeeDetailActivity.class);
                                                                    intent.putExtra("fname", tableAttendees.get(0).getFirst_name());
                                                                    intent.putExtra("lname", tableAttendees.get(0).getLast_name());
                                                                    intent.putExtra("company", tableAttendees.get(0).getCompany_name());
                                                                    intent.putExtra("city", tableAttendees.get(0).getCity());
                                                                    intent.putExtra("designation", tableAttendees.get(0).getDesignation());
                                                                    intent.putExtra("prof_pic", tableAttendees.get(0).getProfile_picture());
                                                                    intent.putExtra("attendee_type", tableAttendees.get(0).getAttendee_type());
                                                                    intent.putExtra("mobile", tableAttendees.get(0).getMobile());
                                                                    intent.putExtra("email", tableAttendees.get(0).getEmail());
                                                                    context.startActivity(intent);*/
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

                                                                        if (tableAttendees.get(0).getFirebase_id() != null) {
                                                                            if (tableAttendees.get(0).getFirebase_id().equalsIgnoreCase("0")) {
                                                                                context.startActivity(new Intent(context, AttendeeDetailActivity.class)
                                                                                        .putExtra("Attendee", (Serializable) attendee));
                                                                            } else {
                                                                                if (tableAttendees.get(0).getFirebase_status().equalsIgnoreCase("0")) {
                                                                                    context.startActivity(new Intent(context, AttendeeDetailActivity.class)
                                                                                            .putExtra("Attendee", (Serializable) attendee));
                                                                                }
                                                                            }
                                                                        }
                                                                    }

                                                                    if (newsFeedDatabaseViewModel != null && newsFeedDatabaseViewModel.getAttendeeDetails().hasObservers()) {
                                                                        newsFeedDatabaseViewModel.getAttendeeDetails().removeObservers((LifecycleOwner) context);
                                                                    }
                                                                }
                                                            }
                                                        });
                                                    }
                                                }, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                stringBuilder.replace(start, end + 1, substring);
                                                holder.testdata.setText(stringBuilder, TextView.BufferType.SPANNABLE);
                                                holder.tv_comment.setMovementMethod(LinkMovementMethod.getInstance());
                                                holder.tv_comment.setText(stringBuilder);
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
                                                                    if (tableAttendees.size() > 0) {
                                                                    /*Intent intent = new Intent(context, AttendeeDetailActivity.class);
                                                                    intent.putExtra("fname", tableAttendees.get(0).getFirst_name());
                                                                    intent.putExtra("lname", tableAttendees.get(0).getLast_name());
                                                                    intent.putExtra("company", tableAttendees.get(0).getCompany_name());
                                                                    intent.putExtra("city", tableAttendees.get(0).getCity());
                                                                    intent.putExtra("designation", tableAttendees.get(0).getDesignation());
                                                                    intent.putExtra("prof_pic", tableAttendees.get(0).getProfile_picture());
                                                                    intent.putExtra("attendee_type", tableAttendees.get(0).getAttendee_type());
                                                                    intent.putExtra("mobile", tableAttendees.get(0).getMobile());
                                                                    intent.putExtra("email", tableAttendees.get(0).getEmail());
                                                                    context.startActivity(intent);*/
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

                                                                        if (tableAttendees.get(0).getFirebase_id() != null) {
                                                                            if (tableAttendees.get(0).getFirebase_id().equalsIgnoreCase("0")) {
                                                                                context.startActivity(new Intent(context, AttendeeDetailActivity.class)
                                                                                        .putExtra("Attendee", (Serializable) attendee));
                                                                            } else {
                                                                                if (tableAttendees.get(0).getFirebase_status().equalsIgnoreCase("0")) {
                                                                                    context.startActivity(new Intent(context, AttendeeDetailActivity.class)
                                                                                            .putExtra("Attendee", (Serializable) attendee));
                                                                                }
                                                                            }
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
                                                holder.testdata.setText(stringBuilder, TextView.BufferType.SPANNABLE);
                                                holder.tv_comment.setMovementMethod(LinkMovementMethod.getInstance());
                                                holder.tv_comment.setText(stringBuilder);

                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }


                        holder.tv_comment.setMovementMethod(LinkMovementMethod.getInstance());
                        holder.tv_comment.setText(stringBuilder);
                    } else {
                        holder.tv_comment.setVisibility(View.GONE);
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
            holder.tv_date_time.setText(CommonFunction.convertDate(comments.getDatetime()));



            if (comments.getProfile_pic() != null) {
                Glide.with(context).load(comments.getProfile_pic().trim()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.iv_profile.setImageResource(R.drawable.profilepic_placeholder);
                        //holder.pb_profile.setVisibility(View.GONE);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //holder.pb_profile.setVisibility(View.GONE);
                        return false;
                    }
                }).into(holder.iv_profile);
            } else {
                holder.iv_profile.setImageResource(R.drawable.profilepic_placeholder);
            }

            // holder.tv_name.setAlpha(0.5f);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return notificationLists.size();
    }

    public interface NotificationAdapterListner {
        void onMoreSelected(NotificationList comment, int position);
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_date_time, testdata, tv_comment;
        FrameLayout fl_gif;
        ImageView iv_gif, iv_profile;
        ProgressBar pb_gif, progressView;
        LinearLayout ll_root, ll_main;
        View v_divider;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            testdata = itemView.findViewById(R.id.testdata);
            tv_date_time = itemView.findViewById(R.id.tv_date_time);
            fl_gif = itemView.findViewById(R.id.fl_gif);
            iv_gif = itemView.findViewById(R.id.iv_gif);
            iv_profile = itemView.findViewById(R.id.iv_profile);
            pb_gif = itemView.findViewById(R.id.pb_gif);
            progressView = itemView.findViewById(R.id.progressView);
            ll_root = itemView.findViewById(R.id.ll_root);
            ll_main = itemView.findViewById(R.id.ll_main);
            v_divider = itemView.findViewById(R.id.v_divider);
            tv_comment = itemView.findViewById(R.id.tv_comment);

            ll_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMoreSelected(notificationLists.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }

    public void add(NotificationList r) {
        notificationLists.add(r);
        notifyItemInserted(notificationLists.size() - 1);
    }

    public void addAll(List<NotificationList> moveResults) {
        for (NotificationList result : moveResults) {
            add(result);
        }
    }

    public void remove(NotificationList r) {
        int position = notificationLists.indexOf(r);
        if (position > -1) {
            notificationLists.remove(position);
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
        add(new NotificationList());
    }

    public void removeLoadingFooter() {
        try {
            isLoadingAdded = false;

            int position = notificationLists.size() - 1;
            NotificationList result = getItem(position);

            if (result != null) {
                notificationLists.remove(position);
                notifyItemRemoved(position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NotificationList getItem(int position) {
        return notificationLists.get(position);
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(notificationLists.size() - 1);
    }


    public List<NotificationList> getNotifications() {
        return notificationLists;
    }
}