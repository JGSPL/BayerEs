package com.procialize.eventapp.ui.agenda.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.procialize.eventapp.R;
import com.procialize.eventapp.ui.AgendaDetails.view.AgendaDetailsActivity;
import com.procialize.eventapp.ui.agenda.model.Agenda;
import com.procialize.eventapp.ui.agenda.model.AgendaList;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AgendaDateWiseAdapter extends RecyclerView.Adapter<AgendaDateWiseAdapter.MyViewHolder> {

    String colorActive;
    private List<Agenda> agendaLists;
    private Context context;


    public AgendaDateWiseAdapter(Context context, List<Agenda> agendaLists) {
        this.agendaLists = agendaLists;
        this.context = context;
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
        holder.tv_description.setText(agenda.getSession_description());


        try {

            SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.UK);
            SimpleDateFormat targetFormat = new SimpleDateFormat("HH:mm aa");

            Date startTime = originalFormat.parse(agenda.getSession_start_time());
            Date endTime = originalFormat.parse(agenda.getSession_end_time());
            String strStartTime = targetFormat.format(startTime);
            String strEndTime = targetFormat.format(endTime);


            holder.tv_start_time.setText(strStartTime.toUpperCase());
            holder.tv_end_time.setText(strEndTime.toUpperCase() );

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent agendaDetail = new Intent(context, AgendaDetailsActivity.class);
                agendaDetail.putExtra("agendaDetails",(Serializable) agenda);
                context.startActivity(agendaDetail);
            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_start_time,
                tv_end_time,
                tv_session_name,
                tv_description;
        public LinearLayout ll_main;

        public MyViewHolder(View view) {
            super(view);
            tv_start_time = view.findViewById(R.id.tv_start_time);
            tv_end_time = view.findViewById(R.id.tv_end_time);
            tv_description = view.findViewById(R.id.tv_description);
            tv_session_name = view.findViewById(R.id.tv_session_name);
            ll_main = view.findViewById(R.id.ll_main);
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
}