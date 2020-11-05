package com.procialize.eventapp.ui.SpotQuiz.View;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.GetterSetter.Header;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.SharedPreference;
import com.procialize.eventapp.costumTools.CustomViewPager;
import com.procialize.eventapp.session.SessionManager;
import com.procialize.eventapp.ui.SpotQuiz.adapter.QuizPagerDialogAdapter;
import com.procialize.eventapp.ui.SpotQuiz.viewModel.SpotQuizSubmitViewModel;
import com.procialize.eventapp.ui.quiz.model.QuizQuestion;
import com.procialize.eventapp.ui.quiz.model.QuizSubmit;

import java.util.ArrayList;
import java.util.List;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_ID;

public class SpotQuizDetailFragment extends Fragment implements View.OnClickListener {

    QuizPagerDialogAdapter pagerAdapter;
    String questionId, folder_id, folder_name, timer, total_correct, session_id;
    List<QuizQuestion> optionLists;
    CustomViewPager pager;
    public static Button submit;
    RecyclerView pollGraph;
    TextView tv_title, questionTv;
    View root;
    public static String quiz_question_id;
    private String quiz_options_id;
    public static boolean submitflag = false;
    int count;
    boolean flag = true;
    boolean flag1 = true;
    boolean flag2 = true;
    public SpotQuizSubmitViewModel quizDetailViewModel;
    SessionManager session;
    String event_id, api_token;
    ConnectionDetector cd;
    public static int count1 = 1;

    public static SpotQuizDetailFragment newInstance() {

        return new SpotQuizDetailFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.spot_quiz_detail, container, false);
        init();
        quizDetailViewModel = ViewModelProviders.of(this).get(SpotQuizSubmitViewModel.class);

        session = new SessionManager(getActivity().getApplicationContext());
        cd = ConnectionDetector.getInstance(getActivity());

        api_token = SharedPreference.getPref(getActivity(), AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(getActivity(), EVENT_ID);

//        if (ConnectionDetector.getInstance(getActivity()).isConnectingToInternet()) {
//
//        } else {
//        }

        return root;
    }

