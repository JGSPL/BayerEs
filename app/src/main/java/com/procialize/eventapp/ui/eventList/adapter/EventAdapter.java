package com.procialize.eventapp.ui.eventList.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EXPIRY_TIME;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.NewsViewHolder>  implements Filterable {

    Context context;
    List<EventList> eventLists;
    List<EventList> eventListsFilter;
    String mediaPath;
    EventAdapter.EventAdapterListner listener;
    public static boolean isClickable = true;

    public EventAdapter(Context context, List<EventList> eventLists, EventAdapter.EventAdapterListner listener) {
        this.context = context;
        this.eventLists = eventLists;
        this.eventListsFilter = eventLists;
        this.listener = listener;

        mediaPath = SharedPreference.getPref(context,EVENT_LIST_MEDIA_PATH);
    }

    @NonNull
    @Override
    public EventAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_event_list, parent, false);
        return new EventAdapter.NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventAdapter.NewsViewHolder holder, final int position) {
        //Newsfeed_detail feedData = feed_detail.get(position);
        final EventList events = eventListsFilter.get(position);

        if (events.getEvent_image().trim() != null) {
            Glide.with(context)
                    .load(mediaPath+events.getEvent_image().trim())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            //holder.progressView.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            //holder.progressView.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(holder.iv_event_logo);
        }
        //holder.iv_event_logo
        holder.tv_date_time.setText(CommonFunction.convertDateToDDMMYYYY(events.getEvent_start_date()) + " to " + CommonFunction.convertDateToDDMMYYYY(events.getEvent_end_date()));
        holder.tv_venue.setText(events.getLocation());

        holder.tv_event_name.setText(events.getName());

        holder.tv_date_time.setTextColor(context.getResources().getColor(R.color.black));
        holder.tv_venue.setTextColor(context.getResources().getColor(R.color.black));

        if(isClickable)
        {holder.ll_main_event_list.setEnabled(true);}
        else
        {holder.ll_main_event_list.setEnabled(false);}

        holder.ll_main_event_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isClickable = false;
                listener.onMoreSelected(events,position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return eventListsFilter.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    eventListsFilter = eventLists;
                } else {
                    List<EventList> filteredList = new ArrayList<>();
                    for (EventList row : eventLists) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        String name = row.getName().toLowerCase();

                        if (name.contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    eventListsFilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = eventListsFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                eventListsFilter = (ArrayList<EventList>) filterResults.values;

                if (eventListsFilter.size() == 0) {
                    Utility.createShortSnackBar(EventListActivity.ll_main,"No Event Found");
                    //Toast.makeText(context, "No Event Found", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };

    }

    public interface EventAdapterListner {
        void onMoreSelected(EventList event, int position);
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_event_logo;
        TextView tv_date_time, tv_venue, tv_event_name;
        LinearLayout ll_main_event_list;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_event_logo = itemView.findViewById(R.id.iv_event_logo);
            tv_date_time = itemView.findViewById(R.id.tv_date_time);
            tv_venue = itemView.findViewById(R.id.tv_venue);
            tv_event_name = itemView.findViewById(R.id.tv_event_name);
            ll_main_event_list = itemView.findViewById(R.id.ll_main_event_list);

        }
    }
}