package com.procialize.eventapp.ui.attendee.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

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
import com.procialize.eventapp.Database.EventAppDB;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.session.SessionManager;
import com.procialize.eventapp.ui.attendee.model.Attendee;
import com.procialize.eventapp.ui.attendee.view.AttendeeFragment;
import com.procialize.eventapp.ui.attendeeChat.model.ChatCount;
import com.procialize.eventapp.ui.attendeeChat.roomDb.Table_Attendee_Chatcount;
import com.procialize.eventapp.ui.newsFeedLike.model.AttendeeList;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.KEY_ATTENDEE_ID;

public class AttendeeAdapter extends RecyclerView.Adapter<AttendeeAdapter.MyViewHolder> implements Filterable {

    //List<EventSettingList> eventSettingLists;
    String attendee_design = "1", attendee_company = "1", attendee_location = "1", attendee_mobile = "1", attendee_save_contact = "1";
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    private Context context;
    private List<Attendee> attendeeListFiltered;
    private AttendeeAdapterListner listener;
    private String picPath;
    private boolean retryPageLoad = false;
    private boolean isLoadingAdded = false;
    private List<Attendee> attendeeLists = new ArrayList<>();


    public AttendeeAdapter(Context context,List<Attendee> commentList, AttendeeAdapterListner listener) {
        //attendeeListFiltered = new ArrayList<>();
        this.attendeeListFiltered = commentList;
        this.attendeeLists = commentList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendee_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        try {
            final Attendee attendee = attendeeListFiltered.get(position);

            String eventColor3 = SharedPreference.getPref(context, EVENT_COLOR_3);

            String eventColor3Opacity40 = eventColor3.replace("#", "");

            holder.nameTv.setTextColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_1)));
            holder.designationTv.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
            holder.locationTv.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
            holder.tv_concat.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
            holder.companyTv.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
            holder.ic_rightarrow.setColorFilter(Color.parseColor("#8C" + eventColor3Opacity40), PorterDuff.Mode.SRC_ATOP);
            holder.linMain.setBackgroundColor(Color.parseColor(SharedPreference.getPref(context,EVENT_COLOR_2)));

            try {
                if (attendee_location.equalsIgnoreCase("0")) {
                    holder.locationTv.setVisibility(View.INVISIBLE);
                } else {
                    if (attendee.getCompany_name().equalsIgnoreCase("N A")) {
                        holder.locationTv.setVisibility(View.INVISIBLE);
                    }
                    if (attendee.getCompany_name().equalsIgnoreCase("")) {
                        holder.locationTv.setVisibility(View.INVISIBLE);
                    }
                    if (attendee.getCompany_name().equalsIgnoreCase(" ")) {
                        holder.locationTv.setVisibility(View.INVISIBLE);
                    } else {
                        holder.locationTv.setVisibility(View.VISIBLE);
                        holder.locationTv.setText(attendee.getCity());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                holder.locationTv.setVisibility(View.INVISIBLE);
            }



            try {
                if (attendee_company.equalsIgnoreCase("0")) {
                    holder.companyTv.setVisibility(View.INVISIBLE);
                } else {
                    if (attendee.getCompany_name().equalsIgnoreCase("N A")) {
                        holder.companyTv.setVisibility(View.INVISIBLE);
                        holder.tv_concat.setVisibility(View.INVISIBLE);
                    }
                    if (attendee.getCompany_name().equalsIgnoreCase("")) {
                        holder.companyTv.setVisibility(View.INVISIBLE);
                        holder.tv_concat.setVisibility(View.INVISIBLE);
                    }
                    if (attendee.getCompany_name().equalsIgnoreCase(" ")) {
                        holder.companyTv.setVisibility(View.INVISIBLE);
                        holder.tv_concat.setVisibility(View.INVISIBLE);
                    } else {
                        holder.tv_concat.setVisibility(View.GONE);
                        holder.designationTv.setVisibility(View.GONE);

                        holder.companyTv.setVisibility(View.VISIBLE);
                        holder.companyTv.setText(attendee.getDesignation()+ " - " + attendee.getCompany_name());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                holder.designationTv.setVisibility(View.INVISIBLE);
            }
            if (attendee_design.equalsIgnoreCase("0") &&
                    attendee_company.equalsIgnoreCase("0")) {
                holder.tv_concat.setVisibility(View.INVISIBLE);
            } else {
                holder.tv_concat.setVisibility(View.VISIBLE);
            }



            if (attendee.getFirst_name() != null) {
                if (attendee.getFirst_name().equalsIgnoreCase("N A")) {
                    holder.nameTv.setText("");
                } else {
                    holder.nameTv.setText(attendee.getFirst_name() + " " + attendee.getLast_name());

                }

                if (attendee.getProfile_picture() != null) {


                    Glide.with(context).load( attendee.getProfile_picture())
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

                String firebase_id = attendee.getFirebase_id();
                int unreadMsgCount = EventAppDB.getDatabase(context).attendeeChatDao().getChatCountId(firebase_id);
          //      List<Table_Attendee_Chatcount> attenChatCount  = EventAppDB.getDatabase(context).attendeeChatDao().getAllDaoAttendee();

                if (unreadMsgCount > 0) {
                    holder.tv_count.setText(String.valueOf(unreadMsgCount));
                    holder.tv_count.setVisibility(View.VISIBLE);
                    holder.rl_count.setVisibility(View.VISIBLE);
                    holder.ic_rightarrow.setVisibility(View.GONE);
                } else {
                    holder.tv_count.setText("");
                    holder.tv_count.setVisibility(View.GONE);
                    holder.rl_count.setVisibility(View.GONE);
                    holder.ic_rightarrow.setVisibility(View.VISIBLE);
                }
            }

            //holder.iv_background.setBackgroundColor(Color.parseColor(colorActive));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return attendeeListFiltered.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    attendeeListFiltered = attendeeLists;
                } else {
                        List<Attendee> filteredList = new ArrayList<>();
                        for (Attendee row : attendeeLists) {
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
                        attendeeListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = attendeeListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                attendeeListFiltered = (ArrayList<Attendee>) filterResults.values;

                if (attendeeListFiltered.size() == 0) {
                    notifyDataSetChanged();
//                    Toast.makeText(context, "No Attendee Found", Toast.LENGTH_SHORT).show();
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

    public interface AttendeeAdapterListner {
        void onContactSelected(Attendee attendee);
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
                    listener.onContactSelected(attendeeListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

     /*
        Helpers - Pagination
   _________________________________________________________________________________________________
    */

    public void add(Attendee r) {
        //attendeeLists.add(r);
        attendeeListFiltered.add(r);
        //notifyItemInserted(attendeeLists.size() - 1);
        notifyItemInserted(attendeeListFiltered.size() - 1);
    }

    public void addAll(List<Attendee> moveResults) {
        for (Attendee result : moveResults) {
            add(result);
        }
    }

    public void remove(Attendee r) {
        int position = attendeeListFiltered.indexOf(r);
        if (position > -1) {
            //attendeeLists.remove(position);
            attendeeListFiltered.remove(position);
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
        add(new Attendee());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = attendeeListFiltered.size() - 1;
        Attendee result = getItem(position);

        if (result != null) {
            attendeeListFiltered.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Attendee getItem(int position) {
        return attendeeListFiltered.get(position);
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(attendeeListFiltered.size() - 1);
    }


    public List<Attendee> getAttendeeListFiltered() {
        return attendeeListFiltered;
    }

}