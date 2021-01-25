package com.procialize.bayer2020.ui.upskill.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.ui.upskill.model.UpskillContentSubArray;

import java.io.Serializable;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;

public class UpskillSurveyActivity extends AppCompatActivity implements View.OnClickListener {

    UpskillContentSubArray upskillContentSubArray;
    Button btn_next;
    WebView webView;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upskill_survey);
        setUpToolbar();
        upskillContentSubArray = (UpskillContentSubArray) getIntent().getSerializableExtra("upskillContent");

       /* tv_title = findViewById(R.id.tv_title);
        String contentDesc = upskillContentSubArray.getContentInfo().get(0).getContent_desc();
        tv_title.setText(contentDesc);*/
        webView = findViewById(R.id.webView);
        btn_next = findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setBackgroundColor(Color.TRANSPARENT);
//        webview.setWebViewClient(new CustomWebViewClient());

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (!UpskillSurveyActivity.this.isFinishing()) {
                    if (progressBar.getVisibility() == View.GONE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    if (progress == 100) {
                        if (progressBar.getVisibility() == View.VISIBLE) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });

        String strUrl = upskillContentSubArray.getContentInfo().get(0).getLink();
        webView.loadUrl(strUrl);
        upskillContentSubArray.getContentInfo().remove(0);
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