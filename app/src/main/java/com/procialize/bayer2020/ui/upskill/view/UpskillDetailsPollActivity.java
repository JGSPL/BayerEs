package com.procialize.bayer2020.ui.upskill.view;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.livepoll.adapter.PollGraphAdapter;
import com.procialize.bayer2020.ui.upskill.model.LivePollOption;
import com.procialize.bayer2020.ui.upskill.model.UpskillContentSubArray;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;

public class UpskillDetailsPollActivity extends AppCompatActivity implements View.OnClickListener {

    TextView questionTv, test;
    ImageView iv_banner;
    String api_token, eventid, quiz_options_id, questionId;
    Button btn_start;
    UpskillContentSubArray upskillContentSubArray;
    RadioGroup viewGroup;
    ArrayList<LivePollOption> optionLists = new ArrayList<>();
    List<RadioButton> radios;
    Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upskill_details_poll);


        setUpToolbar();
        upskillContentSubArray = (UpskillContentSubArray) getIntent().getSerializableExtra("upskillContent");
        radios = new ArrayList<RadioButton>();
        test = findViewById(R.id.test);
        btn_next = findViewById(R.id.btn_next);
        questionTv = findViewById(R.id.questionTv);
        String strQuestion = upskillContentSubArray.getContentInfo().get(0).getContent_desc_poll().get(0).getQuestion();

        if (strQuestion.contains("\n")) {
            strQuestion = strQuestion.trim().replace("\n", "<br/>");
        } else {
            strQuestion = strQuestion.trim();
        }
        String spannedString = String.valueOf(Jsoup.parse(strQuestion)).trim();//Html.fromHtml(feedData.getPost_status(), Html.FROM_HTML_MODE_COMPACT).toString();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Spanned strPost = Html.fromHtml(spannedString, Html.FROM_HTML_MODE_COMPACT);
            questionTv.setText(Utility.trimTrailingWhitespace(strPost));
        } else {
            Spanned strPost = Html.fromHtml(spannedString);
            questionTv.setText(Utility.trimTrailingWhitespace(strPost));
        }

        //questionTv.setText(strQuestion);
        optionLists = (ArrayList<LivePollOption>) upskillContentSubArray.getContentInfo().get(0).getContent_desc_poll().get(0).getOption();
        btn_next.setOnClickListener(this);
        if (optionLists.size() != 0) {

            viewGroup = (RadioGroup) findViewById(R.id.radiogroup);
            // viewGroup.setOnCheckedChangeListener(this);

            addRadioButtons(optionLists.size() + 1);

            if (viewGroup.isSelected()) {
                for (int i = 0; i < optionLists.size(); i++) {
                    test.setText(StringEscapeUtils.unescapeJava(optionLists.get(i).getOption()));
                    if (optionLists.get(i).getOption().equalsIgnoreCase(test.getText().toString())) {

                        quiz_options_id = optionLists.get(i)
                                .getOption_id();

                    }

                }
            }
        } else {
            Toast.makeText(this, "Select Option", Toast.LENGTH_SHORT).show();
        }
        upskillContentSubArray.getContentInfo().remove(0);
    }

    public void addRadioButtons(int number) {

        viewGroup.removeAllViewsInLayout();


        String[] color = {"#112F7A", "#0E73BA", "#04696E", "#00A89C", "#000000", "#4D4D4D", "#949494", "#112F7A", "#0E73BA", "#04696E", "#00A89C", "#000000"};

        Float totalUser = 0.0f;


        for (int k = 0; k < optionLists.size(); k++) {

            if (optionLists.get(k).getLive_poll_id()
                    .equalsIgnoreCase(questionId)) {
                totalUser = (totalUser + Float.parseFloat(optionLists
                        .get(k).getTotal_user()));

            }

        }

       /* if (replyFlag.equalsIgnoreCase("1")) {
            if (show_result.equalsIgnoreCase("0")) {
                viewGroup.setVisibility(View.GONE);
                PollGraphAdapter pollAdapter = new PollGraphAdapter(this, optionLists, questionId);
                pollAdapter.notifyDataSetChanged();
                pollGraph.setAdapter(pollAdapter);
                pollGraph.scheduleLayoutAnimation();
                subBtn.setVisibility(View.GONE);
                PollBtn.setVisibility(View.VISIBLE);
            } else {
                finish();
            }

        } else {

            pollGraph.setVisibility(View.GONE);*/
        viewGroup.setVisibility(View.VISIBLE);

        for (int row = 0; row < 1; row++) {

            for (int i = 1; i < number; i++) {

                LinearLayout ll = new LinearLayout(this);

                LinearLayout l3 = new LinearLayout(this);
                FrameLayout fl = new FrameLayout(this);

                ll.setOrientation(LinearLayout.HORIZONTAL);
                ll.setPadding(5, 10, 5, 10);

                LinearLayout ll2 = new LinearLayout(this);
                ll2.setOrientation(LinearLayout.HORIZONTAL);


                LinearLayout.LayoutParams rprms, rprmsRdBtn, rpms2;

                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId((row * 2) + i);
                rdbtn.setText(StringEscapeUtils.unescapeJava(optionLists.get(i - 1).getOption()));
                rdbtn.setTextColor(Color.parseColor("#000000"));
                rdbtn.setOnClickListener(this);

                radios.add(rdbtn);


                rprms = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                rprms.setMargins(5, 5, 5, 5);

                Float weight = 1.0f;

               /*     if (replyFlag.equalsIgnoreCase("1")) {


                        ll2.setBackgroundColor(Color.parseColor(color[i]));

                        weight = ((Float.parseFloat(optionLists.get(i - 1)
                                .getTotal_user()) / totalUser) * 100);


                    } else {*/

                weight = 100.0f;
                /*}*/

                rpms2 = new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.MATCH_PARENT, weight);
                rpms2.setMargins(5, 5, 5, 5);


                ll.setWeightSum(weight);
                ll.setLayoutParams(rprms);

                l3.setLayoutParams(rprms);
                ll.setPadding(5, 10, 5, 10);
                l3.setWeightSum(weight);

                ll2.setLayoutParams(rpms2);


                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                params.gravity = Gravity.CENTER;
                rprmsRdBtn = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                rprms.setMargins(5, 5, 5, 5);

                rdbtn.setLayoutParams(rprmsRdBtn);
                if (Build.VERSION.SDK_INT >= 21) {

                    ColorStateList colorStateList = new ColorStateList(
                            new int[][]{

                                    new int[]{-android.R.attr.state_checked}, //disabled
                                    new int[]{android.R.attr.state_checked} //enabled
                            },
                            new int[]{

                                    Color.parseColor("#4d4d4d")//disabled
                                    , Color.parseColor("#4d4d4d")//enabled

                            }
                    );


                    rdbtn.setButtonTintList(colorStateList);//set the color tint list
                    rdbtn.invalidate(); //could not be necessary
                }
//                    rdbtn.setButtonDrawable(R.drawable.radio_buttontoggle_first);
                rdbtn.setBackgroundResource(R.drawable.livepollback);

                l3.addView(ll2, rpms2);


                fl.addView(l3, rprms);
                fl.addView(rdbtn, rprmsRdBtn);

                // ll2.addView(rdbtn, rprmsRdBtn);
                ll.addView(fl, params);

                viewGroup.addView(ll, rprms);
                viewGroup.invalidate();
            }

        }
    }

    /*}*/


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (upskillContentSubArray.getContentInfo().get(0).getContent_type().equalsIgnoreCase("Text")) {
                    startActivity(new Intent(this, UpskillDetailsTextActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                } else if (upskillContentSubArray.getContentInfo().get(0).getContent_type().equalsIgnoreCase("Survey")) {
                    startActivity(new Intent(this, UpskillSurveyActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                } else if (upskillContentSubArray.getContentInfo().get(0).getContent_type().equalsIgnoreCase("Poll")) {
                    startActivity(new Intent(this, UpskillDetailsPollActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                } else if (upskillContentSubArray.getContentInfo().get(0).getContent_type().equalsIgnoreCase("Pdf")) {
                    startActivity(new Intent(this, UpskillDetailsPdfActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                } else if (upskillContentSubArray.getContentInfo().get(0).getContent_type().equalsIgnoreCase("Image")) {
                    startActivity(new Intent(this, UpskillDetailsImageActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                } else if (upskillContentSubArray.getContentInfo().get(0).getContent_type().equalsIgnoreCase("Video")) {
                    startActivity(new Intent(this, UpskillDetailsVideoActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                } else if (upskillContentSubArray.getContentInfo().get(0).getContent_type().equalsIgnoreCase("Quiz")) {
                    startActivity(new Intent(this, UpskillDetailsQuizActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                } else if (upskillContentSubArray.getContentInfo().get(0).getContent_type().equalsIgnoreCase("Audio")) {
                    startActivity(new Intent(this, UpskillDetailsAudioActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                }

                break;
        }
    }

    private void setUpToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            mToolbar.showOverflowMenu();
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

        }
    }
}