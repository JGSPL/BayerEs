package com.procialize.bayer2020.ui.upskill.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
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

import java.io.Serializable;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;

public class UpskillDetailsImageActivity extends AppCompatActivity implements View.OnClickListener {

    UpskillContentSubArray upskillContentSubArray;
    String contentType;
    ImageView iv_banner;
    TextView tv_Description;
    Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upskill_details_image);

        upskillContentSubArray = (UpskillContentSubArray) getIntent().getSerializableExtra("upskillContent");
        contentType = upskillContentSubArray.getContentInfo().get(0).getContent_type();
        iv_banner = findViewById(R.id.iv_banner);
        tv_Description = findViewById(R.id.tv_Description);
        btn_next = findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);
        Glide.with(getApplicationContext())
                .load(upskillContentSubArray.getContentInfo().get(0).getContent_url())
                .skipMemoryCache(true)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(iv_banner);
        //tv_Description.setText(upskillContentSubArray.getContentInfo().get(0).getContent_desc());
        String contentDesc = upskillContentSubArray.getContentInfo().get(0).getContent_desc();
        if (contentDesc.contains("\n")) {
            contentDesc = contentDesc.trim().replace("\n", "<br/>");
        } else {
            contentDesc = contentDesc.trim();
        }
        String spannedString = String.valueOf(Jsoup.parse(contentDesc)).trim();//Html.fromHtml(feedData.getPost_status(), Html.FROM_HTML_MODE_COMPACT).toString();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Spanned strPost = Html.fromHtml(spannedString, Html.FROM_HTML_MODE_COMPACT);
            tv_Description.setText(Utility.trimTrailingWhitespace(strPost));
        } else {
            Spanned strPost = Html.fromHtml(spannedString);
            tv_Description.setText(Utility.trimTrailingWhitespace(strPost));
        }
        upskillContentSubArray.getContentInfo().remove(0);
        setUpToolbar();
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
}