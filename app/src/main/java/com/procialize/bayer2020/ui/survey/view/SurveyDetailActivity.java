package com.procialize.bayer2020.ui.survey.view;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.procialize.bayer2020.Utility.GetUserActivityReport;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.session.SessionManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.DOCUMENT_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;

public class SurveyDetailActivity extends AppCompatActivity {

    ImageView iv_back;
    WebView webview;
    Button btn_share, btn_save;
    TextView tv_header;
    RelativeLayout linear;
    SessionManager session;
    String api_token, event_id;
    ProgressBar progressBar;
    String url = "";
    String url1 = "", DocId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_detail);

        iv_back = findViewById(R.id.iv_back);
        webview = findViewById(R.id.webView);
        btn_share = findViewById(R.id.btn_share);
        btn_save = findViewById(R.id.btn_save);
        linear = findViewById(R.id.linear);
        progressBar = findViewById(R.id.progressBar);

        url = getIntent().getStringExtra("url");
       // url1 = getIntent().getStringExtra("url1");
       // DocId = getIntent().getStringExtra("DocId");

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setUpToolbar();
        tv_header = findViewById(R.id.tv_header);
        api_token = SharedPreference.getPref(SurveyDetailActivity.this, AUTHERISATION_KEY);
        event_id = SharedPreference.getPref(SurveyDetailActivity.this, EVENT_ID);

        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview.setBackgroundColor(Color.TRANSPARENT);
//        webview.setWebViewClient(new CustomWebViewClient());

        WebSettings settings = webview.getSettings();
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


        webview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (!SurveyDetailActivity.this.isFinishing()) {
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
        String path = SharedPreference.getPref(this, DOCUMENT_MEDIA_PATH);
        webview.loadUrl(url);

        webview.setWebViewClient(new WebViewClient() {
            boolean checkhasOnPageStarted = false;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                checkhasOnPageStarted = true;
            }

            public void onPageFinished(WebView view, String url) {
                if (view.getTitle().equals(""))
                    view.reload();
            }
        });

       /* btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SurveyDetailActivity.ShareFile().execute(url1);

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SurveyDetailActivity.DownloadFile().execute(url1);
            }
        });*/
    }




    private void setUpToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
           /* setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            mToolbar.showOverflowMenu();*/
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
