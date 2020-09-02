package com.procialize.eventapp.ui.newsFeedDetails.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.procialize.eventapp.R;
import com.procialize.eventapp.ui.newsFeedDetails.adapter.NewsFeedDetailsPagerAdapter;
import com.procialize.eventapp.ui.newsFeedDetails.viewModel.NewsFeedDetailsViewModel;
import com.procialize.eventapp.ui.newsFeedPost.viewModel.PostNewsFeedViewModel;
import com.procialize.eventapp.ui.newsfeed.model.News_feed_media;
import com.procialize.eventapp.ui.newsfeed.model.Newsfeed_detail;

import java.util.ArrayList;

public class NewsFeedDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_save, btn_share;
    ViewPager vp_media;
    NewsFeedDetailsViewModel newsFeedDetailsViewModel;
    private String position;
    private Newsfeed_detail newsfeed_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed_details);

        newsFeedDetailsViewModel = ViewModelProviders.of(this).get(NewsFeedDetailsViewModel.class);

        Intent intent = getIntent();
        try {
            newsfeed_detail = (Newsfeed_detail) getIntent().getSerializableExtra("Newsfeed_detail");
            position = intent.getStringExtra("position");
        } catch (Exception e) {
            e.printStackTrace();
        }

        btn_save = findViewById(R.id.btn_save);
        btn_share = findViewById(R.id.btn_share);
        vp_media = findViewById(R.id.vp_media);

        btn_save.setOnClickListener(this);
        btn_share.setOnClickListener(this);

        setupPagerAdapter();
    }

    public void setupPagerAdapter()
    {

            final ArrayList<String> imagesSelectednew = new ArrayList<>();
            final ArrayList<String> imagesSelectednew1 = new ArrayList<>();
            for (int i = 0; i < newsfeed_detail.getNews_feed_media().size(); i++) {
                imagesSelectednew.add(newsfeed_detail.getNews_feed_media().get(i).getMedia_file());
                imagesSelectednew1.add(newsfeed_detail.getNews_feed_media().get(i).getThumb_image());
            }

            NewsFeedDetailsPagerAdapter swipepagerAdapter = new NewsFeedDetailsPagerAdapter(NewsFeedDetailsActivity.this, imagesSelectednew, imagesSelectednew1);
            vp_media.setAdapter(swipepagerAdapter);
            swipepagerAdapter.notifyDataSetChanged();
            vp_media.setCurrentItem(Integer.parseInt(position));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                String strUrlPathForSave = "";
                String strMediaTypeForSave = "";
                if (strMediaTypeForSave.equalsIgnoreCase("image")) {
                    Bitmap bitmap = null;
                    newsFeedDetailsViewModel.saveImage(this, bitmap);
                } else {
                    newsFeedDetailsViewModel.saveVideo(this, strUrlPathForSave);
                }
                break;
            case R.id.btn_share:
                String strUrlPathForShare = "";
                String strMediaTypeForShare = "";
                //newsFeedDetailsViewModel.shareImage(this, strUrlPathForShare, strMediaTypeForShare);
                break;
        }
    }
}