    public void init() {


        submit = root.findViewById(R.id.submit);
        tv_title = root.findViewById(R.id.tv_title);
        questionTv = root.findViewById(R.id.questionTv);
        pager = root.findViewById(R.id.pager);
        submit.setOnClickListener(this);

        try {
            questionId = getArguments().getString("id");
            folder_id = getArguments().getString("folder_id");
            folder_name = getArguments().getString("folder_name");
            timer = getArguments().getString("timer");
            total_correct = getArguments().getString("total_correct");
            session_id = getArguments().getString("session_id");
            optionLists = (ArrayList<QuizQuestion>) getArguments().getSerializable("questionlist");

            questionTv.setText(folder_name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        pagerAdapter = new QuizPagerDialogAdapter(getActivity(), optionLists, getActivity());
        pager.setAdapter(pagerAdapter);
        pager.setPagingEnabled(false);
        submit.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View v) {
        if (v == submit) {
            submitflag = true;
            Boolean valid = true;
            final int[] check = {0};
            int sum = 0;
            final String[] question_id = {""};
            final String[] question_ans = {""};
            final String[] value = {""};
            final RadioGroup[] radioGroup = new RadioGroup[1];
            final EditText[] ans_edit = new EditText[1];
            final RadioButton[] radioButton = new RadioButton[1];
//            Log.e("size", adapter.getItemCount() + "");


            String[] data = pagerAdapter.getselectedData();
            String[] question = pagerAdapter.getselectedquestion();

            if (data != null) {
                for (int i = 0; i < data.length; i++) {
                    if (i != 0) {
                        question_id[0] = question_id[0] + "$#";
                        question_ans[0] = question_ans[0] + "$#";
                    }

                    String id = optionLists.get(i).getId();
                    question_id[0] = question_id[0] + id;

                    flag = true;
                    flag1 = true;
                    flag2 = true;
                    if (data[i] != null) {
                        if (optionLists.get(i).getReplied() != null) {
                            if (optionLists.get(i).getReplied().equalsIgnoreCase("0")) {
                                if (!data[i].equalsIgnoreCase("")) {
                                    question_ans[0] = question_ans[0] + data[i];
                                } else {
                                    valid = false;
                                }
                            } else {

                                if (!data[i].equalsIgnoreCase("")) {

                                    String idno = optionLists.get(i).getId();

                                    for (int j = 0; j < optionLists.get(0).getQuiz_option().size(); j++) {
                                        if (optionLists.get(0).getQuiz_option().get(j).getOption().equals(data[i]) && optionLists.get(j).getId().equals(idno)) {
                                            question_ans[0] = question_ans[0] + optionLists.get(0).getQuiz_option().get(j).getOption_id();
                                        }
                                    }
                                } else {
                                    String idno = optionLists.get(i).getId();

                                    for (int j = 0; j < optionLists.get(0).getQuiz_option().size(); j++) {
                                        if (optionLists.get(0).getQuiz_option().get(j).getQuiz_id().equals(idno)) {
                                            if (flag1 == true) {
                                                question_ans[0] = question_ans[0] + "0";
                                                //question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                                flag1 = false;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {

                            if (!data[i].equalsIgnoreCase("")) {

                                String idno = optionLists.get(i).getId();

                                for (int j = 0; j < optionLists.get(0).getQuiz_option().size(); j++) {
                                    try {
                                        if (optionLists.get(0).getQuiz_option().get(j).getOption().equals(data[i]) && optionLists.get(0).getQuiz_option().get(j).getQuiz_id().equals(idno)) {
                                            question_ans[0] = question_ans[0] + optionLists.get(0).getQuiz_option().get(j).getOption_id();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                String idno = optionLists.get(i).getId();

                                for (int j = 0; j < optionLists.get(0).getQuiz_option().size(); j++) {
                                    if (optionLists.get(0).getQuiz_option().get(j).getQuiz_id().equals(idno)) {
                                        if (flag2 == true) {
                                            question_ans[0] = question_ans[0] + "0";
                                            // question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                            flag2 = false;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        String idno = optionLists.get(i).getId();


                        for (int j = 0; j < optionLists.get(0).getQuiz_option().size(); j++) {
                            if (optionLists.get(0).getQuiz_option().get(j).getQuiz_id().equals(idno)) {
                                if (flag == true) {
                                    // question_ans[0] = question_ans[0] + quizOptionList.get(j).getOptionId();
                                    question_ans[0] = question_ans[0] + "0";
                                    flag = false;
                                }
                            }
                        }
                    }

                }

                Log.e("valid_ans", question_ans.toString());
                Log.e("valid_id", question_id.toString());
                Log.e("valid_string", valid.toString());


                if (valid == true) {
//                    quiz_question_id = question_id[0];
//                    quiz_options_id = question_ans[0];
//                    int answers = pagerAdapter.getCorrectOption();
                    // Toast.makeText(appDelegate, quiz_options_id, Toast.LENGTH_SHORT).show();

                    if (submitflag == true) {
                        quiz_question_id = question_id[0];
                        quiz_options_id = question_ans[0];
                        int answers = pagerAdapter.getCorrectOption();

                        quizDetailViewModel.quizSubmit(api_token, event_id, quiz_question_id, quiz_options_id);
                        quizDetailViewModel.quizSubmit().observe(getActivity(), new Observer<QuizSubmit>() {
                            @Override
                            public void onChanged(QuizSubmit response) {
                                pagerAdapter.checkArray = null;
                                pagerAdapter.correctAnswer = "";
                                pagerAdapter.selectedOption = "";
                                pagerAdapter.dataArray = null;
                                pagerAdapter.dataIDArray = null;
                                pagerAdapter.ansArray = null;

                                try {
                                    List<Header> header = response.getHeader();

                                    if (header.get(0).getType().equalsIgnoreCase("success")) {
                                        int answers = pagerAdapter.getCorrectOption();


                                        Bundle bundle = new Bundle();
                                        bundle.putString("folderName", folder_name);
                                        bundle.putString("Answers", response.getTotal_correct_answer());
                                        bundle.putString("TotalQue", response.getTotal_questions());
                                        bundle.putString("id", questionId);
                                        bundle.putString("folder_id", folder_id);
                                        bundle.putString("folder_name", folder_name);
                                        bundle.putString("session_id", session_id);


                                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        YourScroreFragment fragInfo = new YourScroreFragment();
                                        fragInfo.setArguments(bundle);
                                        transaction.replace(R.id.fragment_frame, fragInfo);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                        count1 = 1;
                                        pagerAdapter.selectopt = 0;
                                        submitflag = true;


                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(getActivity(), "Please answer all questions", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

}
