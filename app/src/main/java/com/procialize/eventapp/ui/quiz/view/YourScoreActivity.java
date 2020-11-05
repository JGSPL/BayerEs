package com.procialize.eventapp.ui.quiz.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.procialize.eventapp.Constants.RefreashToken;
import com.procialize.eventapp.R;
import com.procialize.eventapp.Utility.CommonFunction;
import com.procialize.eventapp.Utility.SharedPreference;

import org.apache.commons.lang3.StringEscapeUtils;

import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.eventapp.Utility.SharedPreferencesConstant.EVENT_COLOR_2;

public class YourScoreActivity extends AppCompatActivity {

    TextView tv_header,questionTv, txt_count, viewResult, txt_title;
    ProgressBar progressBarCircle;
    Button btn_ok;
    String Page, folderName, correnctcount, totalcount;
    RelativeLayout relative,relativeMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_score);

        new RefreashToken(YourScoreActivity.this).callGetRefreashToken(YourScoreActivity.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


        questionTv = findViewById(R.id.questionTv);
        txt_count = findViewById(R.id.txt_count);
        viewResult = findViewById(R.id.viewResult);
        txt_title = findViewById(R.id.txt_title);
        progressBarCircle = findViewById(R.id.progressBarCircle);
        btn_ok = findViewById(R.id.btn_ok);
        relative = findViewById(R.id.relative);
        tv_header = findViewById(R.id.tv_header);
        relativeMain = findViewById(R.id.relativeMain);

        CommonFunction.showBackgroundImage(YourScoreActivity.this, relative);
        tv_header.setTextColor(Color.parseColor(SharedPreference.getPref(YourScoreActivity.this, EVENT_COLOR_1)));
        questionTv.setTextColor(Color.parseColor(SharedPreference.getPref(YourScoreActivity.this, EVENT_COLOR_1)));
        txt_count.setTextColor(Color.parseColor(SharedPreference.getPref(YourScoreActivity.this, EVENT_COLOR_1)));
        txt_title.setBackgroundColor(Color.parseColor(SharedPreference.getPref(YourScoreActivity.this, EVENT_COLOR_1)));
        txt_title.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)));
        viewResult.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_1)));
        relativeMain.setBackgroundColor(Color.parseColor(SharedPreference.getPref(YourScoreActivity.this, EVENT_COLOR_2)));

        GradientDrawable border = new GradientDrawable();
        border.setStroke(1, Color.parseColor(SharedPreference.getPref(YourScoreActivity.this, EVENT_COLOR_1))); //black border with full opacity
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            viewResult.setBackgroundDrawable(border);
        } else {
            viewResult.setBackground(border);
        }

        Intent intent = getIntent();
        folderName = intent.getStringExtra("folderName");
        correnctcount = intent.getStringExtra("Answers");
        totalcount = intent.getStringExtra("TotalQue");
        Page = intent.getStringExtra("Page");
        if (Page.equalsIgnoreCase("Question")) {
            viewResult.setVisibility(View.VISIBLE);
        } else {
            viewResult.setVisibility(View.GONE);
        }

        viewResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(YourScoreActivity.this, QuizSubmittedActivity.class);
                intent1.putExtra("folder_id", folderName);
                startActivity(intent1);
                finish();
            }
        });

        try {
            questionTv.setText(StringEscapeUtils.unescapeJava(folderName));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();

        }
        txt_count.setText(correnctcount + "/" + totalcount);


        progressBarCircle.setMax(Integer.parseInt(totalcount));
        progressBarCircle.setProgress(Integer.parseInt(correnctcount));


        txt_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(YourScoreActivity.this, QuizListingActivity.class);
                startActivity(intent1);
                finish();
            }
        });
        QuizDetailActivity.submitflag = false;
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(YourScoreActivity.this, QuizListingActivity.class);
                startActivity(intent1);
                finish();
            }
        });

    }
}