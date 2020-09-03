package com.procialize.eventapp.ui.newsFeedComment.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.procialize.eventapp.ui.newsFeedComment.model.Comment;
import com.procialize.eventapp.ui.newsFeedComment.model.CommentDetail;
import com.procialize.eventapp.ui.newsfeed.adapter.SwipeMultimediaAdapter;
import com.procialize.eventapp.ui.newsfeed.model.News_feed_media;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JzvdStd;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.eventapp.Constants.Constant.MY_PREFS_NAME;
import static com.procialize.eventapp.Constants.Constant.NEWS_FEED_MEDIA_PATH;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.NewsViewHolder> {

    Context context;
    ArrayList<CommentDetail> commentDetails;
    public CommentAdapter(Context context, ArrayList<CommentDetail> commentDetails, CommentAdapterListner listener) {
        this.context = context;
        this.commentDetails = commentDetails;
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

    }

    @Override
    public int getItemCount() {
        return commentDetails.size();
    }

    public interface CommentAdapterListner {

    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv;


        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);


        }
    }
}