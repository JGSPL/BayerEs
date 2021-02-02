package com.procialize.bayer2020.ui.upskill.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.procialize.bayer2020.R;
import com.procialize.bayer2020.ui.upskill.model.QuizList;
import com.procialize.bayer2020.ui.upskill.model.QuizOption;
import com.procialize.bayer2020.ui.upskill.model.QuizQuestion;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

public class UpskillQuizSubmitedAdapter extends RecyclerView.Adapter<UpskillQuizSubmitedAdapter.ViewHolder>{

    private Activity activity;
    private LayoutInflater inflater;
    private List<QuizList> quizList;
    private List<QuizQuestion> question;
    private List<com.procialize.bayer2020.ui.upskill.model.QuizOption> quizOptionList = new ArrayList<>();
    ArrayList<QuizOption> quizSpecificOptionListnew = new ArrayList<QuizOption>();
    String[] dataArray;
    int[] righanswe;
    String[] dataIDArray;
    String[] checkArray;
    String[] ansArray;
    String quiz_options_id;

    String[] flagArray;

    int flag = 0;
    String correctAnswer;
    String selectedAnswer, colorActive;
    private SparseIntArray mSpCheckedState = new SparseIntArray();
    String selectedOption;
    int selectopt = 0;
    private RadioGroup lastCheckedRadioGroup = null;
    int count = 0;
    UpskillAnswerAdapter adapter;

    public UpskillQuizSubmitedAdapter(Activity activity, List<QuizList> quizList) {
        this.activity = activity;
        this.quizList = quizList;
        dataArray = new String[quizList.size()];
        dataIDArray = new String[quizList.size()];
        checkArray = new String[quizList.size()];
        ansArray = new String[quizList.size()];
    }

    @SuppressLint("RestrictedApi")
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quiz_row_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);

    /*    String eventColor3 = SharedPreference.getPref(activity, EVENT_COLOR_3);

        String eventColor3Opacity40 = eventColor3.replace("#", "");
*/
        if (quizList.get(position).getReplied() != null) {
            try {
                holder.txt_question.setText(StringEscapeUtils.unescapeJava(quizList.get(position).getQuestion()));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }

           // holder.txt_question.setTextColor(Color.parseColor(SharedPreference.getPref(activity, EVENT_COLOR_1)));

            if (quizList.get(position).getSelected_option() != null) {
                if (quizList.get(position).getSelected_option().equals("0")) {
                    holder.tv_not_attempted.setVisibility(View.VISIBLE);
                } else {
                    holder.tv_not_attempted.setVisibility(View.GONE);
                }
            } else {
                holder.tv_not_attempted.setVisibility(View.GONE);
            }

            if (holder.raiolayout.getVisibility() == View.GONE) {
                holder.raiolayout.setVisibility(View.VISIBLE);
            }

            // holder.txt_question.setTextColor(Color.parseColor(colorActive));
            quizOptionList = quizList.get(position).getOption();
            if (quizSpecificOptionListnew.size() > 0) {
                quizSpecificOptionListnew.clear();
            }


            for (int i = 0; i < quizOptionList.size(); i++) {

                if (quizOptionList.get(i).getQuiz_id().equalsIgnoreCase(quizList.get(position).getQuiz_id())) {

                    QuizOption quizTempOptionList = new QuizOption();
                    try{
                        quizTempOptionList.setOption(StringEscapeUtils.unescapeJava(quizOptionList.get(i).getOption()));
                    }catch (IllegalArgumentException e){
                        e.printStackTrace();

                    }
                    quizTempOptionList.setOption_id(quizOptionList.get(i)
                            .getOption_id());
                    quizTempOptionList.setQuiz_id(quizOptionList.get(i)
                            .getQuiz_id());

                    quizSpecificOptionListnew.add(quizTempOptionList);

                }

            }

            correctAnswer = quizList.get(position).getCorrect_answer();
            selectedAnswer = quizList.get(position).getSelected_option();

            if (correctAnswer.equalsIgnoreCase(selectedAnswer)) {
                count = count + 1;
            }

            int number = quizSpecificOptionListnew.size() + 1;

            adapter = new UpskillAnswerAdapter(activity, quizSpecificOptionListnew, correctAnswer, selectedAnswer);
            holder.ansList.setAdapter(adapter);

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout raiolayout;
        RecyclerView ansList;
        TextView txt_question,tv_not_attempted;


        public ViewHolder(View convertView) {
            super(convertView);

            raiolayout = (LinearLayout) convertView
                    .findViewById(R.id.raiolayout);
            ansList = (RecyclerView) convertView
                    .findViewById(R.id.ansList);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
            ansList.setLayoutManager(mLayoutManager);

            txt_question = convertView.findViewById(R.id.txt_question);
            tv_not_attempted = convertView.findViewById(R.id.tv_not_attempted);

        }
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public String[] getselectedData() {
        return dataArray;

    }

    public String[] getselectedquestion() {
        return dataIDArray;

    }

    public int getSelectedOption() {
        return selectopt;

    }

    public int getCorrectOption() {
        return count;

    }

}
