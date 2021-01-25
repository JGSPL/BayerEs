package com.procialize.bayer2020.ui.survey.adapter;

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
import com.procialize.bayer2020.ui.survey.model.Survey;

import java.util.List;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_3;

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.MyViewHolder> {

    private Context context;
    private List<Survey> surveyList;
    private SurveyAdapter.SurveyAdapterListner listener;


    public SurveyAdapter(Context context, List<Survey> surveyList, SurveyAdapter.SurveyAdapterListner listener) {
        //surveyListFiltered = new ArrayList<>();
        this.surveyList = surveyList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public SurveyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.survey_row, parent, false);

        return new SurveyAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SurveyAdapter.MyViewHolder holder, int position) {

        try {
            final Survey survey = surveyList.get(position);

          /*  String eventColor3 = SharedPreference.getPref(context, EVENT_COLOR_3);

            String eventColor3Opacity40 = eventColor3.replace("#", "");
            holder.tv_title.setTextColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_1)));
            holder.iv_right_arrow.setColorFilter(Color.parseColor("#8C" + eventColor3Opacity40), PorterDuff.Mode.SRC_ATOP);
            holder.ll_row.setBackgroundColor(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_2)));
*/
            holder.tv_title.setText(survey.getSurvey_name());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return surveyList.size();
    }


    public interface SurveyAdapterListner {
        void onContactSelected(Survey survey);
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
                    listener.onContactSelected(surveyList.get(getAdapterPosition()));
                }
            });
        }
    }
}
