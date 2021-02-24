package com.procialize.bayer2020.ui.upskill.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.bayer2020.Constants.ApiUtils;
import com.procialize.bayer2020.R;
import com.procialize.bayer2020.Utility.CommonFunction;
import com.procialize.bayer2020.Utility.SharedPreference;
import com.procialize.bayer2020.Utility.Utility;
import com.procialize.bayer2020.ui.agenda.model.FetchAgenda;
import com.procialize.bayer2020.ui.upskill.model.UpskillContentSubArray;
import com.procialize.bayer2020.ui.upskill.model.UpskillList;

import org.jsoup.Jsoup;

import java.io.Serializable;
import java.util.HashMap;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.AUTHERISATION_KEY;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_ID;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LIST_MEDIA_PATH;
import static com.procialize.bayer2020.Utility.SharedPreferencesConstant.EVENT_LOGO;
import static com.procialize.bayer2020.ui.upskill.view.UpskillDetailsFirstActivity.click_count;

public class UpskillDetailsVideoActivity extends AppCompatActivity implements View.OnClickListener {

    UpskillContentSubArray upskillContentSubArray;
    TextView tv_Description;
    Button btn_next;
    public JzvdStd videoview;
    UpskillList upskillList;

    String api_token, eventid, last_submit;
    String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upskill_details_video);

        setUpToolbar();
        upskillContentSubArray = (UpskillContentSubArray) getIntent().getSerializableExtra("upskillContent");
        upskillList = (UpskillList) getIntent().getSerializableExtra("upskill_info");
        tv_Description = findViewById(R.id.tv_Description);
        btn_next = findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);

        tv_Description.setText(upskillContentSubArray.getContentInfo().get(click_count).getContent_desc());
        String contentDesc = upskillContentSubArray.getContentInfo().get(click_count).getContent_desc();
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
        try {
            Bitmap bitmap = retriveVideoFrameFromVideo(upskillContentSubArray.getContentInfo().get(click_count).getContent_url().trim());
            filePath = CommonFunction.saveImage(this, bitmap, "upskillthumb");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        videoview = findViewById(R.id.videoview);
        videoview.thumbImageView.setImageDrawable(getResources().getDrawable(R.drawable.video_placeholder));
        videoview.setUp(upskillContentSubArray.getContentInfo().get(click_count).getContent_url().trim(), ""
                , JzvdStd.SCREEN_NORMAL);
        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);
        Glide.with(videoview)
                .load(filePath)
                //.placeholder(R.mipmap.placeholder)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)).centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(videoview.thumbImageView);


        if (upskillContentSubArray.getContentInfo().size() == click_count + 1) {
            btn_next.setText("Submit");
        } else {
            btn_next.setText("Next");
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


    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);

            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
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
                "Video", upskillContentSubArray.getMainInfo().getId(), last_submit)
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
        //if (last_submit.equalsIgnoreCase("0")) {
        try {
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
                }else {
                    finish();
                }
            } else {
                startActivity(new Intent(this, UpskillDetailsFirstActivity.class)
                        .putExtra("upskill_info", (Serializable) upskillList));
            }
        } catch (Exception e) {
        }
        //}
    }
}