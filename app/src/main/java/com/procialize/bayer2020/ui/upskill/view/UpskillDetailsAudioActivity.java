package com.procialize.bayer2020.ui.upskill.view;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.procialize.bayer2020.ui.upskill.model.UpskillContentSubArray;


import org.jsoup.Jsoup;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;

public class UpskillDetailsAudioActivity extends AppCompatActivity {

    UpskillContentSubArray upskillContentSubArray;
    ImageView iv_play_pause;
    TextView tv_duration,tv_description;
    String spannedString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upskill_details_audio);
        setUpToolbar();
        upskillContentSubArray = (UpskillContentSubArray) getIntent().getSerializableExtra("upskillContent");
        iv_play_pause = findViewById(R.id.iv_play_pause);
        tv_duration = findViewById(R.id.tv_duration);
        tv_description = findViewById(R.id.tv_description);
        final MediaPlayer mp = new MediaPlayer();
        try {
            String url = upskillContentSubArray.getContentInfo().get(0).getContent_url();
            //you can change the path, here path is external directory(e.g. sdcard) /Music/maine.mp3
            mp.setDataSource(url);

            mp.prepare();
            long duration = mp.getDuration();
            String hms = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)),
                    TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));

            tv_duration.setText(""+hms);
            String contentDesc = upskillContentSubArray.getContentInfo().get(0).getContent_desc();
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
                    iv_play_pause.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                } else {
                    mp.pause();
                    iv_play_pause.setImageDrawable(getResources().getDrawable(R.drawable.play));
                }
            }
        });
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