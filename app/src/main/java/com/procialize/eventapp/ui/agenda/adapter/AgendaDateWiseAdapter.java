package com.procialize.eventapp.ui.agenda.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.ui.AgendaDetails.view.AgendaDetailsActivity;
import com.procialize.eventapp.ui.agenda.model.Agenda;
import com.procialize.eventapp.ui.agenda.model.AgendaList;
import com.procialize.eventapp.ui.newsfeed.adapter.NewsFeedAdapter;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_5;

public class AgendaDateWiseAdapter extends RecyclerView.Adapter<AgendaDateWiseAdapter.MyViewHolder> {

    String colorActive;
    private List<Agenda> agendaLists;
    private Context context;


    String eventColor1, eventColor2, eventColor3, eventColor4, eventColor5;

    public AgendaDateWiseAdapter(Context context, List<Agenda> agendaLists) {
        this.agendaLists = agendaLists;
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
                .inflate(R.layout.recycler_item_agenda_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return agendaLists.size();
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Agenda agenda = agendaLists.get(position);

        holder.tv_session_name.setText(agenda.getSession_name());
        holder.tv_description.setText(agenda.getSession_short_description());


        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
            SimpleDateFormat targetFormat = new SimpleDateFormat("hh:mm aa");

            Date startTime = originalFormat.parse(agenda.getSession_start_time());
            Date endTime = originalFormat.parse(agenda.getSession_end_time());
            String strEndTime = targetFormat.format(endTime);
            String strStartTime = targetFormat.format(startTime);

            holder.tv_start_time.setText(strStartTime.toUpperCase());
            holder.tv_end_time.setText(strEndTime.toUpperCase());

            Date currentTime = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strCurrnetDate = dateFormat.format(currentTime);
            if(CommonFunction.isTimeBetweenTwoTime(agenda.getSession_start_time(), agenda.getSession_end_time(), strCurrnetDate) && !agenda.getLivestream_link().isEmpty())
            {
                holder.iv_next_arrow.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_live_streaming));
            }
            else
            {
                holder.iv_next_arrow.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_right_arrow));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent agendaDetail = new Intent(context, AgendaDetailsActivity.class);
                agendaDetail.putExtra("agendaDetails", (Serializable) agenda);
                context.startActivity(agendaDetail);
            }
        });

        setDynamicColor(holder);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_start_time,
                tv_end_time,
                tv_session_name,
                tv_description;
        public LinearLayout ll_main;
        public ImageView iv_next_arrow;
        View view_divider;

        public MyViewHolder(View view) {
            super(view);
            tv_start_time = view.findViewById(R.id.tv_start_time);
            tv_end_time = view.findViewById(R.id.tv_end_time);
            tv_description = view.findViewById(R.id.tv_description);
            tv_session_name = view.findViewById(R.id.tv_session_name);
            iv_next_arrow = view.findViewById(R.id.iv_next_arrow);
            ll_main = view.findViewById(R.id.ll_main);
            view_divider = view.findViewById(R.id.view_divider);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public void setDynamicColor(@NonNull final AgendaDateWiseAdapter.MyViewHolder holder) {
        String eventColor3Opacity40 = eventColor3.replace("#", "");
        holder.tv_session_name.setTextColor(Color.parseColor(eventColor1));
        holder.tv_description.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        holder.tv_start_time.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        holder.tv_end_time.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
        holder.ll_main.setBackgroundColor(Color.parseColor(eventColor2));
        holder.iv_next_arrow.setColorFilter(Color.parseColor("#8C" + eventColor3Opacity40), PorterDuff.Mode.SRC_ATOP);
        holder.view_divider.setBackgroundColor(Color.parseColor("#8C" + eventColor3Opacity40));
    }
}