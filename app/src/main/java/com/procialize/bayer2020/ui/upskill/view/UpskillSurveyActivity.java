package com.procialize.bayer2020.ui.upskill.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

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
import com.procialize.bayer2020.ui.agenda.model.FetchAgenda;
import com.procialize.bayer2020.ui.upskill.model.UpskillContentSubArray;
import com.procialize.bayer2020.ui.upskill.model.UpskillList;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;
import static com.procialize.bayer2020.ui.upskill.view.UpskillDetailsFirstActivity.click_count;

public class UpskillSurveyActivity extends AppCompatActivity implements View.OnClickListener {

    UpskillContentSubArray upskillContentSubArray;
    Button btn_next;
    WebView webView;
    ProgressBar progressBar;

    String api_token, eventid, last_submit;

    UpskillList upskillList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upskill_survey);
        setUpToolbar();
        upskillContentSubArray = (UpskillContentSubArray) getIntent().getSerializableExtra("upskillContent");

        upskillList = (UpskillList) getIntent().getSerializableExtra("upskill_info");
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

        String strUrl = upskillContentSubArray.getContentInfo().get(click_count).getLink();
        webView.loadUrl(strUrl);

        if (upskillContentSubArray.getContentInfo().size() ==  click_count+1) {
            btn_next.setText("Submit");
        } else {
            btn_next.setText("Next");
        }

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
                /*upskillContentSubArray.getContentInfo().remove(0);
                if(last_submit.equalsIgnoreCase("0")) {
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

    private void submitAnalytics() {
        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(this, EVENT_ID);
        ApiUtils.getAPIService().AddTrainingAnalytics(api_token, eventid, upskillContentSubArray.getContentInfo().get(click_count).getTraining_id(),
                "Survey", upskillContentSubArray.getMainInfo().getId(), last_submit)
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        click_count = click_count - 1;
        onNavigation();
    }

    private void onNavigation() {
        try{
            btn_next.setEnabled(false);
        if (click_count > 0) {
            if (upskillContentSubArray.getContentInfo().size() > click_count) {

                if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Text")) {
                    startActivity(new Intent(this, UpskillDetailsTextActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray)
                            .putExtra("upskill_info", (Serializable) upskillList));
                    finish();
                } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Survey")) {
                    startActivity(new Intent(this, UpskillSurveyActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray)
                            .putExtra("upskill_info", (Serializable) upskillList));
                    finish();
                } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Poll")) {
                    startActivity(new Intent(this, UpskillDetailsPollActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray)
                            .putExtra("upskill_info", (Serializable) upskillList));
                    finish();
                } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Pdf")) {
                    startActivity(new Intent(this, UpskillDetailsPdfActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray)
                            .putExtra("upskill_info", (Serializable) upskillList));
                    finish();
                } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Image")) {
                    startActivity(new Intent(this, UpskillDetailsImageActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray)
                            .putExtra("upskill_info", (Serializable) upskillList));
                    finish();
                } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Video")) {
                    startActivity(new Intent(this, UpskillDetailsVideoActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray)
                            .putExtra("upskill_info", (Serializable) upskillList));
                    finish();
                } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Quiz")) {
                    if (upskillContentSubArray.getContentInfo().get(click_count).getContent_desc_quiz().get(0).getReplied().equalsIgnoreCase("0")) {
                        startActivity(new Intent(this, UpskillDetailsQuizActivity.class)
                                .putExtra("upskillContent", (Serializable) upskillContentSubArray)
                                .putExtra("click_count", click_count)
                                .putExtra("upskill_info", (Serializable) upskillList));
                        finish();
                    } else {
                        Intent intent = new Intent(this, UpskillQuizSubmittedActivity.class);
                        intent.putExtra("folderName", upskillContentSubArray.getContentInfo().get(click_count).getContent_desc_quiz().get(0).getFolder_name());
                        intent.putExtra("folderid", upskillContentSubArray.getContentInfo().get(click_count).getContent_desc_quiz().get(0).getFolder_id());
                        intent.putExtra("upskillContent", (Serializable) upskillContentSubArray);
                        intent.putExtra("Page", "Question");
                        intent.putExtra("click_count", click_count);
                        intent.putExtra("upskill_info", (Serializable) upskillList);
                        startActivity(intent);
                        finish();
                       /* startActivity(new Intent(UpskillDetailsFirstActivity.this, UpskillQuizSubmittedActivity.class)
                                .putExtra("upskillContent", (Serializable) upskillContentSubArray));*/
                    }
                } else if (upskillContentSubArray.getContentInfo().get(click_count).getContent_type().equalsIgnoreCase("Audio")) {
                    startActivity(new Intent(this, UpskillDetailsAudioActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray)
                            .putExtra("upskill_info", (Serializable) upskillList));
                    finish();
                }
            }
        } else {
            startActivity(new Intent(this, UpskillDetailsFirstActivity.class)
                    .putExtra("upskill_info", (Serializable) upskillList));
        }
    }catch (Exception e){}
    }
}