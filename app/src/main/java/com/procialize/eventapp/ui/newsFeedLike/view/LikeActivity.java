package com.procialize.eventapp.ui.newsFeedLike.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.procialize.eventapp.ConnectionDetector;
import com.procialize.eventapp.R;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;

public class LikeActivity extends AppCompatActivity {

    ConnectionDetector connectionDetector;
    private String position;
    private Newsfeed_detail newsfeed_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);

        connectionDetector = ConnectionDetector.getInstance(this);

        Intent intent = getIntent();
        try {
            newsfeed_detail = (Newsfeed_detail) getIntent().getSerializableExtra("Newsfeed_detail");
            position = intent.getStringExtra("position");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}