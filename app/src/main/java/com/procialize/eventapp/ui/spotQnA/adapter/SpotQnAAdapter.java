package com.procialize.eventapp.ui.spotQnA.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.style.IconMarginSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.Utility.Utility;
import com.procialize.eventapp.ui.eventList.model.EventList;
import com.procialize.eventapp.ui.eventList.view.EventListActivity;
import com.procialize.eventapp.ui.spotQnA.model.SpotQnA;
import com.procialize.eventapp.ui.spotQnA.model.SpotQnAList;

import java.util.ArrayList;
import java.util.List;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.PROFILE_PIC_MEDIA_PATH;

public class SpotQnAAdapter extends RecyclerView.Adapter<SpotQnAAdapter.SpotQnAViewHolder> {

    Context context;
    List<SpotQnA> spotQnAList;
    String mediaPath;
    SpotQnAAdapter.EventAdapterListner listener;

    public SpotQnAAdapter(Context context, List<SpotQnA> spotQnALists, SpotQnAAdapter.EventAdapterListner listener) {
        this.context = context;
        this.spotQnAList = spotQnALists;
        this.listener = listener;

        mediaPath = SharedPreference.getPref(context, PROFILE_PIC_MEDIA_PATH);
    }

    @NonNull
    @Override
    public SpotQnAAdapter.SpotQnAViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_spot_qna, parent, false);
        return new SpotQnAAdapter.SpotQnAViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SpotQnAAdapter.SpotQnAViewHolder holder, final int position) {
        final SpotQnA spotQnA = spotQnAList.get(position);
        holder.tv_name.setText(spotQnA.getOrg_first_name() + " " + spotQnA.getOrg_last_name());
        holder.tv_time.setText(CommonFunction.convertDateToTime(spotQnA.getCreated()));
        holder.tv_question.setText(spotQnA.getQuestion());

        if (spotQnA.getLike_flag().equalsIgnoreCase("1")) {
            holder.iv_like.setImageDrawable(context.getDrawable(R.drawable.ic_active_like));
        } else {
            holder.iv_like.setImageDrawable(context.getDrawable(R.drawable.ic_like));
        }

        if (!spotQnA.getReply().isEmpty() && spotQnA.getReply() != null) {
            holder.tv_answer.setText(spotQnA.getReply());
            holder.tr_answer.setVisibility(View.VISIBLE);
        } else {
            holder.tv_answer.setVisibility(View.GONE);
            holder.tr_answer.setVisibility(View.GONE);
        }
        if (spotQnA.getTotal_likes().equalsIgnoreCase("1")) {
            holder.tv_like_count.setText(spotQnA.getTotal_likes() + " Like");
        } else {
            holder.tv_like_count.setText(spotQnA.getTotal_likes() + " Likes");
        }

        if (spotQnA.getProfile_picture() != null) {
            Glide.with(context).load(mediaPath+spotQnA.getProfile_picture().trim()).listener(new RequestListener<Drawable>() {
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

        holder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onLikeClicked(spotQnA, position, holder.tv_like_count, holder.iv_like);
            }
        });
    }

    @Override
    public int getItemCount() {
        return spotQnAList.size();
    }

    public interface EventAdapterListner {
        void onLikeClicked(SpotQnA spotQnA, int position, TextView textView, ImageView imageView);
    }

    public class SpotQnAViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_profile, iv_like;
        ProgressBar pb_profile;
        TextView tv_name, tv_time, tv_question, tv_answer, tv_like_count;
        TableRow tr_answer;

        public SpotQnAViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_profile = itemView.findViewById(R.id.iv_profile);
            pb_profile = itemView.findViewById(R.id.pb_profile);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_question = itemView.findViewById(R.id.tv_question);
            tv_answer = itemView.findViewById(R.id.tv_answer);
            iv_like = itemView.findViewById(R.id.iv_like);
            tv_like_count = itemView.findViewById(R.id.tv_like_count);
            tr_answer = itemView.findViewById(R.id.tr_answer);

        }
    }
}