package com.procialize.bayer2020.ui.newsFeedComment.adapter;

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
import com.procialize.bayer2020.ui.attendeeChat.ChatActivity;
import com.procialize.bayer2020.ui.newsFeedComment.model.CommentDetail;
import com.procialize.bayer2020.ui.newsfeed.PaginationUtils.PaginationAdapterCallback;
import com.procialize.bayer2020.ui.newsfeed.viewmodel.NewsFeedDatabaseViewModel;

import org.jsoup.Jsoup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_5;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.NewsViewHolder> {

    Context context;
    List<CommentDetail> commentDetails;
    String name1 = "", substring;
    CommentAdapterListner listener;
    String eventColor1, eventColor2, eventColor3, eventColor4, eventColor5;
    NewsFeedDatabaseViewModel newsFeedDatabaseViewModel;
    String spannedString;
    String postStatus;
    private PaginationAdapterCallback mCallback;
    private boolean retryPageLoad = false;
    private boolean isLoadingAdded = false;

    public CommentAdapter(Context context/*, List<CommentDetail> commentDetails*/, CommentAdapterListner listener) {
        this.context = context;
        //this.commentDetails = commentDetails;
        this.commentDetails = new ArrayList<>();
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

        try {
            //Newsfeed_detail feedData = feed_detail.get(position);
            CommentDetail comments = commentDetails.get(position);

            if (comments.getComment_id() != null) {
                holder.ll_root.setVisibility(View.VISIBLE);
            } else {
                holder.ll_root.setVisibility(View.GONE);
            }

            if (position + 2 == commentDetails.size()) {
                holder.v_divider.setVisibility(View.INVISIBLE);
            } else {
                holder.v_divider.setVisibility(View.VISIBLE);
            }

            if (comments.getProfile_picture().trim() != null) {

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

            if (comments.getComment().contains("gif")) {
                //name1 = "<font color='#D81B60'>" + comments.getFirst_name() + " " + comments.getLast_name() + " " + "</font>";
                name1 = "<font color='" + eventColor4 + "'>" + comments.getFirst_name() + " " + comments.getLast_name() + " " + "</font>";
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
                    holder.tv_name.setText(Html.fromHtml(name1));
                } else {
                    holder.tv_name.setText(Html.fromHtml(name1, Html.FROM_HTML_MODE_LEGACY));
                }
                holder.fl_gif.setVisibility(View.VISIBLE);
                holder.tv_comment.setVisibility(View.INVISIBLE);
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

                    name1 = "<font color='" + eventColor1 + "'>" + comments.getFirst_name() + " " + comments.getLast_name() + " " + "</font>";
                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
                        holder.tv_name.setText(Html.fromHtml(name1));
                    } else {
                        holder.tv_name.setText(Html.fromHtml(name1, Html.FROM_HTML_MODE_LEGACY));
                    }

                    /**
                     * Code for HTML text + Tagging
                     */
                    if (comments.getComment().contains("\n")) {
                        postStatus = comments.getComment().trim().replace("\n", "<br/>");
                    } else {
                        postStatus = comments.getComment().trim();
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
                    if (comments.getComment() != null) {
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
                                                                                } else {
                                                                                    context.startActivity(new Intent(context, ChatActivity.class)
                                                                                            .putExtra("page", "ListPage")
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
                                                                                } else {
                                                                                    context.startActivity(new Intent(context, ChatActivity.class)
                                                                                            .putExtra("page", "ListPage")
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


                        String eventColor4Opacity40 = eventColor3.replace("#", "");
                        holder.tv_comment.setMovementMethod(LinkMovementMethod.getInstance());
                        holder.tv_comment.setText(stringBuilder);
                        holder.tv_comment.setTextColor(Color.parseColor(eventColor3));
                    } else {
                        holder.tv_comment.setVisibility(View.GONE);
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }

            holder.tv_date_time.setText(CommonFunction.convertDate(comments.getDateTime()));

            String eventColor3Opacity40 = eventColor3.replace("#", "");
            holder.tv_date_time.setTextColor(Color.parseColor("#66" + eventColor3Opacity40));
            int color = Color.parseColor(eventColor3);
            holder.iv_options.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            holder.iv_options.setAlpha(150);
            // holder.tv_name.setAlpha(0.5f);
            holder.ll_root.setBackgroundColor(Color.parseColor(eventColor2));
            holder.v_divider.setBackgroundColor(Color.parseColor("#66" + eventColor3Opacity40));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return commentDetails.size();
    }

    public interface CommentAdapterListner {
        void onMoreSelected(CommentDetail comment, int position);
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_date_time, testdata, tv_comment;
        FrameLayout fl_gif;
        ImageView iv_gif, iv_options, iv_profile;
        ProgressBar pb_gif, progressView;
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
            tv_comment = itemView.findViewById(R.id.tv_comment);

            iv_options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMoreSelected(commentDetails.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }

    public void add(CommentDetail r) {
        commentDetails.add(r);
        notifyItemInserted(commentDetails.size() - 1);
    }

    public void addAll(List<CommentDetail> moveResults) {
        for (CommentDetail result : moveResults) {
            add(result);
        }
    }

    public void remove(CommentDetail r) {
        int position = commentDetails.indexOf(r);
        if (position > -1) {
            commentDetails.remove(position);
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
        add(new CommentDetail());
    }

    public void removeLoadingFooter() {
        try {
            isLoadingAdded = false;

            int position = commentDetails.size() - 1;
            CommentDetail result = getItem(position);

            if (result != null) {
                commentDetails.remove(position);
                notifyItemRemoved(position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CommentDetail getItem(int position) {
        return commentDetails.get(position);
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(commentDetails.size() - 1);
    }


    public List<CommentDetail> getCommentDetails() {
        return commentDetails;
    }
}