package com.procialize.bayer2020.ui.livepoll.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.ui.livepoll.model.LivePoll;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_3;

public class LivePollAdapter extends RecyclerView.Adapter<LivePollAdapter.NewsViewHolder> {

    private List<LivePoll> pollLists;
    private Context context;
    private PollAdapterListner listener;
    private LayoutInflater inflater;

    public LivePollAdapter(Context context, List<LivePoll> pollLists, PollAdapterListner listener) {
        this.pollLists = pollLists;
        this.listener = listener;
        this.context = context;


    }

    @NonNull
    @Override
    public LivePollAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.live_poll_row, parent, false);
        return new LivePollAdapter.NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LivePollAdapter.NewsViewHolder holder, final int position) {
//Newsfeed_detail feedData = feed_detail.get(position);
        final LivePoll pollList = pollLists.get(position);

        holder.linMain.setBackgroundColor(Color.parseColor(SharedPreference.getPref(context,EVENT_COLOR_2)));
        holder.mainLL.setBackgroundColor(Color.parseColor(SharedPreference.getPref(context,EVENT_COLOR_2)));

        String eventColor3 = SharedPreference.getPref(context, EVENT_COLOR_3);

        String eventColor3Opacity40 = eventColor3.replace("#", "");

      //  holder.nameTv.setTextColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_1)));
      //  holder.statusTv.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));

       // holder.ic_rightarrow.setColorFilter(Color.parseColor("#8C" + eventColor3Opacity40), PorterDuff.Mode.SRC_ATOP);

       /* if(pollList.getStatus().equalsIgnoreCase("Tap To Participate")){
            holder.statusTv.setVisibility(View.VISIBLE);
            holder.statusTv.setText("Tap To Participate");
            holder.ivewComplete.setBackgroundColor(Color.parseColor("#898989"));

            holder.ivewComplete.setVisibility(View.VISIBLE);
        }else{
            holder.statusTv.setVisibility(View.VISIBLE);
            holder.statusTv.setText("Completed");
            holder.ivewComplete.setVisibility(View.VISIBLE);

            holder.ivewComplete.setBackgroundColor(Color.parseColor(SharedPreference.getPref(context,EVENT_COLOR_1)));
        }*/
        try{
            holder.nameTv.setText(StringEscapeUtils.unescapeJava(pollList.getQuestion()));
        }catch (IllegalArgumentException e){
            e.printStackTrace();

        }
        holder.linMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onContactSelected(pollLists.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return pollLists.size();
    }


    public interface PollAdapterListner {
        void onContactSelected(LivePoll pollList);
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTv,statusTv;
        public ImageView ic_rightarrow;
        public LinearLayout mainLL,linMain;
        RelativeLayout relative;
        View ivewComplete;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.nameTv);
            statusTv = itemView.findViewById(R.id.statusTv);
            mainLL = itemView.findViewById(R.id.mainLL);
            linMain = itemView.findViewById(R.id.linMain);
            ic_rightarrow = itemView.findViewById(R.id.ic_rightarrow);
            ivewComplete = itemView.findViewById(R.id.ivewComplete);
            ivewComplete = itemView.findViewById(R.id.ivewComplete);

        }
    }
}
