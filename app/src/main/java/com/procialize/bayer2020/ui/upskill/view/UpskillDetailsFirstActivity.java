package com.procialize.bayer2020.ui.upskill.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.Constants.RefreashToken;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.catalogue.view.PestProductDetailsActivity;
import com.procialize.bayer2020.ui.upskill.model.UpskillContent;
import com.procialize.bayer2020.ui.upskill.model.UpskillContentSubArray;
import com.procialize.bayer2020.ui.upskill.model.UpskillList;

import org.jsoup.Jsoup;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;

public class UpskillDetailsFirstActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_title, tv_description;
    ImageView iv_banner;
    UpskillList upskillList;
    String api_token, eventid;
    Button btn_start;
    UpskillContentSubArray upskillContentSubArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upskill_details_first);

        tv_title = findViewById(R.id.tv_title);
        iv_banner = findViewById(R.id.iv_banner);
        tv_description = findViewById(R.id.tv_description);
        btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);
        api_token = SharedPreference.getPref(this, AUTHERISATION_KEY);
        eventid = SharedPreference.getPref(this, EVENT_ID);
        upskillList = (UpskillList) getIntent().getSerializableExtra("upskill_info");

        /*tv_description.setText(upskillList.getDescription());
        tv_title.setText(upskillList.getName());*/

        setUpToolbar();
        getDataFromApi();
    }

    private void getDataFromApi() {
        ApiUtils.getAPIService().UpskillContent(api_token, eventid, upskillList.getId())
                .enqueue(new Callback<UpskillContent>() {
                    @Override
                    public void onResponse(Call<UpskillContent> call, Response<UpskillContent> response) {
                        if (response.isSuccessful()) {
                            String strUpskillList = response.body().getDetail();
                            RefreashToken refreashToken = new RefreashToken(UpskillDetailsFirstActivity.this);
                            String data = refreashToken.decryptedData(strUpskillList);
                            try {
                                Gson gson = new Gson();

                                upskillContentSubArray = gson.fromJson(data, new TypeToken<UpskillContentSubArray>() {
                                }.getType());

                                if (upskillContentSubArray != null) {
                                    tv_title.setText(upskillContentSubArray.getMainInfo().getName());
                                    //tv_description.setText(upskillContentSubArray.getMainInfo().getDescription());

                                    String contentDesc = upskillContentSubArray.getMainInfo().getDescription();
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

                                    Glide.with(getApplicationContext())
                                            .load(upskillContentSubArray.getMainInfo().getCover_img())
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
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UpskillContent> call, Throwable t) {
                        Log.e("Message", t.getMessage());
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                if (upskillContentSubArray.getContentInfo().get(0).getContent_type().equalsIgnoreCase("Text")) {
                    startActivity(new Intent(UpskillDetailsFirstActivity.this, UpskillDetailsTextActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                } else if (upskillContentSubArray.getContentInfo().get(0).getContent_type().equalsIgnoreCase("Survey")) {
                    startActivity(new Intent(UpskillDetailsFirstActivity.this, UpskillSurveyActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                } else if (upskillContentSubArray.getContentInfo().get(0).getContent_type().equalsIgnoreCase("Poll")) {
                    startActivity(new Intent(UpskillDetailsFirstActivity.this, UpskillDetailsPollActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                } else if (upskillContentSubArray.getContentInfo().get(0).getContent_type().equalsIgnoreCase("Pdf")) {
                    startActivity(new Intent(UpskillDetailsFirstActivity.this, UpskillDetailsPdfActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                } else if (upskillContentSubArray.getContentInfo().get(0).getContent_type().equalsIgnoreCase("Image")) {
                    startActivity(new Intent(UpskillDetailsFirstActivity.this, UpskillDetailsImageActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                } else if (upskillContentSubArray.getContentInfo().get(0).getContent_type().equalsIgnoreCase("Video")) {
                    startActivity(new Intent(UpskillDetailsFirstActivity.this, UpskillDetailsVideoActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                } else if (upskillContentSubArray.getContentInfo().get(0).getContent_type().equalsIgnoreCase("Quiz")) {
                    startActivity(new Intent(UpskillDetailsFirstActivity.this, UpskillDetailsQuizActivity.class)
                            .putExtra("upskillContent", (Serializable) upskillContentSubArray));
                } else if (upskillContentSubArray.getContentInfo().get(0).getContent_type().equalsIgnoreCase("Audio")) {
                    startActivity(new Intent(UpskillDetailsFirstActivity.this, UpskillDetailsAudioActivity.class)
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