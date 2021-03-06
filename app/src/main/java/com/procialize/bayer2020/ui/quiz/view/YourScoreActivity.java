package com.procialize.bayer2020.ui.quiz.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.bayer2020.Constants.Constant;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.Utility.SharedPreference;

import org.apache.commons.lang3.StringEscapeUtils;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
/*import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_1;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_2;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_3;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_COLOR_4;*/
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;

public class YourScoreActivity extends AppCompatActivity {

    TextView tv_header,questionTv, txt_count, viewResult, txt_title;
    ProgressBar progressBarCircle;
    Button btn_ok;
    String Page, folderName, correnctcount, folderid,totalcount;
    RelativeLayout relative,relativeMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_score);

        new RefreashToken(YourScoreActivity.this).callGetRefreashToken(YourScoreActivity.this);

/*
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
*/

        ImageView iv_back = findViewById(R.id.iv_back);
        //iv_back.setColorFilter(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_4)), PorterDuff.Mode.SRC_ATOP);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ImageView headerlogoIv = findViewById(R.id.headerlogoIv);

        String eventLogo = SharedPreference.getPref(this, EVENT_LOGO);
        String eventListMediaPath = SharedPreference.getPref(this, EVENT_LIST_MEDIA_PATH);
        Glide.with(this)
                .load(eventListMediaPath + eventLogo)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(headerlogoIv);
        questionTv = findViewById(R.id.questionTv);
        txt_count = findViewById(R.id.txt_count);
        viewResult = findViewById(R.id.viewResult);
        txt_title = findViewById(R.id.txt_title);
        progressBarCircle = findViewById(R.id.progressBarCircle);
        btn_ok = findViewById(R.id.btn_ok);
        relative = findViewById(R.id.relative);
        tv_header = findViewById(R.id.tv_header);
//        tv_score = findViewById(R.id.tv_score);
        relativeMain = findViewById(R.id.relativeMain);
        String api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        String event_id = SharedPreference.getPref(this, EVENT_ID);

        //--------------------------------------------------------------------------------------
       /* GetUserActivityReport getUserActivityReport = new GetUserActivityReport(this,api_token,
                event_id,
                Constant.pageVisited,
                "24",
                "0");
        getUserActivityReport.userActivityReport();*/
        //--------------------------------------------------------------------------------------


        /*CommonFunction.showBackgroundImage(YourScoreActivity.this, relative);
        tv_header.setTextColor(Color.parseColor(SharedPreference.getPref(YourScoreActivity.this, EVENT_COLOR_1)));
        questionTv.setTextColor(Color.parseColor(SharedPreference.getPref(YourScoreActivity.this, EVENT_COLOR_1)));
        txt_count.setTextColor(Color.parseColor(SharedPreference.getPref(YourScoreActivity.this, EVENT_COLOR_1)));
        txt_title.setBackgroundColor(Color.parseColor(SharedPreference.getPref(YourScoreActivity.this, EVENT_COLOR_1)));
        txt_title.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_2)));
        //tv_score.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_3)));
        viewResult.setTextColor(Color.parseColor(SharedPreference.getPref(this, EVENT_COLOR_1)));
        relativeMain.setBackgroundColor(Color.parseColor(SharedPreference.getPref(YourScoreActivity.this, EVENT_COLOR_2)));

        GradientDrawable border = new GradientDrawable();
        border.setStroke(1, Color.parseColor(SharedPreference.getPref(YourScoreActivity.this, EVENT_COLOR_1))); //black border with full opacity
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            viewResult.setBackgroundDrawable(border);
        } else {
            viewResult.setBackground(border);
        }*/

        Intent intent = getIntent();
        folderName = intent.getStringExtra("folderName");
        correnctcount = intent.getStringExtra("Answers");
        totalcount = intent.getStringExtra("TotalQue");
        folderid = intent.getStringExtra("folderid");
        Page = intent.getStringExtra("Page");
        if (Page.equalsIgnoreCase("Question")) {
            viewResult.setVisibility(View.VISIBLE);
            txt_title.setTextColor(Color.parseColor("#e4004b"));

        } else {
            viewResult.setVisibility(View.GONE);
        }

        viewResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(YourScoreActivity.this, QuizSubmittedActivity.class);
                intent1.putExtra("folder_id", folderid);
                intent1.putExtra("folder_name", folderName);
                intent1.putExtra("timer", "");
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
               /* Intent intent1 = new Intent(YourScoreActivity.this, QuizListingActivity.class);
                startActivity(intent1);
                finish();*/
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}