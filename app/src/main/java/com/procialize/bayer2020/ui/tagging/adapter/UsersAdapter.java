package com.procialize.bayer2020.ui.tagging.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.ui.attendee.roomDB.TableAttendee;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * Adapter to the mentions list shown to display the result of an '@' mention.
 */
public class UsersAdapter extends RecyclerArrayAdapter<TableAttendee, UsersAdapter.UserViewHolder> {

    /**
     * {@link Context}.
     */
    private final Context context;

    /**
     * Current search string typed by the user.  It is used highlight the query in the
     * search results.  Ex: @bill.
     */
    private String currentQuery;

    /**
     * {@link ForegroundColorSpan}.
     */
    private final ForegroundColorSpan colorSpan;
    public UsersAdapter(final Context context) {
        this.context = context;
        final int orange = ContextCompat.getColor(context, R.color.mentions_default_color);
        this.colorSpan = new ForegroundColorSpan(orange);
    }

    /**
     * Setter for what user has queried.
     */
    public void setCurrentQuery(final String currentQuery) {
        if (StringUtils.isNotBlank(currentQuery)) {
            this.currentQuery = currentQuery.toLowerCase(Locale.US);
        }
    }

    /**
     * Create UI with views for user name and picture.
     */
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    /**
     * Display user name and picture.
     */
    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        final TableAttendee mentionsUser = getItem(position);

        if (mentionsUser != null && StringUtils.isNotBlank(mentionsUser.getFirst_name())) {

            String profilePic =mentionsUser.getProfile_picture();
            Glide.with(context)
                    .load(profilePic)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(holder.imageView);


            holder.name.setText(mentionsUser.getFirst_name()+ " " + mentionsUser.getLast_name(), TextView.BufferType.SPANNABLE);
            highlightSearchQueryInUserName(holder.name.getText());
        }
    }

    /**
     * Highlights the current search text in the mentions list.
     */
    private void highlightSearchQueryInUserName(CharSequence userName) {
        if (StringUtils.isNotBlank(currentQuery)) {
            int searchQueryLocation = userName.toString().toLowerCase(Locale.US).indexOf(currentQuery);

            if (searchQueryLocation != -1) {
                Spannable userNameSpannable = (Spannable) userName;
                userNameSpannable.setSpan(
                        colorSpan,
                        searchQueryLocation,
                        searchQueryLocation + currentQuery.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    /**
     * View holder for user.
     */
    static class UserViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final ImageView imageView;
        String image_url;

        UserViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.user_full_name);
            imageView = itemView.findViewById(R.id.user_picture);
        }
    }
}

