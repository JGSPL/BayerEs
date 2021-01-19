package com.procialize.bayer2020.ui.upskill.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.ui.upskill.model.UpSkill;
import com.procialize.bayer2020.ui.upskill.model.UpskillList;

import java.util.List;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_3;

public class UpskillAdapter extends RecyclerView.Adapter<UpskillAdapter.MyViewHolder> {

    private Context context;
    private List<UpSkill> UpskillListList;
    private UpskillAdapter.UpskillListAdapterListner listener;


    public UpskillAdapter(Context context, List<UpSkill> UpskillListList, UpskillAdapter.UpskillListAdapterListner listener) {
        //UpskillListListFiltered = new ArrayList<>();
        this.UpskillListList = UpskillListList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public UpskillAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_upskill, parent, false);

        return new UpskillAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UpskillAdapter.MyViewHolder holder, int position) {

        try {
            List<UpskillList> upskillList = UpskillListList.get(position).getTrainingList();

            String eventColor3 = SharedPreference.getPref(context, EVENT_COLOR_3);

            String eventColor3Opacity40 = eventColor3.replace("#", "");
            holder.tv_title.setTextColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_1)));
            holder.iv_right_arrow.setColorFilter(Color.parseColor("#8C" + eventColor3Opacity40), PorterDuff.Mode.SRC_ATOP);
            holder.ll_row.setBackgroundColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_2)));

            holder.tv_title.setText(upskillList.get(position).getName());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return UpskillListList.size();
    }


    public interface UpskillListAdapterListner {
        void onContactSelected(UpskillList UpskillList);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_row;
        TextView tv_title;
        ImageView iv_right_arrow;

        public MyViewHolder(View view) {
            super(view);

            ll_row = view.findViewById(R.id.ll_row);
            tv_title = view.findViewById(R.id.tv_title);
            iv_right_arrow = view.findViewById(R.id.iv_right_arrow);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    //listener.onContactSelected(UpskillListList.get(getAdapterPosition()));
                }
            });
        }
    }
}
