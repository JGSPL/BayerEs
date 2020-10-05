package com.procialize.eventapp.ui.speaker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.ui.speaker.model.Speaker;

import java.util.ArrayList;
import java.util.List;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_3;

public class SpeakerAdapter extends RecyclerView.Adapter<SpeakerAdapter.MyViewHolder> implements Filterable {

    //List<EventSettingList> eventSettingLists;
    String attendee_design = "1", attendee_company = "1", attendee_location = "1", attendee_mobile = "1", attendee_save_contact = "1";
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    private Context context;
    private List<Speaker> speakerListFiltered;
    private SpeakerAdapter.SpeakerAdapterListner listener;
    private String picPath;
    private boolean retryPageLoad = false;
    private boolean isLoadingAdded = false;
    private List<Speaker> speakerLists = new ArrayList<>();


    public SpeakerAdapter(Context context,List<Speaker> speakerList, SpeakerAdapter.SpeakerAdapterListner listener) {
        //speakerListFiltered = new ArrayList<>();
        this.speakerListFiltered = speakerList;
        this.speakerLists = speakerList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public SpeakerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.speaker_row, parent, false);

        return new SpeakerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SpeakerAdapter.MyViewHolder holder, int position) {

        try {
            final Speaker Speaker = speakerListFiltered.get(position);

            String eventColor3 = SharedPreference.getPref(context, EVENT_COLOR_3);

            String eventColor3Opacity40 = eventColor3.replace("#", "");

            holder.nameTv.setTextColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_1)));
            holder.designationTv.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
            holder.locationTv.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
            holder.tv_concat.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
            holder.companyTv.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
            holder.ic_rightarrow.setColorFilter(Color.parseColor("#8C" + eventColor3Opacity40), PorterDuff.Mode.SRC_ATOP);
            holder.linMain.setBackgroundColor(Color.parseColor(SharedPreference.getPref(context,EVENT_COLOR_2)));

           /* try {
                if (attendee_location.equalsIgnoreCase("0")) {
                    holder.locationTv.setVisibility(View.INVISIBLE);
                } else {
                    if (Speaker.getCompany_name().equalsIgnoreCase("N A")) {
                        holder.locationTv.setVisibility(View.INVISIBLE);
                    }
                    if (Speaker.getCompany_name().equalsIgnoreCase("")) {
                        holder.locationTv.setVisibility(View.INVISIBLE);
                    }
                    if (Speaker.getCompany_name().equalsIgnoreCase(" ")) {
                        holder.locationTv.setVisibility(View.INVISIBLE);
                    } else {
                        holder.locationTv.setVisibility(View.VISIBLE);
                        holder.locationTv.setText(Speaker.getCity());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                holder.locationTv.setVisibility(View.INVISIBLE);
            }*/


            try {
                if (attendee_design.equalsIgnoreCase("0")) {
                    holder.designationTv.setVisibility(View.INVISIBLE);
                } else {
                    if (Speaker.getDesignation().equalsIgnoreCase("N A")) {
                        holder.designationTv.setVisibility(View.INVISIBLE);
                        holder.tv_concat.setVisibility(View.GONE);
                    }
                    if (Speaker.getDesignation().equalsIgnoreCase("")) {
                        holder.designationTv.setVisibility(View.INVISIBLE);
                        holder.tv_concat.setVisibility(View.GONE);
                    }
                    if (Speaker.getDesignation().equalsIgnoreCase(" ")) {
                        holder.designationTv.setVisibility(View.INVISIBLE);
                        holder.tv_concat.setVisibility(View.GONE);
                    } else {
                        holder.designationTv.setVisibility(View.VISIBLE);
                        holder.designationTv.setText(Speaker.getDesignation());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                holder.designationTv.setVisibility(View.INVISIBLE);
            }

/*
            try {
                if (attendee_company.equalsIgnoreCase("0")) {
                    holder.companyTv.setVisibility(View.INVISIBLE);
                } else {
                    if (Speaker.getCompany_name().equalsIgnoreCase("N A")) {
                        holder.companyTv.setVisibility(View.INVISIBLE);
                        holder.tv_concat.setVisibility(View.INVISIBLE);
                    }
                    if (Speaker.getCompany_name().equalsIgnoreCase("")) {
                        holder.companyTv.setVisibility(View.INVISIBLE);
                        holder.tv_concat.setVisibility(View.INVISIBLE);
                    }
                    if (Speaker.getCompany_name().equalsIgnoreCase(" ")) {
                        holder.companyTv.setVisibility(View.INVISIBLE);
                        holder.tv_concat.setVisibility(View.INVISIBLE);
                    } else {
                        holder.companyTv.setVisibility(View.VISIBLE);
                        holder.companyTv.setText(Speaker.getCompany_name());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                holder.designationTv.setVisibility(View.INVISIBLE);
            }
*/

           /* if (Speaker.getCompany_name().equalsIgnoreCase("") &&
                    Speaker.getDesignation().equalsIgnoreCase("")) {
                holder.tv_concat.setVisibility(View.INVISIBLE);
            } else {
                holder.tv_concat.setVisibility(View.VISIBLE);
            }*/

            /*if (attendee_design.equalsIgnoreCase("0") &&
                    attendee_company.equalsIgnoreCase("0")) {
                holder.tv_concat.setVisibility(View.INVISIBLE);
            } else {
                holder.tv_concat.setVisibility(View.VISIBLE);
            }*/



            if (Speaker.getFirst_name() != null) {
                if (Speaker.getFirst_name().equalsIgnoreCase("N A")) {
                    holder.nameTv.setText("");
                } else {
                    holder.nameTv.setText(Speaker.getFirst_name() + " " + Speaker.getLast_name());

                }

                if (Speaker.getProfile_picture() != null) {


                    Glide.with(context).load( Speaker.getProfile_picture())
                            .apply(RequestOptions.skipMemoryCacheOf(false))
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).circleCrop()
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    holder.profileIv.setImageResource(R.drawable.profilepic_placeholder);

                                    holder.progressBar.setVisibility(View.GONE);
                                    return true;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    holder.progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            }).into(holder.profileIv);

                } else {
                    holder.progressBar.setVisibility(View.GONE);

                }

                String attendee_id = Speaker.getAttendee_id();
                /*String unreadMsgCount = procializeDB.getAttendeeChatForAttendeeId(attendee_id);
                if (!unreadMsgCount.equalsIgnoreCase("0")) {
                    holder.tv_count.setText(unreadMsgCount);
                    holder.tv_count.setVisibility(View.VISIBLE);
                    holder.rl_count.setVisibility(View.VISIBLE);
                    holder.ic_rightarrow.setVisibility(View.GONE);
                } else {
                    holder.tv_count.setText("");
                    holder.tv_count.setVisibility(View.GONE);
                    holder.rl_count.setVisibility(View.GONE);
                    holder.ic_rightarrow.setVisibility(View.VISIBLE);
                }*/
            }

            //holder.iv_background.setBackgroundColor(Color.parseColor(colorActive));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return speakerListFiltered.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    speakerListFiltered = speakerLists;
                } else {
                    List<Speaker> filteredList = new ArrayList<>();
                    for (Speaker row : speakerLists) {
                        String name="";
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if(row.getLast_name()!=null) {
                            name = row.getFirst_name().toLowerCase() + " " + row.getLast_name().toLowerCase();
                        }else{
                            name = row.getFirst_name().toLowerCase();
                        }

                        if (name.contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    speakerListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = speakerListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                speakerListFiltered = (ArrayList<Speaker>) filterResults.values;

                if (speakerListFiltered.size() == 0) {
                    notifyDataSetChanged();
//                    Toast.makeText(context, "No Speaker Found", Toast.LENGTH_SHORT).show();
                }
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }
/*
    public void applySetting(List<EventSettingList> eventSettingLists) {
        for (int i = 0; i < eventSettingLists.size(); i++) {
            if (eventSettingLists.get(i).getFieldName().equals("event_details")) {
                if (eventSettingLists.get(i).getSub_menuList() != null) {
                    if (eventSettingLists.get(i).getSub_menuList().size() > 0) {
                        for (int k = 0; k < eventSettingLists.get(i).getSub_menuList().size(); k++) {
                            if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("attendee_company")) {
                                attendee_company = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            } else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("attendee_location")) {
                                attendee_location = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            } else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("attendee_mobile")) {
                                attendee_mobile = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            } else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("attendee_save_contact")) {
                                attendee_save_contact = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            } else if (eventSettingLists.get(i).getSub_menuList().get(k).getFieldName().contentEquals("attendee_designation")) {
                                attendee_design = eventSettingLists.get(i).getSub_menuList().get(k).getFieldValue();
                            }
                        }
                    }
                }
            }
        }
    }
*/

    public interface SpeakerAdapterListner {
        void onContactSelected(Speaker Speaker);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv, locationTv, designationTv, tv_count, tv_concat,
                companyTv;
        public ImageView profileIv, ic_rightarrow, iv_background;
        public LinearLayout mainLL;
        public ProgressBar progressBar;
        LinearLayout linMain;
        RelativeLayout rl_count;
        CardView myCardView1;

        public MyViewHolder(View view) {
            super(view);

            nameTv = view.findViewById(R.id.nameTv);
            tv_count = view.findViewById(R.id.tv_count);
            locationTv = view.findViewById(R.id.locationTv);
            tv_concat = view.findViewById(R.id.tv_concat);
            companyTv = view.findViewById(R.id.companyTv);
            designationTv = view.findViewById(R.id.designationTv);
            linMain = view.findViewById(R.id.linMain);
            profileIv = view.findViewById(R.id.profileIV);
            myCardView1 = view.findViewById(R.id.myCardView1);
            iv_background = view.findViewById(R.id.iv_background);

            ic_rightarrow = view.findViewById(R.id.ic_rightarrow);
            rl_count = view.findViewById(R.id.rl_count);

            mainLL = view.findViewById(R.id.mainLL);

            progressBar = view.findViewById(R.id.progressBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(speakerListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

     /*
        Helpers - Pagination
   _________________________________________________________________________________________________
    */

    public void add(Speaker r) {
        //speakerLists.add(r);
        speakerListFiltered.add(r);
        //notifyItemInserted(speakerLists.size() - 1);
        notifyItemInserted(speakerListFiltered.size() - 1);
    }

    public void addAll(List<Speaker> moveResults) {
        for (Speaker result : moveResults) {
            add(result);
        }
    }

    public void remove(Speaker r) {
        int position = speakerListFiltered.indexOf(r);
        if (position > -1) {
            //speakerLists.remove(position);
            speakerListFiltered.remove(position);
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
        add(new Speaker());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = speakerListFiltered.size() - 1;
        Speaker result = getItem(position);

        if (result != null) {
            speakerListFiltered.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Speaker getItem(int position) {
        return speakerListFiltered.get(position);
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(speakerListFiltered.size() - 1);
    }


    public List<Speaker> getSpeakerListFiltered() {
        return speakerListFiltered;
    }

}
