package com.procialize.eventapp.ui.newsFeedLike.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.ui.newsFeedLike.model.LikeDetail;
import com.procialize.eventapp.ui.newsfeed.PaginationUtils.PaginationAdapterCallback;

import java.util.ArrayList;
import java.util.List;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_5;


public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.MyViewHolder> {

    private List<LikeDetail> likeDetails;
    private Context context;
    String eventColor1, eventColor2, eventColor3, eventColor4, eventColor5;

    private PaginationAdapterCallback mCallback;
    private boolean retryPageLoad = false;
    private boolean isLoadingAdded = false;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public ImageView iv_profile;
        ProgressBar pb_profile;
        LinearLayout ll_main;
        View v_divider;

        public MyViewHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            iv_profile = view.findViewById(R.id.iv_profile);
            pb_profile = view.findViewById(R.id.pb_profile);
            ll_main = view.findViewById(R.id.ll_main);
            v_divider = view.findViewById(R.id.v_divider);

        }
    }


    public LikeAdapter(Context context/*, List<LikeDetail> likeDetails*/) {
        //this.likeDetails = likeDetails;
        this.likeDetails = new ArrayList<>();
        this.context = context;
        eventColor1 = SharedPreference.getPref(context, EVENT_COLOR_1);
        eventColor2 = SharedPreference.getPref(context, EVENT_COLOR_2);
        eventColor3 = SharedPreference.getPref(context, EVENT_COLOR_3);
        eventColor4 = SharedPreference.getPref(context, EVENT_COLOR_4);
        eventColor5 = SharedPreference.getPref(context, EVENT_COLOR_5);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_likes, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        LikeDetail likeDetail = likeDetails.get(position);

        if (likeDetail.getLike_id() != null) {
            holder.itemView.setVisibility(View.VISIBLE);
        } else {
            holder.itemView.setVisibility(View.GONE);
        }
        if (position + 1 == likeDetails.size()) {
            holder.v_divider.setVisibility(View.INVISIBLE);
        } else {
            holder.v_divider.setVisibility(View.VISIBLE);
        }

        if (likeDetail.getProfile_picture() != null) {
            Glide.with(context).load(likeDetail.getProfile_picture().trim()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    holder.iv_profile.setImageResource(R.drawable.profilepic_placeholder);
                    holder.pb_profile.setVisibility(View.GONE);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    holder.pb_profile.setVisibility(View.GONE);
                    return false;
                }
            }).into(holder.iv_profile);
        } else {
            holder.iv_profile.setImageResource(R.drawable.profilepic_placeholder);
        }

        holder.tv_name.setText(likeDetail.getFirst_name() + " " + likeDetail.getLast_name());
        holder.ll_main.setBackgroundColor(Color.parseColor(eventColor2));
        holder.tv_name.setBackgroundColor(Color.parseColor(eventColor2));
        holder.tv_name.setTextColor(Color.parseColor(eventColor1));
        holder.v_divider.setBackgroundColor(Color.parseColor(eventColor3));
    }

    @Override
    public int getItemCount() {
        return likeDetails.size();
    }

    public void add(LikeDetail r) {
        likeDetails.add(r);
        notifyItemInserted(likeDetails.size() - 1);
    }

    public void addAll(List<LikeDetail> moveResults) {
        for (LikeDetail result : moveResults) {
            add(result);
        }
    }

    public void remove(LikeDetail r) {
        int position = likeDetails.indexOf(r);
        if (position > -1) {
            likeDetails.remove(position);
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
        add(new LikeDetail());
    }

    public void removeLoadingFooter() {
        try {
            isLoadingAdded = false;

            int position = likeDetails.size() - 1;
            LikeDetail result = getItem(position);

            if (result != null) {
                likeDetails.remove(position);
                notifyItemRemoved(position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LikeDetail getItem(int position) {
        return likeDetails.get(position);
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(likeDetails.size() - 1);
    }


    public List<LikeDetail> getLikeDetails() {
        return likeDetails;
    }
}
