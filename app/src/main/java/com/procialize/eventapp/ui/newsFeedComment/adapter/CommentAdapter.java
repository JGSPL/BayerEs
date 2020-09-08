package com.procialize.eventapp.ui.newsFeedComment.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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
import com.procialize.eventapp.ui.newsFeedComment.model.Comment;
import com.procialize.eventapp.ui.newsFeedComment.model.CommentDetail;
import com.procialize.eventapp.ui.newsfeed.adapter.SwipeMultimediaAdapter;
import com.procialize.eventapp.ui.newsfeed.model.News_feed_media;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JzvdStd;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.NewsViewHolder> {

    Context context;
    List<CommentDetail> commentDetails;
    String name1 = "";
    CommentAdapterListner listener;

    public CommentAdapter(Context context, List<CommentDetail> commentDetails, CommentAdapterListner listener) {
        this.context = context;
        this.commentDetails = commentDetails;
        this.listener = listener;
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
            name1 = "<font color='#D81B60'>" + comments.getFirst_name() + " " + comments.getLast_name() + " " + "</font>";
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
            name1 = "<font color='#D81B60'>" + comments.getFirst_name() + " " + comments.getLast_name() + " " + "</font>" + comments.getComment();
            holder.fl_gif.setVisibility(View.GONE);
        }
        holder.tv_name.setMovementMethod(LinkMovementMethod.getInstance());
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            holder.tv_name.setText(Html.fromHtml(name1));
            //holder.tv_name.append(stringBuilder);
        } else {
            holder.tv_name.setText(Html.fromHtml(name1, Html.FROM_HTML_MODE_LEGACY));   //set text
            // holder.tv_name.append(stringBuilder);   //append text into textView
        }
        holder.tv_date_time.setText(CommonFunction.convertDate(comments.getDateTime()));


    }

    @Override
    public int getItemCount() {
        return commentDetails.size();
    }

    public interface CommentAdapterListner {
        void onMoreSelected(CommentDetail comment, int position);
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_date_time;
        FrameLayout fl_gif;
        ImageView iv_gif, iv_options,iv_profile;
        ProgressBar pb_gif,progressView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_date_time = itemView.findViewById(R.id.tv_date_time);
            fl_gif = itemView.findViewById(R.id.fl_gif);
            iv_gif = itemView.findViewById(R.id.iv_gif);
            iv_options = itemView.findViewById(R.id.iv_options);
            iv_profile = itemView.findViewById(R.id.iv_profile);
            pb_gif = itemView.findViewById(R.id.pb_gif);
            progressView = itemView.findViewById(R.id.progressView);

            iv_options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMoreSelected(commentDetails.get(getAdapterPosition()), getAdapterPosition());
                }
            });

        }
    }
}