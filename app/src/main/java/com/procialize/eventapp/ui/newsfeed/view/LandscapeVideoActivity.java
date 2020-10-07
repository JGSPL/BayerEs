package com.procialize.eventapp.ui.newsfeed.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.procialize.eventapp.R;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class LandscapeVideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landscape_video);

        Intent i =getIntent();
        String url = i.getStringExtra("url");
        JzvdStd videoview = findViewById(R.id.videoview);


        videoview.setUp(url.trim(), ""
                , JzvdStd.SCREEN_NORMAL);
        //JzvdStd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);
        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_ORIGINAL);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }
}