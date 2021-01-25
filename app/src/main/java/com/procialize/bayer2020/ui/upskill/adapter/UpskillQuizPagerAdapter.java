package com.procialize.bayer2020.ui.upskill.adapter;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.viewpager.widget.PagerAdapter;

import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.ui.quiz.model.QuizQuestion;
import com.procialize.bayer2020.ui.quiz.view.QuizDetailActivity;
import com.procialize.bayer2020.ui.upskill.model.QuizList;
import com.procialize.bayer2020.ui.upskill.model.QuizOption;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_3;

public class UpskillQuizPagerAdapter extends PagerAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<QuizList> quizList;
    private List<QuizOption> quizOptionList = new ArrayList<>();
    public static ArrayList<QuizOption> quizSpecificOptionListnew = new ArrayList<QuizOption>();
    ArrayList<QuizOption> quizSpecificOptionListnew1 = new ArrayList<QuizOption>();
    int count = 0;
    public static String correctAnswer;
    public static  String[] dataArray;
    public static String[] dataIDArray;
    public static String[] checkArray;
    public static String[] ansArray;
    public static String[] optionArray;

    String quiz_options_id;
    public static String selectedOption;
    public static int selectopt = 0;
    String[] flagArray;
    private String quizQuestionUrl = "";
    int flag = 0;
    String MY_PREFS_NAME = "ProcializeInfo";
    String accessToken, event_id;

    Typeface typeFace;
    private RadioGroup lastCheckedRadioGroup = null;

    public UpskillQuizPagerAdapter(Activity activity, List<QuizList> quizList) {
        this.activity = activity;
        this.quizList = quizList;
        dataArray = new String[quizList.size()];
        dataIDArray = new String[quizList.size()];
        checkArray = new String[quizList.size()];
        ansArray = new String[quizList.size()];
        optionArray = new String[quizList.size()];
        inflater = LayoutInflater.from(activity);

    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View myImageLayout = inflater.inflate(R.layout.quiz_row_test, view, false);

        TextView quiz_title_txt, txt_page, textno;
        LinearLayout raiolayout;
//        EditText ans_edit;
        RadioGroup viewGroup;

        quiz_title_txt = (TextView) myImageLayout
                .findViewById(R.id.quiz_question);

        quiz_title_txt.setText(quizList.get(0).getQuestion());
        raiolayout = (LinearLayout) myImageLayout.findViewById(R.id.raiolayout);
        txt_page = (TextView) myImageLayout.findViewById(R.id.txt_page);

        viewGroup = (RadioGroup) myImageLayout.findViewById(R.id.radiogroup);

        txt_page.setText(String.valueOf(position + 1) + "/" + String.valueOf(quizList.size()));

        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public int getCount() {
        return quizList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        QuizDetailActivity.submitflag = false;
    }



    public int getItemCount() {
        return quizList.size();
    }
}
