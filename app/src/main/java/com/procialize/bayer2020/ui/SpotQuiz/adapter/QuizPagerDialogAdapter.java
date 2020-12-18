package com.procialize.bayer2020.ui.SpotQuiz.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseIntArray;
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
import com.procialize.bayer2020.ui.quiz.model.QuizOption;
import com.procialize.bayer2020.ui.quiz.model.QuizQuestion;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_1;

public class QuizPagerDialogAdapter extends PagerAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<QuizQuestion> quizList;
    private List<QuizOption> quizOptionList = new ArrayList<>();
    public static ArrayList<QuizOption> quizSpecificOptionListnew = new ArrayList<QuizOption>();
    int count = 0;
    public static String correctAnswer;
    public static String[] dataArray;
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
    private SparseIntArray mSpCheckedState = new SparseIntArray();
    String accessToken, event_id, colorActive;
    Context context;
    private RadioGroup lastCheckedRadioGroup = null;

    public QuizPagerDialogAdapter(Context context, List<QuizQuestion> quizList, Activity activity) {
        this.context = context;
        this.quizList = quizList;
        dataArray = new String[quizList.size()];
        dataIDArray = new String[quizList.size()];
        checkArray = new String[quizList.size()];
        ansArray = new String[quizList.size()];
        optionArray = new String[quizList.size()];
        this.activity = activity;

//        session = new SessionManager(context.getApplicationContext());
//        accessToken = session.getUserDetails().get(SessionManager.KEY_TOKEN);
//        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
//        event_id = prefs.getString("eventid", "1");
//        colorActive = prefs.getString("colorActive", "");
//
//        quizQuestionUrl = constant.baseUrl + constant.quizsubmit;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View myImageLayout = inflater.inflate(R.layout.spot_quiz_row_test, view, false);

        TextView quiz_title_txt, quiz_question_distruct, txt_page, textno;
        LinearLayout raiolayout;
//        EditText ans_edit;
        RadioGroup viewGroup;

        quiz_title_txt = (TextView) myImageLayout
                .findViewById(R.id.quiz_question);


        raiolayout = (LinearLayout) myImageLayout.findViewById(R.id.raiolayout);
        txt_page = (TextView) myImageLayout.findViewById(R.id.txt_page);
        quiz_question_distruct = (TextView) myImageLayout.findViewById(R.id.quiz_question);

        viewGroup = (RadioGroup) myImageLayout.findViewById(R.id.radiogroup);

        txt_page.setText(String.valueOf(position + 1) + "/" + String.valueOf(quizList.size()));
        quiz_question_distruct.setTextColor(Color.parseColor(SharedPreference.getPref(activity, EVENT_COLOR_1)));
        if (quizList.get(position).getReplied() == null) {

            if (raiolayout.getVisibility() == View.GONE) {
                raiolayout.setVisibility(View.VISIBLE);
            }

            quiz_title_txt.setText(StringEscapeUtils.unescapeJava(quizList.get(position).getQuestion()));
            quizOptionList = quizList.get(position).getQuiz_option();
            if (quizSpecificOptionListnew.size() > 0) {
                quizSpecificOptionListnew.clear();
            }


            for (int i = 0; i < quizOptionList.size(); i++) {

                if (quizOptionList.get(i).getQuiz_id().equalsIgnoreCase(quizList.get(position).getId())) {

                    QuizOption quizTempOptionList = new QuizOption();

                    quizTempOptionList.setOption(quizOptionList.get(i).getOption());
                    quizTempOptionList.setOption_id(quizOptionList.get(i)
                            .getOption_id());
                    quizTempOptionList.setQuiz_id(quizOptionList.get(i)
                            .getQuiz_id());

                    quizSpecificOptionListnew.add(quizTempOptionList);

                }

            }

            correctAnswer = quizList.get(position).getCorrect_answer_id();
            int number = quizSpecificOptionListnew.size() + 1;

            //Display  display= ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

            /*Display display = activity.getWindowManager().getDefaultDisplay();
            int width = display.getWidth() - 40;
            double ratio = ((float) (width)) / 300.0;
            int height = (int) (ratio * 50);*/


            for (int row = 0; row < 1; row++) {


                for (int i = 1; i < number; i++) {

                    AppCompatRadioButton rdbtn = new AppCompatRadioButton(context);
                    rdbtn.setId((row * 2) + i);
                    rdbtn.setText(StringEscapeUtils.unescapeJava(quizSpecificOptionListnew.get(i - 1).getOption()));
                    rdbtn.setTextColor(Color.parseColor(SharedPreference.getPref(activity, EVENT_COLOR_1)));
                    GradientDrawable border = new GradientDrawable();
                    border.setStroke(1, Color.parseColor(SharedPreference.getPref(activity, EVENT_COLOR_1))); //black border with full opacity
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        rdbtn.setBackgroundDrawable(border);
                    } else {
                        rdbtn.setBackground(border);
                    }
                    rdbtn.setTextSize(14);
//                    rdbtn.setBackgroundResource(R.drawable.livepollback);
                    if (Build.VERSION.SDK_INT >= 21) {

                        ColorStateList colorStateList = new ColorStateList(
                                new int[][]{

                                        new int[]{-android.R.attr.state_checked}, //disabled
                                        new int[]{android.R.attr.state_checked} //enabled
                                },
                                new int[]{

                                        /*   Color.parseColor("#585e44")//disabled
                                           , Color.parseColor("#e31e24")//enabled */
                                        Color.parseColor("#585e44")//disabled
                                        , Color.parseColor(SharedPreference.getPref(activity, EVENT_COLOR_1))//enabled

                                }
                        );


                        rdbtn.setButtonTintList(colorStateList);//set the color tint list
                        rdbtn.invalidate(); //could not be necessary
                    }
                    /*RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                            width,
                            height
                    );*/

                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    p.setMargins(10, 15, 10, 10);

                    // rdbtn.setPadding(10,10,10,5);
                    rdbtn.setLayoutParams(p);
                    rdbtn.setTag(quizSpecificOptionListnew.get(i - 1).getOption_id());
//                    rdbtn.setBackgroundResource(R.drawable.newsfeedrowbg);

//                    rdbtn.setButtonDrawable(R.drawable.radio_buttontoggle_first);
                    rdbtn.setPaddingRelative(5, 5, 5, 5);
                    rdbtn.setPadding(15, 15, 15, 15);


                    if (checkArray[position] != null) {
                        if (rdbtn.getText().toString().equalsIgnoreCase(checkArray[position])) {
                            rdbtn.setChecked(true);


                        }
                    }


                    viewGroup.addView(rdbtn);

                    flag = 1;
                }


            }

            for (int i = 0; i < quizSpecificOptionListnew.size(); i++) {

                if (quizSpecificOptionListnew
                        .get(0)
                        .getOption()
                        .equalsIgnoreCase(
                                quizSpecificOptionListnew.get(i).getOption())) {

                    quiz_options_id = quizSpecificOptionListnew.get(i)
                            .getOption_id();
                }

            }
            int genid = viewGroup.getCheckedRadioButtonId();
            AppCompatRadioButton radioButton = viewGroup.findViewById(genid);


            //  value[0] = radioButton.getText().toString();
            if (radioButton != null) {
                dataArray[position] = radioButton.getText().toString();
                dataIDArray[position] = radioButton.getText().toString();
            }
        } else if (quizList.get(position).getReplied().equalsIgnoreCase("1")) {

            if (raiolayout.getVisibility() == View.VISIBLE) {
                raiolayout.setVisibility(View.GONE);
            }

            quiz_title_txt.setText(quizList.get(position).getQuestion());
            quiz_question_distruct.setText(quizList.get(position).getQuestion());


        } else if (quizList.get(position).getReplied().equalsIgnoreCase("0")) {

            if (raiolayout.getVisibility() == View.GONE) {
                raiolayout.setVisibility(View.VISIBLE);
            }

            quiz_title_txt.setText(quizList.get(position).getQuestion());
            quizOptionList = quizList.get(position).getQuiz_option();
            if (quizSpecificOptionListnew.size() > 0) {
                quizSpecificOptionListnew.clear();
            }


            for (int i = 0; i < quizOptionList.size(); i++) {

                if (quizOptionList.get(i).getQuiz_id().equalsIgnoreCase(quizList.get(position).getId())) {

                    QuizOption quizTempOptionList = new QuizOption();

                    quizTempOptionList.setOption(quizOptionList.get(i).getOption());
                    quizTempOptionList.setOption_id(quizOptionList.get(i)
                            .getOption_id());
                    quizTempOptionList.setQuiz_id(quizOptionList.get(i)
                            .getQuiz_id());

                    quizSpecificOptionListnew.add(quizTempOptionList);

                }

            }


            int number = quizSpecificOptionListnew.size() + 1;

            Display display = activity.getWindowManager().getDefaultDisplay();
            int width = display.getWidth() - 40;
            double ratio = ((float) (width)) / 300.0;
            int height = (int) (ratio * 50);


            for (int row = 0; row < 1; row++) {
               /* LinearLayout ll = new LinearLayout(activity);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.setGravity(Gravity.CENTER);*/
                //  viewGroup.removeAllViews();
                //	if(flag==0)

                for (int i = 1; i < number; i++) {

                    final AppCompatRadioButton rdbtn = new AppCompatRadioButton(activity);
                    rdbtn.setId((row * 2) + i);
                    rdbtn.setText(StringEscapeUtils.unescapeJava(quizSpecificOptionListnew.get(i - 1).getOption()));
                    rdbtn.setTextColor(Color.parseColor(SharedPreference.getPref(activity, EVENT_COLOR_1)));
                    GradientDrawable border = new GradientDrawable();
                    border.setStroke(1, Color.parseColor(SharedPreference.getPref(activity, EVENT_COLOR_1))); //black border with full opacity
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        rdbtn.setBackgroundDrawable(border);
                    } else {
                        rdbtn.setBackground(border);
                    }
                    rdbtn.setTextSize(9);
//                    rdbtn.setBackgroundResource(R.drawable.livepollback);

                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                            width,
                            height
                    );

                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    p.setMargins(10, 15, 10, 10);
                    // rdbtn.setPadding(10,10,10,5);
                    rdbtn.setLayoutParams(p);
                    rdbtn.setTag(quizSpecificOptionListnew.get(i - 1).getOption_id());
                    if (Build.VERSION.SDK_INT >= 21) {

                        ColorStateList colorStateList = new ColorStateList(
                                new int[][]{

                                        new int[]{-android.R.attr.state_checked}, //disabled
                                        new int[]{android.R.attr.state_checked} //enabled
                                },
                                new int[]{

                                        Color.parseColor("#585e44")//disabled
                                        , Color.parseColor(SharedPreference.getPref(activity, EVENT_COLOR_1))//enabled

                                }
                        );


                        rdbtn.setButtonTintList(colorStateList);//set the color tint list
                        rdbtn.invalidate(); //could not be necessary
                    }
//                    rdbtn.setButtonDrawable(R.drawable.radio_buttontoggle_first);
//                    rdbtn.setGravity(Gravity.CENTER);
                    rdbtn.setPadding(15, 15, 15, 15);
                    rdbtn.setPaddingRelative(5, 5, 5, 5);

                    // rdbtn.setCompoundDrawablePadding(5);

//                    ColorStateList colorStateList = new ColorStateList(
//                            new int[][]{
//                                    new int[]{-android.R.attr.state_checked}, // unchecked
//                                    new int[]{android.R.attr.state_checked}  // checked
//                            },
//                            new int[]{
//                                    Integer.parseInt(String.valueOf(activity.getResources().getDrawable(R.drawable.unchecked_radio))),
//                                    Integer.parseInt(String.valueOf(activity.getResources().getDrawable(R.drawable.checked_radio)))
//                            }
//                    );
//                    rdbtn.setSupportButtonTintList(colorStateList);
                    //  rdbtn.setOnCheckedChangeListener(activity.this);

//                    if (i == 1)
//                        rdbtn.setChecked(true);

                    if (checkArray[position] != null) {
                        if (rdbtn.getText().toString().equalsIgnoreCase(checkArray[position])) {
                            rdbtn.setChecked(true);

                        }
                    }


                    viewGroup.addView(rdbtn);

                    flag = 1;
                }


            }

            for (int i = 0; i < quizSpecificOptionListnew.size(); i++) {

                if (quizSpecificOptionListnew
                        .get(i)
                        .getOption()
                        .equalsIgnoreCase(
                                quizSpecificOptionListnew.get(i).getOption())) {

                    quiz_options_id = quizSpecificOptionListnew.get(i)
                            .getOption_id();
                }

            }
            int genid = viewGroup.getCheckedRadioButtonId();
            AppCompatRadioButton radioButton = viewGroup.findViewById(genid);


            if (radioButton != null) {
                dataArray[position] = radioButton.getText().toString();
                dataIDArray[position] = radioButton.getText().toString();
            }


        }


        final TextWatcher txwatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start,
                                          int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                dataArray[position] = s.toString();
                ansArray[position] = s.toString();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                System.out.print("Hello");
            }
        };


//        ans_edit.addTextChangedListener(txwatcher);
        viewGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int genid = radioGroup.getCheckedRadioButtonId();
                int nonchecked = quizList.indexOf(correctAnswer);
                AppCompatRadioButton radioButton = radioGroup.findViewById(genid);
//                AppCompatRadioButton radioButton2 = radioGroup.findViewById(nonchecked);
                //  value[0] = radioButton.getText().toString();
                dataArray[position] = radioButton.getText().toString();
                checkArray[position] = radioButton.getText().toString();
//                 = radioButton.getText().toString();
//                if (quizOptionList.size() == 1) {
//                    selectedOption = quizOptionList.get(i).getOptionId();
//                } else {
                selectedOption = quizSpecificOptionListnew.get(i - 1).getOption_id();
                selectopt = selectopt + 1;
                optionArray[position] = selectedOption;
//                }
//                correctAnswer=quizList.get(position).getCorrect_answer();
                if (selectedOption.equalsIgnoreCase(correctAnswer)) {
                    count = count + 1;
                }
            }
        });

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
    }

    public String[] getselectedData() {
        return dataArray;

    }

    public String[] getselectedOption() {
        return optionArray;

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

    public int getItemCount() {
        return quizList.size();
    }
}