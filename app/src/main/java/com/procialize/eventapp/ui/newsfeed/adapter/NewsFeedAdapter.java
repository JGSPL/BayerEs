package com.procialize.eventapp.ui.newsfeed.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.costumTools.ClickableViewPager;
import com.procialize.eventapp.costumTools.ScaledImageView;
import com.procialize.eventapp.ui.newsfeed.PaginationUtils.PaginationAdapterCallback;
import com.procialize.eventapp.ui.newsfeed.model.News_feed_media;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JzvdStd;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.NEWS_FEED_MEDIA_PATH;

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
    public NewsFeedAdapter() {

    }
    public NewsFeedAdapter(Context context, ArrayList<Newsfeed_detail> feed_detail, FeedAdapterListner listener) {
        this.context = context;
        this.feed_detail = feed_detail;
        this.listener = listener;

      //  this.mCallback = (PaginationAdapterCallback) context;


        cd = new ConnectionDetector();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.newsfeed_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsViewHolder holder, final int position) {
        Newsfeed_detail feedData = feed_detail.get(position);
        try {

            if(feedData.getFirst_name().equalsIgnoreCase("null")){
                holder.root.setVisibility(View.GONE);
            }else{
                holder.root.setVisibility(View.VISIBLE);
            }
            mediaPath = SharedPreference.getPref(context,NEWS_FEED_MEDIA_PATH);
            
            holder.nameTv.setText(feedData.getFirst_name() + " " + feedData.getLast_name());
            if (feedData.getCity_id() != null && !(feedData.getCity_id().equalsIgnoreCase(""))) {
                holder.designationTv.setText(feedData.getDesignation() + " - " + feedData.getCity_id());
            } else {
                holder.designationTv.setText(feedData.getDesignation());
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

            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (feedData.getNews_feed_media().size() > 0) {
                        listener.onContactSelected(feed_detail.get(position), position);
                    }
                }
            });

            if (feedData.getTotal_comments().equalsIgnoreCase("1")) {
                holder.tv_comment.setText(feedData.getTotal_comments() + " Comment");
            } else {
                holder.tv_comment.setText(feedData.getTotal_comments() + " Comments");
            }

            if (feedData.getTotal_likes().equalsIgnoreCase("1")) {
                holder.tv_like.setText(feedData.getTotal_likes() + " Like");
            } else {
                holder.tv_like.setText(feedData.getTotal_likes() + " Likes");

                holder.vp_slider.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (feedData.getNews_feed_media().size() > 0) {

                            listener.onContactSelected(feed_detail.get(position), position);
                        }
                    }
                });
            }

            holder.tv_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCommentClick(feed_detail.get(position), position);
                }
            });
            holder.iv_comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCommentClick(feed_detail.get(position), position);
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

            holder.iv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cd.isConnectingToInternet()) {
                        listener.likeTvViewOnClick(v, feed_detail.get(position), position, holder.iv_like, holder.tv_like);
                    } else {
                        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            if (!feedData.getPost_status().isEmpty() && feedData.getPost_status() != null) {
                holder.tv_status.setText(feedData.getPost_status());
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
                        setupPagerIndidcatorDots(0, holder.ll_dots, imagesSelectednew.size());
                        holder.vp_slider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                JzvdStd.goOnPlayOnPause();
                            }

                            @Override
                            public void onPageSelected(int position1) {
                                JzvdStd.goOnPlayOnPause();
                                swipableAdapterPosition = position1;
                                setupPagerIndidcatorDots(position1, holder.ll_dots, imagesSelectednew.size());
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                                JzvdStd.goOnPlayOnPause();
                            }
                        });
                    } else {
                        holder.ll_dots.setVisibility(View.GONE);
                    }
                } else /*if (feedData.getNews_feed_media().size() == 0)*/ {
                    holder.ll_dots.setVisibility(View.GONE);
                    holder.vp_slider.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return feed_detail.size();
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

        void onCommentClick(Newsfeed_detail feed, int position);

        void onLikeClick(Newsfeed_detail feed, int position);

        void onSliderClick(Newsfeed_detail feed, int position);

        void moreTvFollowOnClick(View v, Newsfeed_detail feed, int position);

        void likeTvViewOnClick(View v, Newsfeed_detail feed, int position, ImageView likeimage, TextView liketext);

    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv;
        TextView designationTv, tv_concat, companyTv, dateTv, tv_status, testdata;
        TextView tv_like, tv_comment;
        ImageView moreIV, profileIV, iv_comments, iv_like;
        ProgressBar progressView;
        ViewPager vp_slider;
        LinearLayout ll_dots, ll_bottom;
        LinearLayout root;

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

            progressView = itemView.findViewById(R.id.progressView);
            root = itemView.findViewById(R.id.root);
            moreIV = itemView.findViewById(R.id.moreIV);

            ll_bottom = itemView.findViewById(R.id.ll_bottom);
        }
    }

    private void setupPagerIndidcatorDots(int currentPage, LinearLayout ll_dots, int size) {

        TextView[] dots = new TextView[size];
        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(context);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(Color.parseColor("#343434"));
            ll_dots.addView(dots[i]);
        }

        try {
            if (dots.length > 0) {
                if (dots.length != currentPage) {
                    dots[currentPage].setTextColor(Color.parseColor("#A2A2A2"));
                    // dots[currentPage].setTextColor(Color.parseColor(colorActive));
                }
            }
        } catch (Exception e) {

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
}