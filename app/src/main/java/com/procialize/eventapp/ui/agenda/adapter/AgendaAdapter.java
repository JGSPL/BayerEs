package com.procialize.eventapp.ui.agenda.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.ui.agenda.model.Agenda;
import com.procialize.eventapp.ui.agenda.model.AgendaList;
import com.procialize.eventapp.ui.newsfeed.adapter.NewsFeedAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_4;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_5;


public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.MyViewHolder>{

    //List<EventSettingList> eventSettingLists;
    String attendee_design = "1", attendee_company = "1", attendee_location = "1";
    private Context context;
    String newSessiondate = "",date="";
    private List<Agenda> agendaLists;
    private AgendaAdapterListner listener;
    private List<Agenda> mainAgendaList = new ArrayList<Agenda>();
    AgendaDateWiseAdapter agendaDateWiseAdapter;
    String eventColor1, eventColor2, eventColor3, eventColor4, eventColor5;
    public AgendaAdapter(Context context, List<Agenda> agendaList, AgendaAdapterListner listener) {
        this.agendaLists = agendaList;
        this.listener = listener;
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
                .inflate(R.layout.recycler_item_agenda_date, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Agenda agenda = agendaLists.get(position);
        holder.tv_date_time.setText(agenda.getSession_date());

        if (!(mainAgendaList.isEmpty())) {
            mainAgendaList.clear();
        }

        if (agenda.getSession_date().equals(date)) {
            holder.tv_date_time.setVisibility(View.GONE);
            date = agenda.getSession_date();
            holder.rv_agenda_list.setVisibility(View.GONE);
//            tempagendaList.add(agendaLists.get(position));
        } else {
            holder.tv_date_time.setVisibility(View.VISIBLE);
            holder.rv_agenda_list.setVisibility(View.VISIBLE);
            date = agenda.getSession_date();

            try {/*
                SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
                SimpleDateFormat targetFormat = new SimpleDateFormat(" dd MMM, yyyy");
                Date date = originalFormat.parse(agenda.getSession_date());
*/
                String sessiondate = CommonFunction.convertAgendaDate(agenda.getSession_date());
                //String sessiondate = targetFormat.format(date);
                holder.tv_date_time.setText(sessiondate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < agendaLists.size(); i++) {
            if (agenda.getSession_date().equals(newSessiondate)) {
                newSessiondate = agendaLists.get(i).getSession_date();
                if (agenda.getSession_date().equals(newSessiondate)) {
                    mainAgendaList.add(agendaLists.get(i));
                }
            } else {
                newSessiondate = agendaLists.get(i).getSession_date();
                if (agenda.getSession_date().equals(newSessiondate)) {
                    mainAgendaList.add(agendaLists.get(i));
                }
            }
        }

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        holder.rv_agenda_list.setLayoutManager(mLayoutManager);
        agendaDateWiseAdapter = new AgendaDateWiseAdapter(context, mainAgendaList);
        agendaDateWiseAdapter.notifyDataSetChanged();
        holder.rv_agenda_list.setAdapter(agendaDateWiseAdapter);    }

    @Override
    public int getItemCount() {
        return agendaLists.size();
    }


    public interface AgendaAdapterListner {
        void onContactSelected(Agenda attendee);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date_time;
        RecyclerView rv_agenda_list;

        public MyViewHolder(View view) {
            super(view);

            tv_date_time = view.findViewById(R.id.tv_date_time);
            rv_agenda_list = view.findViewById(R.id.rv_agenda_list);
        }
    }
}