package com.procialize.eventapp.ui.newsFeedLike.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.procialize.eventapp.ui.newsFeedLike.model.AttendeeList;

import java.util.List;


public class LikeAdapter  extends RecyclerView.Adapter<LikeAdapter.MyViewHolder> {

    private List<AttendeeList> attendeeListList;
    private Context context;
    String MY_PREFS_NAME = "ProcializeInfo";


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public ImageView iv_profile;
        ProgressBar pb_profile;

        public MyViewHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            iv_profile = view.findViewById(R.id.iv_profile);
            pb_profile = view.findViewById(R.id.pb_profile);

        }
    }


    public LikeAdapter(Context context, List<AttendeeList> attendeeListList) {
        this.attendeeListList = attendeeListList;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_likes, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        AttendeeList attendeeList = attendeeListList.get(position);

        if (attendeeListList.get(position).getProfilePic() != null) {
            Glide.with(context).load(attendeeListList.get(position).getProfilePic()).listener(new RequestListener<Drawable>() {
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

        holder.tv_name.setText(attendeeList.getFirstName() + " " + attendeeList.getLastName());
    }

    @Override
    public int getItemCount() {
        return attendeeListList.size();
    }
}
