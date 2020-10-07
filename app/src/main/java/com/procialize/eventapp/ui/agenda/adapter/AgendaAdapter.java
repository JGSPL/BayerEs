package com.procialize.eventapp.ui.agenda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.procialize.eventapp.R;
import com.procialize.eventapp.ui.agenda.model.Agenda;

import java.util.List;


public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.MyViewHolder>  {

    //List<EventSettingList> eventSettingLists;
    String attendee_design = "1", attendee_company = "1", attendee_location = "1";
    private Context context;
    private List<Agenda> attendeeListFiltered;
    private AgendaAdapterListner listener;


    public AgendaAdapter(Context context, List<Agenda> commentList, AgendaAdapterListner listener) {
        //attendeeListFiltered = new ArrayList<>();
        this.attendeeListFiltered = commentList;
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



    }

    @Override
    public int getItemCount() {
        return attendeeListFiltered.size();
    }

    public interface AgendaAdapterListner {
        void onContactSelected(Agenda attendee);
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



}