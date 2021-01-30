package com.procialize.bayer2020.ui.upskill.view;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.agenda.model.FetchAgenda;
import com.procialize.bayer2020.ui.upskill.model.UpskillContentSubArray;
import com.procialize.bayer2020.ui.upskill.model.UpskillList;


import org.jsoup.Jsoup;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;
import static com.procialize.bayer2020.ui.upskill.view.UpskillDetailsFirstActivity.click_count;

public class UpskillDetailsAudioActivity extends AppCompatActivity implements View.OnClickListener {

    UpskillContentSubArray upskillContentSubArray;
    ImageView iv_play_pause;
    TextView tv_duration,tv_description;
    String spannedString;
    Button btn_next;
    UpskillList upskillList;
    String api_token, eventid,last_submit;
    SeekBar seek_bar;
    MediaPlayer mp;
    private double startTime = 0;
    private Handler myHandler = new Handler();
    private double finalTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upskill_details_audio);
        setUpToolbar();
        upskillContentSubArray = (UpskillContentSubArray) getIntent().getSerializableExtra("upskillContent");
        upskillList = (UpskillList) getIntent().getSerializableExtra("upskill_info");
        iv_play_pause = findViewById(R.id.iv_play_pause);
        tv_duration = findViewById(R.id.tv_duration);
        btn_next = findViewById(R.id.btn_next);
        seek_bar = findViewById(R.id.seek_bar);
        btn_next.setOnClickListener(this);
        tv_description = findViewById(R.id.tv_description);
        mp = new MediaPlayer();
        try {
            String url = upskillContentSubArray.getContentInfo().get(click_count).getContent_url();
            //you can change the path, here path is external directory(e.g. sdcard) /Music/maine.mp3
            mp.setDataSource(url);

            mp.prepare();
            long duration = mp.getDuration();
            String hms = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)),
                    TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));

            tv_duration.setText(""+hms);
            String contentDesc = upskillContentSubArray.getContentInfo().get(click_count).getContent_desc();
            if (contentDesc.contains("\n")) {
                contentDesc = contentDesc.trim().replace("\n", "<br/>");
            } else {
                contentDesc = contentDesc.trim();
            }
            String spannedString = String.valueOf(Jsoup.parse(contentDesc)).trim();//Html.fromHtml(feedData.getPost_status(), Html.FROM_HTML_MODE_COMPACT).toString();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Spanned strPost = Html.fromHtml(spannedString, Html.FROM_HTML_MODE_COMPACT);
                tv_description.setText(Utility.trimTrailingWhitespace(strPost));
            } else {
                Spanned strPost = Html.fromHtml(spannedString);
                tv_description.setText(Utility.trimTrailingWhitespace(strPost));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        iv_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mp.isPlaying()) {
                    mp.start();
                    finalTime = mp.getDuration();
                    seek_bar.setMax((int) finalTime);
                    seek_bar.setProgress((int)startTime);
                    iv_play_pause.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_b));
                    myHandler.postDelayed(UpdateSongTime,100);
                } else {
                    mp.pause();
                    iv_play_pause.setImageDrawable(getResources().getDrawable(R.drawable.play));
                }
            }
        });




        if(upskillContentSubArray.getContentInfo().size() == click_count+1)
        {
            btn_next.setText("Submit");
        }
        else
        {
            btn_next.setText("Next");
        }
    }

    private Runnable UpdateSongTime = new Runnable() {
        @TargetApi(Build.VERSION_CODES.GINGERBREAD) public void run() {
            startTime = mp.getCurrentPosition();
            /*startTimeField.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );*/
            seek_bar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };

    private void setUpToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            mToolbar.showOverflowMenu();
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (upskillContentSubArray.getContentInfo().size() == 1) {
                    last_submit = "1";
                } else {
                    last_submit = "0";
                }
                submitAnalytics();
                click_count = click_count + 1;
                onNavigation();
                //upskillContentSubArray.getContentInfo().remove(0);
                /*if(last_submit.equalsIgnoreCase("0")) {
                    if (upskillContentSubArray.getContentInfo().size() > 0) {

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
                    }
                }*/
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        click_count = click_count - 1;
        onNavigation();
    }

    private void submitAnalytics() {
        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(this, EVENT_ID);
        ApiUtils.getAPIService().AddTrainingAnalytics(api_token, eventid, upskillContentSubArray.getContentInfo().get(click_count).getTraining_id(),
                "Audio", upskillContentSubArray.getMainInfo().getId(), last_submit)
                .enqueue(new Callback<FetchAgenda>() {
                    @Override
                    public void onResponse(Call<FetchAgenda> call, Response<FetchAgenda> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strType = response.body().getHeader().get(0).getType();
                                if (strType.equalsIgnoreCase("success")) {

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FetchAgenda> call, Throwable t) {
                        Log.e("Message", t.getMessage());
                    }
                });
    }

    private void onNavigation() {
        //if (last_submit.equalsIgnoreCase("0")) {
            if (click_count > 0) {
                if (upskillContentSubArray.getContentInfo().size() > click_count) {

                    if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Text")) {
                        startActivity(new Intent(this, UpskillDetailsTextActivity.class)
                                .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                        finish();
                    } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Survey")) {
                        startActivity(new Intent(this, UpskillSurveyActivity.class)
                                .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                        finish();
                    } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Poll")) {
                        startActivity(new Intent(this, UpskillDetailsPollActivity.class)
                                .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                        finish();
                    } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Pdf")) {
                        startActivity(new Intent(this, UpskillDetailsPdfActivity.class)
                                .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                        finish();
                    } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Image")) {
                        startActivity(new Intent(this, UpskillDetailsImageActivity.class)
                                .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                        finish();
                    } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Video")) {
                        startActivity(new Intent(this, UpskillDetailsVideoActivity.class)
                                .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                        finish();
                    } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Quiz")) {
                        startActivity(new Intent(this, UpskillDetailsQuizActivity.class)
                                .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                        finish();
                    } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Audio")) {
                        startActivity(new Intent(this, UpskillDetailsAudioActivity.class)
                                .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                        finish();
                    }
                }
            //}
        }
    }
}