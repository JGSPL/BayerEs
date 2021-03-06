package com.procialize.bayer2020.ui.upskill.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.procialize.bayer2020.R;
import com.procialize.bayer2020.ui.upskill.model.LivePollOption;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;

/**
 * Created by Rahul on 05-10-2018.
 */

public class UpskillPollGraphAdapter extends RecyclerView.Adapter<UpskillPollGraphAdapter.MyViewHolder> {

    String questionId;
    //private List<LivePoll_option> optionLists;
    private ArrayList<LivePollOption> optionLists;
    private Context context;

    public UpskillPollGraphAdapter(Context context, ArrayList<LivePollOption> optionLists, String questionId) {
        this.optionLists = optionLists;
        this.context = context;
        this.questionId = questionId;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.poll_row_graph, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final LivePollOption pollList = optionLists.get(position);

        String[] color = {"#C4A5C6", "#F99EAF", "#95C4D6", "#CCD883", "#C4A5C6", "#F99EAF", "#95C4D6", "#CCD883", "#C4A5C6", "#F99EAF", "#95C4D6", "#CCD883",
                "#C4A5C6", "#F99EAF", "#95C4D6", "#CCD883", "#C4A5C6", "#F99EAF", "#95C4D6", "#CCD883","#C4A5C6", "#F99EAF", "#95C4D6", "#CCD883"};

      //  String activeBarColor = SharedPreference.getPref(context, SharedPreferencesConstant.EVENT_COLOR_1);

        Float totalUser = 0.0f;


        for (int k = 0; k < optionLists.size(); k++) {

            if (optionLists.get(k).getLive_poll_id()
                    .equalsIgnoreCase(questionId)) {
                totalUser = (totalUser + Float.parseFloat(optionLists
                        .get(k).getTotal_user()));

            }

        }
        LinearLayout.LayoutParams rpms2, rprms;
        LinearLayout l3 = new LinearLayout(context);


        Float weight = 0.0f;


        weight = ((Float.parseFloat(optionLists.get(position)
                .getTotal_user()) / totalUser) * 100);


        int num = Math.round(weight);


        LinearLayout ll2 = new LinearLayout(context);
        ll2.setOrientation(LinearLayout.HORIZONTAL);
        ll2.setPadding(10, 10, 10, 10);

        // ll2.setBackgroundResource(R.drawable.poll_cobg);

        GradientDrawable gradientDrawable   =   new GradientDrawable();
        gradientDrawable.setCornerRadii(new float[]{20, 20, 20, 20, 20, 20, 20, 20});
        gradientDrawable.setColor(Color.parseColor(color[position]));

        //ll2.setBackgroundColor(Color.parseColor(color[position]));
        ll2.setBackground(gradientDrawable);
        ll2.setMinimumHeight(15);

        rprms = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rprms.setMargins(0, 0, 0, 0);


        rpms2 = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT, weight);
        rpms2.setMargins(0, 0, 0, 0);

        l3.setLayoutParams(rprms);
        l3.setWeightSum(100);
        l3.setMinimumHeight(40);

        holder.txtOptMessage.setText(StringEscapeUtils.unescapeJava(pollList.getOption()));
        holder.txtper.setText(num + "%");
        // holder.linGraph.setBackgroundColor(Color.parseColor(color[position]));

        l3.addView(ll2, rpms2);
        holder.linGraph.addView(l3);

      /*  weight = ((Float.parseFloat(optionLists.get(position)
                .getTotal_user()) / totalUser) * 100);


        int num = Math.round(weight);


        LinearLayout ll2 = new LinearLayout(context);
        ll2.setOrientation(LinearLayout.HORIZONTAL);
        ll2.setPadding(10, 10, 10, 10);

        // ll2.setBackgroundResource(R.drawable.poll_cobg);

        GradientDrawable gradientDrawable   =   new GradientDrawable();
        gradientDrawable.setCornerRadii(new float[]{20, 20, 20, 20, 20, 20, 20, 20});
        gradientDrawable.setColor(Color.parseColor(color[position]));
      //  gradientDrawable.setColor(Color.parseColor(activeBarColor));
        ll2.setBackground(gradientDrawable);
        ll2.setMinimumHeight(15);

        rprms = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rprms.setMargins(0, 0, 0, 0);


        rpms2 = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT, weight);
        rpms2.setMargins(0, 0, 0, 0);

        l3.setLayoutParams(rprms);
        l3.setWeightSum(100);
        l3.setMinimumHeight(40);

        holder.txtOptMessage.setText(StringEscapeUtils.unescapeJava(pollList.getOption()));
        holder.txtper.setText(num + "%");
         holder.linGraph.setBackgroundColor(Color.parseColor(color[position]));

        l3.addView(ll2, rpms2);
        holder.linGraph.addView(l3);*/

    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return optionLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtOptMessage, txtper;
        public LinearLayout linGraph,linMainGraph;

        public MyViewHolder(View view) {
            super(view);
            txtOptMessage = view.findViewById(R.id.txtOptMessage);
            txtper = view.findViewById(R.id.txtper);

            linGraph = view.findViewById(R.id.linGraph);
            linMainGraph = view.findViewById(R.id.linMainGraph);
            /*linMainGraph.setBackgroundColor(Color.parseColor(SharedPreference.getPref(context,SharedPreferencesConstant.EVENT_COLOR_2)));
            txtOptMessage.setTextColor(Color.parseColor(SharedPreference.getPref(context,SharedPreferencesConstant.EVENT_COLOR_3)));
            txtper.setTextColor(Color.parseColor(SharedPreference.getPref(context,SharedPreferencesConstant.EVENT_COLOR_3)));*/


        }
    }


}