package com.procialize.bayer2020.ui.upskill.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.ui.upskill.model.QuizOption;
import com.procialize.bayer2020.ui.upskill.model.QuizQuestion;

import java.util.List;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_3;


public class UpskillAnswerAdapter extends RecyclerView.Adapter<UpskillAnswerAdapter.ViewHolder>{

    private List<QuizOption> quizSpecificOptionListnew;
    private List<QuizQuestion> quizList;
    private Context context;
    String correctAns;
    String SelectedAns;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;

    public UpskillAnswerAdapter(Context context, List<QuizOption> travelList, String correctAns, String SelectedAns) {
        this.quizSpecificOptionListnew = travelList;
        this.correctAns = correctAns;
        this.SelectedAns = SelectedAns;

        this.context = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_answers, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final QuizOption optionList = quizSpecificOptionListnew.get(position);
        String option = optionList.getOption_id();
        String eventColor3 = SharedPreference.getPref(context, EVENT_COLOR_3);

        String eventColor3Opacity40 = eventColor3.replace("#", "");
        GradientDrawable border = new GradientDrawable();
        border.setStroke(1, Color.parseColor("#8C" + eventColor3Opacity40)); //black border with full opacity
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            holder.raiolayout.setBackgroundDrawable(border);
        } else {
            holder.raiolayout.setBackground(border);
        }
        try {
            if (correctAns.equalsIgnoreCase(option)) {
                if (correctAns.equalsIgnoreCase(SelectedAns)) {
                    holder.raiolayout.setBackgroundResource(R.drawable.quiz_correct);
                    holder.radio.setBackgroundResource(R.drawable.radiobtn_checked);
//                    holder.radio.setColorFilter(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_1)), PorterDuff.Mode.SRC_ATOP);
                } else if (option.equalsIgnoreCase(correctAns)) {
                    holder.raiolayout.setBackgroundResource(R.drawable.quiz_correct);
                    holder.radio.setBackgroundResource(R.drawable.unchecked_radio);
                }
            } else if (option.equalsIgnoreCase(SelectedAns)) {
                holder.raiolayout.setBackgroundResource(R.drawable.quiz_wrong);
                holder.radio.setBackgroundResource(R.drawable.radiobtn_checked);
//                holder.radio.setColorFilter(Color.parseColor(SharedPreference.getPref(context, EVENT_COLOR_1)), PorterDuff.Mode.SRC_ATOP);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.textno1.setText(optionList.getOption());
        holder.textno1.setTextColor(Color.parseColor("#8C" + eventColor3Opacity40));
    }

    @Override
    public int getItemCount() {
        return quizSpecificOptionListnew.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textno1;
        ImageView radio;
        LinearLayout raiolayout;

        public ViewHolder(View itemView) {
            super(itemView);


            textno1 = (TextView) itemView.findViewById(R.id.textno1);
            radio = (ImageView) itemView.findViewById(R.id.radio);
            raiolayout = (LinearLayout) itemView.findViewById(R.id.raiolayout);


        }
    }
}
