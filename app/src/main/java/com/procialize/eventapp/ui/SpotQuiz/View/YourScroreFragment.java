package com.procialize.eventapp.ui.SpotQuiz.View;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.SharedPreference;

import org.apache.commons.lang3.StringEscapeUtils;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_2;

public class YourScroreFragment extends Fragment {

    TextView tv_header, questionTv, txt_count, viewResult, txt_title;
    ProgressBar progressBarCircle;
    Button btn_ok;
    String Page, folderName, correnctcount, totalcount;
    RelativeLayout relative, relativeMain;
    String session_id,folder_id,questionId,TotalQue, Answers;

    public static YourScroreFragment newInstance() {

        return new YourScroreFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_your_score, container, false);

        questionTv = root.findViewById(R.id.questionTv);
        txt_count = root.findViewById(R.id.txt_count);
        viewResult = root.findViewById(R.id.viewResult);
        txt_title = root.findViewById(R.id.txt_title);
        progressBarCircle = root.findViewById(R.id.progressBarCircle);
        btn_ok = root.findViewById(R.id.btn_ok);
//        relative = root.findViewById(R.id.relative);
//        tv_header = root.findViewById(R.id.tv_header);
//        relativeMain = root.findViewById(R.id.relativeMain);

        try {
            folderName = getArguments().getString("folderName");
            Answers = getArguments().getString("Answers");
            TotalQue = getArguments().getString("TotalQue");
            questionId = getArguments().getString("id");
            folder_id = getArguments().getString("folder_id");
            session_id = getArguments().getString("session_id");
        } catch (Exception e) {
            e.printStackTrace();
        }

//        tv_header.setTextColor(Color.parseColor(SharedPreference.getPref(getActivity(), EVENT_COLOR_1)));
        questionTv.setTextColor(Color.parseColor(SharedPreference.getPref(getActivity(), EVENT_COLOR_1)));
        txt_count.setTextColor(Color.parseColor(SharedPreference.getPref(getActivity(), EVENT_COLOR_1)));
        txt_title.setBackgroundColor(Color.parseColor(SharedPreference.getPref(getActivity(), EVENT_COLOR_1)));
        txt_title.setTextColor(Color.parseColor(SharedPreference.getPref(getActivity(), EVENT_COLOR_2)));
        viewResult.setTextColor(Color.parseColor(SharedPreference.getPref(getActivity(), EVENT_COLOR_1)));
//        relativeMain.setBackgroundColor(Color.parseColor(SharedPreference.getPref(getActivity(), EVENT_COLOR_2)));

        GradientDrawable border = new GradientDrawable();
        border.setStroke(1, Color.parseColor(SharedPreference.getPref(getActivity(), EVENT_COLOR_1))); //black border with full opacity
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            viewResult.setBackgroundDrawable(border);
        } else {
            viewResult.setBackground(border);
        }

        viewResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent1 = new Intent(getActivity(), QuizSubmittedActivity.class);
//                intent1.putExtra("folder_id", folderName);
//                startActivity(intent1);
//                finish();
                Bundle bundle = new Bundle();
                bundle.putString("folder_id", folder_id);
                bundle.putString("folderName", folderName);
                bundle.putString("Answers", Answers);
                bundle.putString("TotalQue", TotalQue);
                bundle.putString("id", questionId);
                bundle.putString("session_id", session_id);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                SpotQuizResultFragment fragInfo = new SpotQuizResultFragment();
                fragInfo.setArguments(bundle);
                transaction.replace(R.id.fragment_frame, fragInfo);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        try {
            questionTv.setText(StringEscapeUtils.unescapeJava(folderName));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();

        }
        txt_count.setText(Answers + "/" + TotalQue);


        progressBarCircle.setMax(Integer.parseInt(TotalQue));
        progressBarCircle.setProgress(Integer.parseInt(Answers));
        SpotQuizDetailFragment.submitflag = false;

        return root;
    }
}
